package com.warunya.ricenearby.ui.register;

import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.model.RegisterEntity;

public interface RegisterContract {

    interface Presenter {
        void register(RegisterEntity registerEntity);
    }

    interface View extends BaseView {
        void requireField(RequireField requireField);

        void showUsernameInvalid();

        void showEmailInvalid();

        void showPasswordInvalid();

        void showConfirmPasswordNotMatch();

        void registerSuccess();

        void registerFailed();
    }
}
