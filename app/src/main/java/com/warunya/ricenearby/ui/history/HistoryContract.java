package com.warunya.ricenearby.ui.history;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.model.Order;

import java.util.List;

public interface HistoryContract {

    interface Presenter extends BasePresenter {
        void filterOrder(Filter filter);

        boolean isMyOrder();
    }

    interface View extends BaseView {
        void fetchOrder(List<Order> orders);

    }
}
