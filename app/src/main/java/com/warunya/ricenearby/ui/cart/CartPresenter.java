package com.warunya.ricenearby.ui.cart;

import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.constant.OrderStatus;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;

import java.util.List;

public class CartPresenter implements CartContract.Presenter {

    private CartContract.View view;

    CartPresenter(CartContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        CartManager.getUserCarts(new CartManager.QueryListener() {
            @Override
            public void onComplete(List<Cart> carts) {
                view.hideProgress();
                if (carts != null && carts.size() > 0) {
                    view.hideNotFound();
                    view.fetchCart(carts);
                } else {
                    view.showNotFound();
                }
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void removeCart(String key) {
        CartManager.removeCart(key);
    }

    @Override
    public void editAmount(String key, int amount) {
        CartManager.editAmount(CartManager.getCartReference(key), amount);
        CartManager.editAmount(CartManager.getUserCartReference(key), amount);
    }

    @Override
    public void confirmOrder(List<Cart> carts, int totalPrice) {
        String uid = UserManager.getUid();
        Order order = new Order(uid, OrderStatus.NotPaid, carts, totalPrice, AppInstance.DELIVERY_PRICE);
        OrderManager.createOrder(order, new OrderManager.OnCreateOrderListener() {
            @Override
            public void onSuccess(String key) {
                view.goToConfirmOrderActivity(key);
            }
        });

        for (Cart cart : carts) {
            CartManager.confirmedOrder(cart.key);
        }
    }
}
