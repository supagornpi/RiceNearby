package com.warunya.ricenearby.ui.register;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class RegisterPresenter implements RegisterContract.Presenter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RegisterContract.View view;

    RegisterPresenter(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void register(final RegisterEntity entity) {
        if (validate(entity)) {
            view.showProgress();
            mAuth.createUserWithEmailAndPassword(entity.getEmail(), entity.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    view.hideProgress();
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
}
