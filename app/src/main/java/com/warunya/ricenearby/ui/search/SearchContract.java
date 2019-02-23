package com.warunya.ricenearby.ui.search;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface SearchContract {

    interface Presenter extends BasePresenter {
        void filterFoods(String keyWord);

        void getCurrentAddress();

    }

    interface View extends BaseView {
        void fetchFoods(List<Food> foods);

        void fetchAddress(Address address);

    }
}
