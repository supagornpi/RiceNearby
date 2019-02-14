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
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.model.Upload;

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

    public static DatabaseReference getFoodsReference(String key) {
        return getInstance().mDatabase.child("foods").child(key);
    }

    public static DatabaseReference getUserFoodsReference(String key) {
        return getInstance().mDatabase.child("user-foods").child(getUid()).child(key);
    }

    public static void writeNewFood(final Food food) {
        // Create new submitPost at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
        final String key = getInstance().mDatabase.child("foods").push().getKey();
        food.key = key;
        //set address to food object
        AppInstance appInstance = AppInstance.getInstance();
        try {
            food.latitude = appInstance.getMyLocation().getLatitude();
            food.longitude = appInstance.getMyLocation().getLongitude();
        } catch (NullPointerException e) {
            //do nothing
        }


        Map postValues = food.toMap();

        Map childUpdates = new HashMap<String, Object>();
        childUpdates.put("/foods/" + key, postValues);
        childUpdates.put("/user-foods/" + getUid() + "/" + key, postValues);

        getInstance().mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (food.foodImages == null) return;
                if (food.foodImages.size() > 0) {
//            uploadFile(food.foodImages.get(0).uri, food.foodName);
                    for (FoodImage foodImage : food.foodImages) {
                        uploadFile(foodImage.uri, food.foodName, key);
                    }
                }
            }
        });
    }

    private static void uploadFile(final Uri uri, final String username, final String key) {
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
                FoodManager.uploadFoodImage(getFoodsReference(key), upload);
                FoodManager.uploadFoodImage(getUserFoodsReference(key), upload);
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
        getUserFoods(getUid(), queryListener);
    }

    public static void getUserFoods(String uid, final QueryListener queryListener) {
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
        getInstance().mDatabase.child("user-foods").child(uid).addValueEventListener(getInstance().userProfileEventListener);
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

    public static void addMealTime(String key, final Meal meal, final MealTime mealTime) {
        DatabaseReference foodsReference = FoodManager.getFoodsReference(key);
        DatabaseReference userFoodsReference = FoodManager.getUserFoodsReference(key);
        meal.key = foodsReference.push().getKey();

        addMeal(foodsReference, meal, mealTime);
        addMeal(userFoodsReference, meal, mealTime);
    }

    private static void addMeal(final DatabaseReference reference, final Meal meal, final MealTime mealTime) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }
                if (mealTime == MealTime.Breakfast) {
                    if (value.breakfasts == null) {
                        value.breakfasts = new ArrayList<>();
                    }
                    if (!isAddedMealTime(value.breakfasts, meal.date)) {
                        value.breakfasts.add(meal);
                    }
                } else if (mealTime == MealTime.Lunch) {
                    if (value.lunches == null) {
                        value.lunches = new ArrayList<>();
                    }
                    if (!isAddedMealTime(value.lunches, meal.date)) {
                        value.lunches.add(meal);
                    }
                } else if (mealTime == MealTime.Dinner) {
                    if (value.dinners == null) {
                        value.dinners = new ArrayList<>();
                    }
                    if (!isAddedMealTime(value.dinners, meal.date)) {
                        value.dinners.add(meal);
                    }
                } else if (mealTime == MealTime.LateDinner) {
                    if (value.lateDinners == null) {
                        value.lateDinners = new ArrayList<>();
                    }
                    if (!isAddedMealTime(value.lateDinners, meal.date)) {
                        value.lateDinners.add(meal);
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

    //check duplicate item
    private static boolean isAddedMealTime(List<Meal> meals, String date) {
        boolean isAddedDate = false;
        for (Meal meal : meals) {
            if (meal.date.equals(date)) {
                isAddedDate = true;
            }
        }
        return isAddedDate;
    }

    public static void removeMealTime(String key, final String mealKey, final MealTime mealTime) {
        DatabaseReference foodsReference = FoodManager.getFoodsReference(key);
        DatabaseReference userFoodsReference = FoodManager.getUserFoodsReference(key);

        removeMeal(foodsReference, mealKey, mealTime);
        removeMeal(userFoodsReference, mealKey, mealTime);
    }

    private static void removeMeal(DatabaseReference reference, final String mealKey, final MealTime mealTime) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }

                if (mealTime == MealTime.Breakfast) {
                    value.breakfasts = removeMeal(mealKey, value.breakfasts);
                } else if (mealTime == MealTime.Lunch) {
                    value.lunches = removeMeal(mealKey, value.lunches);
                } else if (mealTime == MealTime.Dinner) {
                    value.dinners = removeMeal(mealKey, value.dinners);
                } else if (mealTime == MealTime.LateDinner) {
                    value.lateDinners = removeMeal(mealKey, value.lateDinners);
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

    public static void removeAllMeal(DatabaseReference reference, final MealTime mealTime) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Food value = mutableData.getValue(Food.class);
                if (value == null) {
                    return Transaction.success(mutableData);
                }

                if (mealTime == MealTime.Breakfast) {
                    value.breakfasts = null;
                } else if (mealTime == MealTime.Lunch) {
                    value.lunches = null;
                } else if (mealTime == MealTime.Dinner) {
                    value.dinners = null;
                } else if (mealTime == MealTime.LateDinner) {
                    value.lateDinners = null;
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

    private static List<Meal> removeMeal(String key, List<Meal> meals) {
        int index = 0;
        List<Meal> copyMeals = new ArrayList<>(meals);
        for (Meal item : meals) {
            if (key.equals(item.key)) {
                copyMeals.remove(index);
            }
            index++;
        }
        return copyMeals;
    }

    public static void updateFoodImage(Food food, String key) {
        if (food.foodImages == null) return;
        if (food.foodImages.size() > 0) {
            for (FoodImage foodImage : food.foodImages) {
                if (foodImage.uri != null) {
                    //upload file to firebase
                    uploadFile(foodImage.uri, food.foodName, food.key);
                } else if (foodImage.isRemoved) {
                    removeFoodImage(getFoodsReference(food.key), foodImage.name);
                    removeFoodImage(getUserFoodsReference(food.key), foodImage.name);
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
