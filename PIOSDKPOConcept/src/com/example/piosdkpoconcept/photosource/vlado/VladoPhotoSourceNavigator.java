package com.example.piosdkpoconcept.photosource.vlado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.photosource.defaultgenericimpl.DefaultPhotoSourceNavigator;
import print.io.photosource.defaultgenericimpl.items.Album;
import print.io.photosource.defaultgenericimpl.items.Folder;
import print.io.photosource.defaultgenericimpl.items.Item;
import print.io.photosource.defaultgenericimpl.items.Photo;
import android.support.v4.app.FragmentActivity;

public class VladoPhotoSourceNavigator extends DefaultPhotoSourceNavigator<VladoPhotoSource> {

	private List<String> IMAGE_URL = Arrays
			.asList("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-9/526189_4617697276507_662207867_n.jpg?oh=8d527dd9102f0124fe0833be2cb24c22&oe=5580D92A&__gda__=1434779874_f9059584437b839eb5149e3e4da2fc97",
					"https://scontent-ams.xx.fbcdn.net/hphotos-prn2/t31.0-8/1268224_10201569069964657_1243815332_o.jpg");
	private List<Album> albums = new ArrayList<Album>();

	public VladoPhotoSourceNavigator(FragmentActivity activity, VladoPhotoSource photoSource, print.io.photosource.PhotoSourceNavigator.PhotoSourceNavigatorHolder holder) {
		super(activity, photoSource, holder);
		for (int i = 0; i < IMAGE_URL.size(); i++) {
			albums.add(new VladoAlbum(IMAGE_URL.get(i), "Album " + (i + 1), 1, i));
		}
	}

	@Override
	protected void onLoadMedia(Folder folder, int startOffset) {
		ArrayList<Item> photos = new ArrayList<Item>();
		if (folder == null) {
			for (String imageUrl : IMAGE_URL) {
				photos.add(new Photo(imageUrl, imageUrl));
			}
			for (Album album : albums) {
				photos.add(album);
			}
		} else {
			VladoAlbum album = (VladoAlbum) folder;
			String img = IMAGE_URL.get(album.getIndex());
			photos.add(new Photo(img, img));
		}
		loadMediaFinished(photos);
	}

	public class VladoAlbum extends Album {

		private int index;

		public VladoAlbum(String thumbnailUrl, String title, int photosCount, int index) {
			super(thumbnailUrl, title, photosCount);
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}

}
