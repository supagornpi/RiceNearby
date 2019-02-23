package com.warunya.ricenearby.ui.home;

import android.location.Location;

import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.FollowManager;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Follow;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.utils.SortUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private List<Food> recommendFoods = new ArrayList<>();
    private List<Food> followFoods = new ArrayList<>();
    private List<Food> nearbyFoods = new ArrayList<>();

    HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        view.hideNotFound();
        FoodManager.getAllFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideProgress();
                if (foods != null && foods.size() > 0) {
                    view.hideNotFound();
                    setDistance(foods);
                    Collections.shuffle(foods);
                    view.fetchFoods(foods);
                    recommendFoods = foods;

                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void filterFoods(String keyWord) {
        view.showProgress();
//        view.hideNotFound();
        FoodManager.filterFoods(keyWord, new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideProgress();
                if (foods != null && foods.size() > 0) {
                    view.hideNotFound();
                    setDistance(foods);
                    view.fetchFoods(foods);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void getFollow() {
        view.showProgressFollow();
        FollowManager.getUserFollow(new FollowManager.QueryListener() {
            @Override
            public void onComplete(List<Follow> follows) {
                view.hideProgressFollow();
                if (follows.size() == 0) {
                    view.showNotFoundFollow();
                } else {
                    view.hideNotFoundFollow();
                    followFoods = new ArrayList<>();
                    for (Follow follow : follows) {
                        FoodManager.getUserFoods(follow.uid, new FoodManager.QueryListener() {
                            @Override
                            public void onComplete(List<Food> foods) {
                                view.fetchFollow(foods);
                                followFoods.addAll(foods);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void getNearBy() {
        view.showProgressNearby();
        FoodManager.getAllFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideProgressNearby();
                foods = setDistanceLimit(foods);
                if (foods != null && foods.size() > 0) {
                    view.hideNotFoundNearby();
                    view.fetchNearby(foods);
                    nearbyFoods = foods;
                } else {
                    view.showNotFoundNearby();
                }
            }
        });
    }

    @Override
    public void getFoodType() {

    }

    @Override
    public List<Food> getRecommendFoods() {
        return recommendFoods;
    }

    @Override
    public List<Food> getFollowFoods() {
        return followFoods;
    }

    @Override
    public List<Food> getNearbyFoods() {
        return nearbyFoods;
    }

    private void setDistance(List<Food> foods) {
        for (Food food : foods) {
            if (food.latitude != null && food.longitude != null) {
                Location foodLocation = new Location("");
                foodLocation.setLatitude(food.latitude);
                foodLocation.setLongitude(food.longitude);
                Location currentLocation = AppInstance.getInstance().getCurrentLocation();
                if (currentLocation == null) return;
                food.distance = currentLocation.distanceTo(foodLocation);
            }
        }
        //sorting
        SortUtils.sortDistance(foods);
    }


    private List<Food> setDistanceLimit(List<Food> foods) {
        List<Food> newFoods = new ArrayList<>();
        for (Food food : foods) {
            if (food.latitude != null && food.longitude != null) {
                Location foodLocation = new Location("");
                foodLocation.setLatitude(food.latitude);
                foodLocation.setLongitude(food.longitude);
                Location currentLocation = AppInstance.getInstance().getCurrentLocation();
                if (currentLocation != null) {
                    food.distance = currentLocation.distanceTo(foodLocation);
                    if (food.distance <= 3000) {
                        newFoods.add(food);
                    }
                }
            }
        }
        //sorting
        SortUtils.sortDistance(newFoods);
        return newFoods;
    }
}
