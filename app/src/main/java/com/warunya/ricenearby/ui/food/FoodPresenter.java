package com.warunya.ricenearby.ui.food;

public class FoodPresenter implements FoodContract.Presenter {

    private FoodContract.View view;

    FoodPresenter(FoodContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
