package com.warunya.ricenearby.ui.cart;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.constant.OrderStatus;
import com.warunya.ricenearby.firebase.CartManager;
import com.warunya.ricenearby.firebase.OrderManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.ConvertDateUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

        getAddress();
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
    public void confirmOrder(List<Cart> carts, int totalPrice, Address address, String additionalAddress) {
        List<Order> newOrders = new ArrayList<>();

        for (Cart cart : carts) {
            String orderNo = ConvertDateUtils.getDate() + gen();
            String uid = UserManager.getUid();
            Log.d("Order no", orderNo + " ,  uid: " + uid);
            //create order
            List<Cart> cartsOrder = new ArrayList<>();
            cartsOrder.add(cart);
            Order order = new Order(orderNo, uid, OrderStatus.NotPaid, cartsOrder, totalPrice, AppInstance.DELIVERY_PRICE);
            //set address to order
            order.address = address;
            order.additionalAddress = additionalAddress;

            if (newOrders.size() > 1) {
                Iterator<Order> iter = newOrders.iterator();
                boolean isSameSeller = false;
                int index = 0;
                int sameOrderAt = 0;

                //เช็คว่าเคยสร้าง order ของผู้ขายคนนี้แล้วหรือยัง
                while (iter.hasNext()) {
                    Iterator<Cart> iterCart = iter.next().carts.iterator();
                    while (iterCart.hasNext()) {
                        Food food = iterCart.next().food;
                        if (food.uid.equals(cart.food.uid)) {
                            isSameSeller = true;
                            sameOrderAt = index;
                        }
                    }
                    index++;
                }
                if (isSameSeller) {
                    //เคยสร้าง order แล้วให้ add อาหารเข้าไปเพิ่ม
                    newOrders.get(sameOrderAt).carts.add(cart);
                } else {
                    //order ใหม่
                    newOrders.add(order);
                }
            } else {
                //order ใหม่
                newOrders.add(order);
            }
        }

        for (Order orderToConfirm : newOrders) {
            if (orderToConfirm.carts != null) {
                for (Cart cartToConfirm : orderToConfirm.carts) {
                    //เปลี่ยนสถานะเป็น ยืนยันออเดอร์แล้ว
                    CartManager.confirmedOrder(cartToConfirm.key);
                }
            }
            orderToConfirm.totalPrice = getTotalPrice(orderToConfirm.carts);
            //สร้าง order ที่ firebase
            OrderManager.createOrder(orderToConfirm, new OrderManager.OnCreateOrderListener() {
                @Override
                public void onSuccess(String key) {
                    view.goToConfirmOrderActivity(key);
                }
            });
        }
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

    public int gen() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    private int getFoodPrice(List<Cart> carts) {
        int price = 0;
        for (Cart cart : carts) {
            for (Meal meal : cart.meals) {
                price += cart.food.price * meal.amount;
            }
        }
        return price;
    }

    private int getTotalPrice(List<Cart> carts) {
        return getFoodPrice(carts) + AppInstance.DELIVERY_PRICE;
    }
}
