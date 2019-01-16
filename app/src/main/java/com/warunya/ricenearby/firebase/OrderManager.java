package com.warunya.ricenearby.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.Upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {

    public static final String STORAGE_PATH_FOOD = "orders/";

    private static final OrderManager ourInstance = new OrderManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    static OrderManager getInstance() {
        return ourInstance;
    }

    private OrderManager() {

    }

    public static boolean isLogin() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static DatabaseReference getOrdersReference(String key) {
        return getInstance().mDatabase.child("orders").child(key);
    }

    public static DatabaseReference getBuyerReference(String key) {
        return getInstance().mDatabase.child("user-orders").child(UserManager.getUid()).child(key);
    }

    public static void createOrder(final Order order, final OnCreateOrderListener onCreateOrderListener) {
        // Create new submitPost at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        final String key = getInstance().mDatabase.child("orders").push().getKey();

        order.key = key;
        Map postValues = order.toMap();

        Map childUpdates = new HashMap<String, Object>();
        childUpdates.put("/orders/" + key, postValues);
        childUpdates.put("/user-orders/" + UserManager.getUid() + "/" + key, postValues);

        getInstance().mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (onCreateOrderListener != null) {
                    onCreateOrderListener.onSuccess(key);
                }
            }
        });
    }

    private static void uploadFile(final Uri uri, final String username, final String key) {
//        view.showProgressDialog();
        //getting the storage reference
        StorageReference sRef = getInstance().storageReference.child(OrderManager.STORAGE_PATH_FOOD + username + System.currentTimeMillis());
        //adding the file to reference

        sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //dismissing the progress dialog
//                view.hideProgressDialog();
                //creating the upload object to store uploaded image details
                Upload upload = new Upload(username, uri.getLastPathSegment(), taskSnapshot.getDownloadUrl().toString());

                //adding an upload to firebase database
                OrderManager.uploadFoodImage(getOrdersReference(key), upload);
                OrderManager.uploadFoodImage(getBuyerReference(key), upload);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                view.hideProgressDialog();
//                view.error(e.getMessage());

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //displaying the upload progress
//                Double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
//                view.updateProgress("Uploading " + progress.intValue() + " %...");
            }
        });
    }

    public static void getUserOrder(String key, final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order orders = dataSnapshot.getValue(Order.class);
                if (queryListener == null) return;
                queryListener.onComplete(orders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-orders").child(UserManager.getUid()).child(key)
                .addListenerForSingleValueEvent(getInstance().userProfileEventListener);

    }

    public static void uploadFoodImage(DatabaseReference reference, final Upload upload) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                if (value.uploads == null) {
                    value.uploads = new ArrayList<>();
                    value.uploads.add(upload);
                } else {
                    value.uploads.add(upload);
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

    public static void editFoodDate(DatabaseReference reference, final Food food) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.date = food.date;
                value.meal = food.meal;

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

    public static void updateFoodImage(Food food, String key) {
        if (food.foodImages == null) return;
        if (food.foodImages.size() > 0) {
            for (FoodImage foodImage : food.foodImages) {
                if (foodImage.uri != null) {
                    //upload file to firebase
                    uploadFile(foodImage.uri, food.foodName, food.key);
                } else if (foodImage.isRemoved) {
                    removeFoodImage(getOrdersReference(food.key), foodImage.name);
                    removeFoodImage(getBuyerReference(food.key), foodImage.name);
                }
            }
        }
    }

    public static void removeFoodImage(DatabaseReference reference, final String foodName) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                if (value.uploads != null) {
                    for (int i = 0; i < value.uploads.size(); i++) {
                        if (value.uploads.get(i).name.equals(foodName)) {
                            //remove item
                            value.uploads.remove(i);
                            //end loop
                            i = value.uploads.size();
                        }
                    }
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


    public interface OnValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
    }

    public interface OnCreateOrderListener {
        void onSuccess(String key);
    }

    public interface QueryListener {
        void onComplete(Order order);
    }

    public interface Handler {
        void onComplete();
    }
}
