package com.huewu.example.provider;

import java.util.ArrayList;
import java.util.List;

import com.huewu.example.R;

import android.content.Context;

public class ResourceMusicProvider implements IMusicProvider{
	
	List<IMusicItem> list = null;
	Context context = null;

	public ResourceMusicProvider(Context context) {
		this.context = context;
	}

	@Override
	public List<IMusicItem> getMusicList(int type) {
		buildMusicList(type);
		return list;
	}
	
	@Override
	public boolean isAllResourceAvailable() {
		return true;
	}	
	
	/**
	 * this api only works correctly when raw mp3 media files included in Resource.
	 * @param type
	 */
	@Deprecated
	private void buildMusicList(int type){
		list = new ArrayList<IMusicItem>();
		/*		
		switch(type){
		case IWeatherProvider.WEATHER_SUNNY:
			list.add(new ResourceMusicItem(context, R.raw.sunny1));
			list.add(new ResourceMusicItem(context, R.raw.sunny2));
			list.add(new ResourceMusicItem(context, R.raw.cloudy1));
			list.add(new ResourceMusicItem(context, R.raw.snow1));
			break;
		case IWeatherProvider.WEATHER_CLODUY:
			list.add(new ResourceMusicItem(context, R.raw.cloudy1));
			list.add(new ResourceMusicItem(context, R.raw.cloudy2));
			list.add(new ResourceMusicItem(context, R.raw.rain1));
			list.add(new ResourceMusicItem(context, R.raw.snow1));
			break;
		case IWeatherProvider.WEATHER_RAIN:
			list.add(new ResourceMusicItem(context, R.raw.rain1));
			list.add(new ResourceMusicItem(context, R.raw.rain2));
			list.add(new ResourceMusicItem(context, R.raw.cloudy1));
			list.add(new ResourceMusicItem(context, R.raw.snow1));
			break;
		case IWeatherProvider.WEATHER_SNOW:
			list.add(new ResourceMusicItem(context, R.raw.snow1));
			list.add(new ResourceMusicItem(context, R.raw.snow2));
			list.add(new ResourceMusicItem(context, R.raw.cloudy1));
			list.add(new ResourceMusicItem(context, R.raw.sunny1));
			break;
		}
		*/
	}
}//end of class
