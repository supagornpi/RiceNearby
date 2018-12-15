package com.warunya.ricenearby.ui.profile.edit;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constance.Gender;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.SpinnerUtils;

public class EditProfileActivity extends AbstractActivity implements EditProfileContract.View {

    private Button btnEditProfile;
    private TextView tvUsername;
    private TextView tvEmail;
    private EditText edtName;
    private EditText edtMobile;
    private EditText edtBirthday;
    private Spinner spnGender;

    private EditProfileContract.Presenter presenter = new EditProfilePresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, EditProfileActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void setupView() {
        setTitle("");
        setToolbarColor(android.R.color.transparent);
        showBackButton();
        bindView();
        bindAction();

        presenter.start();
    }

    private void bindView() {
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        tvUsername = findViewById(R.id.tv_username);
        edtName = findViewById(R.id.edt_name);
        tvEmail = findViewById(R.id.tv_email);
        edtMobile = findViewById(R.id.edt_mobile);
        edtBirthday = findViewById(R.id.edt_birthday);
        spnGender = findViewById(R.id.spinner_gender);

        SpinnerUtils.setSpinner(getApplicationContext(), spnGender, R.array.gender_list, true);

    }

    private void bindAction() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.editUserProfile(getUserProfile());
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
    public void bindUserData(User user) {
        tvUsername.setText(user.username);
        edtName.setText(user.name);
        tvEmail.setText(user.email);
        edtMobile.setText(user.mobile);
    }

    private User getUserProfile() {
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        Gender gender = Gender.Companion.parse(spnGender.getSelectedItemPosition());
        return new User(name, mobile, gender, birthday);
    }
}
