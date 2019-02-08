package com.warunya.ricenearby.ui.favorite;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Follow;

import java.util.List;

public interface FavoriteContract {

    interface Presenter extends BasePresenter {

        void follow(String uid);

        void unFollow(String uid);
    }

    interface View extends BaseView {

        void fetchFollower(List<Follow> follows);

    }
}
