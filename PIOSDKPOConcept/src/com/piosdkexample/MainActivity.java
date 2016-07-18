package com.piosdkexample;

import print.io.PIO;
import print.io.PIOConfig;
import print.io.PIOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	private PhotoSourceFactory psFactory = new PhotoSourceFactory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClickStartSDK(View v) {
		// Create PIOConfig object (with default configuration) which will be used to configure SDK
		PIOConfig config = new PIOConfig();

		// Set your Gooten Recipe ID key (provided for every partner after sign up)
		// This is mandatory configuration. Please set valid key.
		config.setRecipeID(PIOConstants.RECIPE_ID_LIVE);

		// --------------------------- Optional configuration --------------------------- //
		// Here we are going to show you some optional configuration options for SDK.
		// You can fit these configuration to your needs.


		// If you would like to test order purchase process without charging you 
		// real money you can enable "order testing" like so:
		config.setOrderTesting(true);

		// Set available photo sources.
		// In this example we use PhotoSourceFactory to encapsulate photo source creation.
		config.setPhotoSources(psFactory.getAll());

		// Needed for Facebook photo source
		// Also "facebook_app_id" must be defined in strings.xml
		config.setFacebookAppId(getString(R.string.facebook_app_id));

		// When user wants to exit SDK, where do we want to navigate him/her?
		// In this example we will open this screen.
		config.setHostAppActivity(getComponentName().getClassName());

		config.setHelpUrl(PIOConstants.HELP_URL);
		config.setSupportEmail(PIOConstants.SUPPORT_EMAIL);
		config.setGooglePlayRateUrl(PIOConstants.GOOGLE_PLAY_RATE_URL);
		config.setFacebookPageUrl(PIOConstants.FACEBOOK_PAGE_URL);

		// --------------------------- END Optional configuration --------------------------- //

		// Launch SDK
		try {
			PIO.setConfig(this, config);
			PIO.start(this);
		} catch (PIOException e) {
			// Exception could be thrown if configuration is invalid.
			// You can look at log to see warning messages about configuration (if any).
			e.printStackTrace();
		}
	}

}