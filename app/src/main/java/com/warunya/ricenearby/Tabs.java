package com.warunya.ricenearby;

import java.util.HashMap;
import java.util.Map;

public enum Tabs {
    Home,
    Favorite,
    Cart,
    History,
    Schedule,
    Profile,
    Seller,
    Order;

    public static Tabs parse(int type) {
        Map<Integer, Tabs> creatorMap = new HashMap<>();
        creatorMap.put(Home.ordinal(), Home);
        creatorMap.put(Favorite.ordinal(), Favorite);
        creatorMap.put(Cart.ordinal(), Cart);
        creatorMap.put(History.ordinal(), History);
        creatorMap.put(Schedule.ordinal(), Schedule);
        creatorMap.put(Profile.ordinal(), Profile);
        creatorMap.put(Seller.ordinal(), Seller);
        creatorMap.put(Order.ordinal(), Order);
        return creatorMap.get(type);
    }
}
