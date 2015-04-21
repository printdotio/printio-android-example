package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.List;

import print.io.piopublic.ProductType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SpinnerAdapterJumpToProduct extends BaseAdapter implements SpinnerAdapter {

	private LayoutInflater inflater;
	private List<NameIdPair> items;

	public SpinnerAdapterJumpToProduct(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameIdPair>();
		items.add(new NameIdPair("No product selected", null));

		items.add(new NameIdPair("Acrylic Blocks", ProductType.ACRYLIC_BLOCKS));
		items.add(new NameIdPair("Acrylic Prints", ProductType.ACRYLIC_PRINTS));
		items.add(new NameIdPair("Acrylic Trays", ProductType.ACRYLIC_TRAYS));
		items.add(new NameIdPair("Art Posters", ProductType.ART_POSTERS));
		items.add(new NameIdPair("Beach Towel", ProductType.BEACH_TOWEL));
		items.add(new NameIdPair("Canvas Minis", ProductType.CANVAS_MINIS));
		items.add(new NameIdPair("Canvas Posters", ProductType.CANVAS_POSTERS));
		items.add(new NameIdPair("Canvas Wraps", ProductType.CANVAS_WRAPS));
		items.add(new NameIdPair("Calendars", ProductType.CALENDARS));
		items.add(new NameIdPair("Cloth Napkins", ProductType.CLOTH_NAPKINS));
		items.add(new NameIdPair("Coasters", ProductType.COASTERS));
		items.add(new NameIdPair("Cube Decor", ProductType.CUBE_DECOR));
		items.add(new NameIdPair("Dog Beds", ProductType.DOG_BEDS));
		items.add(new NameIdPair("Duvet Cover", ProductType.DUVET_COVER));
		items.add(new NameIdPair("Everything Bags", ProductType.EVERYTHING_BAGS));
		items.add(new NameIdPair("Framed Prints", ProductType.FRAMED_PRINTS));
		items.add(new NameIdPair("Flat Cards", ProductType.FLAT_CARDS));
		items.add(new NameIdPair("Fleece Blankets", ProductType.FLEECE_BLANKETS));
		items.add(new NameIdPair("Folder Cards", ProductType.FOLDED_CARDS));
		items.add(new NameIdPair("Glass Cutting Boards", ProductType.GLASS_CUTTING_BOARDS));
		items.add(new NameIdPair("Laptop Skins", ProductType.LAPTOP_SKINS));
		items.add(new NameIdPair("Magnetgram", ProductType.MAGNETGRAM));
		items.add(new NameIdPair("Metal Magnets", ProductType.METAL_MAGNETS));
		items.add(new NameIdPair("Metal Prints", ProductType.METAL_PRINTS));
		items.add(new NameIdPair("Minibook Deluxe", ProductType.MINIBOOK_DELUXE));
		items.add(new NameIdPair("Minibooks", ProductType.MINIBOOKS));
		items.add(new NameIdPair("Mousepads", ProductType.MOUSEPADS));
		items.add(new NameIdPair("Mugs", ProductType.MUGS));
		items.add(new NameIdPair("Ottomans", ProductType.OTTOMANS));
		items.add(new NameIdPair("Pillow Shams", ProductType.PILLOW_SHAMS));
		items.add(new NameIdPair("Puzzles", ProductType.PUZZLES));
		items.add(new NameIdPair("Phone Cases", ProductType.PHONE_CASES));
		items.add(new NameIdPair("Placemats", ProductType.PLACEMATS));
		items.add(new NameIdPair("Playing Cards", ProductType.PLAYING_CARDS));
		items.add(new NameIdPair("Postcards", ProductType.POSTCARDS));
		items.add(new NameIdPair("Posters", ProductType.POSTERS));
		items.add(new NameIdPair("Prints", ProductType.PRINTS));
		items.add(new NameIdPair("Professional prints", ProductType.PROFESSIONAL_PRINTS));
		items.add(new NameIdPair("Rugs", ProductType.RUGS));
		items.add(new NameIdPair("Shower Curtains", ProductType.SHOWER_CURTAINS));
		items.add(new NameIdPair("Stickerbooks", ProductType.STICKERBOOKS));
		items.add(new NameIdPair("Stone Coasters", ProductType.STONE_COASTERS));
		items.add(new NameIdPair("Tablet Cases", ProductType.TABLET_CASES));
		items.add(new NameIdPair("Tabletop Canvas Wraps", ProductType.TABLETOP_CANVAS_WRAPS));
		items.add(new NameIdPair("Tinybooks", ProductType.TINYBOOKS));
		items.add(new NameIdPair("Tote Bags", ProductType.TOTE_BAGS));
		items.add(new NameIdPair("Thick Prints", ProductType.THICK_PRINTS));
		items.add(new NameIdPair("Throw Pillows", ProductType.THROW_PILLOWS));
		items.add(new NameIdPair("T-Shirts", ProductType.T_SHIRTS));
		items.add(new NameIdPair("Wall Calendars", ProductType.WALL_CALENDARS));
		items.add(new NameIdPair("Wall Skin", ProductType.WALL_SKIN));
		items.add(new NameIdPair("Woven Blankets", ProductType.WOVEN_BLANKETS));
		items.add(new NameIdPair("Wood Prints", ProductType.WOOD_PRINTS));

	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position).productIdType;
	}

	@Override
	public long getItemId(int position) {
		ProductType idType = items.get(position).productIdType;
		return idType == null ? SpinnerAdapter.NO_SELECTION : idType.ordinal();
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

	private class ViewHolder {

		private TextView textViewName;

	}

	private class NameIdPair {

		public String name;
		public ProductType productIdType;

		public NameIdPair(String name, ProductType id) {
			this.name = name;
			this.productIdType = id;
		}

	}
}
