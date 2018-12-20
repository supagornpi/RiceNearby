package com.warunya.ricenearby.ui.login;

import com.facebook.AccessToken;
import com.warunya.ricenearby.base.BaseView;

public interface LoginContract {

    interface Presenter {
        void login(String email, String password);

        void handleFacebookAccessToken(AccessToken token);
    }

    interface View extends BaseView{
        void showEmailInvalid();

        void showPasswordInvalid();

        void loginSuccess();

        void loginFailed();
    }
}
