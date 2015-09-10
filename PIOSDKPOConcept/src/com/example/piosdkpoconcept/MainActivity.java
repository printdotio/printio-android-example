package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.PIO;
import print.io.PIOConfig;
import print.io.PIOException;
import print.io.PublicConstants;
import print.io.beans.cart.ShoppingCart;
import print.io.photosource.PhotoSource;
import print.io.piopublic.PaymentOptionType;
import print.io.piopublic.ProductType;
import print.io.piopublic.Screen;
import print.io.piopublic.ScreenVersion;
import print.io.piopublic.SideMenuButton;
import print.io.piopublic.SideMenuInfoButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.piosdkpoconcept.adapters.SpinnerAdapterJumpToProduct;
import com.example.piosdkpoconcept.adapters.SpinnerAdapterJumpToScreen;
import com.example.piosdkpoconcept.adapters.SpinnerAdapterScreenVersion;

public class MainActivity extends Activity {

	private EditText editTextAddImageToSdk;
	private EditText editTextScreenProductImageUti;
	private Spinner spinnerJumpToProduct;
	private Spinner spinnerJumpToScreen;
	private Spinner spinnerNavigateBackToScreen;
	private Switch switchHideComingSoonProducts;

	private PIOConfig config = new PIOConfig();

	private List<String> imageUris = new ArrayList<String>();
	private List<SideMenuButton> sideMenuButtonsTop = new ArrayList<SideMenuButton>(Arrays.asList(SideMenuButton.values()));
	private List<SideMenuInfoButton> sideMenuInfoButtons = new ArrayList<SideMenuInfoButton>(Arrays.asList(SideMenuInfoButton.values()));
	private PhotoSourceFactory photoSourceFactory = new PhotoSourceFactory();
	private List<PhotoSource> allSources = photoSourceFactory.getAll();
	private List<PhotoSource> selectedPhotoSources = new ArrayList<PhotoSource>();
	private List<ProductType> selectedProductTypes = new ArrayList<ProductType>(config.getAvailableProducts());
	private List<PaymentOptionType> selectedPaymentOptions = new ArrayList<PaymentOptionType>(Arrays.asList(PaymentOptionType.values()));
	private List<Screen> screensWithCountryBar = new ArrayList<Screen>(Arrays.asList(Screen.PRODUCTS));
	private List<Screen> availableScreens = Arrays.asList(Screen.values());
	private ScreenVersion screenVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		config.setDisabledScreens(new ArrayList<Screen>());

