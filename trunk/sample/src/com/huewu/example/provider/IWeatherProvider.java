package com.huewu.example.provider;

import android.graphics.drawable.Drawable;

public interface IWeatherProvider {
	
	public final static int WEATHER_SUNNY = 100;
	public final static int WEATHER_CLODUY = 101;
	public final static int WEATHER_RAIN = 102;
	public final static int WEATHER_SNOW = 103;
	
	public int getWeather();
	public Drawable getWeatherDrawable();
	public String getWeatherDescription();
}
