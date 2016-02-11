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
import print.io.piopublic.AddMoreProductsButtonStrategy;
import print.io.piopublic.HeroItem;
import print.io.piopublic.LayoutStepStrategy;
import print.io.piopublic.PaymentOptionType;
import print.io.piopublic.ProductType;
import print.io.piopublic.Screen;
import print.io.piopublic.ScreenVersion;
import print.io.piopublic.ShoppingCartDeleteMode;
import print.io.piopublic.SideMenuButton;
import print.io.piopublic.SingleOptionStepStrategy;

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

import com.example.piosdkpoconcept.Utils.ChooseDialogoOnItemSelected;
import com.example.piosdkpoconcept.adapters.SpinnerAdapterJumpToProduct;
import com.example.piosdkpoconcept.adapters.SpinnerAdapterJumpToScreen;
import com.example.piosdkpoconcept.adapters.SpinnerAdapterScreenVersion;

public class MainActivity extends Activity {

	private EditText editTextAddImageToSdk;
	private EditText editTextScreenProductImageUti;
	private Spinner spinnerJumpToProduct;
	private Spinner spinnerJumpToScreen;
	private Spinner spinnerNavigateBackToScreen;

	private PIOConfig config = new PIOConfig();

	private List<String> imageUris = new ArrayList<String>();
	private List<SideMenuButton> sideMenuButtons = new ArrayList<SideMenuButton>(Arrays.asList(SideMenuButton.values()));
	private PhotoSourceFactory photoSourceFactory = new PhotoSourceFactory();
	private List<PhotoSource> allSources = photoSourceFactory.getAll();
	private List<PhotoSource> selectedPhotoSources = new ArrayList<PhotoSource>();
	private List<ProductType> selectedProductTypes = new ArrayList<ProductType>(config.getAvailableProducts());
	private List<ProductType> featuredProductTypes = new ArrayList<ProductType>();
	private List<PaymentOptionType> selectedPaymentOptions = new ArrayList<PaymentOptionType>(Arrays.asList(PaymentOptionType.values()));
	private List<Screen> screensWithCountryBar = new ArrayList<Screen>(Arrays.asList(Screen.PRODUCTS));
	private List<Screen> availableScreens = Arrays.asList(Screen.values());
	private List<ProductType> productsWithSpecailOfferBanner = new ArrayList<ProductType>();
	private ScreenVersion screenVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		((ToggleButton) findViewById(R.id.toggleButtonProduction)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				initSdkMode(isChecked);
			}
		});

		config.setDisabledScreens(new ArrayList<Screen>());
		config.setPartnerName(getResources().getString(R.string.hellopics));
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
		//selectedPhotoSources.add(photoSourceFactory.getFlickrPS());
		selectedPhotoSources.add(photoSourceFactory.getPhotobucketPS());
		selectedPhotoSources.add(photoSourceFactory.getDropboxPS());
		//selectedPhotoSources.add(photoSourceFactory.getPicasaPS());
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

	public void onClickSetSideMenuButtons(View v) {
		String title = "Select visible side menu buttons";
		Utils.<SideMenuButton> showChooseDialogEnum(this, true, SideMenuButton.values(), sideMenuButtons, title, new ChooseDialogoOnItemSelected<SideMenuButton>() {

			@Override
			public void onSelected(SideMenuButton val, boolean isChecked) {
				if (isChecked) {
					sideMenuButtons.add(val);
				} else {
					sideMenuButtons.remove(val);
				}
			}

		});
	}

	public void onClickChangeAvailablePaymentOptions(View v) {
		String title = "Select available payment options";
		Utils.<PaymentOptionType> showChooseDialogEnum(this, true, PaymentOptionType.values(), selectedPaymentOptions, title, new ChooseDialogoOnItemSelected<PaymentOptionType>() {

			@Override
			public void onSelected(PaymentOptionType val, boolean isChecked) {
				if (isChecked) {
					selectedPaymentOptions.add(val);
				} else {
					selectedPaymentOptions.remove(val);
				}
			}

		});
	}

	public void onClickSetScreensWithCountryBar(View v) {
		Screen[] allScreens = (Screen[]) Arrays.asList(Screen.PRODUCTS, Screen.PRODUCT_DETAILS, Screen.OPTIONS).toArray();
		String title = "Select screens with country dropdown";
		Utils.<Screen> showChooseDialogEnum(this, true, allScreens, screensWithCountryBar, title, new ChooseDialogoOnItemSelected<Screen>() {

			@Override
			public void onSelected(Screen val, boolean isChecked) {
				if (isChecked) {
					screensWithCountryBar.add(val);
				} else {
					screensWithCountryBar.remove(val);
				}
			}

		});
	}

	public void onClickChangeAvailableProducts(View v) {
		String title = "Select available product types";
		Utils.<ProductType> showChooseDialogEnum(this, true, ProductType.values(), selectedProductTypes, title, new ChooseDialogoOnItemSelected<ProductType>() {

			@Override
			public void onSelected(ProductType val, boolean isChecked) {
				if (isChecked) {
					selectedProductTypes.add(val);
				} else {
					selectedProductTypes.remove(val);
				}
			}

		});
	}
	
	public void onClickChangeFeaturedProducts(View v) {
		String title = "Select featured product types";
		Utils.<ProductType> showChooseDialogEnum(this, true, ProductType.values(), featuredProductTypes, title, new ChooseDialogoOnItemSelected<ProductType>() {

			@Override
			public void onSelected(ProductType val, boolean isChecked) {
				if (isChecked) {
					featuredProductTypes.add(val);
				} else {
					featuredProductTypes.remove(val);
				}
			}

		});
	}

	public void onClickSetDisabledScreens(View v) {
		final List<Screen> screens = new ArrayList<Screen>();
		for (Screen screen : availableScreens) {
			if (screen.isDisableReady()) {
				screens.add(screen);
			}
		}
		Screen[] vals = screens.toArray(new Screen[screens.size()]);
		Utils.<Screen> showChooseDialogEnum(this, true, vals, config.getDisabledScreens(), "Disable screens", new ChooseDialogoOnItemSelected<Screen>() {

			@Override
			public void onSelected(Screen val, boolean isChecked) {
				if (isChecked) {
					config.getDisabledScreens().add(val);
				} else {
					config.getDisabledScreens().remove(val);
				}
			}

		});
	}

	public void onClickSetProductsWithSpecailOfferBanner(View v) {
		String title = "Select products with special offer banner";
		Utils.<ProductType> showChooseDialogEnum(this, true, ProductType.values(), productsWithSpecailOfferBanner, title, new ChooseDialogoOnItemSelected<ProductType>() {

			@Override
			public void onSelected(ProductType val, boolean isChecked) {
				if (isChecked) {
					productsWithSpecailOfferBanner.add(val);
				} else {
					productsWithSpecailOfferBanner.remove(val);
				}
			}

		});
	}

	public void onClickSetAddMoreProductsButtonStrategy(View v) {
		String title = "Choose strategy";
		List<AddMoreProductsButtonStrategy> selected = new ArrayList<AddMoreProductsButtonStrategy>(1);
		selected.add(config.getAddMoreProductsButtonStrategy());
		Utils.<AddMoreProductsButtonStrategy> showChooseDialogEnum(this, false, AddMoreProductsButtonStrategy.values(), selected, title, new ChooseDialogoOnItemSelected<AddMoreProductsButtonStrategy>() {

			@Override
			public void onSelected(AddMoreProductsButtonStrategy val, boolean isChecked) {
				config.setAddMoreProductsButtonStrategy(val);
			}

		});
	}

	public void onClickSetShoppingCartDeleteMode(View v) {
		String title = "Choose delete mode";
		List<ShoppingCartDeleteMode> selected = new ArrayList<ShoppingCartDeleteMode>(1);
		selected.add(config.getShoppingCartDeleteMode());
		Utils.<ShoppingCartDeleteMode> showChooseDialogEnum(this, false, ShoppingCartDeleteMode.values(), selected, title, new ChooseDialogoOnItemSelected<ShoppingCartDeleteMode>() {

			@Override
			public void onSelected(ShoppingCartDeleteMode val, boolean isChecked) {
				config.setShoppingCartDeleteMode(val);
			}

		});
	}

	public void onClickStepLayoutStepStrategy(View v) {
		String title = "Choose strategy";
		List<LayoutStepStrategy> selected = new ArrayList<LayoutStepStrategy>(1);
		selected.add(config.getLayoutStepStrategy());
		Utils.<LayoutStepStrategy> showChooseDialogEnum(this, false, LayoutStepStrategy.values(), selected, title, new ChooseDialogoOnItemSelected<LayoutStepStrategy>() {

			@Override
			public void onSelected(LayoutStepStrategy val, boolean isChecked) {
				config.setLayoutStepStrategy(val);
			}

		});
	}

	public void onClickSingleOptionStepStrategy(View v) {
		String title = "Choose strategy";
		List<SingleOptionStepStrategy> selected = new ArrayList<SingleOptionStepStrategy>(1);
		selected.add(config.getSingleOptionStepStrategy());
		Utils.<SingleOptionStepStrategy> showChooseDialogEnum(this, false, SingleOptionStepStrategy.values(), selected, title, new ChooseDialogoOnItemSelected<SingleOptionStepStrategy>() {

			@Override
			public void onSelected(SingleOptionStepStrategy val, boolean isChecked) {
				config.setSingleOptionStepStrategy(val);
			}

		});
	}

	public void onClickRemoveAllItemsFromShoppingCart(View v) {
		ShoppingCart cart = PIO.getShoppingCart(this);
		cart.removeAllItems();
		PIO.setShoppingCart(this, cart);
		Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
	}

	public void onClickClearShippingAddresses(View v) {
		PIO.clearShippingAddresses(this);
		Toast.makeText(this, "Shipping addresses cleared", Toast.LENGTH_SHORT).show();
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

	public void onClickClearScreenProductImageUri(View v) {
		config.setScreenProductImageUrl(null);
		editTextScreenProductImageUti.setText("");
		Toast.makeText(this, "Screen product image URL cleared", Toast.LENGTH_SHORT).show();
	}

	public void toggleHeroArea(View v) {
		View holder = findViewById(R.id.hero_item_holder);
		holder.setVisibility(holder.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	public void toggleAdvancedArea(View v) {
		View holder = findViewById(R.id.advanced_area_holder);
		holder.setVisibility(holder.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	public void onClickCopyShoppingCartJSON(View v) {
		ShoppingCart cart = PIO.getShoppingCart(this);
		String json = cart != null ? cart.toJson() : "";
		Utils.copyToClipboard(this, json, "Shopping cart copied to clipboard!");
	}

	public void onClickSetShoppingCart(View v) {
		String json = Utils.getTextFromClipboard(this);
		if (json != null && json.length() > 0) {
			try {
				ShoppingCart cart = new ShoppingCart(json);
				PIO.setShoppingCart(this, cart);
				Toast.makeText(this, "Shopping cart loaded from successfully!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(this, "Failed to set cart from clipboard!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "No data in clipboard!", Toast.LENGTH_SHORT).show();
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
		config.setHostAppActivity(getComponentName().getClassName());

		String recepiID = getEditText(R.id.edittext_recipe_id);
		if (StringUtils.isNotBlank(recepiID)) {
			config.setRecipeID(recepiID);
		}
		config.setCountryCode(getEditText(R.id.editCountry));
		config.setChangeableCountry(isChecked(R.id.switch_changeable_country));
		config.setCurrencyCode(getEditText(R.id.editCurrency));
		config.setChangeableCurrency(isChecked(R.id.switch_changeable_currency));
		try {
			String colorString = getEditText(R.id.editColorHex);
			config.setHeaderColor(Color.parseColor("#" + colorString));
		} catch (NumberFormatException e) {}
		if (isChecked(R.id.switch_enable_custom_fonts)) {
			config.setFontPathInAssetsLight("HelveticaNeueLTStd-Lt.otf");
			config.setFontPathInAssetsNormal("HelveticaNeueLTStd-Roman.otf");
			config.setFontPathInAssetsBold("HelveticaNeueLTStd-Bd.otf");
		} else {
			config.setFontPathInAssetsLight(null);
			config.setFontPathInAssetsNormal(null);
			config.setFontPathInAssetsBold(null);
			config.setFontPathInAssetsTitle(null);
		}
		config.useThreeButtonsBarStyle(isChecked(R.id.switch_three_buttons_bar_style));
		config.setMenuIconGear(isChecked(R.id.switch_menu_icon_gear));
		config.setHideStatusBar(isChecked(R.id.full_Screen));
		config.setSdkAppearsFromRight(isChecked(R.id.switch_appear_from_right));
		config.setPassedImageThumb(isChecked(R.id.switch_passed_image_thumb));

		// Products configuration
		boolean coastersDiff = isChecked(R.id.switch_coasters_different);
		boolean coastersDuplicate = isChecked(R.id.switch_coasters_duplicate);
		config.setCoastersType(-1); // Reset from previous launch
		if (coastersDiff != coastersDuplicate) {
			config.setCoastersType(coastersDiff ? PublicConstants.CoastersTypes.COASTERS_4_DIFFERENT : PublicConstants.CoastersTypes.COASTERS_1_DUPLICATED);
		}
		config.setAvailableProducts(selectedProductTypes);
		
		// Side menu
		config.setSideMenuEnabled(isChecked(R.id.switch_enable_side_menu));
		config.setRightSideMenu(isChecked(R.id.switch_right_side_menu));
		config.setVisibleSideMenuButtons(sideMenuButtons);

		// All screens
		config.setShowHelp(isChecked(R.id.switch_hide_help));
		config.showCountrySelectionOnScreen(screensWithCountryBar);

		// Products screen
		config.setHideCategorySearchBar(isChecked(R.id.switch_hide_category_search_bar));
		config.setHideComingSoonProducts(isChecked(R.id.switch_hide_coming_soon_products));
		config.setProductsBottomBarVisibility(isChecked(R.id.switch_show_bottom_bar));
		if (isChecked(R.id.switch_show_logo_in_products_screen)) {
			config.setTitleBarVendorLogoOnScreen(Screen.PRODUCTS, R.drawable.icon_logo);
		} else {
			config.setTitleBarVendorLogoOnScreen(Screen.PRODUCTS, null);
		}
		config.setFeaturedProducts(featuredProductTypes);

		// Custom hero item
		String image = getEditText(R.id.edittext_hero_item_image);
		if (StringUtils.isBlank(image)) {
			config.setHeroItem(null);
		} else {
			String url = getEditText(R.id.edittext_hero_item_link);
			int pos = -1;
			try {
				pos = Integer.parseInt(getEditText(R.id.edittext_hero_item_position));
			} catch (Exception e) {}
			config.setHeroItem(new HeroItem(pos, image, url));
		}


		// Product Details screen
		config.setPriceTitleHidden(isChecked(R.id.switch_hide_price_title));
		try {
			config.setRetailDiscountPercent(Float.parseFloat(getEditText(R.id.edittext_retail_price_discount_percentage)));
		} catch (Exception e) {}

		// Product Options screen
		config.setCancelOptionsButtonVisibility(isChecked(R.id.switch_show_cancel_options_button));
		config.setShowCouchHintOverlay(isChecked(R.id.switch_show_couch_hint_overlay));

		// Shopping Cart screen
		config.setShowCheckoutProgress(isChecked(R.id.switch_show_checkout_progress));
		config.setShowAddMoreProductsInShoppingCart(isChecked(R.id.switch_show_add_more_products));
		config.closeWidgetFromShoppingCart(isChecked(R.id.switch_close_widget_from_shopping_cart));
		if (isChecked(R.id.switch_show_logo_in_shopping_cart)) {
			config.setTitleBarVendorLogoOnScreen(Screen.SHOPPING_CART, R.drawable.icon_logo);
		} else {
			config.setTitleBarVendorLogoOnScreen(Screen.SHOPPING_CART, null);
		}

		// Customize Product screen
		config.setShowPhotosInCustomize(isChecked(R.id.switch_show_photos_customize));
		config.setShowOptionsInCustomize(isChecked(R.id.switch_show_options_customize));
		config.enablePhotoSourcesInCustomizeProduct(isChecked(R.id.switch_show_enable_photosource_when_disabled));
		config.setAutoArrange(isChecked(R.id.auto_arrange));

		// Edit Image screen
		boolean isRotateEnabledInCropScreen = isChecked(R.id.switch_is_rotate_enabled_in_crop_screen);
		boolean isTextEnabledInCropScreen = isChecked(R.id.switch_is_text_enabled_in_crop_screen);
		boolean isEffectsEnabledInCropScreen = isChecked(R.id.switch_is_effects_enabled_in_crop_screen);
		config.setUpCropScreen(isRotateEnabledInCropScreen, isTextEnabledInCropScreen, isEffectsEnabledInCropScreen);

		// Shipping Addresses screen
		// empty

		// Payment screen
		if (isChecked(R.id.switch_show_logo_on_payment)) {
			config.setVendorLogoOnScreen(Screen.PAYMENT, R.drawable.icon_logo_payment_screen);
		} else {
			config.setVendorLogoOnScreen(Screen.PAYMENT, null);
		}
		config.setPartnerName(getEditText(R.id.edittext_payee_name));
		config.setPaymentOptions(selectedPaymentOptions);
		String promoCode = getEditText(R.id.edittext_promo_code);
		config.setPromoCode(StringUtils.isBlank(promoCode) ? null : promoCode);

		// Order Completed screen
		if (isChecked(R.id.switch_show_logo_on_order_completed)) {
			config.setVendorLogoOnScreen(Screen.ORDER_COMPLETED, R.drawable.icon_logo);
		} else {
			config.setVendorLogoOnScreen(Screen.ORDER_COMPLETED, null);
		}

		// Photo sources
		config.setPhotoSources(selectedPhotoSources);
		if (isChecked(R.id.switch_use_first_photosource_as_default) && config.getPhotoSources() != null && !config.getPhotoSources().isEmpty()) {
			config.setDefaultPhotoSource(config.getPhotoSources().get(0));
		}
		config.hidePhotoSourcesInSideMenu(isChecked(R.id.switch_hide_photo_sources_side_menu));
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
		config.setProductSkuFromApp(getEditText(R.id.editSKU));
		if (StringUtils.isNotBlank(config.getProductSkuFromApp()) && selectedProductType == null) {
			showMessage("Product must be specified when SKU is supplied");
			return;
		}

		// Jump to screen
		Object screen = spinnerJumpToScreen.getSelectedItem();
		Screen jumpToScreen = screen == null ? null : (Screen) screen;
		screen = spinnerNavigateBackToScreen.getSelectedItem();
		Screen navigateBackScreen = screen == null ? null : (Screen) screen;
		config.setJumpToScreen(jumpToScreen, navigateBackScreen);

		// V2 screens specific
		if (isChecked(R.id.switch_show_logo_on_product_details_v2_screen)) {
			config.setVendorLogoOnScreen(Screen.PRODUCT_DETAILS, R.drawable.samsung_logo);
		} else {
			config.setVendorLogoOnScreen(Screen.PRODUCT_DETAILS, null);
		}
		config.setProductsWithSpecialOfferBanner(productsWithSpecailOfferBanner);

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

	private boolean isChecked(int switchResId) {
		return ((Switch) findViewById(switchResId)).isChecked();
	}

	private String getEditText(int editTextResId) {
		return ((EditText) findViewById(editTextResId)).getText().toString();
	}

}