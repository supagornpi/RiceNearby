package com.warunya.ricenearby.ui.home;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface HomeContract {

    interface Presenter extends BasePresenter {
        void filterFoods(String keyWord);

        void getFollow();

        void getNearBy();

        void getFoodType();

        List<Food> getRecommendFoods();

        List<Food> getFollowFoods();

        List<Food> getNearbyFoods();

    }

    interface View extends BaseView {
        void fetchFoods(List<Food> foods);

        void fetchFollow(List<Food> foods);

        void fetchNearby(List<Food> foods);

        void showProgressRecommend();

        void hideProgressRecommend();

        void showProgressFollow();

        void hideProgressFollow();

        void notFoundFollow();

        void showProgressNearby();

        void hideProgressNearby();


    }
}
