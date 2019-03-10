package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.warunya.ricenearby.constant.OrderStatus;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Order {

    public String key;
    public String uid;
    public String orderNo;
    public long timestamp;
    public OrderStatus orderStatus;
    public List<Cart> carts;
    public Cart cart;
    public int totalPrice;
    public int deliveryPrice;
    public Upload billingImage;
    public String additionalAddress;
    public Address address;

    public Order() {
    }

    public Order(String orderNo, String uid, OrderStatus orderStatus, Cart cart, int totalPrice, int deliveryPrice) {
        this.orderNo = orderNo;
        this.uid = uid;
        this.orderStatus = orderStatus;
        this.cart = cart;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("orderNo", orderNo);
        result.put("orderStatus", orderStatus);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("carts", carts);
        result.put("cart", cart);
        result.put("totalPrice", totalPrice);
        result.put("deliveryPrice", deliveryPrice);
        result.put("billingImage", billingImage);
        result.put("additionalAddress", additionalAddress);
        result.put("address", address);
        return result;
    }

}
