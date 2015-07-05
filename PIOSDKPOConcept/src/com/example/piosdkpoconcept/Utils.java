package com.example.piosdkpoconcept;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import print.io.beans.OrderInfo;
import print.io.beans.cart.CartItem;
import print.io.beans.cart.ShoppingCart;

public class Utils {

	public static String cartToString(ShoppingCart cart) {
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

	public static String orderToString(OrderInfo order) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\nOrder ID: ").append(order.getOrderId());
		stringBuilder.append("\nItems count: ").append(order.getItems().size());
		stringBuilder.append("\nTotal: ").append(order.getTotalPrice());
		stringBuilder.append("\nDiscount: ").append(order.getDiscountAmount());
		stringBuilder.append("\nCoupon code: ").append(order.getCouponCode());
		return stringBuilder.toString();
	}

	public static int dipToPx(Context context, int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}

	public static int getScreenWidthPixels(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
}
