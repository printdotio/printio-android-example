package com.example.piosdkpoconcept.photosource.vlado;

import print.io.R;
import print.io.photosource.PhotoSourceNavigator;
import print.io.photosource.PhotoSourceNavigator.PhotoSourceNavigatorHolder;
import print.io.photosource.defaultgenericimpl.DefaultPhotoSource;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSource extends DefaultPhotoSource {

	private static final long serialVersionUID = -2804726157663465818L;

	@Override
	public int getServiceId() {
		return 50;
	}

	@Override
	public String getName(Context context) {
		return "Vlado";
	}

	@Override
	public void login(Activity context, AuthorizationCompleteListener authorizationCompleteListener) {
		// NOP
	}

	@Override
	public void logout(Activity context) {
		// NOP
	}

	@Override
	public boolean isAuthorized(Context context) {
		return true;
	}

	@Override
	public boolean isVisibleInSideMenu() {
		return false;
	}

	@Override
	public Drawable getSideMenuIcon(Context context) {
		return null;
	}

	@Override
	public Drawable getSelectImagesIcon(Context context) {
		return context.getResources().getDrawable(R.drawable.icon_phone_60);
	}

	@Override
	public PhotoSourceNavigator<VladoPhotoSource> createPhotoSourceNavigator(FragmentActivity activity, PhotoSourceNavigatorHolder holder) {
		return new VladoPhotoSourceNavigator(activity, this, holder);
	}

}
