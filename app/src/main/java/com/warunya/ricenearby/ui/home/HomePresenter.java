package com.warunya.ricenearby.ui.home;

import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

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
                    view.fetchFoods(foods);
                } else {
                    view.showNotFound();
                }
            }
        });
    }
}
