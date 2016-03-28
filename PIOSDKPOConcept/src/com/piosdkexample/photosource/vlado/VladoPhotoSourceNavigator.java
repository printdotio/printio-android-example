package com.piosdkexample.photosource.vlado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.photosource.PhotoSourceNavigatorHolder;
import print.io.photosource.defaultgenericimpl.DefaultLoadMediaResult;
import print.io.photosource.defaultgenericimpl.DefaultPhotoSourceNavigator;
import print.io.photosource.defaultgenericimpl.items.Album;
import print.io.photosource.defaultgenericimpl.items.Folder;
import print.io.photosource.defaultgenericimpl.items.Item;
import print.io.photosource.defaultgenericimpl.items.Photo;

/**
 * Custom photo source navigator example used by {@link VladoPhotoSource}.
 * 
 * This navigator will show albums which will hold one photo from
 * {@link #IMAGE_URL} list each.
 * 
 * @author Vlado
 */
public class VladoPhotoSourceNavigator extends DefaultPhotoSourceNavigator<VladoPhotoSource> {

	private final List<String> IMAGE_URL = Arrays
			.asList("https://www.dropbox.com/s/8zpchy1la2dpnog/526189_4617697276507_662207867_n.jpg?dl=1",
					"https://www.dropbox.com/s/olh9i8kif2vx65h/1268224_10201569069964657_1243815332_o.jpg?dl=1");
	private final List<Album> albums = new ArrayList<Album>();

	public VladoPhotoSourceNavigator(VladoPhotoSource photoSource, PhotoSourceNavigatorHolder holder) {
		super(photoSource, holder);

		// Create albums and assign each album one photo 
		for (int i = 0; i < IMAGE_URL.size(); i++) {
			albums.add(new VladoAlbum(IMAGE_URL.get(i), "Album " + (i + 1), 1, i));
		}
		// Add empty album as showcase
		albums.add(new VladoAlbum(null, "Empty album", 0, -1));
	}

	@Override
	protected LoadMediaResult onLoadMedia(Folder folder, int startOffset, int defaultPageSize) {
		ArrayList<Item> items = new ArrayList<Item>();
		if (folder == null) {
			// This is root view; show all albums
			for (Album album : albums) {
				items.add(album);
			}
		} else {
			// Show picture from this album
			int index = ((VladoAlbum) folder).getIndex();
			if (index != -1) {
				String img = IMAGE_URL.get(index);
				items.add(new Photo(img, img));
			}
		}
		return new DefaultLoadMediaResult(items);
	}

	class VladoAlbum extends Album {

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
