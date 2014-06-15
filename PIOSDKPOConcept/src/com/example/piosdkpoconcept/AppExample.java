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
		String parseApplicationId = "X4Wgk4EVJqLD7VeMfajKWcfjIHNq9UDaDBnXC0iF";
		String parseClientKey = "up6KceVp9Tkg9wMWuTMAvtCnD0kEwzq42LEUvkuD";
		PIO.setParseCredentials(parseApplicationId, parseClientKey);
		PIO.initializeParse(this);
		//ACRA.init(this);
	}

}
