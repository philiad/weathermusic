package com.huewu.example.weathermusic.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiStateChecker {

	Context context = null;
	WifiManager manager = null;
	
	public WifiStateChecker(Context context){
		manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean isWifiConnected(){
		boolean isEnable = manager.isWifiEnabled();
		
		if(isEnable == false)	//wi-fi is off.
			return false;
		
		if(manager.getWifiState() != WifiManager.WIFI_STATE_ENABLED)	//wi-if is on, but not enabled.  
			return false;
		
		if(manager.getConnectionInfo().getIpAddress() == 0)	//invalid ip-addres 
			return false;
		
		return true;
	}
	
	public boolean isReachable(String ipAddress){
		try {
			InetAddress ia = InetAddress.getByName(ipAddress);
			return ia.isReachable(3000);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}//end of class
