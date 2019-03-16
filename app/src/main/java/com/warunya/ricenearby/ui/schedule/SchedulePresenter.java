package com.warunya.ricenearby.ui.schedule;

import android.util.Log;

import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.MealByDate;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.OrderByDate;
import com.warunya.ricenearby.utils.ConvertDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchedulePresenter implements ScheduleContract.Presenter {

    private ScheduleContract.View view;
    private List<Order> mOrders;

    SchedulePresenter(ScheduleContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void getOrder(final Filter filter) {
        view.showProgress();

        OrderManager.getUserOrdersOnlyPaid(new OrderManager.QueryListListener() {
            @Override
            public void onComplete(List<Order> orders) {
                view.hideProgress();
                if (orders.size() > 0) {
//                    orders = filterPeriod(orders, filter);
//                    orders = isMyOrder ? filterWithCurrentUser(orders) : orders;
                    mOrders = orders;
                    Calendar current = Calendar.getInstance();
                    current.setTimeInMillis(System.currentTimeMillis());
                    filterByDate(current);
                } else {
                    view.showNotFound();
                }
            }
        });

    }

    @Override
    public void filterByDate(Calendar calendar) {
        if (mOrders == null || mOrders.size() == 0) {
            //ไม่มีออเดอร์
            view.showNotFound();
            return;
        }
        List<MealByDate> mealByDates = filterDate(calendar);
        if (mealByDates.size() == 0) {
            view.showNotFound();
        } else {
            view.hideNotFound();
        }
        view.fetchSummaryOrder(mealByDates);
    }

    private List<MealByDate> filterDate(Calendar selectedDate) {
        List<OrderByDate> orderByDates = summaryByDate(mOrders);
        List<MealByDate> mealByDates = new ArrayList<>();
        for (OrderByDate orderByDate : orderByDates) {
            String selectedDateStr = ConvertDateUtils.convertToString(selectedDate.getTime());
            Calendar cMealDate = ConvertDateUtils.convertToCalendar(orderByDate.date);
            if (orderByDate.date.equals(selectedDateStr)) {
                mealByDates = orderByDate.mealByDateList;
            }
        }
        return mealByDates;
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
