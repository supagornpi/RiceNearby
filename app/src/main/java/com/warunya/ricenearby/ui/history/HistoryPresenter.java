package com.warunya.ricenearby.ui.history;

import android.util.Log;

import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.MealByDate;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.OrderByDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HistoryPresenter implements HistoryContract.Presenter {

    private boolean normalMode = true;
    private boolean isMyOrder;
    private HistoryContract.View view;
    private List<Order> mOrders;

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
            //Order
            OrderManager.getAllOrders(new OrderManager.QueryListListener() {
                @Override
                public void onComplete(List<Order> orders) {
                    view.hideProgress();
                    if (orders.size() > 0) {
                        view.hideNotFound();
                        //filter with my user id
                        orders = filterWithCurrentUser(orders);
                        //filter period (Between selected filter period)
                        orders = filterPeriod(orders, filter);
                        mOrders = orders;
                        if (normalMode) {
                            //show group by order
                            view.fetchOrder(mOrders);
                        } else {
                            //show group by date
                            view.fetchSummaryOrder(summaryByDate(mOrders));
                        }
                        if (orders.size() == 0) {
                            view.showNotFound();
                        }
                    } else {
                        view.showNotFound();
                    }
                }
            });
        } else {
            //History
            OrderManager.getUserOrders(new OrderManager.QueryListListener() {
                @Override
                public void onComplete(List<Order> orders) {
                    view.hideProgress();
                    if (orders.size() > 0) {
                        //filter period (Between selected filter period)
                        orders = filterPeriod(orders, filter);
                        orders = isMyOrder ? filterWithCurrentUser(orders) : orders;
                        mOrders = orders;
                        view.hideNotFound();
                        //show order history
                        view.fetchOrder(mOrders);
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

    @Override
    public void switchMode() {
        if (normalMode) {
            normalMode = false;
            view.fetchSummaryOrder(summaryByDate(mOrders));
        } else {
            normalMode = true;
            view.fetchOrder(mOrders);
        }
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


    private List<OrderByDate> summaryByDate(List<Order> orders) {
        Iterator<Order> iter = orders.iterator();
        HashMap<String, Cart> cartByDateMap = new HashMap<>();
        HashMap<String, Meal> mealByDateMap = new HashMap<>();
        HashMap<MealTime, Food> foodByDateMap = new HashMap<>();

        while (iter.hasNext()) {
            Iterator<Cart> iterCart = iter.next().carts.iterator();
            while (iterCart.hasNext()) {
                Cart cart = iterCart.next();
//                List<Meal> iterMeals = cart.meals;
                for (Meal meal : cart.meals) {
                    cartByDateMap.put(meal.date, cart);
                    mealByDateMap.put(meal.mealTime.getMealTimeText(), meal);
                    foodByDateMap.put(meal.mealTime, cart.food);
                }
            }
        }

        List<OrderByDate> orderByDatesList = new ArrayList<>();


        for (Map.Entry<String, Cart> entry : cartByDateMap.entrySet()) {
            OrderByDate orderByDate = new OrderByDate(entry.getKey());
            orderByDate.date = entry.getKey();
            orderByDate.mealByDateList = new ArrayList<>();

            for (Map.Entry<String, Meal> entryMeal : mealByDateMap.entrySet()) {
                MealByDate mealByDate = null;

                //loop all orders
                for (Order order : orders) {
                    for (Cart cart : order.carts) {
                        for (Meal meal : cart.meals) {
                            if (entry.getKey().equals(meal.date) && meal.mealTime.getMealTimeText().equals(entryMeal.getKey())) {
                                if (mealByDate == null) {
                                    //create new meal time
                                    mealByDate = new MealByDate(meal.mealTime, meal);
                                }
                                boolean hasFood = false;
                                if (mealByDate.foodList == null) {
                                    mealByDate.foodList = new ArrayList<>();
                                } else {
                                    //looping food list check for duplicate item
                                    for (Food food : mealByDate.foodList) {
                                        if (food.key.equals(entry.getValue().food.key)) {
                                            hasFood = true;
                                        }
                                    }
                                }

                                if (hasFood) {
                                    //add amount
                                    mealByDate.meal.amount += meal.amount;
                                } else {
                                    //add new food
                                    mealByDate.foodList.add(entry.getValue().food);
                                }
                            }
                        }
                    }
                }
                if (mealByDate != null) {
                    orderByDate.mealByDateList.add(mealByDate);
                }

            }
            orderByDatesList.add(orderByDate);
        }

        return orderByDatesList;
    }
}
