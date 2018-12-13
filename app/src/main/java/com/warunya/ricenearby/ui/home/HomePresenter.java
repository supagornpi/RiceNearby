package com.warunya.ricenearby.ui.home;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

    HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
