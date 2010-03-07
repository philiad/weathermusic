package com.huewu.example.provider;

import android.content.Context;
import android.net.Uri;

public class ResourceMusicItem implements IMusicItem{
	private String title = "";
	private Uri uri = null;
	
	public ResourceMusicItem(Context context, int res){
		title = context.getResources().getResourceName(res);
		
		String uriStr = "android.resource://"+ context.getPackageName() + "/" + res;
		uri = Uri.parse(uriStr);
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

}
