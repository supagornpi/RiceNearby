package com.warunya.ricenearby.ui.profile;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;

    ProfilePresenter(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
