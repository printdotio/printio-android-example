package com.example.piosdkpoconcept;

import java.util.ArrayList;

import print.io.PIOConfig.ProductIdType;
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
		public ProductIdType productIdType;

		public NameIdPair(String name, ProductIdType id) {
			this.name = name;
			this.productIdType = id;
		}
	}

	private LayoutInflater inflater;
	private ArrayList<NameIdPair> items;

	public SpinnerAdapterJumpToProduct(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameIdPair>();
		items.add(new NameIdPair("No product selected", null));

		items.add(new NameIdPair("Phone Cases", ProductIdType.PHONE_CASES));
		items.add(new NameIdPair("Acrylic Blocks", ProductIdType.ACRYLIC_BLOCKS));
		items.add(new NameIdPair("Acrylic Prints", ProductIdType.ACRYLIC_PRINTS));
		items.add(new NameIdPair("Everything Bags", ProductIdType.EVERYTHING_BAGS));
		items.add(new NameIdPair("Tote Bags", ProductIdType.TOTE_BAGS));
		// items.add(new NameIdPair("Calendars", ProductIdType.CALENDARS)); // NOT AVALIABLE!
		items.add(new NameIdPair("Canvas Minis", ProductIdType.CANVAS_MINIS));
		items.add(new NameIdPair("Canvas Posters", ProductIdType.CANVAS_POSTERS));
		items.add(new NameIdPair("Canvas Wraps", ProductIdType.CANVAS_WRAPS));
		items.add(new NameIdPair("Coasters", ProductIdType.COASTERS));
		items.add(new NameIdPair("Cube Decor", ProductIdType.CUBE_DECOR));
		items.add(new NameIdPair("Duvet Cover", ProductIdType.DUVET_COVER));
		items.add(new NameIdPair("Dog Beds", ProductIdType.DOG_BEDS));
		items.add(new NameIdPair("Flat Cards", ProductIdType.FLAT_CARDS));
		items.add(new NameIdPair("Fleece Blankets", ProductIdType.FLEECE_BLANKETS));
		items.add(new NameIdPair("Folder Cards", ProductIdType.FOLDED_CARDS));
		items.add(new NameIdPair("Framed Prints", ProductIdType.FRAMED_PRINTS));
		items.add(new NameIdPair("Laptop Skins", ProductIdType.LAPTOP_SKINS));
		items.add(new NameIdPair("Magnetgram", ProductIdType.MAGNETGRAM));
		items.add(new NameIdPair("Metal Magnets", ProductIdType.METAL_MAGNETS));
		items.add(new NameIdPair("Metal Prints", ProductIdType.METAL_PRINTS));
		// items.add(new NameIdPair("Minibook Deluxe", ProductIdType.MINIBOOK_DELUXE)); // NOT AVALIABLE!
		items.add(new NameIdPair("Minibooks", ProductIdType.MINIBOOKS));
		items.add(new NameIdPair("Mousepads", ProductIdType.MOUSEPADS));
		items.add(new NameIdPair("Mugs", ProductIdType.MUGS));
		items.add(new NameIdPair("Playing Cards", ProductIdType.PLAYING_CARDS));
		items.add(new NameIdPair("Postcards", ProductIdType.POSTCARDS));
		// items.add(new NameIdPair("Posters", ProductIdType.POSTERS)); // NOT AVALIABLE!
		items.add(new NameIdPair("Prints", ProductIdType.PRINTS));
		items.add(new NameIdPair("Professional prints", ProductIdType.PROFESSIONAL_PRINTS));
		items.add(new NameIdPair("Rugs", ProductIdType.RUGS));
		items.add(new NameIdPair("Shower Curtains", ProductIdType.SHOWER_CURTAINS));
		items.add(new NameIdPair("Stickerbooks", ProductIdType.STICKERBOOKS));
		items.add(new NameIdPair("T-Shirts", ProductIdType.T_SHIRTS));
		items.add(new NameIdPair("Tablet Cases", ProductIdType.TABLET_CASES));
		items.add(new NameIdPair("Thick Prints", ProductIdType.THICK_PRINTS));
		items.add(new NameIdPair("Throw Pillows", ProductIdType.THROW_PILLOWS));
		items.add(new NameIdPair("Tinybooks", ProductIdType.TINYBOOKS));
		items.add(new NameIdPair("Wall Calendars", ProductIdType.WALL_CALENDARS));
		items.add(new NameIdPair("Wall Skin", ProductIdType.WALL_SKIN));
		// items.add(new NameIdPair("Wood Prints", ProductIdType.WOOD_PRINTS)); // NOT AVALIABLE!
		items.add(new NameIdPair("Woven Blankets", ProductIdType.WOVEN_BLANKETS));
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
		ProductIdType idType = items.get(position).productIdType;
		return idType == null ? SpinnerAdapter.NO_SELECTION : idType.ordinal();
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
