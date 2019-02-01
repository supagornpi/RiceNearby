package com.warunya.ricenearby.ui.seller;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;

public class SellerPresenter implements SellerContract.Presenter {

    private SellerContract.View view;
    private boolean hasAddress;
    private User user;

    SellerPresenter(SellerContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
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
