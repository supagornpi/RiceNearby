package com.warunya.ricenearby.ui.history;

import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.model.Order;

import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    private HistoryContract.View view;

    HistoryPresenter(HistoryContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        view.showProgress();
        OrderManager.getUserOrders(new OrderManager.QueryListListener() {
            @Override
            public void onComplete(List<Order> orders) {
                view.hideProgress();
                if (orders.size() > 0) {
                    view.hideNotFound();
                    view.fetchOrder(orders);
                } else {
                    view.showNotFound();
                }
            }
        });

    }

    @Override
    public void stop() {

    }
}
