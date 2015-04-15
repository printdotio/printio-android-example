package com.example.piosdkpoconcept.photosource.vlado;

import print.io.R;
import print.io.photosource.PhotoSourceNavigator;
import print.io.photosource.PhotoSourceNavigatorHolder;
import print.io.photosource.defaultgenericimpl.DefaultPhotoSource;
import print.io.photosource.defaultgenericimpl.DialogPhotoSourceLogin.DialogPhotoSourceLoginCallback;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSource extends DefaultPhotoSource {

	private static final long serialVersionUID = -2804726157663465818L;

	private static final int SERVICE_ID = 50;
	private static final String VLADO_PREFS_NAME = "VLADO_PREFS";
	private static final String PREFS_LOGGED = "LOGGED";

	@Override
	public int getServiceId() {
		return SERVICE_ID;
	}

	@Override
	public String getName(Context context) {
		return "Vlado";
	}

	@Override
	public void login(final Activity context, final AuthorizationCompleteCallback authorizationCompleteCallback) {
		showLoginDialog((FragmentActivity) context, new DialogPhotoSourceLoginCallback() {

			@Override
			public boolean attemptLogin(String username, String password) {
				boolean result = "pajo".equals(username) && "car".equals(password);
				if (result) {
					SharedPreferences.Editor editor = context.getSharedPreferences(VLADO_PREFS_NAME, 0).edit();
					editor.putBoolean(PREFS_LOGGED, result).commit();
				}
				return result;
			}

			@Override
			public void onLoginComplete(boolean success, String response) {
				if (authorizationCompleteCallback != null) {
					authorizationCompleteCallback.call(false, success, response);
				}
			}

			@Override
			public void onCancel() {
				if (authorizationCompleteCallback != null) {
					authorizationCompleteCallback.call(true, false, null);
				}
			}

		});
	}

	@Override
	public void logout(Activity context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(VLADO_PREFS_NAME, 0).edit();
		editor.putBoolean(PREFS_LOGGED, false).commit();
	}

	@Override
	public boolean isAuthorized(Context context) {
		return context.getSharedPreferences(VLADO_PREFS_NAME, 0).getBoolean(PREFS_LOGGED, false);
	}

	@Override
	public boolean isVisibleInSideMenu() {
		return true;
	}

	@Override
	public Drawable getSideMenuIcon(Context context) {
		return context.getResources().getDrawable(R.drawable.icon_phone_60);
	}

	@Override
	public Drawable getSelectImagesIcon(Context context) {
		return context.getResources().getDrawable(R.drawable.icon_phone_60);
	}

	@Override
	public PhotoSourceNavigator<VladoPhotoSource> createPhotoSourceNavigator(PhotoSourceNavigatorHolder holder) {
		return new VladoPhotoSourceNavigator(this, holder);
	}

}
