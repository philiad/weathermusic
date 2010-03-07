package com.huewu.example.provider;

import java.util.List;

public class InternetMusicProvider implements IMusicProvider{

	@Override
	public List<IMusicItem> getMusicList(int type) {
		return null;
	}

	@Override
	public boolean isAllResourceAvailable() {
		return false;
	}
}//end of class
