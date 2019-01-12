package com.warunya.ricenearby.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.ui.main.MainActivity;
import com.warunya.ricenearby.utils.DismissKeyboardListener;
import com.warunya.ricenearby.utils.ValidatorUtils;

public class RegisterActivity extends AbstractActivity implements RegisterContract.View {

    private RelativeLayout rootView;
    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private Button btnRegister;

    private RegisterContract.Presenter presenter = new RegisterPresenter(this);

    public static void Start() {
        Intent intent = new Intent(MyApplication.applicationContext, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        bindView();
        bindAction();

    }

    private void bindView() {
        rootView = findViewById(R.id.rootView);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void bindAction() {
        rootView.setOnTouchListener(new DismissKeyboardListener(this));

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register
                presenter.register(getRegisterEntity());
            }
        });
    }

    @Override
    public void requireField(RequireField requireField) {
        EditText editText = null;
        switch (requireField) {
            case Username:
                editText = edtUsername;
                break;
            case Email:
                editText = edtEmail;
                break;
            case Password:
                editText = edtPassword;
                break;
            case ConfirmPassword:
                editText = edtConfirmPassword;
                break;
        }
        ValidatorUtils.setErrorInput(getApplicationContext(), editText, R.string.error_please_fill);
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
        MainActivity.start();
    }

    @Override
    public void registerFailed() {
        DialogAlert.Companion.show(this, R.string.dialog_register_failed);
    }

    private RegisterEntity getRegisterEntity() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        return new RegisterEntity(username, email, password, confirmPassword);
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
}
