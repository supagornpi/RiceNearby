package com.warunya.ricenearby.model;

import org.parceler.Parcel;

@Parcel
public class FoodType {

    public String typeName;
    public boolean isSelected;

    public FoodType() {
    }

    public FoodType(String typeName) {
        this.typeName = typeName;
    }
}
