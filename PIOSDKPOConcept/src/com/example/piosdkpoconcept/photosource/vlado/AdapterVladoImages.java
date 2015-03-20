package com.example.piosdkpoconcept.photosource.vlado;

import java.io.InputStream;
import java.util.List;

import print.io.beans.GenericPhoto;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piosdkpoconcept.R;
import com.example.piosdkpoconcept.Utils;

public class AdapterVladoImages extends BaseAdapter {

	private LayoutInflater inflater;
	private List<VladoPhoto> items;
	private int minBitmapDim;

	public AdapterVladoImages(Context context, List<VladoPhoto> items, List<GenericPhoto> selectedPhotos) {
		this.items = items;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.minBitmapDim = (Utils.getScreenWidthPixels(context) / 2) - Utils.dipToPx(context, 20);
		updateSelectedPhotos(selectedPhotos);
	}

	public void setItems(List<VladoPhoto> items, List<GenericPhoto> selectedPhotos) {
		this.items = items;
		updateSelectedPhotos(selectedPhotos);
	}

	public List<VladoPhoto> getItems() {
		return items;
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

	protected void updateSelectedPhotos(List<GenericPhoto> selectedPhotos) {
		if (items != null) {
			for (VladoPhoto item : items) {
				GenericPhoto photo = GenericPhoto.findByImageUrl(selectedPhotos, item.getImageUrl());
				if (photo != null) {
					item.setSelected(true);
					item.setIndexOfPhoto(photo.getIndexOfPhoto());
				}
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(R.layout.vlado_image_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder.update(position);
		return convertView;
	}

	private class ViewHolder {

		private ImageView imageViewImage;
		private View viewOverlay;
		private TextView textViewImageIndex;

		public ViewHolder(View convertView) {
			imageViewImage = (ImageView) convertView.findViewById(R.id.imageview_image);
			viewOverlay = (View) convertView.findViewById(R.id.overlay_translucent);
			textViewImageIndex = (TextView) convertView.findViewById(R.id.textview_image_index);

			imageViewImage.getLayoutParams().width = minBitmapDim;
			imageViewImage.getLayoutParams().height = minBitmapDim;
		}

		public void update(int position) {
			VladoPhoto item = (VladoPhoto) getItem(position);
			if (item != null) {
				new DownloadImageTask(imageViewImage).execute(item.getThumbnailUrl());
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

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("VladoSource", "Error downloading image");
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}

	}

}