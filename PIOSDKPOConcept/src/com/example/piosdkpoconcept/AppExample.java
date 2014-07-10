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
		PIO.setFontPathInAssetsLight("HelveticaNeueLTStd-Lt.otf");
		PIO.setFontPathInAssetsNormal("HelveticaNeueLTStd-Roman.otf");
		PIO.setFontPathInAssetsBold("HelveticaNeueLTStd-Bd.otf");

		PIO.setPartnerName(getResources().getString(R.string.hellopics));
		PIO.setHelpUrl(PIOConstants.HELP_URL);
		PIO.setGooglePlayRateUrl(PIOConstants.GOOGLE_PLAY_RATE_URL);
		PIO.setFacebookPageUrl(PIOConstants.FACEBOOK_PAGE_URL);

		PIO.setFacebookAppId(getString(R.string.facebook_app_id));

		PIO.setInstagramClientId(PIOConstants.Instagram.CLIENT_ID);

		PIO.setFlickrConsumerKey(PIOConstants.Flickr.CONSUMER_KEY);
		PIO.setFlickrConsumerSecret(PIOConstants.Flickr.CONSUMER_SECRET);

		PIO.setDropboxConsumerKey(PIOConstants.Dropbox.CONSUMER_KEY);
		PIO.setDropboxConsumerSecret(PIOConstants.Dropbox.CONSUMER_SECRET);

		PIO.setPhotobucketClientId(PIOConstants.Photobucket.CLIENT_ID);
		PIO.setPhotobucketClientSecret(PIOConstants.Photobucket.CLIENT_SECRET);

		PIO.setAmazonAccessKey(PIOConstants.Amazon.ACCESS_KEY);
		PIO.setAmazonSecretKey(PIOConstants.Amazon.SECRET_KEY);

		PIO.setPayPalAppId(PIOConstants.PayPal.APP_ID);
		PIO.setPayPalClientId(PIOConstants.PayPal.CLIENT_ID);
		PIO.setPayPalReceiverEmail(PIOConstants.PayPal.RECEIVERS_MAIL);
		PIO.setPayPalFeePayer(PIOConstants.PayPal.FEE_PAYER);

		PIO.setParseApplicationId(PIOConstants.Parse.APPLICATION_ID);
		PIO.setParseClientKey(PIOConstants.Parse.CLIENT_KEY);
		PIO.initializeParse(this);
		//ACRA.init(this);
	}

}
