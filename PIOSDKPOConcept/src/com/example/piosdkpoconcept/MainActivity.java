package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.Arrays;

import print.io.PIO;
import print.io.PIO.PhotoSource;
import print.io.PIO.SideMenuButton;
import print.io.PIO.SideMenuInfoButton;
import print.io.PIOCallback;
import print.io.PIOException;
import print.io.PublicConstants;
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

public class MainActivity extends Activity {

	private ArrayList<String> imageUris = new ArrayList<String>();

	private ArrayList<SideMenuButton> sideMenuButtonsTop = new ArrayList<PIO.SideMenuButton>();
	private ArrayList<PhotoSource> photoSourcesTest = new ArrayList<PIO.PhotoSource>();
	private ArrayList<SideMenuInfoButton> sideMenuInfoButtons = new ArrayList<PIO.SideMenuInfoButton>();

	private EditText editTextAddImageToSdk;

	private Spinner spinnerPaymentOptions;

	private Spinner spinnerJumpToProduct;
	private Switch switchSkipProductDetailsScreen;

	private Switch switchHideComingSoonProducts;

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

		//@milos on resume from print.io sdk
		ArrayList<String> cartItems = getIntent().getStringArrayListExtra("ShoppingCartItems");
		if (cartItems != null) {
			StringBuilder stringBuilder = new StringBuilder("Feedback to host app: ");
			stringBuilder.append("\nShopping Cart Items Quantity:\n").append(Integer.toString(cartItems.size()));

			if(cartItems.size() > 0) {
				stringBuilder.append("\n").append("Content of Shopping Cart:");
				for(String s : cartItems) {
					stringBuilder.append("\n").append(s);
				}
			}

			View feedbackDialog = findViewById(R.id.dialog_feedback);
			((TextView)feedbackDialog.findViewById(R.id.textview_feedback)).setText(stringBuilder.toString());
			feedbackDialog.setVisibility(View.VISIBLE);
		}

