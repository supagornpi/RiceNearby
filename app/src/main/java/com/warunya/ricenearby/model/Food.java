package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food {

    public String uid;
    public List<String> url;
    public String name;
    public String detail;
    public int amount;
    public int price;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", name);
        result.put("uid", uid);
        result.put("foodName", name);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("detail", detail);
        result.put("amount", amount);
        result.put("price", price);
        return result;
    }

    public Food(String uid, String name, int amount, int price, String detail) {
        this.uid = uid;
        this.name = name;
        this.detail = detail;
        this.amount = amount;
        this.price = price;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
