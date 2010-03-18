package com.huewu.example.weathermusic.provider;

import com.huewu.example.weathermusic.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class AbstractWeatherProvider implements IWeatherProvider{

	private Context context = null;
	
	public AbstractWeatherProvider(Context context) {
		this.context = context;
	}

	@Override
	public String getWeatherDescription(){
		int weather = getWeather();
		String desc = "";
		switch(weather){
		case WEATHER_SUNNY:
			return context.getString(R.string.sunny);
		case WEATHER_CLODUY:
			return context.getString(R.string.cloudy);
		case WEATHER_RAIN:
			return context.getString(R.string.rain);
		case WEATHER_SNOW:
			return context.getString(R.string.snow);
		default:
		}
		return desc;
	}

	@Override
	public Drawable getWeatherDrawable(){
		int weather = getWeather();
		switch(weather){
		case WEATHER_SUNNY:
			return context.getResources().getDrawable(R.drawable.sunny);
		case WEATHER_CLODUY:
			return context.getResources().getDrawable(R.drawable.cloudy5);
		case WEATHER_RAIN:
			return context.getResources().getDrawable(R.drawable.shower3);
		case WEATHER_SNOW:
			return context.getResources().getDrawable(R.drawable.snow4);
		default:
			return context.getResources().getDrawable(R.drawable.dunno);
		}
	}
}//end of class

