package com.huewu.example.appwidget; 

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;

public class TestWidgetProvider extends AppWidgetProvider {

	private static final String CLICK_ACTION = "com.huewu.example.widget.CLICK";
	private static final String CHECKED = "checked";
	
	private boolean isChecked = false;

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals(CLICK_ACTION) == true){
			//button is clicked.
			SharedPreferences pref = context.getSharedPreferences(CHECKED, Activity.MODE_PRIVATE);
			isChecked = pref.getBoolean(CHECKED, true);	//default value is true.
			Editor e = pref.edit();
			e.putBoolean(CHECKED, !isChecked);
			e.commit();
//			isChecked = intent.getBooleanExtra(CHECKED, false);
//			isChecked = !isChecked;	//toggle button.
			//Update Widgets. 
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, TestWidgetProvider.class)));
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent intent = new Intent(CLICK_ACTION);	//button is clicked.
			intent.putExtra(CHECKED, isChecked);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			if(isChecked == true)
				views.setImageViewResource(R.id.onoff, android.R.drawable.checkbox_on_background);
			else
				views.setImageViewResource(R.id.onoff, android.R.drawable.checkbox_off_background);

			views.setOnClickPendingIntent(R.id.onoff, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}		
	}
	
}//end of class
