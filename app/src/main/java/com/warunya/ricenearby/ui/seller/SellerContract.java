package com.warunya.ricenearby.ui.seller;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;

public interface SellerContract {

    interface Presenter extends BasePresenter {
        boolean hasAddress();
    }

    interface View extends BaseView {

    }
}
