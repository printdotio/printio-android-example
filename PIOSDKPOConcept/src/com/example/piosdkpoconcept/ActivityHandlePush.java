package com.example.piosdkpoconcept;

import print.io.PIO;
import print.io.PIOException;
import android.app.Activity;
import android.os.Bundle;

public class ActivityHandlePush extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// We can read properties from getIntent();
		try {
			PIO.start(this, MainActivity.callback);
		} catch (PIOException e) {
			e.printStackTrace();
		}
	}

}