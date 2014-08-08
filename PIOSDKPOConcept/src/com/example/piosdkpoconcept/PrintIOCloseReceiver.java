package com.example.piosdkpoconcept;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class PrintIOCloseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		ArrayList<String> cartItems = intent.getStringArrayListExtra("ShoppingCartItems");
		StringBuilder stringBuilder = new StringBuilder("Feedback to host app: ");
		stringBuilder.append("\nShopping Cart Items Quantity:\n").append(Integer.toString(cartItems.size()));

		if (cartItems.size() > 0) {
			stringBuilder.append("\n").append("Content of Shopping Cart:");
			for (String s : cartItems) {
				stringBuilder.append("\n").append(s);
			}
		}

		View feedbackDialog = ((Activity) ctx).getWindow().getDecorView().findViewById(R.id.dialog_feedback);
		((TextView) feedbackDialog.findViewById(R.id.textview_feedback)).setText(stringBuilder.toString());
		feedbackDialog.setVisibility(View.VISIBLE);
	}

}
