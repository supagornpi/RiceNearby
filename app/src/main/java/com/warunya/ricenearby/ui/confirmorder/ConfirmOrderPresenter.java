package com.warunya.ricenearby.ui.confirmorder;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.User;

public class ConfirmOrderPresenter implements ConfirmOrderContract.Presenter {

    private ConfirmOrderContract.View view;
    private Order order;

    ConfirmOrderPresenter(ConfirmOrderContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        getAddress();
    }

    @Override
    public void stop() {

    }

    private void getAddress() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                if (user.addresses == null) return;
                view.fetchAddress(user.addresses);

            }
        });
    }

    @Override
    public void findOrder(String key) {
        view.showProgress();
        OrderManager.getUserOrderByKey(key, new OrderManager.QueryListener() {
            @Override
            public void onComplete(Order order) {
                view.hideProgress();
                if (order == null) return;
                if (order.carts != null) {
                    view.hideNotFound();
                    ConfirmOrderPresenter.this.order = order;
                    view.fetchCart(order.carts);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void confirmPayment(Uri uri) {
        view.showProgressDialog();
        OrderManager.uploadBillingImage(uri, String.valueOf(ServerValue.TIMESTAMP), order.key,
                new OrderManager.Handler() {
                    @Override
                    public void onComplete() {
                        view.hideProgressDialog();
                        view.paymentSuccess();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
    }
}
