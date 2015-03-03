package com.example.piosdkpoconcept;

import java.util.List;

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
		stringBuilder.append("\n").append("Order ID: ").append(order.getOrderId());
		stringBuilder.append("\n").append("Total: ").append(order.getTotalPrice());
		stringBuilder.append("\n").append("Items count: ").append(order.getItems().size());
		return stringBuilder.toString();
	}

}
