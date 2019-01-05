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
import com.warunya.ricenearby.model.FoodImage;
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

    public static DatabaseReference getOrdersReference(String key) {
        return getInstance().mDatabase.child("carts").child(key);
    }

    public static DatabaseReference getBuyerReference(String key) {
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

    private static void uploadFile(final Uri uri, final String username, final String key) {
//        view.showProgressDialog();
        //getting the storage reference
        StorageReference sRef = getInstance().storageReference.child(CartManager.STORAGE_PATH_FOOD + username + System.currentTimeMillis());
        //adding the file to reference

        sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //dismissing the progress dialog
//                view.hideProgressDialog();
                //creating the upload object to store uploaded image details
                Upload upload = new Upload(username, uri.getLastPathSegment(), taskSnapshot.getDownloadUrl().toString());

                //adding an upload to firebase database
                CartManager.uploadFoodImage(getOrdersReference(key), upload);
                CartManager.uploadFoodImage(getBuyerReference(key), upload);
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

    public static void getUserFoods(final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Food>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Food>>() {
                };
                Map<String, Food> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) return;
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

    public static void getAllFoods(final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Food>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Food>>() {
                };
                Map<String, Food> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) return;
                List<Food> foods = new ArrayList<Food>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("foods").addValueEventListener(getInstance().userProfileEventListener);

    }

    public static void filterFoods(String keyWord, final QueryListener queryListener) {
        getInstance().userProfileEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Food>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Food>>() {
                };
                Map<String, Food> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                if (objectHashMap == null) {
                    if (queryListener == null) return;
                    queryListener.onComplete(null);
                    return;
                }
                List<Food> foods = new ArrayList<Food>(objectHashMap.values());
                if (queryListener == null) return;
                queryListener.onComplete(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        getInstance().mDatabase.child("foods").orderByChild("foodName").startAt(keyWord).addValueEventListener(getInstance().userProfileEventListener);

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

    public interface QueryListener {
        void onComplete(List<Food> foods);
    }

    public interface Handler {
        void onComplete();
    }
}
