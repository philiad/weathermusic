package com.huewu.example.provider;

import java.io.File;

import android.net.Uri;

public class FileMusicItem implements IMusicItem{
	
	private String title = "";
	private Uri uri = null;
	
	public static FileMusicItem createMusicItem(String path, int size){
		File file = new File(path);
		
		if(file.exists() == false || file.length() != size){
			return null;
		}
		
		String title = file.getName();
		String uriStr = "file://"+ file.getAbsolutePath();
		Uri uri = Uri.parse(uriStr);		
		
		return new FileMusicItem(title, uri);
	}
	
	private FileMusicItem(String title, Uri uri){
		this.title = title;
		this.uri = uri;
	}

	@Override
	public String getTtitle() {
		return title;
	}

	@Override
	public Uri getUri() {
		return uri;
	}

	@Override
	public boolean isLocal() {
		return true;
	}
}//end of class
