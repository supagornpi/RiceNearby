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
    public long timestamp;
    public OrderStatus orderStatus;
    public List<Cart> carts;
    public int totalPrice;
    public int deliveryPrice;
    public Upload billingImage;

    public Order() {
    }

    public Order(String uid, OrderStatus orderStatus, List<Cart> carts, int totalPrice, int deliveryPrice) {
        this.uid = uid;
        this.orderStatus = orderStatus;
        this.carts = carts;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("orderStatus", orderStatus);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("carts", carts);
        result.put("totalPrice", totalPrice);
        result.put("deliveryPrice", deliveryPrice);
        result.put("billingImage", billingImage);

        return result;
    }

}
