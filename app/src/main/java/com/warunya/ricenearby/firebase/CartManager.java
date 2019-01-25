package com.warunya.ricenearby.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {

    public static final String STORAGE_PATH_FOOD = "orders/";

    private static final CartManager ourInstance = new CartManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    static CartManager getInstance() {
        return ourInstance;
    }

    private CartManager() {

    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static DatabaseReference getCartReference(String key) {
        return getInstance().mDatabase.child("carts").child(key);
    }

    public static DatabaseReference getUserCartReference(String key) {
        return getInstance().mDatabase.child("user-carts").child(UserManager.getUid()).child(key);
    }

    public static void writeNewCart(final Cart cart) {
        // Create new submitPost at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        final String key = getInstance().mDatabase.child("carts").push().getKey();
        cart.key = key;
        Map postValues = cart.toMap();

        Map childUpdates = new HashMap<String, Object>();
        childUpdates.put("/carts/" + key, postValues);
        childUpdates.put("/user-carts/" + UserManager.getUid() + "/" + key, postValues);

        getInstance().mDatabase.updateChildren(childUpdates);
    }

    public static void getUserCarts(final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Cart>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Cart>>() {
                };
                Map<String, Cart> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListener == null) return;
                    queryListener.onComplete(new ArrayList<Cart>());
                    return;
                }
                List<Cart> carts = new ArrayList<Cart>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(carts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-carts").child(UserManager.getUid())
                .orderByChild("isConfirmOrder").equalTo(false)
                .addListenerForSingleValueEvent(getInstance().userProfileEventListener);
    }

    public static void removeCart(String key) {
        getUserCartReference(key).removeValue();
        getCartReference(key).removeValue();
    }

    public static void editAmount(DatabaseReference reference, final int amount) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Cart value = mutableData.getValue(Cart.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.amount = amount;

                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void addAmount(DatabaseReference reference, final int amount) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Cart value = mutableData.getValue(Cart.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.amount += amount;

                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void addMealAndAmount(DatabaseReference reference, final Meal meal, final int amount) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Cart value = mutableData.getValue(Cart.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }

                boolean isInCart = false;
                for (Meal mealInCart : value.meals) {
                    if (mealInCart.key.equals(meal.key)) {
                        isInCart = true;
                        mealInCart.amount += amount;
                        //maximum amount
                        if (mealInCart.amount > meal.amount) {
                            mealInCart.amount = meal.amount;
                        }
                    }
                }


                if (!isInCart) {
                    value.meals.add(meal);
                }

                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }


    public static void confirmedOrder(final String key) {
        confirmedOrder(getCartReference(key));
        confirmedOrder(getUserCartReference(key));
    }

    private static void confirmedOrder(DatabaseReference reference) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Cart value = mutableData.getValue(Cart.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.isConfirmOrder = true;

                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void addToCart(final Cart newCart, final Meal meal, final int amount) {
        getUserCarts(new QueryListener() {
            @Override
            public void onComplete(List<Cart> carts) {
                boolean isInCart = false;
                String cartKey = null;
                for (Cart cart : carts) {
                    if (cart.food.key.equals(newCart.food.key)) {
                        isInCart = true;
                        cartKey = cart.key;
                    }
                }

                if (!isInCart) {
                    if (newCart.meals == null) {
                        newCart.meals = new ArrayList<>();
                    }
                    meal.amount = amount;
                    newCart.meals.add(meal);
                    writeNewCart(newCart);
                } else {
                    if (cartKey == null) return;
                    addMealAndAmount(getCartReference(cartKey), meal, amount);
                    addMealAndAmount(getUserCartReference(cartKey), meal, amount);
//                    addAmount(getCartReference(cartKey), amount);
//                    addAmount(getUserCartReference(cartKey), amount);
                }
            }
        });
    }

    public interface OnValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
    }

    public interface QueryListener {
        void onComplete(List<Cart> carts);
    }

    public interface Handler {
        void onComplete();
    }
}
