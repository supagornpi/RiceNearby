package com.warunya.ricenearby.ui.confirmorder;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class ConfirmOrderPresenter implements ConfirmOrderContract.Presenter {

    private ConfirmOrderContract.View view;

    ConfirmOrderPresenter(ConfirmOrderContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        CartManager.getUserCarts(new CartManager.QueryListener() {
            @Override
            public void onComplete(List<Cart> carts) {
                view.hideProgress();
                if (carts != null) {
                    view.hideNotFound();
                    view.fetchCart(carts);
                } else {
                    view.showNotFound();
                }
            }
        });

        getAddress();
    }

    @Override
    public void stop() {

    }

    private void getAddress(){
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
}
