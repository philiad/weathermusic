package com.huewu.example.weathermusic.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpDownloader {
	
	private final int BUFFER_SIZE = 8096;
	
	private DefaultHttpClient client = null;
	private byte[] buffer = null;
	
	
	public HttpDownloader(){
		client = new DefaultHttpClient();
		buffer = new byte[BUFFER_SIZE];
	}
	
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
	
	private boolean fileAlreadyExist(){
		return true;
	}
}//end of class