		TextView textViewItemsInCart = (TextView) findViewById(R.id.textview_items_in_cart);
		textViewItemsInCart.setText("Items in shopping cart: "+PIO.getNumberOfItemsInShoppingCart(MainActivity.this));
	}

	private void initSdkMode(boolean isLive) {
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

		if (PIO.isLiveApplication() || PIO.isLiveTestingApplication()) {
			PIO.setApiUrl(PublicConstants.API_URL_LIVE);
		} else {
			PIO.setApiUrl(PublicConstants.API_URL_STAGING);
		}

		spinnerJumpToProduct.setAdapter(new SpinnerAdapterJumpToProduct(MainActivity.this));
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

	private void addAllSideMenuButtonsTop() {
		sideMenuButtonsTop.add(SideMenuButton.SEARCH_BAR);
		sideMenuButtonsTop.add(SideMenuButton.EXIT_BUTTON);
		sideMenuButtonsTop.add(SideMenuButton.FEATURED_PRODUCTS);
		sideMenuButtonsTop.add(SideMenuButton.PRODUCTS);
		sideMenuButtonsTop.add(SideMenuButton.HELP);
		sideMenuButtonsTop.add(SideMenuButton.VIEW_CART);
		sideMenuButtonsTop.add(SideMenuButton.EMAIL_SUPPORT);
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
			//imageLists.add(getPath(data.getData()));
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

		PIO.setSideMenuEnabled(false);
		PIO.setPhotosourcesDisabled(((CheckBox) findViewById(R.id.checkboxDisablePhotosources)).isChecked());

		PIO.setImageUris(imageUris);
		PIO.setPassedImageFirstInPhotoSources(((Switch) findViewById(R.id.switch_set_passed_image_first_in_photo_sources)).isChecked());

		PIO.hidePhotoSourcesInSideMenu(((Switch) findViewById(R.id.switch_hide_photo_sources_side_menu)).isChecked());

		String country = ((EditText) findViewById(R.id.editCountry)).getText().toString();
		if (country.length() == 2) {
			PIO.setCountryCode(country);
		}

		PIO.setChangeableCountry(((Switch) findViewById(R.id.switch_changeable_country)).isChecked());

		String currency = ((EditText) findViewById(R.id.editCurrency)).getText().toString();
		if (currency.length() == 3) {
			PIO.setCurrencyCode(currency);
		}

		PIO.setChangeableCurrency(((Switch) findViewById(R.id.switch_changeable_currency)).isChecked());

		String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
		PIO.setPartnerName(name);

		String colorString = ((EditText) findViewById(R.id.editColorHex)).getText().toString();
		try {
			int colorHex = Color.parseColor("#"+colorString);
			PIO.setHeaderColor(colorHex);
		} catch (NumberFormatException e) { 
		}

		PIO.useThreeButtonsBarStyle(((Switch) findViewById(R.id.switch_three_buttons_bar_style)).isChecked());

		boolean isSideMenuEnabled = ((CheckBox) findViewById(R.id.checkbox_enable_side_menu)).isChecked();
		PIO.setSideMenuEnabled(isSideMenuEnabled);
		PIO.setRightSideMenu(((Switch) findViewById(R.id.switch_right_side_menu)).isChecked());
		PIO.setMenuIconGear(((Switch) findViewById(R.id.switch_menu_icon_gear)).isChecked());
		boolean isFullScreen = ((CheckBox) findViewById(R.id.full_Screen)).isChecked();
		PIO.setHideStatusBar(isFullScreen);
		boolean autoArrange = ((CheckBox) findViewById(R.id.auto_arrange)).isChecked();
		PIO.setAutoArrange(autoArrange);

		PIO.setSdkDemo(true);
		PIO.setSdkAppearsFromRight(((Switch) findViewById(R.id.switch_appear_from_right)).isChecked());
		//PIO.setMenuIconGear(((Switch) findViewById(R.id.switch_gear)).isChecked());
		PIO.setCountryOnFeaturedProducts(((Switch) findViewById(R.id.switch_country_drop_down)).isChecked());
		PIO.setPassedImageThumb(((Switch) findViewById(R.id.switch_passed_image_thumb)).isChecked());
		PIO.setHideCategorySearchBar(((Switch) findViewById(R.id.switch_hide_category_search_bar)).isChecked());
		PIO.setStepByStep(((Switch) findViewById(R.id.switch_step_by_step)).isChecked());

		PIO.setHostAppActivity(getComponentName().getClassName());//"com.example.piosdkpoconcept.ActivityTest"

		int jumpToScreenId = -1;
		if (((Switch) findViewById(R.id.switch_jump_to_shopping_cart)).isChecked()) {
			jumpToScreenId = PublicConstants.ScreenIds.SCREEN_SHOPPING_CART;
		}
		int flags = 0;
		if (((Switch) findViewById(R.id.switch_back_goes_to_featured_products)).isChecked()) {
			flags |= PublicConstants.Flags.FLAG_GO_BACK_TO_FEATURED_PRODUCTS;
		}
		PIO.setJumpToScreen(jumpToScreenId, flags);

		//		boolean coastersDiff = ((Switch) findViewById(R.id.switch_coasters_different)).isChecked();
		//		boolean coastersDuplicate = ((Switch) findViewById(R.id.switch_coasters_duplicate)).isChecked();
		//		if (coastersDiff != coastersDuplicate) {
		//			PIO.setCoastersType(coastersDiff?Constants.CaseOptions.COASTERS_4_DIFFERENT:Constants.CaseOptions.COASTERS_1_DUPLICATED);
		//		}

		PIO.setShowHelp(((Switch) findViewById(R.id.switch_hide_help)).isChecked());
		PIO.hideHelpButtonInCustomizeProduct(!((Switch) findViewById(R.id.switch_hide_help_customize_product)).isChecked());

		PIO.removePlusFromAddMoreProductsButton(((Switch) findViewById(R.id.switch_remove_plus_from_add_more_products)).isChecked());
		PIO.removeLogoFromPaymentScreen(((Switch) findViewById(R.id.switch_remove_logo_on_payment)).isChecked());

		PIO.setShowPhotosInCustomize(((Switch) findViewById(R.id.switch_show_photos_customize)).isChecked());
		PIO.setShowOptionsInCustomize(((Switch) findViewById(R.id.switch_show_options_customize)).isChecked());
		boolean isRotateEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_rotate_enabled_in_crop_screen)).isChecked();
		boolean isTextEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_text_enabled_in_crop_screen)).isChecked();
		boolean isEffectsEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_effects_enabled_in_crop_screen)).isChecked();
		PIO.setUpCropScreen(isRotateEnabledInCropScreen, isTextEnabledInCropScreen, isEffectsEnabledInCropScreen);

		PIO.hideEditButtonInShoppingCart(((Switch) findViewById(R.id.switch_hide_edit_button)).isChecked());

		if (sideMenuButtonsTop.size() == 0) {
			addAllSideMenuButtonsTop();
		}
		PIO.setSideMenuButtonsTop(sideMenuButtonsTop);

		if (photoSourcesTest.size() == 0 ) {
			addDefaultPhotoSources();
		}
		if (photoSourcesTest.size() != 0) {
			PIO.setPhotoSources(photoSourcesTest);
			PIO.setDefaultPhotoSource(photoSourcesTest.get(0));
		}

		if (sideMenuInfoButtons.size() == 0) {
			addAllSideMenuInfoButtons();
		}
		PIO.setSideMenuInfoButtons(sideMenuInfoButtons);

		PIO.setHideComingSoonProducts(switchHideComingSoonProducts.isChecked());

		int selectedPaymentOptions = (int) spinnerPaymentOptions.getSelectedItemId();
		PIO.setPaymentOptions(selectedPaymentOptions);

		int selectedProductId = (int) spinnerJumpToProduct.getSelectedItemId();
		if (selectedProductId != SpinnerAdapter.NO_SELECTION) {
			PIO.setProductIdFromApp(selectedProductId);
		} else {
			PIO.setProductIdFromApp(-1);
		}

		PIO.setSkipProductDetails(switchSkipProductDetailsScreen.isChecked());

		String promoCode = ((EditText) findViewById(R.id.edittext_promo_code)).getText().toString();
		if (promoCode.equals("")) {
			PIO.setPromoCode(null);
		} else {
			PIO.setPromoCode(promoCode);
		}

		try {
			PIO.start(this, callback);
		} catch (PIOException e) {
			e.printStackTrace();
		}
	}

	public void onClickOkFeedback(View v) {
		findViewById(R.id.dialog_feedback).setVisibility(View.GONE);
	}

}