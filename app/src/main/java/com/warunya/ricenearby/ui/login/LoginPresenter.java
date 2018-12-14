package com.warunya.ricenearby.ui.login;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class LoginPresenter implements LoginContract.Presenter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private LoginContract.View view;

    LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (validate(email, password)) {
            view.showProgress();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    view.hideProgress();
                    if (task.isSuccessful()) {
                        view.loginSuccess();
                    } else {
                        view.loginFailed();
                    }
                }
            });
        }
    }

    private Boolean validate(String email, String password) {
        boolean isValid = false;
        if (ValidatorUtils.isInValidEmail(email)) {
            view.showEmailInvalid();
        } else if (ValidatorUtils.isInValidPassword(password)) {
            view.showPasswordInvalid();
        } else {
            isValid = true;
        }
        return isValid;
    }
}
