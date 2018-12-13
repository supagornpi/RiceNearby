package com.warunya.ricenearby.ui.register;

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;

    RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(String email, String password) {

    }
}
