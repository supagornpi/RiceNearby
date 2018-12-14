package com.warunya.ricenearby.ui.menu;

public class MenuPresenter implements MenuContract.Presenter {

    private MenuContract.View view;

    MenuPresenter(MenuContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
