package com.warunya.ricenearby.ui.seller.profile;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;

import java.util.List;

public interface SellerProfileContract {

    interface Presenter extends BasePresenter {

        void findRelateFood();

        void checkFollow();

        void follow();

        void unFollow();
    }

    interface View extends BaseView {

        void fetchSellerInfo(User user);

        void fetchRelateFood(List<Food> foods);

        void updateFallow(boolean hasFollow);

        void showProgressFollow();

        void hideProgressFollow();
    }
}
