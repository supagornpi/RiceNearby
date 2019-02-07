package com.warunya.ricenearby.ui.food;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.FollowManager;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class FoodPresenter implements FoodContract.Presenter {

    private FoodContract.View view;
    private String uidSeller;

    FoodPresenter(FoodContract.View view, String uidSeller) {
        this.view = view;
        this.uidSeller = uidSeller;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void addToCart(Food food, int amount, Meal meal) {
        Cart cart = new Cart(UserManager.getUid(), food, amount);
        CartManager.addToCart(cart, meal, amount, null);
    }

    @Override
    public void buyNow(Food food, int amount, Meal meal) {
        view.showProgress();
        Cart cart = new Cart(UserManager.getUid(), food, amount);
        CartManager.addToCart(cart, meal, amount, new CartManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgress();
                view.addCartSuccess();
            }
        });
    }

    @Override
    public void getSellerInfo() {
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
