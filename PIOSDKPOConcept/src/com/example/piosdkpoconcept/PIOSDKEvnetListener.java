package com.example.piosdkpoconcept;

import print.io.PIO;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PIOSDKEvnetListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String type = intent.getStringExtra(PIOConstants.PIO_SDK_EVENT_TYPE);

		if (PIOConstants.PIO_SDK_EVENT_CART_CHANGED.equals(type)) {
			Log.d("PIO_SDK_EXAMPLE", "Cart changed:");
			Log.d("PIO_SDK_EXAMPLE", Utils.cartToString(PIO.getShoppingCart(context)));
		} else if (PIOConstants.PIO_SDK_EVENT_ORDER_COMPLETED.equals(type)) {
			Log.d("PIO_SDK_EXAMPLE", "Order finished:");
			Log.d("PIO_SDK_EXAMPLE", Utils.orderToString(PIO.getLastOrder(context)));
		}
	}

}
