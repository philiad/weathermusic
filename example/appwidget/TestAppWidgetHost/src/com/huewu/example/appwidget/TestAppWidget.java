package com.huewu.example.appwidget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TestAppWidget extends Activity {

	LinearLayout container;
	AppWidgetHost host;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        host = new AppWidgetHost(this, 0);	//create a new app widget host.
        container = (LinearLayout) findViewById(R.id.widget_container);	//linear layout included in a scroll view.
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	host.startListening();	//start to listen widget related broadcast event.
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	host.stopListening();	//stop to listen widget related broadcast event.
    }
    
    public void handleClick(View v){
    	
    	switch(v.getId()){
    	case R.id.btn1:
    		//add a widget directly.
        	int newWidgetId = host.allocateAppWidgetId();
        	List<AppWidgetProviderInfo> widgets = AppWidgetManager.getInstance(this).getInstalledProviders();
        	addAppWidget(newWidgetId, widgets.get(0));	//always pick up a first widget.  
    		break;
    	case R.id.btn2:
    		//add a widget via an app widget pick activity.
        	Intent i = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        	i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, host.allocateAppWidgetId());
        	ArrayList<AppWidgetProviderInfo> infos = new ArrayList<AppWidgetProviderInfo>();
        	i.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, infos);
        	startActivityForResult(i, 0);
    		break;
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode == Activity.RESULT_OK){
    		int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
    		Log.i("TestAppWidget", "WidagetID: " + widgetId);
    		AppWidgetProviderInfo info = AppWidgetManager.getInstance(this).getAppWidgetInfo(widgetId);    		
    		addAppWidget(widgetId, info);
    	}
    }
    
    private void addAppWidget(int id, AppWidgetProviderInfo info){
    	
		AppWidgetHostView hv = host.createView(this, id, info);
		container.addView(hv);
    }
}//end of class