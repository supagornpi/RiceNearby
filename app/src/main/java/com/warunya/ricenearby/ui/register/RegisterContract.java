package com.warunya.ricenearby.ui.register;

import android.net.Uri;

import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.model.RegisterEntity;

public interface RegisterContract {

    interface Presenter {
        void register(RegisterEntity registerEntity);

        void registerSeller(RegisterEntity registerEntity);

        void setCopyIdCardUri(Uri uri);

        void setCopyBookBankUri(Uri uri);

    }

    interface View extends BaseView.Progress {
        void requireField(RequireField requireField);

        void showUsernameInvalid();

        void showEmailInvalid();

        void showPasswordInvalid();

        void showConfirmPasswordNotMatch();

        void registerSuccess();

        void registerFailed();
    }
}
