package com.warunya.ricenearby.ui.profile.edit;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.User;

public interface EditProfileContract {

    interface Presenter extends BasePresenter {
        void editUserProfile(User user);

        void getAddress();
    }

    interface View extends BaseView.Progress {
        void bindUserData(User user);

        void bindAddress(User user);

    }
}
