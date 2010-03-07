package com.huewu.example.provider;

import java.util.List;

public interface IMusicProvider {

	boolean isAllResourceAvailable();
	List<IMusicItem> getMusicList(int type);
}
