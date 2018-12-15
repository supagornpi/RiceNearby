package com.warunya.ricenearby.ui.addfood;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

public interface AddFoodContract {

    interface Presenter extends BasePresenter {
        void addNewFood(Food food);
    }

    interface View extends BaseView.Progress {
        void addSuccess();
    }
}
