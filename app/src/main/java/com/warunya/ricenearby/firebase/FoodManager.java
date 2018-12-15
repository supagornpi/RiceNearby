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
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodManager {

    private static final String STORAGE_PATH_PROFILE = "foods/";

    private static final FoodManager ourInstance = new FoodManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;

    static FoodManager getInstance() {
        return ourInstance;
    }

    private FoodManager() {

    }

    public static String getUid() {
        return (FirebaseAuth.getInstance().getCurrentUser() != null) ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static DatabaseReference getDatabaseReference() {
        return getInstance().mDatabase.child("foods").child(FoodManager.getUid());
    }

    public static void writeNewFood(Food food) {
        // Create new submitPost at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        String key = getInstance().mDatabase.child("foods").push().getKey();
        Map postValues = food.toMap();

        Map childUpdates = new HashMap<String, Object>();
        childUpdates.put("/foods/" + key, postValues);
        childUpdates.put("/user-foods/" + getUid() + "/" + key, postValues);

        getInstance().mDatabase.updateChildren(childUpdates);
    }

    public static void getUserFoods(final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Food>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Food>>() {
                };
                Map<String, Food> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                List<Food> foods = new ArrayList<Food>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-foods").child(UserManager.getUid()).addValueEventListener(getInstance().userProfileEventListener);

    }

    public static void getUserProfile(final OnValueEventListener onValueEventListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                onValueEventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("users").child(FoodManager.getUid()).addValueEventListener(getInstance().userProfileEventListener);
    }

    public static void editUserProfile(final User user, final Handler handler) {
        getDatabaseReference().runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User value = mutableData.getValue(User.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
//                        ?: return Transaction.success(mutableData);
                value.name = user.name;
                value.mobile = user.mobile;
                value.birthday = user.birthday;
                value.gender = user.gender;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (handler == null) return;
                handler.onComplete();
            }
        });
    }

    public static void updateUserData(String uid, User user) {
        getInstance().mDatabase.child("users").child(uid).setValue(user);
    }

    public static void updateUserData(String uid, RegisterEntity entity) {
        String email = entity.getEmail();
        String username = entity.getUsername();

        User user = new User(username, email);
        updateUserData(uid, user);
    }

    public static void updateUserImage(Upload upload) {
        getInstance().mDatabase.child("user-images").child(getUid()).setValue(upload);
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public interface OnValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
    }

    public interface QueryListener {
        void onComplete(List<Food> foods);
    }

    public interface Handler {
        void onComplete();
    }
}
