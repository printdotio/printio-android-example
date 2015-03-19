package com.example.piosdkpoconcept.photosource.vlado;

import print.io.R;
import print.io.beans.GenericPhoto;
import print.io.imagedownloader.PhotoSourceImageDownloader;
import print.io.imagedownloader.PhotoSourceImageDownloaderFactory;
import print.io.imagedownloader.imagesource.UriImageDownloader;
import print.io.photosource.PhotoSource;
import print.io.photosource.PhotoSourceNavigator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSource extends PhotoSource {

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
	public int getSelectImagesIcon() {
		return R.drawable.icon_phone_60;
	}

	@Override
	public PhotoSourceNavigator<VladoPhotoSource> createPhotoSourceNavigator() {
		return new VladoPhotoSourceNavigator(this);
	}

	@Override
	public PhotoSourceImageDownloaderFactory createPhotoSourceImageDownloaderFactory() {
		return new PhotoSourceImageDownloaderFactory() {

			@Override
			public PhotoSourceImageDownloader create(GenericPhoto photo) {
				return new UriImageDownloader(photo);
			}
		};
	}
}
