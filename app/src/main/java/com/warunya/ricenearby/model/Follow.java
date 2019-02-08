package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Follow {

    public String key;
    public String uidSeller;
    public String uid;

    public User user; //get in view
    public boolean isFollowing; //get in view

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uidSeller", uidSeller);
        result.put("uid", uid);
        result.put("timestamp", ServerValue.TIMESTAMP);
        return result;
    }
}
