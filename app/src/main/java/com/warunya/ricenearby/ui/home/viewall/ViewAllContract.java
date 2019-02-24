package com.warunya.ricenearby.ui.home.viewall;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface ViewAllContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView {
        void fetchFoods(List<Food> foods);

    }
}
