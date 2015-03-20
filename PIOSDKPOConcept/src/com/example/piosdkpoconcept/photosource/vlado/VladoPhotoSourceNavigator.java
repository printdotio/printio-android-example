package com.example.piosdkpoconcept.photosource.vlado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import print.io.beans.GenericPhoto;
import print.io.photosource.PhotoSourceNavigator;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class VladoPhotoSourceNavigator extends PhotoSourceNavigator<VladoPhotoSource> {

	private List<String> IAMGE_URL = Arrays
			.asList("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xfa1/v/t1.0-9/526189_4617697276507_662207867_n.jpg?oh=8d527dd9102f0124fe0833be2cb24c22&oe=5580D92A&__gda__=1434779874_f9059584437b839eb5149e3e4da2fc97",
					"https://scontent-ams.xx.fbcdn.net/hphotos-prn2/t31.0-8/1268224_10201569069964657_1243815332_o.jpg");

	private AdapterVladoImages adapter;


	public VladoPhotoSourceNavigator(FragmentActivity activity, VladoPhotoSource photoSource, PhotoSourceNavigatorHolder holder) {
		super(activity, photoSource, holder);
	}

	@Override
	public void prepare(RelativeLayout layout) {
		super.prepare(layout);
		gridView.setNumColumns(2);
	}

	@Override
	public void load(LoadingFinishedListener loadFinished) {
		loadFinished.onLoadingFinished(true);

		// Create photos of Vlado
		List<VladoPhoto> photos = new ArrayList<VladoPhoto>();
		for (String imageUrl : IAMGE_URL) {
			photos.add(new VladoPhoto(imageUrl, imageUrl));
		}

		adapter = new AdapterVladoImages(activity, photos, holder.getSelectedPhotos());
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(photoOnClickListener);
		holder.onPreviewChanged();
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
			VladoPhoto photo = (VladoPhoto) adapterView.getItemAtPosition(position);

			if (!photo.isSelected()) {
				int index = holder.onPhotoSelected(new GenericPhoto(photo.getImageUrl(), photo.getThumbnailUrl(), 0, 0, photoSource.getServiceId()));
				if (index != -1) {
					photo.setSelected(true);
					photo.setIndexOfPhoto(index);
				}
			} else {
				holder.onPhotoUnselected(photo.getImageUrl());
				photo.setSelected(false);
				int indexOfRemoved = photo.getIndexOfPhoto();

				List<VladoPhoto> items = adapter.getItems();
				for (int i = 0; i < items.size(); i++) {
					VladoPhoto photoFromAdapter = items.get(i);
					if (photoFromAdapter.getIndexOfPhoto() > indexOfRemoved) {
						photoFromAdapter.setIndexOfPhoto(photoFromAdapter.getIndexOfPhoto() - 1);
					}
				}
			}

			adapter.notifyDataSetChanged();
		}
	};


	@Override
	public void selectAllButtonOnClick() {
		// TODO Auto-generated method stub
		
	}

}
