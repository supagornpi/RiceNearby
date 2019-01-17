package com.warunya.ricenearby.ui.profile;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.constant.UserType;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private User user;

    ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                view.bindUserData(user);
                checkSellerStatus();

                if (user.addresses != null && user.addresses.size() > 0) {
                    Location location = new Location("");
                    location.setLatitude(user.addresses.get(0).latitude);
                    location.setLongitude(user.addresses.get(0).longitude);
                    AppInstance.getInstance().setMyLocation(location);
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void registerSeller() {
        if (user == null) return;
        if (user.userType != UserType.Seller) {
            view.openRegisterSellerActivity();
        }
    }

    private void checkSellerStatus() {
        if (user.userType == UserType.Seller) {
            view.disableRegisterSellerButton();
        } else {
            view.enableRegisterSellerButton();
        }
    }
}
