package com.warunya.ricenearby.ui.menu;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class MenuPresenter implements MenuContract.Presenter {

    private MenuContract.View view;
    private boolean hasAddress;
    private User user;

    MenuPresenter(MenuContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        view.hideNotFound();
        FoodManager.getUserFoods(new FoodManager.QueryListener() {
            @Override
            public void onComplete(List<Food> foods) {
                view.hideNotFound();
                if (foods != null && foods.size() > 0) {
                    view.bindItem(foods);
                } else {
                    view.showNotFound();
                }
            }
        });

        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user == null) return;
//                view.bindUserData(user);
//                checkSellerStatus();

                if (user.addresses != null && user.addresses.size() > 0) {
                    Location location = new Location("");
                    location.setLatitude(user.addresses.get(0).latitude);
                    location.setLongitude(user.addresses.get(0).longitude);
                    AppInstance.getInstance().setMyLocation(location);
                    hasAddress = true;
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean hasAddress() {
        return this.hasAddress;
    }
}
