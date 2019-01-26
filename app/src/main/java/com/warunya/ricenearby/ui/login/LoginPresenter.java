package com.warunya.ricenearby.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.warunya.ricenearby.constant.UserType;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.ValidatorUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        view.showProgress();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        view.hideProgress();
                        if (task.isSuccessful()) {
                            Profile profile = Profile.getCurrentProfile();
                            final User user = new User();
                            user.username = profile.getId();
                            user.name = profile.getName();
                            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            try {
                                                user.email = object.getString("email");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            //update firebase
                                            user.userType = UserType.Normal;
                                            UserManager.updateUserData(task.getResult().getUser().getUid(), user);
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender");
                            graphRequest.setParameters(parameters);
                            graphRequest.executeAsync();

                            view.loginSuccess();
                        } else {
                            view.loginFailed();
                        }
                    }
                });
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
