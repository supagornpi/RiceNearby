package com.warunya.ricenearby.constant;

import com.warunya.ricenearby.R;

import java.util.HashMap;

public enum FoodType {

    None("", 0),
    MeatFood("", 0),
    Dessert("", 0),
    ChineseFood("อาหารจีน", R.drawable.ic_china),
    HealthyFood("อาหารสุขภาพ", R.drawable.ic_healthy_food),
    HalalFood("อาหารฮาลาล", R.drawable.ic_islamic_food),
    Vegetarian("", 0),
    KoreanFood("อาหารเกาหลี", R.drawable.ic_korean_food),
    ItalianFood("", 0),
    VegetarianFood("อาหารเจ", R.drawable.ic_vegetarian_food),
    JapaneseFood("อาหารญี่ปุ่น", R.drawable.ic_japan),
    ThaiFood("อาหารไทย", R.drawable.ic_thai_food);

    private String foodTypeName;
    private int iconId;

    FoodType(String foodTypeName, int iconId) {
        this.foodTypeName = foodTypeName;
        this.iconId = iconId;
    }

    public static FoodType parse(int type) {
        HashMap<Integer, FoodType> creatorMap = new HashMap<>();
        creatorMap.put(None.ordinal(), None);
        creatorMap.put(MeatFood.ordinal(), MeatFood);
        creatorMap.put(Dessert.ordinal(), Dessert);
        creatorMap.put(ChineseFood.ordinal(), ChineseFood);
        creatorMap.put(HealthyFood.ordinal(), HealthyFood);
        creatorMap.put(HalalFood.ordinal(), HalalFood);
        creatorMap.put(Vegetarian.ordinal(), Vegetarian);
        creatorMap.put(KoreanFood.ordinal(), KoreanFood);
        creatorMap.put(ItalianFood.ordinal(), ItalianFood);
        creatorMap.put(VegetarianFood.ordinal(), VegetarianFood);
        creatorMap.put(JapaneseFood.ordinal(), JapaneseFood);
        creatorMap.put(ThaiFood.ordinal(), ThaiFood);

        return (creatorMap.get(type) == null) ? None : creatorMap.get(type);
    }

    public static FoodType parse(String type) {
        HashMap<String, FoodType> creatorMap = new HashMap<>();
        creatorMap.put(None.getFoodTypeName(), None);
        creatorMap.put(MeatFood.getFoodTypeName(), MeatFood);
        creatorMap.put(Dessert.getFoodTypeName(), Dessert);
        creatorMap.put(ChineseFood.getFoodTypeName(), ChineseFood);
        creatorMap.put(HealthyFood.getFoodTypeName(), HealthyFood);
        creatorMap.put(HalalFood.getFoodTypeName(), HalalFood);
        creatorMap.put(Vegetarian.getFoodTypeName(), Vegetarian);
        creatorMap.put(KoreanFood.getFoodTypeName(), KoreanFood);
        creatorMap.put(ItalianFood.getFoodTypeName(), ItalianFood);
        creatorMap.put(VegetarianFood.getFoodTypeName(), VegetarianFood);
        creatorMap.put(JapaneseFood.getFoodTypeName(), JapaneseFood);
        creatorMap.put(ThaiFood.getFoodTypeName(), ThaiFood);

        return (creatorMap.get(type) == null) ? None : creatorMap.get(type);
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public int getIconId() {
        return iconId;
    }
}
