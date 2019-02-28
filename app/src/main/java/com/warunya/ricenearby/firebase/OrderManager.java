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
import com.warunya.ricenearby.constant.OrderStatus;
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
    private ValueEventListener userOrderEventListener = null;
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

    public static DatabaseReference getUserOrderReference(String key) {
        return getUserOrderReference(key, UserManager.getUid());
    }

    public static DatabaseReference getUserOrderReference(String key, String uid) {
        return getInstance().mDatabase.child("user-orders").child(uid).child(key);
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

    public static void uploadBillingImage(final Uri uri, final String username, final String key, final Handler handler) {
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
                OrderManager.uploadBillingImage(getOrdersReference(key), upload);
                OrderManager.uploadBillingImage(getUserOrderReference(key), upload);

                //change order status to Paid
                changeOrderStatus(getOrdersReference(key), OrderStatus.WaitingForReview);
                changeOrderStatus(getUserOrderReference(key), OrderStatus.WaitingForReview);

                handler.onComplete();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                view.hideProgressDialog();
//                view.error(e.getMessage());
                handler.onFailure();

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

    public static void getOrderByKey(String key, final QueryListener queryListener) {
        getInstance().userOrderEventListener = new ValueEventListener() {
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
        getInstance().mDatabase.child("orders").child(key)
                .addListenerForSingleValueEvent(getInstance().userOrderEventListener);

    }

    public static void getUserOrders(final QueryListListener queryListListener) {
        getInstance().userOrderEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Order>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Order>>() {
                };
                Map<String, Order> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListListener == null) return;
                    queryListListener.onComplete(new ArrayList<Order>());
                    return;
                }
                List<Order> carts = new ArrayList<Order>(objectHashMap.values());
                if (queryListListener == null) return;
                queryListListener.onComplete(carts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-orders").child(UserManager.getUid())
                .addListenerForSingleValueEvent(getInstance().userOrderEventListener);

    }

    public static void getUserOrdersOnlyPaid(final QueryListListener queryListListener) {
        getInstance().userOrderEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Order>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Order>>() {
                };
                Map<String, Order> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListListener == null) return;
                    queryListListener.onComplete(new ArrayList<Order>());
                    return;
                }
                List<Order> carts = new ArrayList<Order>(objectHashMap.values());
                if (queryListListener == null) return;
                queryListListener.onComplete(carts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("user-orders").child(UserManager.getUid()).child("orderStatus").equalTo("Paid")
                .addListenerForSingleValueEvent(getInstance().userOrderEventListener);

    }

    public static void getAllOrders(final QueryListListener queryListListener) {
        getInstance().userOrderEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Order>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Order>>() {
                };
                Map<String, Order> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListListener == null) return;
                    queryListListener.onComplete(new ArrayList<Order>());
                    return;
                }
                List<Order> carts = new ArrayList<Order>(objectHashMap.values());
                if (queryListListener == null) return;
                queryListListener.onComplete(carts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("orders")
                .addListenerForSingleValueEvent(getInstance().userOrderEventListener);

    }

    private static void uploadBillingImage(DatabaseReference reference, final Upload upload) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Order value = mutableData.getValue(Order.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.billingImage = upload;
                // Set value and report transaction success
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void changeOrderStatus(DatabaseReference reference, final OrderStatus orderStatus) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Order value = mutableData.getValue(Order.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                value.orderStatus = orderStatus;

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

    public interface QueryListListener {
        void onComplete(List<Order> orders);
    }

    public interface Handler {
        void onComplete();

        void onFailure();
    }
}
