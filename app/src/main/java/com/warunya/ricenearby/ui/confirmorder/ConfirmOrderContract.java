package com.warunya.ricenearby.ui.confirmorder;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;

import java.util.List;

public interface ConfirmOrderContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView {
        void fetchCart(List<Cart> carts);

        void fetchAddress(List<Address> addresses);

    }
}
