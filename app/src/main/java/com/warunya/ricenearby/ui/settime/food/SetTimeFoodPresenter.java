package com.warunya.ricenearby.ui.settime.food;

import android.widget.CheckBox;

import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class SetTimeFoodPresenter implements SetTimeFoodContract.Presenter {

    private SetTimeFoodContract.View view;
    private MealTime mealTime;

    SetTimeFoodPresenter(SetTimeFoodContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public void findFoodWithMealTime(final MealTime mealTime) {
        this.mealTime = mealTime;
        FoodManager.getUserFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                List<Food> foodsWithMeal = new ArrayList<>();
                if (foods.size() > 0) {
                    for (Food food : foods) {
                        if (mealTime == MealTime.Breakfast && food.breakfasts != null && food.breakfasts.size() > 0) {
                            foodsWithMeal.add(food);
                        } else if (mealTime == MealTime.Lunch && food.lunches != null && food.lunches.size() > 0) {
                            foodsWithMeal.add(food);
                        } else if (mealTime == MealTime.Dinner && food.dinners != null && food.dinners.size() > 0) {
                            foodsWithMeal.add(food);
                        }
                    }
                }
                view.fetchFoods(foodsWithMeal);
            }
        });
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

    @Override
    public void addMeal(String key, Meal meal, MealTime mealTime) {
        FoodManager.addMeal(FoodManager.getFoodsReference(key), meal, mealTime);
        FoodManager.addMeal(FoodManager.getUserFoodsReference(key), meal, mealTime);
    }

    @Override
    public void removeAllMeals(String foodKey) {
        FoodManager.removeAllMeal(FoodManager.getFoodsReference(foodKey), mealTime);
        FoodManager.removeAllMeal(FoodManager.getUserFoodsReference(foodKey), mealTime);
    }

    @Override
    public void removeMeal(String foodKey, String mealKey) {
        FoodManager.removeMeal(FoodManager.getFoodsReference(foodKey), mealKey, mealTime);
        FoodManager.removeMeal(FoodManager.getUserFoodsReference(foodKey), mealKey, mealTime);
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
