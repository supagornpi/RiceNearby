package com.warunya.ricenearby;

import java.util.HashMap;
import java.util.Map;

public enum Tabs {
    Home,
    Favorite,
    History,
    Profile;

    public static Tabs parse(int type) {
        Map<Integer, Tabs> creatorMap = new HashMap<>();
        creatorMap.put(Home.ordinal(), Home);
        creatorMap.put(Favorite.ordinal(), Favorite);
        creatorMap.put(History.ordinal(), History);
        creatorMap.put(Profile.ordinal(), Profile);
        return creatorMap.get(type);
    }
}
