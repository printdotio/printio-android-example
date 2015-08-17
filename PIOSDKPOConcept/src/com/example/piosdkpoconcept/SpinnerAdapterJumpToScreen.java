package com.example.piosdkpoconcept;

import java.util.ArrayList;
import java.util.List;

import print.io.piopublic.Screen;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class SpinnerAdapterJumpToScreen extends BaseAdapter implements SpinnerAdapter {

	private LayoutInflater inflater;
	private List<NameScreenPair> items;

	public SpinnerAdapterJumpToScreen(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameScreenPair>();
		items.add(new NameScreenPair(null));

		items.add(new NameScreenPair(Screen.PRODUCT_DETAILS_V2));
		items.add(new NameScreenPair(Screen.SHOPPING_CART));
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position).screen;
	}

	@Override
	public long getItemId(int position) {
		Screen idType = items.get(position).screen;
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

	private class NameScreenPair {

		public String name;
		public Screen screen;

		public NameScreenPair(Screen screen) {
			this.screen = screen;
			this.name = screen != null ? screen.toString() : "Screen not selected";
		}
	}
}
