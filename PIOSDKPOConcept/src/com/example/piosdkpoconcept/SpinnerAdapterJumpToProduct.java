package com.example.piosdkpoconcept;

import java.util.ArrayList;

import print.io.PublicConstants;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SpinnerAdapterJumpToProduct extends BaseAdapter implements SpinnerAdapter {

	private class NameIdPair {
		public String name;
		public int id;
		public NameIdPair(String name, int id) {
			this.name = name;
			this.id = id;
		}
	}

	private LayoutInflater inflater;
	private ArrayList<NameIdPair> items;

	public SpinnerAdapterJumpToProduct(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameIdPair>();
		items.add(new NameIdPair("No product selected", SpinnerAdapter.NO_SELECTION));

		items.add(new NameIdPair("Phone Cases", PublicConstants.ProductIds.PHONE_CASES));
		items.add(new NameIdPair("Acrylic Blocks", PublicConstants.ProductIds.ACRYLIC_BLOCKS));
		items.add(new NameIdPair("Acrylic Prints", PublicConstants.ProductIds.ACRYLIC_PRINTS));
		items.add(new NameIdPair("Beach Bags", PublicConstants.ProductIds.BEACH_BAGS));
		items.add(new NameIdPair("Calendars", PublicConstants.ProductIds.CALENDARS));
		items.add(new NameIdPair("Canvas Wraps", PublicConstants.ProductIds.CANVAS_WRAPS));
		items.add(new NameIdPair("Coasters", PublicConstants.ProductIds.COASTERS));
		items.add(new NameIdPair("Cube Decor", PublicConstants.ProductIds.CUBE_DECOR));
		items.add(new NameIdPair("Duvet Cover", PublicConstants.ProductIds.DUVET_COVER));
		items.add(new NameIdPair("Flat Cards", PublicConstants.ProductIds.FLAT_CARDS));
		items.add(new NameIdPair("Fleece Blankets", PublicConstants.ProductIds.FLEECE_BLANKETS));
		items.add(new NameIdPair("Folder Cards", PublicConstants.ProductIds.FOLDED_CARDS));
		items.add(new NameIdPair("Framed Prints", PublicConstants.ProductIds.FRAMED_PRINTS));
		items.add(new NameIdPair("Laptop Skins", PublicConstants.ProductIds.LAPTOP_SKINS));
		items.add(new NameIdPair("Magnetgram", PublicConstants.ProductIds.MAGNETGRAM));
		items.add(new NameIdPair("Metal Magnets", PublicConstants.ProductIds.METAL_MAGNETS));
		items.add(new NameIdPair("Metal Prints", PublicConstants.ProductIds.METAL_PRINTS));
		items.add(new NameIdPair("Minibook Deluxe", PublicConstants.ProductIds.MINIBOOK_DELUXE));
		items.add(new NameIdPair("Minibooks", PublicConstants.ProductIds.MINIBOOKS));
		items.add(new NameIdPair("Mousepads", PublicConstants.ProductIds.MOUSEPADS));
		items.add(new NameIdPair("Mugs", PublicConstants.ProductIds.MUGS));
		items.add(new NameIdPair("Playing Cards", PublicConstants.ProductIds.PLAYING_CARDS));
		items.add(new NameIdPair("Postcards", PublicConstants.ProductIds.POSTCARDS));
		items.add(new NameIdPair("Posters", PublicConstants.ProductIds.POSTERS));
		items.add(new NameIdPair("Prints", PublicConstants.ProductIds.PRINTS));
		items.add(new NameIdPair("Shower Curtains", PublicConstants.ProductIds.SHOWER_CURTAINS));
		items.add(new NameIdPair("Stickerbooks", PublicConstants.ProductIds.STICKERBOOKS));
		items.add(new NameIdPair("T-Shirts", PublicConstants.ProductIds.T_SHIRTS));
		items.add(new NameIdPair("Tablet Cases", PublicConstants.ProductIds.TABLET_CASES));
		items.add(new NameIdPair("Thick Prints", PublicConstants.ProductIds.THICK_PRINTS));
		items.add(new NameIdPair("Throw Pillows", PublicConstants.ProductIds.THROW_PILLOWS));
		items.add(new NameIdPair("Tinybooks", PublicConstants.ProductIds.TINYBOOKS));
		items.add(new NameIdPair("Wall Skin", PublicConstants.ProductIds.WALL_SKIN));
		items.add(new NameIdPair("Wood Prints", PublicConstants.ProductIds.WOOD_PRINTS));
		items.add(new NameIdPair("Woven Blankets", PublicConstants.ProductIds.WOVEN_BLANKETS));
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
		return items.get(position).id;
	}

	private class ViewHolder {
		private TextView textViewName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_product, parent, false);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.textview_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textViewName.setText(items.get(position).name);

		return convertView;
	}

}
