package com.example.piosdkpoconcept.photosource.vlado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.photosource.defaultgenericimpl.DefaultPhotoSourceNavigator;
import print.io.photosource.defaultgenericimpl.Folder;
import print.io.photosource.defaultgenericimpl.MediaItem;
import print.io.photosource.defaultgenericimpl.Photo;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSourceNavigator extends DefaultPhotoSourceNavigator<VladoPhotoSource> {

	private List<String> IAMGE_URL = Arrays
			.asList("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-9/526189_4617697276507_662207867_n.jpg?oh=8d527dd9102f0124fe0833be2cb24c22&oe=5580D92A&__gda__=1434779874_f9059584437b839eb5149e3e4da2fc97",
					"https://scontent-ams.xx.fbcdn.net/hphotos-prn2/t31.0-8/1268224_10201569069964657_1243815332_o.jpg");

	public VladoPhotoSourceNavigator(FragmentActivity activity, VladoPhotoSource photoSource, print.io.photosource.PhotoSourceNavigator.PhotoSourceNavigatorHolder holder) {
		super(activity, photoSource, holder);
	}

	@Override
	protected void onLoadMedia(Folder folder, int startOffset) {
		ArrayList<MediaItem> photos = new ArrayList<MediaItem>();
		for (String imageUrl : IAMGE_URL) {
			photos.add(new Photo(imageUrl, imageUrl));
		}
		loadMediaFinished(photos);
	}

}
