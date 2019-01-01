package com.warunya.ricenearby.ui.home;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface HomeContract {

    interface Presenter extends BasePresenter {
        void filterFoods(String keyWord);

    }

    interface View extends BaseView {
        void fetchFoods(List<Food> Foods);

    }
}
