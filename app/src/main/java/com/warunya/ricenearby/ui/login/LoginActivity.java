package com.warunya.ricenearby.ui.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.ui.register.RegisterActivity;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class LoginActivity extends AbstractActivity implements LoginContract.View {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnLoginFacebook;
    private Button btnRegister;
    private Button btnForgotPassword;

    private LoginContract.Presenter presenter = new LoginPresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView() {
        bindView();
        bindAction();

    }

    private void bindView() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginFacebook = findViewById(R.id.btn_facebook);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void bindAction() {
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

    }

    @Override
    public void loginFailed() {
        DialogAlert.Companion.show(this, R.string.dialog_login_failed);
    }
}
