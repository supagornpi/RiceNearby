package com.warunya.ricenearby.ui.settime;

import android.widget.CheckBox;

import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public class SetTimePresenter implements SetTimeContract.Presenter {

    private SetTimeContract.View view;

    SetTimePresenter(SetTimeContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public void submit(boolean isSelectedDate, String date, List<CheckBox> checkBoxListMeal, List<Food> foodList) {
        if (isValidate(isSelectedDate, checkBoxListMeal, foodList)) {
            String meal = "";
            for (CheckBox checkBox : checkBoxListMeal) {
                if (checkBox.isChecked()) {
                    meal = checkBox.getText().toString();
                }
            }

            for (Food food : foodList) {
                food.date = date;
                food.meal = meal;
                FoodManager.editFoodDate(FoodManager.getFoodsReference(food.key), food);
                FoodManager.editFoodDate(FoodManager.getUserFoodsReference(food.key), food);
            }
            view.submitSuccess();
        }
    }

    private boolean isValidate(boolean isSelectedDate, List<CheckBox> checkBoxListMeal, List<Food> foodList) {
        boolean isValid = false;
        boolean isSelectedMeal = false;
        for (CheckBox checkBox : checkBoxListMeal) {
            if (checkBox.isChecked()) {
                isSelectedMeal = true;
            }
        }
        if (!isSelectedDate) {
            view.pleaseSelectedDate();
        } else if (!isSelectedMeal) {
            view.pleaseSelectedMeal();
        } else if (foodList.size() == 0) {
            view.pleaseSelectedMenu();
        } else {
            isValid = true;
        }
        return isValid;
    }
}
