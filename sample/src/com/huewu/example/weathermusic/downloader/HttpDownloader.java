package com.huewu.example.weathermusic.downloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.huewu.example.weathermusic.resource.NetworkResource;

public class HttpDownloader {
	
	public interface onDownloadProgressListener{
		void onProgress(NetworkResource res);
	}
	
	private final String TAG = "HttpDownloader";
	private final int BUFFER_SIZE = 8096;
	
	private DefaultHttpClient client = null;
	private byte[] buffer = null;
	private onDownloadProgressListener listener = null;
	private boolean isRunning = false;
	
	
	public HttpDownloader(){
		client = new DefaultHttpClient();
		buffer = new byte[BUFFER_SIZE];
	}
	
	public void setOnDownloadProgressListener(onDownloadProgressListener listener){
		this.listener = listener; 
	}
	
	protected void notifiyProgressChange(NetworkResource res){
		if(listener != null)
			listener.onProgress(res);
	}	
	
	synchronized void setRunningState(boolean flag){
		isRunning = flag;
	}
	
	public void abort(){
		setRunningState(false);
	}

	public String downloadNetworkResource(NetworkResource res){
		setRunningState(true);
		
		InputStream is = null; 	
		BufferedOutputStream bos = null;
		File file = new File(res.data);
		HttpGet request = new HttpGet(res.uri);

		try {
			notifiyProgressChange(res);
			
			if(file.exists() == true){
				//set range header & download only part of remaining contents.
				long len = file.length();
				if(len > res.totalSize)		//if resource size is too big, remove it.
					file.createNewFile();	
				else
					request.addHeader("Range", "bytes=" + String.valueOf(len) + "-");
			}else{
				file.createNewFile();	
			}
			
			bos = new BufferedOutputStream(new FileOutputStream(file, true));
			HttpResponse resp =  client.execute(request);
			HttpEntity content =  resp.getEntity();

			is = content.getContent();

			long contentLen = content.getContentLength();
			long readBytes = 0;
			
			while(contentLen > readBytes){
				int temp = is.read(buffer);
				res.currentSize += temp;
				notifiyProgressChange(res);
				bos.write(buffer, 0, temp);
				readBytes += temp;
				
				if(isRunning == false)
					break;
			}
			
			is.close();
			bos.close();
			
		} catch (ClientProtocolException e) {
			setRunningState(false);
			return null;
		} catch (IOException e) {
			setRunningState(false);
			return null;
		}
		
		setRunningState(false);
		return res.data;
	}
	
	@Deprecated
	public String downaloadUrl(String uri, String path){

		InputStream is = null; 		
		FileOutputStream fos = null; 			
		File file = new File(path);
		HttpGet request = new HttpGet(uri);

		//download resume.
		//set range header.
		//request.setHeader(name, value);
		
		try {
			file.createNewFile();	
			fos = new FileOutputStream(file);
			
			HttpResponse resp =  client.execute(request);
			HttpEntity content =  resp.getEntity();

			is = content.getContent();

			long contentLen = content.getContentLength();
			long readBytes = 0;
			
			while(contentLen > readBytes){
				int temp = is.read(buffer);
				fos.write(buffer, 0, temp);
				readBytes += temp;
			}
			
			is.close();
			fos.close();
			
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return path;
	}
}//end of class
