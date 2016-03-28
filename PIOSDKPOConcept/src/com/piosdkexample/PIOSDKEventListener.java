package com.piosdkexample;

import java.util.List;

import print.io.PIO;
import print.io.beans.OrderInfo;
import print.io.beans.cart.CartItem;
import print.io.beans.cart.ShoppingCart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Simple PIO SDK events listener implementation. See {@code AppExample} to see
 * how listener is hooked.
 * 
 * @author Vlado
 */
public class PIOSDKEventListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String type = intent.getStringExtra(PIOConstants.PIO_SDK_EVENT_TYPE);

		if (PIOConstants.PIO_SDK_EVENT_CART_CHANGED.equals(type)) {
			Log.d("PIO_SDK_EXAMPLE", "Cart changed:");
			Log.d("PIO_SDK_EXAMPLE", cartToString(PIO.getShoppingCart(context)));
		} else if (PIOConstants.PIO_SDK_EVENT_ORDER_COMPLETED.equals(type)) {
			Log.d("PIO_SDK_EXAMPLE", "Order finished:");
			Log.d("PIO_SDK_EXAMPLE", orderToString(PIO.getLastOrder(context)));
		}
	}

	private static String cartToString(ShoppingCart cart) {
		List<CartItem> cartItems = cart.getItems();
		StringBuilder stringBuilder = new StringBuilder();
		if (cartItems != null) {
			stringBuilder.append("\nShopping Cart Items Quantity:\n").append(Integer.toString(cartItems.size()));
			if (cartItems.size() > 0) {
				stringBuilder.append("\n").append("Content of Shopping Cart:");
				for (CartItem item : cartItems) {
					stringBuilder.append("\n").append(item.getProductName());
				}
			}
		}
		return stringBuilder.toString();
	}

	private static String orderToString(OrderInfo order) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\nOrder ID: ").append(order.getOrderId());
		stringBuilder.append("\nItems count: ").append(order.getItems().size());
		stringBuilder.append("\nTotal: ").append(order.getTotalPrice());
		stringBuilder.append("\nDiscount: ").append(order.getDiscountAmount());
		stringBuilder.append("\nCoupon code: ").append(order.getCouponCode());
		return stringBuilder.toString();
	}


}
