package com.warunya.ricenearby.ui.login;

public interface LoginContract {

    interface Presenter {
        void login(String email, String password);
    }

    interface View {
        void showEmailInvalid();

        void showPasswordInvalid();

        void loginSuccess();

        void loginFailed();
    }
}
