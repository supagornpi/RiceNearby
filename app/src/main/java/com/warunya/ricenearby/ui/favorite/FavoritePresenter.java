package com.warunya.ricenearby.ui.favorite;

import com.warunya.ricenearby.firebase.FollowManager;
import com.warunya.ricenearby.model.Follow;

import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View view;

    FavoritePresenter(FavoriteContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        FollowManager.getUserFollow(new FollowManager.QueryListener() {
            @Override
            public void onComplete(List<Follow> follows) {
                view.hideProgress();
                if (follows.size() > 0) {
                    view.hideNotFound();
                    view.fetchFollower(follows);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void follow(String uidSeller) {
        FollowManager.follow(uidSeller, new FollowManager.Handler() {
            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unFollow(String uidSeller) {
        FollowManager.unFollow(uidSeller, new FollowManager.Handler() {
            @Override
            public void onComplete() {

            }
        });
    }
}
