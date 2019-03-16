package com.warunya.ricenearby.constant;

import java.util.HashMap;

public enum Filter {

    OneWeek(7),
    TwoWeeks(14),
    OneMonth(30),
    TwoMonths(60),
    ThreeMonths(90),
    SixMonths(180),
    All(0);

    int period;

    Filter(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    public static Filter parse(int type) {
        HashMap<Integer, Filter> creatorMap = new HashMap<>();
        creatorMap.put(OneWeek.ordinal(), OneWeek);
        creatorMap.put(TwoWeeks.ordinal(), TwoWeeks);
        creatorMap.put(OneMonth.ordinal(), OneMonth);
        creatorMap.put(TwoMonths.ordinal(), TwoMonths);
        creatorMap.put(ThreeMonths.ordinal(), ThreeMonths);
        creatorMap.put(SixMonths.ordinal(), SixMonths);
        creatorMap.put(All.ordinal(), All);

        return (creatorMap.get(type) == null) ? All : creatorMap.get(type);
    }
}
