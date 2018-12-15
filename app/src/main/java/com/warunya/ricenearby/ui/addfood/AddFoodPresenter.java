package com.warunya.ricenearby.ui.addfood;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.warunya.ricenearby.firebase.FoodManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.model.Upload;

public class AddFoodPresenter implements AddFoodContract.Presenter {

    private AddFoodContract.View view;

    AddFoodPresenter(AddFoodContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void addNewFood(Food food) {
        FoodManager.writeNewFood(food);
        view.addSuccess();

    }

}
