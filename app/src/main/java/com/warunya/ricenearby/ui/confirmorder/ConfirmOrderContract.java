package com.warunya.ricenearby.ui.confirmorder;

import android.net.Uri;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;

import java.util.List;

public interface ConfirmOrderContract {

    interface Presenter extends BasePresenter {
        void findOrder(String key);

        void confirmPayment(Uri uri);

        Order getOrder();

        void setOrder(Order order);

    }

    interface View extends BaseView {
        void fetchCart(List<Cart> carts);

        void fetchAddress(Address address, String additionalAddress);

        void showProgressDialog();

        void hideProgressDialog();

        void paymentSuccess();

    }
}
