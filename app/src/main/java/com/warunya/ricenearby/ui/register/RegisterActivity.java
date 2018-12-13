package com.warunya.ricenearby.ui.register;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class RegisterActivity extends AbstractActivity implements RegisterContract.View {

    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private Button btnRegister;

    private RegisterContract.Presenter presenter = new RegisterPresenter(this);

    public static void Start() {
        Intent intent = new Intent(MyApplication.applicationContext, RegisterActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupView() {
        bindView();
        bindAction();

    }

    private void bindView() {
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void bindAction() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();
                //register
                presenter.register(username, email, password, confirmPassword);
            }
        });
    }

    @Override
    public void showUsernameInvalid() {
        ValidatorUtils.setErrorInput(getApplicationContext(), edtEmail, R.string.error_username_invalid);
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
    public void showConfirmPasswordNotMatch() {
        ValidatorUtils.setErrorInput(getApplicationContext(), edtPassword, R.string.error_password_not_match);

    }

    @Override
    public void registerSuccess() {

    }

    @Override
    public void registerFailed() {
        DialogAlert.Companion.show(this, R.string.dialog_register_failed);
    }
}
