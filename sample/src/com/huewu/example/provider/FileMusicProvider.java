package com.huewu.example.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huewu.example.R;

import android.content.Context;

public class FileMusicProvider implements IMusicProvider{
	
	List<IMusicItem> list = null;
	HashMap<Integer, IMusicItem> musics = null;
	
	Context context = null;

	public FileMusicProvider(Context context) {
		this.context = context;
		buildMusicList();
	}

	@Override
	public List<IMusicItem> getMusicList(int type) {
		list = new ArrayList<IMusicItem>();

		switch(type){
		case IWeatherProvider.WEATHER_SUNNY:
			list.add(musics.get(R.string.sunny1));
			list.add(musics.get(R.string.sunny2));
			list.add(musics.get(R.string.cloudy1));
			list.add(musics.get(R.string.snow1));
			break;
		case IWeatherProvider.WEATHER_CLODUY:
			list.add(musics.get(R.string.cloudy1));
			list.add(musics.get(R.string.cloudy2));
			list.add(musics.get(R.string.rain1));
			list.add(musics.get(R.string.snow1));
			break;
		case IWeatherProvider.WEATHER_RAIN:
			list.add(musics.get(R.string.rain1));
			list.add(musics.get(R.string.rain2));
			list.add(musics.get(R.string.cloudy1));
			list.add(musics.get(R.string.snow1));
			break;
		case IWeatherProvider.WEATHER_SNOW:
			list.add(musics.get(R.string.snow1));
			list.add(musics.get(R.string.snow2));
			list.add(musics.get(R.string.cloudy1));
			list.add(musics.get(R.string.sunny1));
			break;
		}
		
		return list;
	}
	
	@Override
	public boolean isAllResourceAvailable() {
		if(musics.size() != 8)
			buildMusicList();
		return (musics.size() == 8);
	}		
	
	private void buildMusicList(){
		
		musics = new HashMap<Integer, IMusicItem>();
		IMusicItem item = null;
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.sunny1), context.getResources().getInteger(R.integer.sunny1_size));
		if(item != null)
			musics.put(R.string.sunny1, item);

		item = FileMusicItem.createMusicItem(
				context.getString(R.string.sunny2), context.getResources().getInteger(R.integer.sunny2_size));
		if(item != null)
			musics.put(R.string.sunny2, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.cloudy1), context.getResources().getInteger(R.integer.cloud1_size));
		if(item != null)
			musics.put(R.string.cloudy1, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.cloudy2), context.getResources().getInteger(R.integer.cloud2_size));
		if(item != null)
			musics.put(R.string.cloudy2, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.rain1), context.getResources().getInteger(R.integer.rain1_size));
		if(item != null)
			musics.put(R.string.rain1, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.rain2), context.getResources().getInteger(R.integer.rain2_size));
		if(item != null)
			musics.put(R.string.rain2, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.snow1), context.getResources().getInteger(R.integer.snow1_size));
		if(item != null)
			musics.put(R.string.snow1, item);
		
		item = FileMusicItem.createMusicItem(
				context.getString(R.string.snow2), context.getResources().getInteger(R.integer.snow2_size));
		if(item != null)
			musics.put(R.string.snow2, item);
	}
}//end of class
