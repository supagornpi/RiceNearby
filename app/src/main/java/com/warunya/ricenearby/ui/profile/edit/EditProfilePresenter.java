package com.warunya.ricenearby.ui.profile.edit;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;

public class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileContract.View view;

    EditProfilePresenter(EditProfileContract.View view) {
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

    @Override
    public void editUserProfile(User user) {
        view.showProgress();
        UserManager.editUserProfile(user, new UserManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgress();
            }
        });
    }
}
