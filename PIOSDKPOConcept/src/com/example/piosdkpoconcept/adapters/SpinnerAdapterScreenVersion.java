package com.example.piosdkpoconcept.adapters;

import java.util.ArrayList;
import java.util.List;

import print.io.piopublic.ScreenVersion;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.piosdkpoconcept.R;

public class SpinnerAdapterScreenVersion extends BaseAdapter implements SpinnerAdapter {

	private LayoutInflater inflater;
	private List<NameScreenPair> items;

	public SpinnerAdapterScreenVersion(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameScreenPair>();

		for (ScreenVersion sv : ScreenVersion.values()) {
			items.add(new NameScreenPair(sv));
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position).screenVersion;
	}

	@Override
	public long getItemId(int position) {
		ScreenVersion idType = items.get(position).screenVersion;
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
		public ScreenVersion screenVersion;

		public NameScreenPair(ScreenVersion screenVersion) {
			this.screenVersion = screenVersion;
			this.name = screenVersion != null ? screenVersion.toString() : "Screen version not selected";
		}
	}
}
