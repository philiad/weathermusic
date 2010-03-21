package com.huewu.example.weathermusic;

import java.io.File;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.huewu.example.weathermusic.R;
import com.huewu.example.weathermusic.RadioStation.MusicItem;
import com.huewu.example.weathermusic.adapter.MusicItemAdapter;
import com.huewu.example.weathermusic.downloader.HttpDownloader;
import com.huewu.example.weathermusic.network.WifiStateChecker;
import com.huewu.example.weathermusic.provider.RandomWeatherProvider;
import com.huewu.example.weathermusic.resource.NetworkResource;
import com.huewu.example.weathermusic.resource.ResourceListChecker;

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
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeatherMusic extends Activity {

    private static final String TAG = "WeatherMusic";
    private static final int MUSIC_REQUEST_DOWNLOAD = 100;
    private static final int MUSIC_WIFI_NOT_CONNECTED = 101;
    private static final int MUSIC_SDCARD_DISAPPEAR = 102;
    private static final int MUSIC_DOWNLOAD_PROGRESS = 103;
    private static final int MUSIC_SPACE_IS_NOT_ENOUGH = 104;
    
	private TextView textView = null;
	private ImageView weatherIcon = null;
	private TextView musicTextView = null;
	private ListView listView = null;
	private ProgressDialog downloadProgress = null;
	private RadioStation station = null;
	private UnmountBroadcastReceiver receiver = null;

	private MusicDownloadTask downloader = null;
	private WifiStateChecker wifiChecker = null;
	private ResourceListChecker resChecker = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		station = new RadioStation(
				getApplicationContext(), new RandomWeatherProvider(getApplicationContext()));
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
		
		String desc = station.getWeatherDescription();
		textView.setText(desc);
		weatherIcon.setImageDrawable(station.getWeatherDrawable());

		wifiChecker = new WifiStateChecker(getApplicationContext());
		resChecker = new ResourceListChecker(getResources().openRawResource(R.raw.network_resource_list));
		
//		Log.i(TAG, "Wifi State: " + wifiChecker.isWifiConnected());
//		Log.i(TAG, "Wifi State: " + wifiChecker.isReachable("192.168.0.1"));
//		Log.i(TAG, "Resource State: " + resChecker.isAllResourcListAvailable());
		
		prepareMusic();		
	}

	@Override
	protected void onResume() {
		super.onResume();
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
		case MUSIC_SPACE_IS_NOT_ENOUGH:
			return builder
			.setTitle(R.string.resource_error)
			.setMessage(R.string.resource_no_space)
			.setNegativeButton(R.string.cancel, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setPositiveButton(R.string.ok, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(MUSIC_SPACE_IS_NOT_ENOUGH);
					prepareMusic();
				}
			})
			.setCancelable(false)
			.create();
		case MUSIC_REQUEST_DOWNLOAD:
			return builder
			.setTitle(R.string.resource_error)
			.setMessage(R.string.resource_request_download)
			.setNegativeButton(R.string.cancel, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setPositiveButton(R.string.ok, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(MUSIC_REQUEST_DOWNLOAD);
					showDialog(MUSIC_DOWNLOAD_PROGRESS);
				}
			})
			.setCancelable(false)
			.create();
		case MUSIC_WIFI_NOT_CONNECTED:
			return builder
			.setTitle(R.string.resource_error)
			.setMessage(R.string.wifi_connect)
			.setNegativeButton(R.string.cancel, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setPositiveButton(R.string.ok, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(MUSIC_WIFI_NOT_CONNECTED);
					prepareMusic();
				}
			})
			.setCancelable(false)
			.create();
		case MUSIC_SDCARD_DISAPPEAR:
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
					prepareMusic();
				}
			})
			.setCancelable(false)
			.create();

		case MUSIC_DOWNLOAD_PROGRESS:
			downloadProgress = new ProgressDialog(this);
			downloadProgress.setIndeterminate(false);
			downloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			downloadProgress.setTitle(this.getString(R.string.download));
			downloadProgress.setOnCancelListener(new OnCancelListener(){
				@Override
				public void onCancel(DialogInterface dialog) {
					downloader.cancel(true);
					downloader = null;
					finish();
				}
			});
			
			if(downloader != null)
				downloader.cancel(true);
			downloader = new MusicDownloadTask();
			downloader.execute(resChecker.getMissingResourceList());
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
	
	
	private void prepareMusic(){
		if(resChecker.isAllResourcListAvailable() == false){
			//sd card space is enough?
			String state = Environment.getExternalStorageState();
			if(state.equals(Environment.MEDIA_MOUNTED) == false){
				showDialog(MUSIC_SPACE_IS_NOT_ENOUGH);	//space is not enough popup.
			}else{
				StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
				long space = stat.getBlockSize() * stat.getAvailableBlocks();
				Log.i(TAG, "Required Space: " + resChecker.getRequiredSize());
				if(resChecker.getRequiredSize() > space)
					showDialog(MUSIC_SPACE_IS_NOT_ENOUGH); //space is not enough popup.
				else if(wifiChecker.isWifiConnected() == false)
					showDialog(MUSIC_WIFI_NOT_CONNECTED);
				else
					showDialog(MUSIC_REQUEST_DOWNLOAD); //request download.
			}
		}else{
			//everything is ok!
			station.setResources(resChecker.getResourceList());
			showMusic();			
		}
	}

	private void showMusic(){
        MusicItem[] musics = null;
        musics = station.getMusics();
        MusicItemAdapter adapter 
                = new MusicItemAdapter(WeatherMusic.this, R.layout.list_item, musics);
        listView.setAdapter(adapter);
        updateMusicStatus();
	}
	
	/**
	 * MusicDowloadTask
	 * Download necessary mp3 files by interent.
	 */
	private class MusicDownloadTask extends AsyncTask<NetworkResource[], NetworkResource, Void>{
		HttpDownloader downloader = null;
		
		@Override
		protected Void doInBackground(NetworkResource[]... params) {
			downloader = new HttpDownloader();
			downloader.setOnDownloadProgressListener(new HttpDownloader.onDownloadProgressListener(){

				@Override
				public void onProgress(NetworkResource res) {
					publishProgress(res);
				}
			});
			NetworkResource[] list = params[0];
			
			for(NetworkResource res : list){
				String result = downloader.downloadNetworkResource(res);
				if(result == null)
					return null;	//network error.
			}
			return null;
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if(downloader != null)
				downloader.abort();
		}
		
		@Override
		protected void onProgressUpdate(NetworkResource... values) {
			super.onProgressUpdate(values);
			if(downloadProgress != null && downloadProgress.isShowing() == true){
				NetworkResource res = values[0];
				
				if(downloadProgress.getMax() != (res.totalSize / 1024))
					downloadProgress.setMax((int) (res.totalSize / 1024));
				
				downloadProgress.setTitle(res.title + " 를 다운로드 하고  있습니다");
				downloadProgress.setProgress((int) (res.currentSize / 1024));
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			removeDialog(MUSIC_DOWNLOAD_PROGRESS);
			prepareMusic();
		}
	}
	
	private class UnmountBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "SD Card Unmounted");
			WeatherMusic.this.showDialog(MUSIC_SDCARD_DISAPPEAR);
		}
	}		
}//end of class
