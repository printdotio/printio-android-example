package com.example.piosdkpoconcept.photosource.vlado;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.beans.GenericPhoto;
import print.io.photosource.BasePhotoSourceAdapter;
import print.io.photosource.PhotoSourceNavigator;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;


public class VladoPhotoSourceNavigator extends PhotoSourceNavigator<VladoPhotoSource> {

	private List<String> IAMGE_URL = Arrays
			.asList("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-9/526189_4617697276507_662207867_n.jpg?oh=8d527dd9102f0124fe0833be2cb24c22&oe=5580D92A&__gda__=1434779874_f9059584437b839eb5149e3e4da2fc97",
					"https://scontent-ams.xx.fbcdn.net/hphotos-prn2/t31.0-8/1268224_10201569069964657_1243815332_o.jpg");

	private BasePhotoSourceAdapter<VladoPhotoSource, VladoPhoto> adapter;

	public VladoPhotoSourceNavigator(VladoPhotoSource photoSource) {
		super(photoSource);
	}

	@Override
	public void load(LoadingFinishedListener loadFinished) {
		loadFinished.onLoadingFinished(true);

		// Create photos of Vlado
		List<VladoPhoto> photos = new ArrayList<VladoPhoto>();
		for (String imageUrl : IAMGE_URL) {
			photos.add(new VladoPhoto(imageUrl, imageUrl));
		}

		adapter = new AdapterVladoImages(activity, photoSource, photos, holder.getSelectedPhotos());
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(photoOnClickListener);
		holder.onAdapterChanged();
	}

	@Override
	public boolean hasPrintableMedia() {
		return adapter.getCount() > 0;
	}

	@Override
	public void cancel() {
		// NOP
	}

	@Override
	public boolean onNavigateBack() {
		return false;
	}

	private OnItemClickListener photoOnClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int position, long id) {
			AbstractMap<String, GenericPhoto> selectedPhotos = holder.getSelectedPhotos();
			VladoPhoto photo = (VladoPhoto) adapterView.getItemAtPosition(position);

			if (!photo.isSelected() && selectedPhotos.size() == maxNumberOfImages) {
				holder.onMaxImagesSelected();
				return;
			}

			photo.setSelected(!photo.isSelected());
			if (photo.isSelected()) {
				int index = selectedPhotos.size();
				photo.setIndexOfPhoto(index);
				selectedPhotos.put(photo.getImageUrl(), new GenericPhoto(photo.getImageUrl(), photo.getThumbnailUrl(), 0, 0, photoSource.getServiceId(), index));
			} else {
				int indexOfRemoved = photo.getIndexOfPhoto();
				selectedPhotos.remove(photo.getImageUrl());
				photoUnselected(indexOfRemoved);

				List<VladoPhoto> items = adapter.getItems();
				for (int i = 0; i < items.size(); i++) {
					VladoPhoto photoFromAdapter = items.get(i);
					if (photoFromAdapter.getIndexOfPhoto() > indexOfRemoved) {
						photoFromAdapter.setIndexOfPhoto(photoFromAdapter.getIndexOfPhoto() - 1);
					}
				}
			}

			adapter.notifyDataSetChanged();
			holder.updateSelectedPhotosCount();
		}
	};

}
