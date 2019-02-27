package com.warunya.ricenearby.ui.confirmorder;

import android.net.Uri;

import com.google.firebase.database.ServerValue;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.model.Order;

public class ConfirmOrderPresenter implements ConfirmOrderContract.Presenter {

    private ConfirmOrderContract.View view;
    private Order mOrder;

    ConfirmOrderPresenter(ConfirmOrderContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void findOrder(String key) {
        view.showProgress();
        OrderManager.getOrderByKey(key, new OrderManager.QueryListener() {
            @Override
            public void onComplete(Order order) {
                view.hideProgress();
                if (order == null) return;
                if (order.carts != null) {
                    view.hideNotFound();
                    mOrder = order;
                    view.fetchCart(order.carts);
                    view.fetchAddress(order.address, order.additionalAddress);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void confirmPayment(Uri uri) {
        view.showProgressDialog();
        OrderManager.uploadBillingImage(uri, String.valueOf(ServerValue.TIMESTAMP), mOrder.key,
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

    @Override
    public Order getOrder() {
        return mOrder;
    }

    @Override
    public void setOrder(Order order) {
        this.mOrder = order;
    }
}
