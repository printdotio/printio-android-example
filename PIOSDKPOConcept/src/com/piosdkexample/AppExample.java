package com.piosdkexample;

import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AppExample extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// Register event listener for events sent by SDK
		LocalBroadcastManager.getInstance(this).registerReceiver(new PIOSDKEventListener(), new IntentFilter(PIOConstants.PIO_SDK_EVENTS));
	}

}
