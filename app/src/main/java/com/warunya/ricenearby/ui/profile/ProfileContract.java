package com.warunya.ricenearby.ui.profile;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.User;

public interface ProfileContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView {

        void bindUserData(User user);

    }
}
