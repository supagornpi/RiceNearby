package com.warunya.ricenearby.ui.settime.food;

import android.widget.CheckBox;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;

import java.util.List;

public interface SetTimeFoodContract {

    interface Presenter extends BasePresenter {
        void findFoodWithMealTime(MealTime mealTime);

        void submit(boolean isSelectedDate, String date, List<CheckBox> checkBoxListMeal, List<Food> foodList);

        void addMeal(String key, Meal meal, MealTime mealTime);

        void removeAllMeals(String foodKey);

        void removeMeal(String foodKey, String mealKey);
    }

    interface View extends BaseView.Progress {
        void fetchFoods(List<Food> foods);

        void pleaseSelectedDate();

        void pleaseSelectedMeal();

        void pleaseSelectedMenu();

        void submitSuccess();
    }
}
