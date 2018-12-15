package com.warunya.ricenearby.ui.menu;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface MenuContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView {
        void bindItem(List<Food> Foods);
    }
}
