package com.example.piosdkpoconcept.photosource.vlado;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import print.io.R;
import print.io.photosource.PhotoSource;
import print.io.photosource.PhotoSourceNavigator;
import print.io.photosource.PhotoSourceNavigator.PhotoSourceNavigatorHolder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSource implements PhotoSource {

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
	public void login(FragmentActivity context, AuthorizationCompleteListener authorizationCompleteListener) {
		// NOP
	}

	@Override
	public void logout(FragmentActivity context) {
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

	@Override
	public URLConnection openConnectionForImageDownload(Context context, String path) {
		URLConnection connection = null;
		try {
			URL url = new URL(path);
			connection = url.openConnection();
		} catch (MalformedURLException e) {
			connection = null;
		} catch (IOException e) {
			connection = null;
		}
		return connection;
	}

}
