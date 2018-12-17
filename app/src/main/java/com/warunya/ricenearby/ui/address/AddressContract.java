package com.warunya.ricenearby.ui.address;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Address;

import java.util.List;

public interface AddressContract {

    interface Presenter extends BasePresenter {
        void submit(List<Address> addresses);
    }

    interface View extends BaseView.Progress {
        void fetchAddress(List<Address> addresses);
    }
}
