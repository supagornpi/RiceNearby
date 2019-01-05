package com.warunya.ricenearby.ui.food;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

public interface FoodContract {

    interface Presenter extends BasePresenter {
        void addToCart(Food food, int amount);
    }

    interface View extends BaseView {
        void addCartSuccess();
    }
}
