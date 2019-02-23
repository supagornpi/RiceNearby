package com.warunya.ricenearby.ui.home;

import android.location.Location;

import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.utils.SortUtils;

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
        //sorting
        SortUtils.sortDistance(foods);
    }
}
