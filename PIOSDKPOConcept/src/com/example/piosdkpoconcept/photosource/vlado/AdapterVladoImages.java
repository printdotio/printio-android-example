package com.example.piosdkpoconcept.photosource.vlado;

import java.util.AbstractMap;
import java.util.List;

import print.io.R;
import print.io.beans.GenericPhoto;
import print.io.imageloader.BitmapManager;
import print.io.imageloader.MyImageView;
import print.io.photosource.BaseAdapterImages;
import print.io.utils.FileChooserFileUtils;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;


public class AdapterVladoImages extends BaseAdapterImages<VladoPhotoSource, VladoPhoto> {

	public AdapterVladoImages(Context context, VladoPhotoSource photoSource, List<VladoPhoto> items, AbstractMap<String, GenericPhoto> selectedPhotos) {
		super(context, photoSource);
		this.items = items;
		updateSelectedPhotos(selectedPhotos);
	}

	public void setItems(List<VladoPhoto> items, AbstractMap<String, GenericPhoto> selectedPhotos) {
		this.items = items;
		updateSelectedPhotos(selectedPhotos);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(R.layout.grid_item_image_upload, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder.update(position);
		return convertView;
	}

	private class ViewHolder {

		private MyImageView imageViewImage;
		private View viewOverlay;
		private TextView textViewImageIndex;

		public ViewHolder(View convertView) {
			imageViewImage = (MyImageView) convertView.findViewById(R.id.imageview_image);
			viewOverlay = (View) convertView.findViewById(R.id.overlay_translucent);
			textViewImageIndex = (TextView) convertView.findViewById(R.id.textview_image_index);
		}

		public void update(int position) {
			VladoPhoto item = (VladoPhoto) getItem(position);
			if (item != null) {
				if (FileChooserFileUtils.isLocal(item.getImageUrl())) {
					String thumbnailUrl = FileChooserFileUtils.getPath(context, Uri.parse(item.getThumbnailUrl()));
					BitmapManager.loadImageInBackgroundLocal(context, thumbnailUrl, imageViewImage, ScaleType.CENTER_CROP, R.drawable.placeholder_featured, ScaleType.CENTER_CROP, minBitmapDim, null);
				} else {
					BitmapManager.loadImageInBackground(context, item.getThumbnailUrl(), imageViewImage, ScaleType.CENTER_CROP, R.drawable.placeholder_featured, ScaleType.CENTER_CROP, minBitmapDim,
							null);
				}

				if (item.isSelected()) {
					viewOverlay.setVisibility(View.VISIBLE);
					textViewImageIndex.setVisibility(View.VISIBLE);
					textViewImageIndex.setText(String.valueOf(item.getIndexOfPhoto() + 1));
				} else {
					viewOverlay.setVisibility(View.INVISIBLE);
					textViewImageIndex.setVisibility(View.INVISIBLE);
				}
			}

		}
	}

}