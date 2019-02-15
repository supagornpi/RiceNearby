package com.warunya.ricenearby.ui.history;

import android.util.Log;

import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;

import java.util.Calendar;
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

    }

    @Override
    public void stop() {

    }

    @Override
    public void filterOrder(final Filter filter) {
        view.showProgress();
        if (isMyOrder) {
            OrderManager.getAllOrders(new OrderManager.QueryListListener() {
                @Override
                public void onComplete(List<Order> orders) {
                    view.hideProgress();
                    if (orders.size() > 0) {
                        view.hideNotFound();
                        orders = filterWithCurrentUser(orders);
                        orders = filterPeriod(orders, filter);
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
                        orders = filterPeriod(orders, filter);
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

    private List<Order> filterPeriod(List<Order> orders, Filter filter) {
        Iterator<Order> iter = orders.iterator();
        while (iter.hasNext()) {

            Calendar cOrder = Calendar.getInstance();
            Calendar cCurrent = Calendar.getInstance();
            Calendar cPrevious = Calendar.getInstance();

            cOrder.setTimeInMillis(iter.next().timestamp);
            cCurrent.setTimeInMillis(System.currentTimeMillis());
            if (filter != Filter.All) {
                cPrevious.setTimeInMillis(System.currentTimeMillis());
                int period = filter.getPeriod();
                cPrevious.add(Calendar.DATE, -period);
                if (cOrder.after(cCurrent) || cOrder.before(cPrevious)) {
                    iter.remove();
                }
                Log.d("Period", "is " + period + " : " + cPrevious.getTime());
            }
        }
        return orders;
    }
}
