package com.warunya.ricenearby.ui.history;

import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;

import java.util.Iterator;
import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    private boolean isMyOrder;
    private HistoryContract.View view;

    HistoryPresenter(HistoryContract.View view, boolean isMyOrder) {
        this.view = view;
        this.isMyOrder = isMyOrder;
    }

    @Override
    public void start() {
        view.showProgress();
        if (isMyOrder) {
            OrderManager.getAllOrders(new OrderManager.QueryListListener() {
                @Override
                public void onComplete(List<Order> orders) {
                    view.hideProgress();
                    if (orders.size() > 0) {
                        view.hideNotFound();
                        orders = filterWithCurrentUser(orders);
                        view.fetchOrder(orders);
                        if (orders.size() == 0) {
                            view.showNotFound();
                        }
                    } else {
                        view.showNotFound();
                    }
                }
            });
        } else {
            OrderManager.getUserOrders(new OrderManager.QueryListListener() {
                @Override
                public void onComplete(List<Order> orders) {
                    view.hideProgress();
                    if (orders.size() > 0) {
                        view.hideNotFound();
                        view.fetchOrder(isMyOrder ? filterWithCurrentUser(orders) : orders);
                    } else {
                        view.showNotFound();
                    }
                }
            });
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isMyOrder() {
        return this.isMyOrder;
    }

    private List<Order> filterWithCurrentUser(List<Order> orders) {
        String currentUid = UserManager.getUid();

        Iterator<Order> iter = orders.iterator();
        while (iter.hasNext()) {
            Iterator<Cart> iterCart = iter.next().carts.iterator();
            boolean isMyOrder = false;
            while (iterCart.hasNext()) {
                if (iterCart.next().food.uid.equals(currentUid)) {
                    isMyOrder = true;
                } else {
                    iterCart.remove();
                }

            }
            if (!isMyOrder) {
                iter.remove();
            }
        }
        return orders;
    }
}
