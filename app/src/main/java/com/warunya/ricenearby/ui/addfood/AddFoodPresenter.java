package com.warunya.ricenearby.ui.addfood;

import com.warunya.ricenearby.constant.RequireField;
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
        if (validate(food)) {
            FoodManager.writeNewFood(food);
            view.addSuccess();
        }

    }

    @Override
    public void editFood(Food food) {
        if (validate(food)) {
            FoodManager.editFood(FoodManager.getUserFoodsReference(food.key), food);
            FoodManager.editFood(FoodManager.getFoodsReference(food.key), food);
            FoodManager.updateFoodImage(food, food.key);
            view.addSuccess();
        }
    }

    private Boolean validate(Food food) {
        boolean isValid = false;
        if (food.foodName.isEmpty()) {
            view.requireField(RequireField.FoodName);
        } else if (food.price == 0) {
            view.requireField(RequireField.Price);
        } else if (food.detail.isEmpty()) {
            view.requireField(RequireField.Detail);
        } else if (food.foodTypes.size() == 0) {
            view.requireField(RequireField.FoodType);
        } else {
            isValid = true;
        }
        return isValid;
    }

}
