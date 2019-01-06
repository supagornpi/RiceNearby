package com.warunya.ricenearby.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Upload;

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
                if (objectHashMap == null) return;
                List<Cart> carts = new ArrayList<Cart>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(carts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-carts").child(UserManager.getUid()).addValueEventListener(getInstance().userProfileEventListener);

    }

    public static void removeCart(String key) {
        getUserCartReference(key).removeValue();
        getCartReference(key).removeValue();
    }


    public static void editAmount(DatabaseReference reference, final int  amount) {
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

    public static void editFood(DatabaseReference reference, final Food food) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.foodName = food.foodName;
                value.amount = food.amount;
                value.price = food.price;
                value.detail = food.detail;
                value.foodTypes = food.foodTypes;

                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

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
