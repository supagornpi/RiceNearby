package com.warunya.ricenearby.ui.profile.edit;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;

public class EditProfileActivity extends AbstractActivity implements EditProfileContract.View {

    private Button btnEditProfile;

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
        showBackButton();
        bindView();
        bindAction();
    }

    private void bindView() {
        btnEditProfile = findViewById(R.id.btn_edit_profile);
    }

    private void bindAction() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNotFound() {

    }

    @Override
    public void hideNotFound() {

    }
}
