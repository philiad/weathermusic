package com.huewu.example.downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.net.Uri;

public class HttpDownloader {
	private DefaultHttpClient client = null;
	
	
	public HttpDownloader(){
		client = new DefaultHttpClient();
	}
	
	public void downaloadUrl(URI uri){
		String u = "http://weathermusic.googlecode.com/files/snow2.mp3";
		HttpGet request = new HttpGet(u);
		try {
			HttpResponse resp =  client.execute(request);
			HttpEntity content =  resp.getEntity();
			content.getContentType();
			InputStream is = content.getContent();
			byte[] buffer = new byte[(int) content.getContentLength()];
			is.read(buffer);
			is.close();
			
			//write to file.
			FileOutputStream fos = new FileOutputStream("sdcard/WeatherMusic/snow2_internet.mp3");
			fos.write(buffer);
			fos.close();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}//end of class
