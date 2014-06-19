package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.Arrays;

import print.io.Constants;
import print.io.PIO;
import print.io.PIO.PhotoSource;
import print.io.PIOCallback;
import print.io.PIOException;
import print.io.beans.CallbackInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

	private String[] images;
	private ArrayList<String> imageLists = new ArrayList<String>();

	private static final int IMAGES_CURSOR_LOADER = 0xA1;

	ArrayList<PhotoSource> photoSourcesTest = new ArrayList<PIO.PhotoSource>();

	public static PIOCallback callback = new PIOCallback() {
		@Override
		public void onCartChange(int count) {

		}

		@Override
		public void onOrderComplete(CallbackInfo callBackInfo) {
			Log.d("Order Information: ", callBackInfo.toString());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getLoaderManager().initLoader(
				IMAGES_CURSOR_LOADER
				, null
				, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this
				).forceLoad();

		Switch photoSourcesSwitch = (Switch) findViewById(R.id.switch_photo_sources);
		final Button buttonAddSource = (Button) findViewById(R.id.button_add_source);
		buttonAddSource.setEnabled(photoSourcesSwitch.isChecked());
		photoSourcesSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				photoSourcesTest.clear();
				buttonAddSource.setEnabled(isChecked);
				if (!isChecked) {
					//addDefaultPhotoSources();
					((TextView) findViewById(R.id.textView_photo_sources)).setText("");
				}
			}
		});
	}

	public static String[] getNames(Class<? extends Enum<?>> e) {
		return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
	}

	public void onClickAddPhotoSource(View v) {
		final TextView photoSourcesText = (TextView) findViewById(R.id.textView_photo_sources);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				photoSourcesTest.add(PhotoSource.values()[which]);
				photoSourcesText.setText(photoSourcesText.getText() + "  |  " + PhotoSource.values()[which]);
			}
		};
		builder.setTitle("Pick source").setItems(getNames(PhotoSource.class), onClickListener);
		builder.show();
	}

	private void addDefaultPhotoSources() {
		// Only up to 6
		photoSourcesTest.add(PhotoSource.PHONE);
		photoSourcesTest.add(PhotoSource.INSTAGRAM);
		photoSourcesTest.add(PhotoSource.FACEBOOK);
		photoSourcesTest.add(PhotoSource.FLICKR);
		photoSourcesTest.add(PhotoSource.PHOTOBUCKET);
		photoSourcesTest.add(PhotoSource.DROPBOX);
		//photoSourcesTest.add(PhotoSource.PICASA);
	}

	@Override
	public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		Uri uriPhoneImages = Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = {
				MediaStore.Images.ImageColumns._ID
				, MediaStore.Images.ImageColumns.DATA
				, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
		};
		String sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
		return new android.content.CursorLoader(this, uriPhoneImages, projection, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
		images = new String[3];
		int index = 0;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (index >= images.length) {
					break;
				}
				images[index] = cursor.getString(1);
				index++;
			}
		}
	}

	@Override
	public void onLoaderReset(android.content.Loader<Cursor> arg0) {
	}

	public void onClickAddImageToSDK(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			imageLists.add(getPath(data.getData()));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
	public String getPath(Uri uri) {
		// just some safety built in 
		if (uri == null) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}

	public void onClickStartSDK(View v) {
		Log.d("Facebook", "ID:"+getString(R.string.facebook_app_id));

		images = new String[imageLists.size()];
		images = imageLists.toArray(images);
		PIO.setSideMenuEnabled(false);
		PIO.setCanUseUpload(((CheckBox) findViewById(R.id.checkImage)).isChecked());
		//		for (String string : images) {
		//			L.d("image array", string);
		//		}
		PIO.setImageUrls(images);
		PIO.setPassedImageFirstInPhotoSources(((Switch) findViewById(R.id.switch_set_passed_image_first_in_photo_sources)).isChecked());

		String coutry = ((EditText) findViewById(R.id.editCountry)).getText().toString();
		if (coutry.length() == 2) {
			PIO.setCountryCode(coutry);
		}
		String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
		PIO.setPartnerName(name);

		String colorString = ((EditText) findViewById(R.id.editColorHex)).getText().toString();
		try {
			int colorHex = Color.parseColor("#"+colorString);
			PIO.setHeaderColor(colorHex);
		} catch (NumberFormatException e) { 
		}

		boolean isSideMenuEnabled = ((CheckBox) findViewById(R.id.sidemenu)).isChecked();
		PIO.setSideMenuEnabled(isSideMenuEnabled);
		boolean isFullScreen = ((CheckBox) findViewById(R.id.full_Screen)).isChecked();
		PIO.setHideStatusBar(isFullScreen);
		boolean autoArrange = ((CheckBox) findViewById(R.id.auto_arrange)).isChecked();
		PIO.setAutoArrange(autoArrange);

		PIO.setSdkDemo(true);
		PIO.setCountryOnFeaturedProducts(((Switch) findViewById(R.id.switch_country_drop_down)).isChecked());
		PIO.setPassedImageThumb(((Switch) findViewById(R.id.switch_passed_image_thumb)).isChecked());
		PIO.setHideCategorySearchBar(((Switch) findViewById(R.id.switch_hide_category_search_bar)).isChecked());
		PIO.setStepByStep(((Switch) findViewById(R.id.switch_step_by_step)).isChecked());

		PIO.setScreenIdFromApp(((Switch) findViewById(R.id.switch_jump_to_shopping_cart)).isChecked()?Constants.ScreenIds.SCREEN_SHOPPING_CART:-1);

		boolean coastersDiff = ((Switch) findViewById(R.id.switch_coasters_different)).isChecked();
		boolean coastersDuplicate = ((Switch) findViewById(R.id.switch_coasters_duplicate)).isChecked();
		//		if (coastersDiff != coastersDuplicate) {
		//			PIO.setCoastersType(coastersDiff?Constants.CaseOptions.COASTERS_4_DIFFERENT:Constants.CaseOptions.COASTERS_1_DUPLICATED);
		//		}

		//		//@milos example of jumping to certain product/sku. sku has priority
		//		if (((Switch) findViewById(R.id.switch_jump_to_sku)).isChecked()) {
		//			//PIO.setIdAndSku(Constants.ProductIds.COASTERS, Integer.toString(Constants.CaseOptions.COASTERS_4_DIFFERENT));
		//			//PIO.setIdAndSku(Constants.ProductIds.FLEECE_BLANKETS, "FleeceBlanket_60x80");
		//			PIO.setIdAndSku(Constants.ProductIds.TABLET_CASES, "TabletCase-iPad3/4-Gloss");
		//			//PIO.setIdAndSku(Constants.ProductIds.PHONE_CASES, "PhoneCase-GalaxyNote2-Matte");
		//		} else if (((Switch) findViewById(R.id.switch_jump_to_phone_cases)).isChecked()) {
		//			PIO.setProductIdFromApp(Constants.ProductIds.PHONE_CASES);
		//		} else {
		//			PIO.setIdAndSku(-1, null);
		//		}

		PIO.setShowHelp(((Switch) findViewById(R.id.switch_hide_help)).isChecked());

		boolean isLive = ((ToggleButton) findViewById(R.id.toggleButtonProduction)).isChecked();

		PIO.setShowPhotosInCustomize(((Switch) findViewById(R.id.switch_show_photos_customize)).isChecked());
		PIO.setShowOptionsInCustomize(((Switch) findViewById(R.id.switch_show_options_customize)).isChecked());
		boolean isRotateEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_rotate_enabled_in_crop_screen)).isChecked();
		boolean isTextEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_text_enabled_in_crop_screen)).isChecked();
		boolean isEffectsEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_effects_enabled_in_crop_screen)).isChecked();
		PIO.setUpCropScreen(isRotateEnabledInCropScreen, isTextEnabledInCropScreen, isEffectsEnabledInCropScreen);

		if (photoSourcesTest.size() == 0 ) {
			addDefaultPhotoSources();
		}
		if (photoSourcesTest.size() != 0) {
			PIO.setPhotoSources(photoSourcesTest);
		}
		PIO.setLiveApplication(isLive);

		String recipeId;
		String braintreeEncryptionKey;
		if (isLive) {
			recipeId = PIOConstants.RECIPE_ID_LIVE;
			braintreeEncryptionKey = PIOConstants.Braintree.ENCRYPTION_KEY_LIVE;
		} else {
			recipeId = PIOConstants.RECIPE_ID_STAGING;
			braintreeEncryptionKey = PIOConstants.Braintree.ENCRYPTION_KEY_STAGING;
		}
		PIO.setRecipeID(recipeId);
		PIO.setBraintreeEncryptionKey(braintreeEncryptionKey);

		String apiUrl;
		if (PIO.isLiveApplication() || PIO.isLiveTestingApplication()) {
			apiUrl = PIOConstants.API_URL_LIVE;
		} else {
			apiUrl = PIOConstants.API_URL_STAGING;
		}
		PIO.setApiUrl(apiUrl);

		try {
			PIO.start(this, callback);
		} catch (PIOException e) {
			e.printStackTrace();
		}
	}

}