		editTextAddImageToSdk = (EditText) findViewById(R.id.edittext_add_image_to_sdk);
		editTextScreenProductImageUti = (EditText) findViewById(R.id.edittext_screen_product_image_uri);
		spinnerJumpToProduct = (Spinner) findViewById(R.id.spinner_jump_to_product);
		spinnerJumpToProduct.setAdapter(new SpinnerAdapterJumpToProduct(this));
		spinnerJumpToScreen = (Spinner) findViewById(R.id.spinner_jump_to_screen);
		spinnerJumpToScreen.setAdapter(new SpinnerAdapterJumpToScreen(this, availableScreens));
		spinnerNavigateBackToScreen = (Spinner) findViewById(R.id.spinner_navigate_back_to_screen);
		spinnerNavigateBackToScreen.setAdapter(new SpinnerAdapterJumpToScreen(this, Arrays.asList(Screen.PRODUCTS)));
		Spinner spinnerScreenVersion = ((Spinner) findViewById(R.id.spinner_screen_version));
		spinnerScreenVersion.setAdapter(new SpinnerAdapterScreenVersion(this));
		spinnerScreenVersion.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				screenVersion = ScreenVersion.values()[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {}

		});
		spinnerScreenVersion.setSelection(0);
		switchHideComingSoonProducts = (Switch) findViewById(R.id.switch_hide_coming_soon_products);

		((ToggleButton) findViewById(R.id.toggleButtonProduction)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				initSdkMode(isChecked);
			}
		});

		config.setPartnerName(getResources().getString(R.string.hellopics));
		config.setHelpUrl(PIOConstants.HELP_URL);
		config.setSupportEmail(PIOConstants.SUPPORT_EMAIL);
		config.setGooglePlayRateUrl(PIOConstants.GOOGLE_PLAY_RATE_URL);
		config.setFacebookPageUrl(PIOConstants.FACEBOOK_PAGE_URL);
		config.setFacebookAppId(getString(R.string.facebook_app_id));

		addDefaultPhotoSources();
		initSdkMode(false);
	}

	private void addDefaultPhotoSources() {
		// Only up to 6
		selectedPhotoSources.add(photoSourceFactory.getPhonePS());
		selectedPhotoSources.add(photoSourceFactory.getInstagramPS());
		selectedPhotoSources.add(photoSourceFactory.getFacebookPS());
		//		photoSourcesTest.add(photoSourceFactory.getFlickrPS());
		selectedPhotoSources.add(photoSourceFactory.getPhotobucketPS());
		selectedPhotoSources.add(photoSourceFactory.getDropboxPS());
		//		photoSourcesTest.add(photoSourceFactory.getPicasaPS());
		selectedPhotoSources.add(photoSourceFactory.getPreselectedPS());
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
	}

	public void onClickChangeAvailablePaymentOptions(View v) {
		PaymentOptionType[] allTypes = PaymentOptionType.values();
		boolean[] isSelected = new boolean[allTypes.length];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = selectedPaymentOptions.contains(allTypes[i]);
		}
		List<String> names = new ArrayList<String>(allTypes.length);
		for (PaymentOptionType pType : allTypes) {
			names.add(pType.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select available payment options");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					selectedPaymentOptions.add(PaymentOptionType.values()[which]);
				} else {
					selectedPaymentOptions.remove(PaymentOptionType.values()[which]);
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickSetScreensWithCountryBar(View v) {
		final Screen[] allScreens = (Screen[]) Arrays.asList(Screen.PRODUCTS, Screen.PRODUCT_DETAILS, Screen.OPTIONS).toArray();
		boolean[] isSelected = new boolean[allScreens.length];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = screensWithCountryBar.contains(allScreens[i]);
		}
		List<String> names = new ArrayList<String>(allScreens.length);
		for (Screen screen : allScreens) {
			names.add(screen.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select screens with country dropdown");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					screensWithCountryBar.add(allScreens[which]);
				} else {
					screensWithCountryBar.remove(allScreens[which]);
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickChangeAvailablePhotoSource(View v) {
		boolean[] isSelected = new boolean[allSources.size()];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = selectedPhotoSources.contains(allSources.get(i));
		}
		List<String> names = new ArrayList<String>(allSources.size());
		for (PhotoSource ps : allSources) {
			names.add(ps.getName(this));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select available photo sources");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					selectedPhotoSources.add(allSources.get(which));
				} else {
					selectedPhotoSources.remove(allSources.get(which));
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickChangeAvailableProducts(View v) {
		final List<ProductType> allProductTypes = Arrays.asList(ProductType.values());
		boolean[] isSelected = new boolean[allProductTypes.size()];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = selectedProductTypes.contains(allProductTypes.get(i));
		}
		List<String> names = new ArrayList<String>(allProductTypes.size());
		for (ProductType pt : allProductTypes) {
			names.add(pt.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select available product types");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					selectedProductTypes.add(allProductTypes.get(which));
				} else {
					selectedProductTypes.remove(allProductTypes.get(which));
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickSetSideMenuInfoButtons(View v) {
		final List<SideMenuInfoButton> allButtons = Arrays.asList(SideMenuInfoButton.values());
		boolean[] isSelected = new boolean[allButtons.size()];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = sideMenuInfoButtons.contains(allButtons.get(i));
		}
		List<String> names = new ArrayList<String>(allButtons.size());
		for (SideMenuInfoButton button : allButtons) {
			names.add(button.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select side menu info buttons");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					sideMenuInfoButtons.add(allButtons.get(which));
				} else {
					sideMenuInfoButtons.remove(allButtons.get(which));
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickSetSideMenuButtonsTop(View v) {
		final List<SideMenuButton> allButtons = Arrays.asList(SideMenuButton.values());
		boolean[] isSelected = new boolean[allButtons.size()];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = sideMenuButtonsTop.contains(allButtons.get(i));
		}
		List<String> names = new ArrayList<String>(allButtons.size());
		for (SideMenuButton button : allButtons) {
			names.add(button.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select top side menu buttons");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					sideMenuButtonsTop.add(allButtons.get(which));
				} else {
					sideMenuButtonsTop.remove(allButtons.get(which));
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickSetDisabledScreens(View v) {
		final List<Screen> screens = new ArrayList<Screen>();
		for (Screen screen : availableScreens) {
			if (screen.isDisableReady()) {
				screens.add(screen);
			}
		}
		boolean[] isSelected = new boolean[screens.size()];
		for (int i = 0; i < isSelected.length; i++) {
			isSelected[i] = config.getDisabledScreens().contains(screens.get(i));
		}
		List<String> names = new ArrayList<String>(screens.size());
		for (Screen screen : screens) {
			names.add(screen.name());
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Disable screens");
		builder.setMultiChoiceItems(names.toArray(new String[names.size()]), isSelected, new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					config.getDisabledScreens().add(screens.get(which));
				} else {
					config.getDisabledScreens().remove(screens.get(which));
				}
			}
		});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NOP
			}
		});
		builder.show();
	}

	public void onClickRemoveAllItemsFromShoppingCart(View v) {
		ShoppingCart cart = PIO.getShoppingCart(this);
		cart.removeAllItems();
		PIO.setShoppingCart(this, cart);
	}

	public void onClickClearShippingAddresses(View v) {
		PIO.clearShippingAddresses(this);
	}

	public void onClickAddImageToSDK(View v) {
		if (!editTextAddImageToSdk.getText().toString().isEmpty()) {
			imageUris.add(editTextAddImageToSdk.getText().toString());
			editTextAddImageToSdk.setText("");
			Toast.makeText(this, "Image URL added. Count: " + imageUris.size(), Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(intent, 1);
		}
	}

	public void onClickSetScreenProductImageUri(View v) {
		if (!editTextScreenProductImageUti.getText().toString().isEmpty()) {
			config.setScreenProductImageUrl(editTextScreenProductImageUti.getText().toString());
			editTextScreenProductImageUti.setText("");
			Toast.makeText(this, "Screen product image URL set", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(intent, 2);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			imageUris.add(data.getData().toString());
			Toast.makeText(this, "Image URL added. Count: " + imageUris.size(), Toast.LENGTH_SHORT).show();
		}
		if (requestCode == 2 && resultCode == RESULT_OK) {
			config.setScreenProductImageUrl(data.getData().toString());
			Toast.makeText(this, "Screen product image URL set", Toast.LENGTH_SHORT).show();
			Log.d("PIO_SDK_EXAMPLE", "Screen product image URL: " + config.getScreenProductImageUrl());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClickStartSDK(View v) {
		config.setSdkDemo(true);
		config.setHostAppActivity(getComponentName().getClassName()); //"com.example.piosdkpoconcept.ActivityTest"

		String recepiID = ((EditText) findViewById(R.id.edittext_recipe_id)).getText().toString();
		if (StringUtils.isNotBlank(recepiID)) {
			config.setRecipeID(recepiID);
		}
		config.setCountryCode(((EditText) findViewById(R.id.editCountry)).getText().toString());
		config.setChangeableCountry(((Switch) findViewById(R.id.switch_changeable_country)).isChecked());
		config.setCurrencyCode(((EditText) findViewById(R.id.editCurrency)).getText().toString());
		config.setChangeableCurrency(((Switch) findViewById(R.id.switch_changeable_currency)).isChecked());
		try {
			String colorString = ((EditText) findViewById(R.id.editColorHex)).getText().toString();
			config.setHeaderColor(Color.parseColor("#" + colorString));
		} catch (NumberFormatException e) {}
		if (((Switch) findViewById(R.id.switch_enable_custom_fonts)).isChecked()) {
			config.setFontPathInAssetsLight("HelveticaNeueLTStd-Lt.otf");
			config.setFontPathInAssetsNormal("HelveticaNeueLTStd-Roman.otf");
			config.setFontPathInAssetsBold("HelveticaNeueLTStd-Bd.otf");
		}
		config.useThreeButtonsBarStyle(((Switch) findViewById(R.id.switch_three_buttons_bar_style)).isChecked());
		config.setMenuIconGear(((Switch) findViewById(R.id.switch_menu_icon_gear)).isChecked());
		config.setHideStatusBar(((Switch) findViewById(R.id.full_Screen)).isChecked());
		config.setSdkAppearsFromRight(((Switch) findViewById(R.id.switch_appear_from_right)).isChecked());
		config.setPassedImageThumb(((Switch) findViewById(R.id.switch_passed_image_thumb)).isChecked());

		// Products configuration
		boolean coastersDiff = ((Switch) findViewById(R.id.switch_coasters_different)).isChecked();
		boolean coastersDuplicate = ((Switch) findViewById(R.id.switch_coasters_duplicate)).isChecked();
		config.setCoastersType(-1); // Reset from previous launch
		if (coastersDiff != coastersDuplicate) {
			config.setCoastersType(coastersDiff ? PublicConstants.CoastersTypes.COASTERS_4_DIFFERENT : PublicConstants.CoastersTypes.COASTERS_1_DUPLICATED);
		}
		config.setAvailableProducts(selectedProductTypes);

		// Side menu
		config.setSideMenuEnabled(((Switch) findViewById(R.id.switch_enable_side_menu)).isChecked());
		config.setRightSideMenu(((Switch) findViewById(R.id.switch_right_side_menu)).isChecked());
		config.setSideMenuButtonsTop(sideMenuButtonsTop);
		config.setSideMenuInfoButtons(sideMenuInfoButtons);

		// All Screens
		config.setShowHelp(((Switch) findViewById(R.id.switch_hide_help)).isChecked());
		config.showCountrySelectionOnScreen(screensWithCountryBar);

		// Featured products screen
		config.setHideCategorySearchBar(((Switch) findViewById(R.id.switch_hide_category_search_bar)).isChecked());
		config.setShowFeaturedProductsByDefault(((Switch) findViewById(R.id.default_products_screen_featured)).isChecked());
		config.setHideComingSoonProducts(switchHideComingSoonProducts.isChecked());

		// Product details
		config.setPriceTitleHidden(((Switch) findViewById(R.id.switch_hide_price_title)).isChecked());

		// Shopping cart screen
		config.setShowAddMoreProductsInShoppingCart(((Switch) findViewById(R.id.switch_show_add_more_products)).isChecked());
		config.hideEditButtonInShoppingCart(((Switch) findViewById(R.id.switch_hide_edit_button)).isChecked());
		config.closeWidgetFromShoppingCart(((Switch) findViewById(R.id.closeWidgetFromShoppingCart)).isChecked());

		// Customize product screen
		config.setShowPhotosInCustomize(((Switch) findViewById(R.id.switch_show_photos_customize)).isChecked());
		config.setShowOptionsInCustomize(((Switch) findViewById(R.id.switch_show_options_customize)).isChecked());
		config.enablePhotoSourcesInCustomizeProduct(((Switch) findViewById(R.id.switch_show_enable_photosource_when_disabled)).isChecked());
		config.setAutoArrange(((Switch) findViewById(R.id.auto_arrange)).isChecked());

		// Edit image screen
		boolean isRotateEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_rotate_enabled_in_crop_screen)).isChecked();
		boolean isTextEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_text_enabled_in_crop_screen)).isChecked();
		boolean isEffectsEnabledInCropScreen = ((Switch) findViewById(R.id.switch_is_effects_enabled_in_crop_screen)).isChecked();
		config.setUpCropScreen(isRotateEnabledInCropScreen, isTextEnabledInCropScreen, isEffectsEnabledInCropScreen);

		// Shipping addresses screen
		// empty

		// Payment screen
		config.removeLogoFromPaymentScreen(((Switch) findViewById(R.id.switch_remove_logo_on_payment)).isChecked());
		config.setPartnerName(((EditText) findViewById(R.id.edittext_payee_name)).getText().toString());
		config.setPaymentOptions(selectedPaymentOptions);
		String promoCode = ((EditText) findViewById(R.id.edittext_promo_code)).getText().toString();
		config.setPromoCode(StringUtils.isBlank(promoCode) ? null : promoCode);

		// Photo sources
		config.setPhotoSources(selectedPhotoSources);
		if (((Switch) findViewById(R.id.switch_use_first_photosource_as_default)).isChecked() && config.getPhotoSources() != null && !config.getPhotoSources().isEmpty()) {
			config.setDefaultPhotoSource(config.getPhotoSources().get(0));
		}
		config.hidePhotoSourcesInSideMenu(((Switch) findViewById(R.id.switch_hide_photo_sources_side_menu)).isChecked());
		config.setImageUris(imageUris);
		if (config.isPhotosourcesDisabled() && (config.getImageUris() == null || config.getImageUris().isEmpty())) {
			showMessage("Photosources are disabled and no images were passed to the SDK");
			return;
		}

		// Screens
		config.setScreenVersion(screenVersion);

		// Jump to product
		Object item = spinnerJumpToProduct.getSelectedItem();
		ProductType selectedProductType = item == null ? null : (ProductType) item;
		config.setProductFromApp(selectedProductType);
		config.setProductSkuFromApp(((EditText) findViewById(R.id.editSKU)).getText().toString());
		if (StringUtils.isNotBlank(config.getProductSkuFromApp()) && selectedProductType == null) {
			showMessage("Product must be specified when SKU is supplied");
			return;
		}

		// Jump to Screen
		Object screen = spinnerJumpToScreen.getSelectedItem();
		Screen jumpToScreen = screen == null ? null : (Screen) screen;
		screen = spinnerNavigateBackToScreen.getSelectedItem();
		Screen navigateBackScreen = screen == null ? null : (Screen) screen;
		config.setJumpToScreen(jumpToScreen, navigateBackScreen);

		// Launch SDK
		try {
			PIO.setConfig(this, config);
			PIO.start(this);
		} catch (PIOException e) {
			showMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	private void showMessage(String msg) {
		((TextView) findViewById(R.id.textview_feedback)).setText(msg);
		findViewById(R.id.dialog_feedback).setVisibility(View.VISIBLE);
	}

	public void onClickOkFeedback(View v) {
		findViewById(R.id.dialog_feedback).setVisibility(View.GONE);
	}

}