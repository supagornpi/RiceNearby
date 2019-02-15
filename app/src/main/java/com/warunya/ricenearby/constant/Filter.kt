package com.warunya.ricenearby.constant

enum class Filter(val period: Int) {

    OneWeek(7),
    TwoWeeks(14),
    OneMonth(30),
    TwoMonths(60),
    ThreeMonths(90),
    SixMonths(180),
    All(0);

    companion object {
        fun parse(type: Int): Filter {
            val creatorMap = HashMap<Int, Filter>()
            creatorMap[OneWeek.ordinal] = OneWeek
            creatorMap[TwoWeeks.ordinal] = TwoWeeks
            creatorMap[OneMonth.ordinal] = OneMonth
            creatorMap[TwoMonths.ordinal] = TwoMonths
            creatorMap[ThreeMonths.ordinal] = ThreeMonths
            creatorMap[SixMonths.ordinal] = SixMonths
            creatorMap[All.ordinal] = All

            return if (creatorMap[type] == null) All else creatorMap[type]!!
        }
    }
}