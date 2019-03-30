package com.warunya.ricenearby.ui.profile.edit;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.model.User;

public class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileContract.View view;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    EditProfilePresenter(EditProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.bindUserData(user);
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void editUserProfile(final User user) {
        view.showProgressDialog();
        UserManager.editUserProfile(user, new UserManager.Handler() {
            @Override
            public void onComplete() {
                if (user.imageUri != null) {
                    uploadFile(user.imageUri, user.username);
                } else {
                    view.hideProgressDialog();
                }
            }
        });
    }

    @Override
    public void getAddress() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.bindAddress(user);
            }
        });
    }

    private void uploadFile(final Uri uri, final String username) {
        view.showProgressDialog();
        //getting the storage reference
        StorageReference sRef = storageReference.child(UserManager.STORAGE_PATH_PROFILE + username + System.currentTimeMillis());
        //adding the file to reference

        sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //dismissing the progress dialog
                view.hideProgressDialog();
                //creating the upload object to store uploaded image details
                Upload upload = new Upload(username, uri.getLastPathSegment(), taskSnapshot.getDownloadUrl().toString());

                //adding an upload to firebase database
                UserManager.updateUserImage(upload, new UserManager.Handler() {
                    @Override
                    public void onComplete() {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.hideProgressDialog();
                view.error(e.getMessage());

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //displaying the upload progress
                Double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                view.updateProgress("Uploading " + progress.intValue() + " %...");
            }
        });
    }
}
