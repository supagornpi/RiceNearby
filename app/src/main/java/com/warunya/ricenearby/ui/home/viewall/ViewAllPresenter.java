package com.warunya.ricenearby.ui.home.viewall;

import com.warunya.ricenearby.constant.FoodType;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

import java.util.ArrayList;
import java.util.List;

public class ViewAllPresenter implements ViewAllContract.Presenter {

    private ViewAllContract.View view;
    private FoodType mFoodType;

    ViewAllPresenter(ViewAllContract.View view, FoodType foodType) {
        this.view = view;
        this.mFoodType = foodType;
    }

    @Override
    public void start() {
        view.showProgress();
        view.hideNotFound();
        FoodManager.getAllFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideProgress();
                foods = filterFoodType(foods);
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

    private List<Food> filterFoodType(List<Food> foods) {
        List<Food> newFoods = new ArrayList<>();
        for (Food food : foods) {
            for (com.warunya.ricenearby.model.FoodType foodType : food.foodTypes) {
                if (FoodType.parse(foodType.typeName) == mFoodType) {
                    newFoods.add(food);
                }
            }
        }
        return newFoods;
    }
}
