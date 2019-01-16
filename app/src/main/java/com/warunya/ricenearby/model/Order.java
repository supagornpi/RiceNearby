package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.warunya.ricenearby.constant.OrderStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    public String key;
    public String uid;
    public OrderStatus orderStatus;
    public List<Cart> carts;

    public Order() {
    }

    public Order(String uid, OrderStatus orderStatus, List<Cart> carts) {
        this.uid = uid;
        this.orderStatus = orderStatus;
        this.carts = carts;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("orderStatus", orderStatus);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("carts", carts);

        return result;
    }

}
