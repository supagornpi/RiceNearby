package com.warunya.ricenearby.ui.login;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String email, String password) {

    }
}
