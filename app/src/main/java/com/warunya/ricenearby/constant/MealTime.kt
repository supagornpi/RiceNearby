package com.warunya.ricenearby.constant

import java.util.*

enum class MealTime {

    Breakfast,
    Lunch,
    Dinner;

    companion object {
        fun parse(type: Int): MealTime {
            val creatorMap = HashMap<Int, MealTime>()
            creatorMap[Breakfast.ordinal] = Breakfast
            creatorMap[Lunch.ordinal] = Lunch
            creatorMap[Dinner.ordinal] = Dinner
            return if (creatorMap[type] == null) Breakfast else creatorMap[type]!!
        }
    }
}