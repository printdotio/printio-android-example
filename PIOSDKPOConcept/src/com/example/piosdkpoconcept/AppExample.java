package com.example.piosdkpoconcept;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

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
		Parse.initialize(this, "X4Wgk4EVJqLD7VeMfajKWcfjIHNq9UDaDBnXC0iF", "up6KceVp9Tkg9wMWuTMAvtCnD0kEwzq42LEUvkuD");
		PushService.setDefaultPushCallback(this, ActivityHandlePush.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}

}
