package com.warunya.ricenearby.ui.cart;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;

import java.util.List;

public interface CartContract {

    interface Presenter extends BasePresenter {
        void removeCart(String key);

        void editAmount(String key, int amount);

        void confirmOrder(List<Cart> carts, int totalPrice, Address address, String additionalAddress);
    }

    interface View extends BaseView {
        void fetchCart(List<Cart> carts);

        void fetchAddress(List<Address> addresses);

        void goToConfirmOrderActivity(String key);
    }
}
