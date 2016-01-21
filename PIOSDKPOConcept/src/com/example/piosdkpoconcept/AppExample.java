package com.example.piosdkpoconcept;

import print.io.PIO;
import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AppExample extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PIO.initializeParse(this, PIOConstants.Parse.APPLICATION_ID, PIOConstants.Parse.CLIENT_KEY);
		LocalBroadcastManager.getInstance(this).registerReceiver(new PIOSDKEventListener(), new IntentFilter(PIOConstants.PIO_SDK_EVENTS));
	}

}
