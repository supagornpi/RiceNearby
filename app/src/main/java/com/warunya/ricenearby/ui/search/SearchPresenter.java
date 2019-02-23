package com.warunya.ricenearby.ui.search;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodType;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.SortUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private List<Food> allFoods = new ArrayList<>();
    private SearchContract.View view;

    SearchPresenter(SearchContract.View view) {
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
                    allFoods = foods;
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void filterFoods(String keyWord) {
        view.showProgress();
        if (keyWord.length() > 0) {
            List<Food> foods = new ArrayList<>();
            for (Food food : allFoods) {
                for (FoodType foodType : food.foodTypes) {
                    if (food.foodName.toLowerCase().contains(keyWord.toLowerCase())
                            || foodType.typeName.toLowerCase().contains(keyWord.toLowerCase())) {
                        if (!hasFood(foods, food.foodName))
                            foods.add(food);
                    }
                }
            }
            view.hideProgress();
            if (foods.size() == 0) {
                view.showNotFound();
            } else {
                view.hideNotFound();
                view.fetchFoods(foods);
            }
        } else {
            start();
        }
    }

    @Override
    public void getCurrentAddress() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                if (user.addresses == null) return;
                if (user.addresses.size() > 0) {
                    view.fetchAddress(user.addresses.get(0));
                }
            }
        });
    }

    @Override
    public void stop() {

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

    private boolean hasFood(List<Food> foods, String foodName) {
        boolean hasFood = false;
        for (Food food : foods) {
            if (food.foodName.equals(foodName)) {
                hasFood = true;
            }
        }
        return hasFood;
    }

}
