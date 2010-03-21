package com.huewu.example.weathermusic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;

import com.huewu.example.weathermusic.provider.IWeatherProvider;
import com.huewu.example.weathermusic.provider.RandomWeatherProvider;
import com.huewu.example.weathermusic.resource.NetworkResource;

public class RadioStation {
	
	public class MusicItem {
		public String title = "";
		public Uri uri = null;
		public int type = 0;
	}	
	
	private IWeatherProvider weatherProvider = null;
	private MediaPlayer player = null;
	private Context context = null;
	
	private List<MusicItem> musics = null;
	private int weather = 0;
	
	public RadioStation(Context context, IWeatherProvider weather){
		this.weatherProvider = weather;
		this.player = new MediaPlayer();
		this.context = context; 
		this.weather = weather.getWeather();
	}
	
	public String getWeatherDescription(){
		return weatherProvider.getWeatherDescription();
	}
	
	public Drawable getWeatherDrawable(){
		return weatherProvider.getWeatherDrawable();
	}
	
	public void handlePlayorStopMusic(int index){
		if(isPlaying() == true){
			stopMusic();
		} else {
			setMusic(index);
			playMusic();
		}
	}
	
	public MusicItem[] getMusics(){
		if(musics == null)
			return new MusicItem[0];
		
		ArrayList<MusicItem> list = new ArrayList<MusicItem>();
		
		for(MusicItem music : musics){
			if(weather == music.type)
				list.add(music);
		}
		
		MusicItem[] result = new MusicItem[list.size()];
		return list.toArray(result);
	}
	
	private void setMusic(int index){
		try {
			player.reset();
			player.setDataSource(context, musics.get(index).uri);
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

	public void setResources(NetworkResource[] resourceList) {
		musics = new ArrayList<MusicItem>();
		//convert NetworkResource to MusicItem
		for(NetworkResource res : resourceList){
			MusicItem music = new MusicItem();
			music.title = res.title;
			music.uri = Uri.parse(res.uri);
			
			if(res.data.contains("sunny") == true)
				music.type = IWeatherProvider.WEATHER_SUNNY;
			else if(res.data.contains("cloud") == true)
				music.type = IWeatherProvider.WEATHER_CLODUY;
			else if(res.data.contains("rain") == true)
				music.type = IWeatherProvider.WEATHER_RAIN;
			else if(res.data.contains("snow") == true)
				music.type = IWeatherProvider.WEATHER_SNOW;
			else
				music.type = IWeatherProvider.WEATHER_NONE;

			musics.add(music);
		}
	}
}//end of class
