package com.huewu.example.weathermusic.provider;

import java.util.List;

public class NetworkMusicProvider implements IMusicProvider{
	
	@Override
	public boolean isAllResourceAvailable() {
		return false;
	}

	@Override
	public List<IMusicItem> getMusicList(int type) {
		return null;
	}

	@Override
	public void setResourceList() {
	}

}//end of class
