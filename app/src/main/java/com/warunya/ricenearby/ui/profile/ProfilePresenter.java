package com.warunya.ricenearby.ui.profile;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;

    ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.bindUserData(user);
            }
        });
    }

    @Override
    public void stop() {

    }
}
