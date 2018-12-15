package com.warunya.ricenearby.ui.menu;

import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public class MenuPresenter implements MenuContract.Presenter {

    private MenuContract.View view;

    MenuPresenter(MenuContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        view.hideNotFound();
        FoodManager.getUserFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideNotFound();
                if (foods != null && foods.size() > 0) {
                    view.bindItem(foods);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void stop() {

    }
}
