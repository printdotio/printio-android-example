package com.example.piosdkpoconcept;

import print.io.PIO;
import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

//@ReportsCrashes(
//		formKey = "",
//		formUri = "http://bomberaya.iriscouch.com/acra-printio/_design/acra-storage/_update/report",
//		reportType = org.acra.sender.HttpSender.Type.JSON,
//		httpMethod = org.acra.sender.HttpSender.Method.PUT,
//		formUriBasicAuthLogin="printsdk",
//		formUriBasicAuthPassword="testpass876543"
//		)
public class AppExample extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//ACRA.init(this);
		PIO.initializeParse(this, PIOConstants.Parse.APPLICATION_ID, PIOConstants.PayPal.CLIENT_ID);
		LocalBroadcastManager.getInstance(this).registerReceiver(new PIOSDKEvnetListener(), new IntentFilter(PIOConstants.PIO_SDK_EVENTS));
	}

}
