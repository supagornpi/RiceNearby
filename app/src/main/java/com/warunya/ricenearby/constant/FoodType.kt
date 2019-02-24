package com.warunya.ricenearby.constant

import com.warunya.ricenearby.R
import java.util.*

enum class FoodType(val foodTypeName: String, val iconId: Int) {

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

    companion object {
        fun parse(type: Int): FoodType {
            val creatorMap = HashMap<Int, FoodType>()
            creatorMap[MeatFood.ordinal] = MeatFood
            creatorMap[Dessert.ordinal] = Dessert
            creatorMap[ChineseFood.ordinal] = ChineseFood
            creatorMap[HealthyFood.ordinal] = HealthyFood
            creatorMap[HalalFood.ordinal] = HalalFood
            creatorMap[Vegetarian.ordinal] = Vegetarian
            creatorMap[KoreanFood.ordinal] = KoreanFood
            creatorMap[ItalianFood.ordinal] = ItalianFood
            creatorMap[VegetarianFood.ordinal] = VegetarianFood
            creatorMap[JapaneseFood.ordinal] = JapaneseFood
            creatorMap[ThaiFood.ordinal] = ThaiFood
            return if (creatorMap[type] == null) None else creatorMap[type]!!
        }

        fun parse(type: String): FoodType {
            val creatorMap = HashMap<String, FoodType>()
            creatorMap[MeatFood.foodTypeName] = MeatFood
            creatorMap[Dessert.foodTypeName] = Dessert
            creatorMap[ChineseFood.foodTypeName] = ChineseFood
            creatorMap[HealthyFood.foodTypeName] = HealthyFood
            creatorMap[HalalFood.foodTypeName] = HalalFood
            creatorMap[Vegetarian.foodTypeName] = Vegetarian
            creatorMap[KoreanFood.foodTypeName] = KoreanFood
            creatorMap[ItalianFood.foodTypeName] = ItalianFood
            creatorMap[VegetarianFood.foodTypeName] = VegetarianFood
            creatorMap[JapaneseFood.foodTypeName] = JapaneseFood
            creatorMap[ThaiFood.foodTypeName] = ThaiFood
            return if (creatorMap[type] == null) None else creatorMap[type]!!
        }
    }
}