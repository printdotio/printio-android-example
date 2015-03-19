package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import print.io.PIO;
import print.io.PIOConfig;
import print.io.PIOException;
import print.io.PublicConstants;
import print.io.piopublic.ProductType;
import print.io.piopublic.SideMenuButton;
import print.io.piopublic.SideMenuInfoButton;
import print.io.photosource.PhotoSource;
import print.io.photosource.impl.dropbox.DropboxPhotoSource;
import print.io.photosource.impl.facebook.FacebookPhotoSource;
import print.io.photosource.impl.flickr.FlickrPhotoSource;
import print.io.photosource.impl.instagram.InstagramPhotoSource;
import print.io.photosource.impl.phone.PhonePhotoSource;
import print.io.photosource.impl.photobucket.PhotobucketPhotoSource;
import print.io.photosource.impl.picasa.PicasaPhotoSource;
import print.io.photosource.impl.preselected.PreselectedPhotoSource;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.piosdkpoconcept.photosource.vlado.VladoPhotoSource;

public class MainActivity extends Activity {

	private static final int DROPBOX_INDEX = 0;
	private static final int FACEBOOK_INDEX = 1;
	private static final int FLICKR_INDEX = 2;
	private static final int INSTAGRAM_INDEX = 3;
	private static final int PHONE_INDEX = 4;
	private static final int PHOTOBUCKET_INDEX = 5;
	private static final int PICASA_INDEX = 6;
	private static final int PRESELECTED_INDEX = 7;
	private static final int VLADO_PHOTO = 8;
	private static final List<PhotoSource> ALL_SOURCES;
	static {
		DropboxPhotoSource dropboxPhotoSource = new DropboxPhotoSource();
		dropboxPhotoSource.setConsumerKey(PIOConstants.Dropbox.CONSUMER_KEY);
		dropboxPhotoSource.setConsumerSecret(PIOConstants.Dropbox.CONSUMER_SECRET);

		InstagramPhotoSource instagramPhotoSource = new InstagramPhotoSource();
		instagramPhotoSource.setClientId(PIOConstants.Instagram.CLIENT_ID);
		instagramPhotoSource.setCallbackUri(PIOConstants.Instagram.CALLBACK_URI);

		FlickrPhotoSource flickrPhotoSource = new FlickrPhotoSource();
		flickrPhotoSource.setConsumerKey(PIOConstants.Flickr.CONSUMER_KEY);
		flickrPhotoSource.setConsumerSecret(PIOConstants.Flickr.CONSUMER_SECRET);

		PhotobucketPhotoSource photobucketPhotoSource = new PhotobucketPhotoSource();
		photobucketPhotoSource.setClientId(PIOConstants.Photobucket.CLIENT_ID);
		photobucketPhotoSource.setClientSecret(PIOConstants.Photobucket.CLIENT_SECRET);

		ALL_SOURCES = Collections.unmodifiableList(Arrays.asList(
				dropboxPhotoSource,
				new FacebookPhotoSource(),
				flickrPhotoSource,
				instagramPhotoSource,
				new PhonePhotoSource(),
				photobucketPhotoSource,
				new PicasaPhotoSource(),
				new PreselectedPhotoSource(),
				new VladoPhotoSource()));
	}

	private EditText editTextAddImageToSdk;
	private Spinner spinnerPaymentOptions;
	private Spinner spinnerJumpToProduct;
	private Switch switchSkipProductDetailsScreen;
	private Switch switchHideComingSoonProducts;

	private PIOConfig config = new PIOConfig();

