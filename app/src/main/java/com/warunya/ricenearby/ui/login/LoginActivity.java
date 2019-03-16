package com.warunya.ricenearby.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.ui.main.MainActivity;
import com.warunya.ricenearby.ui.register.RegisterActivity;
import com.warunya.ricenearby.utils.DismissKeyboardListener;
import com.warunya.ricenearby.utils.FacebookUtils;
import com.warunya.ricenearby.utils.ValidatorUtils;

import org.json.JSONObject;

public class LoginActivity extends AbstractActivity implements LoginContract.View {

    private CallbackManager callbackManager = null;
    private LoginContract.Presenter presenter = new LoginPresenter(this);

    private RelativeLayout rootView;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnLoginFacebook;
    private Button btnRegister;
    private Button btnForgotPassword;
    private LoginButton btnLoginButton;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        bindView();
        bindAction();
        initFacebookLogin();

    }

    private void bindView() {
        rootView = findViewById(R.id.rootView);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginFacebook = findViewById(R.id.btn_facebook);
        btnRegister = findViewById(R.id.btn_register);
        btnLoginButton = findViewById(R.id.loginButton);
    }

    private void bindAction() {
        rootView.setOnTouchListener(new DismissKeyboardListener(this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                //login
                presenter.login(email, password);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to register
                RegisterActivity.Start();
            }
        });
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FacebookUtils.isLoginFacebook()) {
                    FacebookUtils.logoutFacebook();
                }
                btnLoginButton.performClick();
            }
        });
    }

    private void initFacebookLogin() {
        callbackManager = FacebookUtils.initFacebookLogin(btnLoginButton, new FacebookUtils.OnCallbackFacebook() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showNotFound() {

    }

    @Override
    public void hideNotFound() {

    }

    @Override
    public void showEmailInvalid() {
        ValidatorUtils.setErrorInput(getApplicationContext(), edtEmail, R.string.error_email_invalid);
    }

    @Override
    public void showPasswordInvalid() {
        ValidatorUtils.setErrorInput(getApplicationContext(), edtPassword, R.string.error_password_invalid);
    }

    @Override
    public void loginSuccess() {
        finish();
        MainActivity.start();
    }

    @Override
    public void loginFailed() {
        DialogAlert.show(this, R.string.dialog_login_failed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
