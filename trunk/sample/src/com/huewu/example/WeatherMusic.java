package com.huewu.example;

import com.huewu.example.adapter.MusicItemAdapter;
import com.huewu.example.provider.FileMusicProvider;
import com.huewu.example.provider.IMusicItem;
import com.huewu.example.provider.RandomWeatherProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeatherMusic extends Activity {

    private static final String TAG = "WeatherMusic";
    private static final int MUSIC_NOT_READY_1 = 100;
    private static final int MUSIC_NOT_READY_2 = 101;
    private static final int MUSIC_DISAPPEAR = 102;
    
	private TextView textView = null;
	private ImageView weatherIcon = null;
	private TextView musicTextView = null;
	private ListView listView = null;
	
	private RadioStation station = null;
	private UnmountBroadcastReceiver receiver = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		station = new RadioStation(this, new RandomWeatherProvider(this), new FileMusicProvider(this));
		textView = (TextView) findViewById(R.id.weather_desc);
		musicTextView = (TextView) findViewById(R.id.music_status);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		listView = (ListView)findViewById(R.id.music_list);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				station.handlePlayorStopMusic(arg2);
				updateMusicStatus();
			}
		});
		
		receiver = new UnmountBroadcastReceiver();
		
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		intentFilter.addDataScheme("file");		
		registerReceiver(receiver, intentFilter);		
	}

	@Override
	protected void onResume() {
		super.onResume();
		String desc = station.getWeatherDescription();
		textView.setText(desc);
		weatherIcon.setImageDrawable(station.getWeatherDrawable());
		
		if(isAllMusicAvailable() == true){
			prepareMusic();
		}else{
			showDialog(MUSIC_NOT_READY_1);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		station.stopMusic();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new Builder(this);
		switch(id){
		case MUSIC_NOT_READY_1:
			return builder
				.setTitle(R.string.resource_error)
				.setMessage(R.string.resource_not_ready)
				.setNegativeButton(R.string.cancel, new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setPositiveButton(R.string.ok, new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isAllMusicAvailable() == false)
							showDialog(MUSIC_NOT_READY_2);
						else
							prepareMusic();
					}
				})
				.setCancelable(false)
				.create();
		case MUSIC_NOT_READY_2:
			return builder
				.setTitle(R.string.resource_error)
				.setMessage(R.string.resource_not_ready)
				.setNegativeButton(R.string.cancel, new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setPositiveButton(R.string.ok, new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isAllMusicAvailable() == false)
							showDialog(MUSIC_NOT_READY_1);
						else
							prepareMusic();
					}
				})
				.setCancelable(false)
				.create();			
		case MUSIC_DISAPPEAR:
			return builder
			.setTitle(R.string.resource_error)
			.setMessage(R.string.resource_disappear)
			.setNegativeButton(R.string.cancel, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setPositiveButton(R.string.ok, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(isAllMusicAvailable() == false)
						showDialog(MUSIC_NOT_READY_1);
					else
						prepareMusic();
				}
			})
			.setCancelable(false)
			.create();
		}
		
		return super.onCreateDialog(id);
	}
	
	private void updateMusicStatus(){
		if( station.isPlaying() == true )
			musicTextView.setText(R.string.playing);
		else
			musicTextView.setText(R.string.stopped);
	}
	
	private boolean isAllMusicAvailable(){
		return station.isAllMusicAvailable();
	}
	
	private void prepareMusic(){
		IMusicItem[] musics = new IMusicItem[station.getMusicList().size()];
		musics = station.getMusicList().toArray(musics);
		
		MusicItemAdapter adapter 
			= new MusicItemAdapter(this, R.layout.list_item, musics);
		
		listView.setAdapter(adapter);
		updateMusicStatus();
	}
	
	private class UnmountBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "SD Card Unmounted");
			WeatherMusic.this.showDialog(MUSIC_DISAPPEAR);
		}
	}		
}//end of class
