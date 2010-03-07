package com.huewu.example.provider;

import java.util.Random;

import android.content.Context;

public class RandomWeatherProvider extends AbstractWeatherProvider{

	private Random rand = null;
	private int randomNum = 0;
	
	public RandomWeatherProvider(Context context) {
		super(context);
		rand = new Random();
		randomNum = rand.nextInt() % 4;
	}

	@Override
	public int getWeather() {
		switch(randomNum){
		case 0:
			return WEATHER_SUNNY;
		case 1:
			return WEATHER_CLODUY;
		case 2:
			return WEATHER_RAIN;
		case 3:
			return WEATHER_SNOW;
		default:
			return 100;
		}
	}
}//end of class
