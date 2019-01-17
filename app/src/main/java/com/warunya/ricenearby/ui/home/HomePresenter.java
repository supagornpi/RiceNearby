package com.warunya.ricenearby.ui.home;

import android.location.Location;

import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

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
                    view.fetchFoods(foods);


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

        Collections.sort(foods, new Comparator<Food>() {
            @Override
            public int compare(Food food1, Food food2) {
                // ## Ascending order
//                return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                if (food1.distance != null && food2.distance != null) {
                    return Integer.valueOf(food1.distance.intValue()).compareTo(food2.distance.intValue()); // To compare integer values
                } else {
                    return -1;
                }
            }
        });
    }
}
