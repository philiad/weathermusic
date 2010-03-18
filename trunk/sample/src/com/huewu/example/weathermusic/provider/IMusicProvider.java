package com.huewu.example.weathermusic.provider;

import java.util.List;

public interface IMusicProvider {

	void setResourceList();
	boolean isAllResourceAvailable();
	List<IMusicItem> getMusicList(int type);
}
