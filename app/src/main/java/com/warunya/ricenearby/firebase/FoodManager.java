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
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodManager {

    public static final String STORAGE_PATH_FOOD = "foods/";

    private static final FoodManager ourInstance = new FoodManager();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener userProfileEventListener = null;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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

    public static DatabaseReference getDatabaseReference(String uid) {
        return getInstance().mDatabase.child("foods").child(uid);
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

        if (food.foodImages == null) return;
        if (food.foodImages.size() > 0) {
//            uploadFile(food.foodImages.get(0).uri, food.foodName);
            for (FoodImage foodImage : food.foodImages) {
                uploadFile(foodImage.uri, key, food.foodName);
            }
        }
    }

    private static void uploadFile(final Uri uri, final String username, final String uid) {
//        view.showProgressDialog();
        //getting the storage reference
        StorageReference sRef = getInstance().storageReference.child(FoodManager.STORAGE_PATH_FOOD + username + System.currentTimeMillis());
        //adding the file to reference

        sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //dismissing the progress dialog
//                view.hideProgressDialog();
                //creating the upload object to store uploaded image details
                Upload upload = new Upload(username, uri.getLastPathSegment(), taskSnapshot.getDownloadUrl().toString());

                //adding an upload to firebase database
                FoodManager.uploadFoodImage(upload, uid, new FoodManager.Handler() {
                    @Override
                    public void onComplete() {

                    }
                });

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

    public static void uploadFoodImage(final Upload upload, String uid, final Handler handler) {
        getDatabaseReference(uid).runTransaction(new Transaction.Handler() {
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
                if (handler == null) return;
                handler.onComplete();
            }
        });
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

//    public static void editUserProfile(final User user, final Handler handler) {
//        getDatabaseReference().runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                User value = mutableData.getValue(User.class);
//                if (value == null) {
//                    return Transaction.success(mutableData);
//                }
////                        ?: return Transaction.success(mutableData);
//                value.name = user.name;
//                value.mobile = user.mobile;
//                value.birthday = user.birthday;
//                value.gender = user.gender;
//                // Set value and report transaction success
//                mutableData.setValue(value);
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                if (handler == null) return;
//                handler.onComplete();
//            }
//        });
//    }

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
