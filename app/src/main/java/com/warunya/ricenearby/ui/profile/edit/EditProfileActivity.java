package com.warunya.ricenearby.ui.profile.edit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constance.Gender;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;
import com.warunya.ricenearby.utils.SpinnerUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class EditProfileActivity extends AbstractActivity implements EditProfileContract.View {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private Uri imageUri = null;
    private EditProfileContract.Presenter presenter = new EditProfilePresenter(this);

    private Button btnEditProfile;
    private TextView tvUsername;
    private TextView tvEmail;
    private EditText edtName;
    private EditText edtMobile;
    private EditText edtBirthday;
    private Spinner spnGender;
    private ImageView ivProfile;


    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        ivProfile = findViewById(R.id.iv_profile);

        SpinnerUtils.setSpinner(getApplicationContext(), spnGender, R.array.gender_list, true);

    }

    private void bindAction() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.editUserProfile(getUserProfile());
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public void bindUserData(User user) {
        tvUsername.setText(user.username);
        edtName.setText(user.name);
        tvEmail.setText(user.email);
        edtMobile.setText(user.mobile);

        if (user.image ==null) return;
        GlideLoader.Companion.loadImageCircle(user.image.url, ivProfile);

    }

    private User getUserProfile() {
        String username = tvUsername.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        Gender gender = Gender.Companion.parse(spnGender.getSelectedItemPosition());
        File file = null;
        if (imageUri != null) {
            file = FileUtils.getResizedBitmap(this, new File(FileUtils.getRealPathFromURI(this, imageUri)));
        }

        return new User(username, name, mobile, gender, birthday, Uri.fromFile(file));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                GlideLoader.Companion.loadImageCircle(data.getData(), ivProfile);
                imageUri = data.getData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.Companion.getPERMISSION_READ_EXTERNAL_STORAGE()) {
            if (PermissionUtils.Companion.isGrantAll(permissions)) {
                openGalleryIntent();
            }
        }
    }

    private void openGalleryIntent() {
        IntentUtils.INSTANCE.startIntentGallery(this, REQUEST_IMAGE_GALLERY);
    }
}
