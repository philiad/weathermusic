package com.huewu.example;

import java.util.ArrayList;

import com.huewu.example.adapter.MusicItemAdapter;
import com.huewu.example.downloader.HttpDownloader;
import com.huewu.example.provider.FileMusicProvider;
import com.huewu.example.provider.IMusicItem;
import com.huewu.example.provider.RandomWeatherProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
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
    private static final int MUSIC_DOWNLOAD_PROGRESS = 103;
    
	private TextView textView = null;
	private ImageView weatherIcon = null;
	private TextView musicTextView = null;
	private ListView listView = null;
	private ProgressDialog downloadProgress = null;
	
	private RadioStation station = null;
	private UnmountBroadcastReceiver receiver = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		station = new RadioStation(
				getApplicationContext(), new RandomWeatherProvider(getApplicationContext()), new FileMusicProvider(getApplicationContext()));
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
		prepareMusic();
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

		case MUSIC_DOWNLOAD_PROGRESS:
			downloadProgress = new ProgressDialog(this);
			downloadProgress.setTitle(this.getString(R.string.download));
			return downloadProgress;
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
		if(isAllMusicAvailable() == false){
			showDialog(MUSIC_DOWNLOAD_PROGRESS);
			MusicDownloader downloader = new MusicDownloader();
			downloader.execute(null);
		}else{
			showMusic();			
		}
	}

	private void showMusic(){
        IMusicItem[] musics = new IMusicItem[station.getMusicList().size()];
        musics = station.getMusicList().toArray(musics);
        
        MusicItemAdapter adapter 
                = new MusicItemAdapter(WeatherMusic.this, R.layout.list_item, musics);
        
        listView.setAdapter(adapter);
        updateMusicStatus();
	}
	
	private class MusicDownloader extends AsyncTask<Object, String, ArrayList<String>>{
		
		@Override
		protected ArrayList<String> doInBackground(Object... params) {
			HttpDownloader downloader = new HttpDownloader();
			
			ArrayList<String> files = new ArrayList<String>();
			String[] uris = getResources().getStringArray(R.array.uris);
			String[] path = getResources().getStringArray(R.array.path);
			
			int len = uris.length;
			
			for(int i = 0; i < len; ++i){
				this.publishProgress(uris[i]);
				String file = downloader.downaloadUrl(uris[i], path[i]);
				if(file != null){
					files.add(file);
					Log.e(TAG, "File Downloaded: " + file);
				}
			}
			return files;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			if(downloadProgress != null && downloadProgress.isShowing() == true)
				downloadProgress.setMessage(values[0]);
		}

		
		@Override
		protected void onPostExecute(ArrayList<String> files) {
			super.onPostExecute(files);
			
			removeDialog(MUSIC_DOWNLOAD_PROGRESS);
			
			if(isAllMusicAvailable() == true){
				showMusic();
            }else{
				showDialog(MUSIC_NOT_READY_1);
			}			
		}
	}
	
	private class UnmountBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "SD Card Unmounted");
			WeatherMusic.this.showDialog(MUSIC_DISAPPEAR);
		}
	}		
}//end of class
