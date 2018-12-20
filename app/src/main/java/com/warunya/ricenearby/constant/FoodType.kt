package com.warunya.ricenearby.constant

import java.util.*

enum class FoodType {

    None,
    MeatFood,
    Dessert,
    ChineseFood,
    HealthyFood,
    HalalFood,
    Vegetarian,
    KoreanFood,
    ItalianFood,
    VegetarianFood,
    JapaneseFood,
    ThaiFood;

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
    }
}