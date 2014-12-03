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

public class SpinnerAdapterPaymentOptions extends BaseAdapter implements SpinnerAdapter {

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

	public SpinnerAdapterPaymentOptions(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = new ArrayList<NameIdPair>();
		items.add(new NameIdPair("All", PublicConstants.PaymentOptions.PAYMENT_OPTION_ALL));
		items.add(new NameIdPair("PayPal", PublicConstants.PaymentOptions.PAYMENT_OPTION_PAY_PAL));
		items.add(new NameIdPair("Credit Card", PublicConstants.PaymentOptions.PAYMENT_OPTION_CREDIT_CARD));
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
