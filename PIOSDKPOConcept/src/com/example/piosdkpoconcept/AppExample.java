package com.example.piosdkpoconcept;

import print.io.PIO;
import android.app.Application;

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
	}

}
