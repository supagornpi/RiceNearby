package com.warunya.ricenearby.ui.register;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.warunya.ricenearby.constant.ImageType;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class RegisterPresenter implements RegisterContract.Presenter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private RegisterContract.View view;
    public Uri uriCopyIdCard = null;
    public Uri uriCopyBookBank = null;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(final RegisterEntity entity) {
        if (validate(entity)) {
            view.showProgressDialog();
            mAuth.createUserWithEmailAndPassword(entity.getEmail(), entity.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    view.hideProgressDialog();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        //update firebase
                        UserManager.updateUserData(firebaseUser.getUid(), entity);
                        view.registerSuccess();
                    } else {
                        view.registerFailed();
                    }
                }
            });
        }
    }

    @Override
    public void registerSeller(final RegisterEntity registerEntity) {
        if (validateSeller(registerEntity, uriCopyIdCard, uriCopyBookBank)) {
            view.showProgressDialog();
            UserManager.updateUserTypeToSeller(registerEntity.seller, new UserManager.Handler() {
                @Override
                public void onComplete() {
                    for (int index = 0; index < 2; index++) {
                        if (index == 0) {
                            uploadFile(uriCopyIdCard, ImageType.CopyIdCard, registerEntity.seller.idCard, index);
                        } else {
                            uploadFile(uriCopyBookBank, ImageType.CopyBookBank, registerEntity.seller.bankName, index);
                        }
                    }

//                    view.hideProgressDialog();
//                    view.registerSuccess();
                }
            });
        }
    }

    @Override
    public void setCopyIdCardUri(Uri uri) {
        this.uriCopyIdCard = uri;
    }

    @Override
    public void setCopyBookBankUri(Uri uri) {
        this.uriCopyBookBank = uri;
    }

    private void uploadFile(final Uri uri, final ImageType imageType, final String username, final int index) {
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
                UserManager.updateSellerImage(upload, imageType, new UserManager.Handler() {
                    @Override
                    public void onComplete() {
                        if (index > 0) {
                            view.hideProgressDialog();
                            view.registerSuccess();
                        }
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
                view.updateProgress("กำลังอัพโหลดไฟล์ " + (index + 1) + "/2 --> " + progress.intValue() + " %...");
            }
        });
    }

    private Boolean validate(RegisterEntity entity) {
        boolean isValid = false;
        if (entity.getUsername().isEmpty()) {
            //require username
            view.requireField(RequireField.Username);
        } else if (entity.getEmail().isEmpty()) {
            //require email
            view.requireField(RequireField.Email);
        } else if (ValidatorUtils.isInValidEmail(entity.getEmail())) {
            //email is invalid
            view.showEmailInvalid();
        } else if (entity.getPassword().isEmpty()) {
            //require password
            view.requireField(RequireField.Password);
        } else if (ValidatorUtils.isInValidPassword(entity.getPassword())) {
            //password is invalid
            view.showPasswordInvalid();
        } else if (entity.getConfirmPassword().isEmpty()) {
            //require confirm password
            view.requireField(RequireField.ConfirmPassword);
        } else if (!entity.getPassword().equals(entity.getConfirmPassword())) {
            //confirm password not match
            view.showConfirmPasswordNotMatch();
        } else {
            isValid = true;
        }
        return isValid;
    }

    private Boolean validateSeller(RegisterEntity entity, Uri copyIdCardUri, Uri copyBookBankUri) {
        boolean isValid = false;
        if (entity.seller.idCard.isEmpty() || entity.seller.idCard.length() != 13) {
            view.requireField(RequireField.IdentityId);
        } else if (entity.seller.bankAccount.isEmpty() || entity.seller.bankAccount.length() != 10) {
            view.requireField(RequireField.BankAccount);
        } else if (entity.seller.bankName.isEmpty()) {
            view.requireField(RequireField.BankName);
        } else if (entity.seller.bank.isEmpty()) {
            view.requireField(RequireField.Bank);
        } else if (entity.seller.bankBranch.isEmpty()) {
            view.requireField(RequireField.BankBranch);
        } else if (copyIdCardUri == null) {
            view.requireField(RequireField.CopyIDCard);
        } else if (copyBookBankUri == null) {
            view.requireField(RequireField.CopyBookBank);
        } else {
            isValid = true;
        }
        return isValid;
    }

}