	private List<String> imageUris = new ArrayList<String>();
	private List<SideMenuButton> sideMenuButtonsTop = new ArrayList<SideMenuButton>();
	private List<PhotoSource> photoSourcesTest = new ArrayList<PhotoSource>();
	private List<SideMenuInfoButton> sideMenuInfoButtons = new ArrayList<SideMenuInfoButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Switch photoSourcesSwitch = (Switch) findViewById(R.id.switch_photo_sources);
		final Button buttonAddSource = (Button) findViewById(R.id.button_add_source);
		buttonAddSource.setEnabled(photoSourcesSwitch.isChecked());
		photoSourcesSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				photoSourcesTest.clear();
				buttonAddSource.setEnabled(isChecked);
				if (!isChecked) {
					((TextView) findViewById(R.id.textView_photo_sources)).setText("");
				}
			}
		});

		editTextAddImageToSdk = (EditText) findViewById(R.id.edittext_add_image_to_sdk);
		spinnerJumpToProduct = (Spinner) findViewById(R.id.spinner_jump_to_product);
		initSdkMode(false);
		switchSkipProductDetailsScreen = (Switch) findViewById(R.id.switch_skip_product_details_screen);
		switchHideComingSoonProducts = (Switch) findViewById(R.id.switch_hide_coming_soon_products);
		spinnerPaymentOptions = (Spinner) findViewById(R.id.spinner_payment_options);
		spinnerPaymentOptions.setAdapter(new SpinnerAdapterPaymentOptions(MainActivity.this));

		((ToggleButton) findViewById(R.id.toggleButtonProduction)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				initSdkMode(isChecked);
			}
		});

		config.setFontPathInAssetsLight("HelveticaNeueLTStd-Lt.otf");
		config.setFontPathInAssetsNormal("HelveticaNeueLTStd-Roman.otf");
		config.setFontPathInAssetsBold("HelveticaNeueLTStd-Bd.otf");
		config.setPartnerName(getResources().getString(R.string.hellopics));
		config.setHelpUrl(PIOConstants.HELP_URL);
		config.setSupportEmail(PIOConstants.SUPPORT_EMAIL);
		config.setGooglePlayRateUrl(PIOConstants.GOOGLE_PLAY_RATE_URL);
		config.setFacebookPageUrl(PIOConstants.FACEBOOK_PAGE_URL);
		config.setFacebookAppId(getString(R.string.facebook_app_id));
		config.setGoogleAnalyticsTrackId("UA-28619845-2");
	}

	private void initSdkMode(boolean isLive) {
		config.setLiveApplication(isLive);

		String recipeId;
		String braintreeEncryptionKey;
		String payPalClientId;
		if (isLive) {
			recipeId = PIOConstants.RECIPE_ID_LIVE;
			braintreeEncryptionKey = PIOConstants.Braintree.ENCRYPTION_KEY_LIVE;
			payPalClientId = PIOConstants.PayPal.CLIENT_ID_LIVE;
		} else {
			recipeId = PIOConstants.RECIPE_ID_STAGING;
			braintreeEncryptionKey = PIOConstants.Braintree.ENCRYPTION_KEY_STAGING;
			payPalClientId = PIOConstants.PayPal.CLIENT_ID_STAGING;
		}
		config.setRecipeID(recipeId);
		config.setBraintreeEncryptionKey(braintreeEncryptionKey);
		config.setPayPalClientId(payPalClientId);

		if (config.isLiveApplication() || config.isLiveTestingApplication()) {
			config.setApiUrl(PublicConstants.API_URL_LIVE);
		} else {
			config.setApiUrl(PublicConstants.API_URL_STAGING);
		}

		spinnerJumpToProduct.setAdapter(new SpinnerAdapterJumpToProduct(this));
	}

	private String[] getNames() {
		List<String> names = new ArrayList<String>(ALL_SOURCES.size());
		for (PhotoSource ps : ALL_SOURCES) {
			names.add(ps.getName(this));
		}
		return names.toArray(new String[names.size()]);
	}

	public void onClickAddPhotoSource(View v) {
		final TextView photoSourcesText = (TextView) findViewById(R.id.textView_photo_sources);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				photoSourcesTest.add(ALL_SOURCES.get(which));
				photoSourcesText.setText(photoSourcesText.getText() + "  |  " + ALL_SOURCES.get(which).getName(MainActivity.this));
			}
		};
		builder.setTitle("Pick source").setItems(getNames(), onClickListener);
		builder.show();
	}

	private void addAllSideMenuButtonsTop() {
		sideMenuButtonsTop.add(SideMenuButton.SEARCH_BAR);
		sideMenuButtonsTop.add(SideMenuButton.EXIT);
		sideMenuButtonsTop.add(SideMenuButton.FEATURED_PRODUCTS);
		sideMenuButtonsTop.add(SideMenuButton.PRODUCTS);
		sideMenuButtonsTop.add(SideMenuButton.HELP);
		sideMenuButtonsTop.add(SideMenuButton.VIEW_CART);
		sideMenuButtonsTop.add(SideMenuButton.EMAIL_SUPPORT);
	}

	private void addDefaultPhotoSources() {
		// Only up to 6
		photoSourcesTest.add(ALL_SOURCES.get(PHONE_INDEX));
		photoSourcesTest.add(ALL_SOURCES.get(INSTAGRAM_INDEX));
		photoSourcesTest.add(ALL_SOURCES.get(FACEBOOK_INDEX));
		//		photoSourcesTest.add(ALL_SOURCES.get(FLICKR_INDEX));
		//		photoSourcesTest.add(ALL_SOURCES.get(PHOTOBUCKET_INDEX));
		//		photoSourcesTest.add(ALL_SOURCES.get(DROPBOX_INDEX));

		//		photoSourcesTest.add(ALL_SOURCES.get(PICASA_INDEX));
		photoSourcesTest.add(ALL_SOURCES.get(PRESELECTED_INDEX));
	}

	private void addAllSideMenuInfoButtons() {
		sideMenuInfoButtons.add(SideMenuInfoButton.PRICING_CHART);
		sideMenuInfoButtons.add(SideMenuInfoButton.SHARE_APP);
		sideMenuInfoButtons.add(SideMenuInfoButton.LIKE_US_FB);
		sideMenuInfoButtons.add(SideMenuInfoButton.RATE_APP);
		sideMenuInfoButtons.add(SideMenuInfoButton.ABOUT);
		sideMenuInfoButtons.add(SideMenuInfoButton.HOW_IT_WORKS);
		sideMenuInfoButtons.add(SideMenuInfoButton.PAST_ORDERS);
	}

	public void onClickAddImageToSDK(View v) {
		if (!editTextAddImageToSdk.getText().toString().isEmpty()) {
			imageUris.add(editTextAddImageToSdk.getText().toString());
			editTextAddImageToSdk.setText("");
			Toast.makeText(this, "Image URL added", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(intent, 1);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			imageUris.add(data.getData().toString());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		// just some safety built in 
		if (uri == null) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = {
				MediaStore.Images.Media.DATA
		};
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
		Log.d("Facebook", "ID:" + getString(R.string.facebook_app_id));

		config.setSideMenuEnabled(false);
		config.setPhotoSourcesDisabled(((CheckBox) findViewById(R.id.checkboxDisablePhotosources)).isChecked());
		config.setImageUris(imageUris);
		config.hidePhotoSourcesInSideMenu(((Switch) findViewById(R.id.switch_hide_photo_sources_side_menu)).isChecked());
		config.setCountryCode(((EditText) findViewById(R.id.editCountry)).getText().toString());
		config.setChangeableCountry(((Switch) findViewById(R.id.switch_changeable_country)).isChecked());
		config.setCurrencyCode(((EditText) findViewById(R.id.editCurrency)).getText().toString());
		config.setChangeableCurrency(((Switch) findViewById(R.id.switch_changeable_currency)).isChecked());
		config.setPartnerName(((EditText) findViewById(R.id.editTextName)).getText().toString());
		try {
			String colorString = ((EditText) findViewById(R.id.editColorHex)).getText().toString();
			config.setHeaderColor(Color.parseColor("#" + colorString));
		} catch (NumberFormatException e) {}

		config.useThreeButtonsBarStyle(((Switch) findViewById(R.id.switch_three_buttons_bar_style)).isChecked());

		config.setSideMenuEnabled(((CheckBox) findViewById(R.id.checkbox_enable_side_menu)).isChecked());
		config.setRightSideMenu(((Switch) findViewById(R.id.switch_right_side_menu)).isChecked());
		config.setMenuIconGear(((Switch) findViewById(R.id.switch_menu_icon_gear)).isChecked());
		config.setHideStatusBar(((CheckBox) findViewById(R.id.full_Screen)).isChecked());
		config.setAutoArrange(((CheckBox) findViewById(R.id.auto_arrange)).isChecked());
		config.setSdkDemo(true);
		config.setSdkAppearsFromRight(((Switch) findViewById(R.id.switch_appear_from_right)).isChecked());
		//PIO.setMenuIconGear(((Switch) findViewById(R.id.switch_gear)).isChecked());
		config.setPassedImageThumb(((Switch) findViewById(R.id.switch_passed_image_thumb)).isChecked());
		// Featured products screen
		config.setCountryOnFeaturedProducts(((Switch) findViewById(R.id.switch_country_drop_down)).isChecked());
		config.setHideCategorySearchBar(((Switch) findViewById(R.id.switch_hide_category_search_bar)).isChecked());
		config.setShowFeaturedProductsByDefault(((Switch) findViewById(R.id.default_products_screen_featured)).isChecked());
		// Shopping cart screen
		config.removePlusFromAddMoreProductsButton(((Switch) findViewById(R.id.switch_remove_plus_from_add_more_products)).isChecked());
		config.setShowAddMoreProductsInShoppingCart(((Switch) findViewById(R.id.switch_show_add_more_products)).isChecked());
		config.hideEditButtonInShoppingCart(((Switch) findViewById(R.id.switch_hide_edit_button)).isChecked());
		config.closeWidgetFromShoppingCart(((Switch) findViewById(R.id.closeWidgetFromShoppingCart)).isChecked());

		config.setStepByStep(((Switch) findViewById(R.id.switch_step_by_step)).isChecked());
		config.setHostAppActivity(getComponentName().getClassName());//"com.example.piosdkpoconcept.ActivityTest"

		int jumpToScreenId = -1;
		if (((Switch) findViewById(R.id.switch_jump_to_shopping_cart)).isChecked()) {
			jumpToScreenId = PublicConstants.ScreenIds.SCREEN_SHOPPING_CART;
		}
		int flags = 0;
		if (((Switch) findViewById(R.id.switch_back_goes_to_featured_products)).isChecked()) {
			flags |= PublicConstants.Flags.FLAG_GO_BACK_TO_FEATURED_PRODUCTS;
		}
		config.setJumpToScreen(jumpToScreenId, flags);

		boolean coastersDiff = ((Switch) findViewById(R.id.switch_coasters_different)).isChecked();
		boolean coastersDuplicate = ((Switch) findViewById(R.id.switch_coasters_duplicate)).isChecked();
		config.setCoastersType(-1); // Reset from previous launch
		if (coastersDiff != coastersDuplicate) {
			config.setCoastersType(coastersDiff ? PublicConstants.CoastersTypes.COASTERS_4_DIFFERENT : PublicConstants.CoastersTypes.COASTERS_1_DUPLICATED);
		}

		config.setShowHelp(((Switch) findViewById(R.id.switch_hide_help)).isChecked());
		config.hideHelpButtonInCustomizeProduct(!((Switch) findViewById(R.id.switch_hide_help_customize_product)).isChecked());

		config.removeLogoFromPaymentScreen(((Switch) findViewById(R.id.switch_remove_logo_on_payment)).isChecked());

		config.enablePhotoSourcesInCustomizeProduct(((Switch) findViewById(R.id.switch_show_enable_photosource_when_disabled)).isChecked());
		config.setShowPhotosInCustomize(((Switch) findViewById(R.id.switch_show_photos_customize)).isChecked());
		config.setShowOptionsInCustomize(((Switch) findViewById(R.id.switch_show_options_customize)).isChecked());
		boolean isRotateEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_rotate_enabled_in_crop_screen)).isChecked();
		boolean isTextEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_text_enabled_in_crop_screen)).isChecked();
		boolean isEffectsEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_effects_enabled_in_crop_screen)).isChecked();
		config.setUpCropScreen(isRotateEnabledInCropScreen, isTextEnabledInCropScreen, isEffectsEnabledInCropScreen);

		if (sideMenuButtonsTop.size() == 0) {
			addAllSideMenuButtonsTop();
		}
		config.setSideMenuButtonsTop(sideMenuButtonsTop);

		if (photoSourcesTest.size() == 0) {
			addDefaultPhotoSources();
		}
		if (photoSourcesTest.size() != 0) {
			config.setPhotoSources(photoSourcesTest);
		}

		if (sideMenuInfoButtons.size() == 0) {
			addAllSideMenuInfoButtons();
		}
		config.setSideMenuInfoButtons(sideMenuInfoButtons);
		config.setHideComingSoonProducts(switchHideComingSoonProducts.isChecked());
		config.setPaymentOptions((int) spinnerPaymentOptions.getSelectedItemId());

		// Jump to product
		ProductType selectedProductIdType;
		int id = (int) spinnerJumpToProduct.getSelectedItemId();
		if (id == SpinnerAdapter.NO_SELECTION) {
			selectedProductIdType = null;
		} else {
			selectedProductIdType = ProductType.values()[id];
		}
		config.setProductFromApp(selectedProductIdType);
		config.setSkipProductDetails(switchSkipProductDetailsScreen.isChecked());
		config.setProductSkuFromApp(((EditText) findViewById(R.id.editSKU)).getText().toString());
		if (StringUtils.isNotBlank(config.getProductSkuFromApp()) && selectedProductIdType == null) {
			Toast.makeText(this, "Product must be specified when SKU is supplied", Toast.LENGTH_LONG).show();
			return;
		}

		String promoCode = ((EditText) findViewById(R.id.edittext_promo_code)).getText().toString();
		if (StringUtils.isBlank(promoCode)) {
			config.setPromoCode(null);
		} else {
			config.setPromoCode(promoCode);
		}

		if (config.isPhotosourcesDisabled() && ((config.getImageUris() == null) || (config.getImageUris().isEmpty()))) {
			Toast.makeText(this, "Photosources are disabled and no images were passed to the SDK", Toast.LENGTH_LONG).show();
			return;
		}

		try {
			PIO.setConfig(this, config);
			PIO.start(this);
		} catch (PIOException e) {
			e.printStackTrace();
		}
	}

	public void onClickOkFeedback(View v) {
		findViewById(R.id.dialog_feedback).setVisibility(View.GONE);
	}

}