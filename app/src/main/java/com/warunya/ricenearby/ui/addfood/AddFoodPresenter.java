package com.warunya.ricenearby.ui.addfood;

import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

public class AddFoodPresenter implements AddFoodContract.Presenter {

    private AddFoodContract.View view;

    AddFoodPresenter(AddFoodContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void addNewFood(Food food) {
        FoodManager.writeNewFood(food);
    }
}
