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
		items.add(new NameIdPair(null));

		for (ProductType type : ProductType.values()) {
			items.add(new NameIdPair(type));
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position).product;
	}

	@Override
	public long getItemId(int position) {
		ProductType idType = items.get(position).product;
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
		public ProductType product;

		public NameIdPair(ProductType product) {
			this.name = product == null ? "Product not selected" : product.toString();
			this.product = product;
		}

	}
}
