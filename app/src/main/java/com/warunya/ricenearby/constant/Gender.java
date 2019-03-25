package com.warunya.ricenearby.constant;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public enum Gender {

    @SerializedName("0")
    None,
    @SerializedName("1")
    Male,
    @SerializedName("2")
    Female;

    public static Gender parse(int type) {
        HashMap<Integer, Gender> creatorMap = new HashMap<>();
        creatorMap.put(None.ordinal(), None);
        creatorMap.put(Male.ordinal(), Male);
        creatorMap.put(Female.ordinal(), Female);

        return (creatorMap.get(type) == null) ? None : creatorMap.get(type);
    }
}
