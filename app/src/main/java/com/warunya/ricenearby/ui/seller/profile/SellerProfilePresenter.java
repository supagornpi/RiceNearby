package com.warunya.ricenearby.ui.seller.profile;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.FollowManager;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class SellerProfilePresenter implements SellerProfileContract.Presenter {

    private SellerProfileContract.View view;
    private String  uidSeller;

    SellerProfilePresenter(SellerProfileContract.View view, String uidSeller) {
        this.view = view;
        this.uidSeller = uidSeller;
    }

    @Override
    public void start() {
        UserManager.getUserProfile(uidSeller, new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    view.fetchSellerInfo(user);
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void findRelateFood() {
        FoodManager.getUserFoods(uidSeller, new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.fetchRelateFood(foods);
            }
        });
    }

    @Override
    public void checkFollow() {
        view.showProgressFollow();
        FollowManager.hasFollow(uidSeller, new FollowManager.HandlerFollow() {
            @Override
            public void onComplete(boolean hasFollow) {
                view.updateFallow(hasFollow);
                view.hideProgressFollow();
            }
        });
    }

    @Override
    public void follow() {
        view.showProgressFollow();
        FollowManager.follow(uidSeller, new FollowManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgressFollow();
            }
        });
    }

    @Override
    public void unFollow() {
        view.showProgressFollow();
        FollowManager.unFollow(uidSeller, new FollowManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgressFollow();
            }
        });
    }
}
