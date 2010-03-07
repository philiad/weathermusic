package com.huewu.example;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import com.huewu.example.provider.IMusicItem;
import com.huewu.example.provider.IMusicProvider;
import com.huewu.example.provider.IWeatherProvider;
import com.huewu.example.provider.RandomWeatherProvider;
import com.huewu.example.provider.ResourceMusicProvider;

public class RadioStation {
	
	private IWeatherProvider weatherProvider = null;
	private IMusicProvider musicProvider = null;
	private MediaPlayer player = null;
	private Context context = null;
	
	private List<IMusicItem> musics = null;
	private int weather = 0;
	
	public RadioStation(Context context){
		this(context, new RandomWeatherProvider(context), new ResourceMusicProvider(context));
	}
	
	public RadioStation(Context context, IWeatherProvider weather, IMusicProvider music){
		this.weatherProvider = weather;
		this.musicProvider = music;
		this.player = new MediaPlayer();
		this.context = context; 
		this.weather = weather.getWeather();
		this.musics = musicProvider.getMusicList(this.weather);
	}
	
	public String getWeatherDescription(){
		return weatherProvider.getWeatherDescription();
	}
	
	public Drawable getWeatherDrawable(){
		return weatherProvider.getWeatherDrawable();
	}
	
	public List<IMusicItem> getMusicList(){
		return musicProvider.getMusicList(weatherProvider.getWeather());
	}
	
	public boolean isAllMusicAvailable(){
		return musicProvider.isAllResourceAvailable();
	}
	
	public void handlePlayorStopMusic(int index){
		if(isPlaying() == true){
			stopMusic();
		} else {
			setMusic(index);
			playMusic();
		}
	}
	
	private void setMusic(int index){
		try {
			player.reset();
			player.setDataSource(context, musics.get(index).getUri());
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playMusic(){
		player.start();
	}
	
	public void stopMusic(){
		player.stop();
	}
	
	public boolean isPlaying(){
		return player.isPlaying();
	}
}//end of class
