package com.warunya.ricenearby.ui.cart;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Cart;

import java.util.List;

public interface CartContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView.Progress {
        void fetchCart(List<Cart> carts);
    }
}
