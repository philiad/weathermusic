package com.huewu.example.weathermusic.provider;

import android.net.Uri;

public interface IMusicItem {
	public boolean isLocal();
	public Uri getUri();
	public String getTtitle();
}
