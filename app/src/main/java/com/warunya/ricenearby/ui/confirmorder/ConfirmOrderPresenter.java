package com.warunya.ricenearby.ui.confirmorder;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class ConfirmOrderPresenter implements ConfirmOrderContract.Presenter {

    private ConfirmOrderContract.View view;

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
        OrderManager.getUserOrder(key, new OrderManager.QueryListener() {
            @Override
            public void onComplete(Order order) {
                view.hideProgress();
                if (order == null) return;
                if (order.carts != null) {
                    view.hideNotFound();
                    view.fetchCart(order.carts);
                } else {
                    view.showNotFound();
                }
            }
        });
    }
}
