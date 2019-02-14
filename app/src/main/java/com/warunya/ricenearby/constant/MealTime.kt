package com.warunya.ricenearby.constant

import java.util.*

enum class MealTime(val mealTimeText: String) {

    Breakfast("มื้อเช้า"),
    Lunch("มื้อกลางวัน"),
    Dinner("มื้อเย็น"),
    LateDinner("มื้อค่ำ");


    companion object {
        fun parse(type: Int): MealTime {
            val creatorMap = HashMap<Int, MealTime>()
            creatorMap[Breakfast.ordinal] = Breakfast
            creatorMap[Lunch.ordinal] = Lunch
            creatorMap[Dinner.ordinal] = Dinner
            creatorMap[LateDinner.ordinal] = LateDinner
            return if (creatorMap[type] == null) Breakfast else creatorMap[type]!!
        }
    }
}