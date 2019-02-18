package com.warunya.ricenearby.ui.profile.edit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.Gender;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.address.AddressActivity;
import com.warunya.ricenearby.utils.DismissKeyboardListener;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;
import com.warunya.ricenearby.utils.SpinnerUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.warunya.ricenearby.constant.AppInstance.DATE_FORMAT_BIRTHDATE;

public class EditProfileActivity extends AbstractActivity implements EditProfileContract.View {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private Uri imageUri = null;
    private EditProfileContract.Presenter presenter = new EditProfilePresenter(this);
    private String currentSelectedDate = "";
    private Calendar myCalendar = Calendar.getInstance();

    private Button btnEditProfile;
    private TextView tvUsername;
    private TextView tvEmail;
    private EditText edtName;
    private EditText edtMobile;
    private TextView tvBirthday;
    private TextView tvGender;
    private TextView tvAddress;
    private Spinner spnGender;
    private ImageView ivProfile;
    private LinearLayout layoutAddress;
    private LinearLayout layoutRootView;
    private ScrollView scrollView;

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
    protected void setupView(Bundle savedInstanceState) {
        setTitle("");
        setToolbarColor(android.R.color.transparent);
        showBackButton();
        bindView();
        bindAction();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    private void bindView() {
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        tvUsername = findViewById(R.id.tv_username);
        edtName = findViewById(R.id.edt_name);
        tvEmail = findViewById(R.id.tv_email);
        edtMobile = findViewById(R.id.edt_mobile);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvGender = findViewById(R.id.tv_gender);
        spnGender = findViewById(R.id.spinner_gender);
        ivProfile = findViewById(R.id.iv_profile);
        tvAddress = findViewById(R.id.tv_address);
        layoutAddress = findViewById(R.id.layout_address);
        layoutRootView = findViewById(R.id.rootView);
        scrollView = findViewById(R.id.scrollView);

        SpinnerUtils.setSpinner(getApplicationContext(), spnGender, R.array.gender_list, true);

    }

    private void bindAction() {
        layoutRootView.setOnTouchListener(new DismissKeyboardListener(this));
        scrollView.setOnTouchListener(new DismissKeyboardListener(this));

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

        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressActivity.start();
            }
        });

        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        tvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnGender.performClick();
            }
        });

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvGender.setText(spnGender.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        if (user.birthday != null) {
            currentSelectedDate = user.birthday;
            tvBirthday.setText(user.birthday);
        }

        if (user.image == null) return;
        GlideLoader.Companion.loadImageCircle(user.image.url, ivProfile);

        if (user.gender != null) {
            if (user.gender == Gender.Male) {
                spnGender.setSelection(1);
            } else if (user.gender == Gender.Female) {
                spnGender.setSelection(2);
            }
        }

        if (user.addresses != null && user.addresses.size() > 0) {
            tvAddress.setText(user.addresses.get(0).name);
        }

    }

    private User getUserProfile() {
        String username = tvUsername.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String birthday = currentSelectedDate;
        Gender gender = Gender.Companion.parse(spnGender.getSelectedItemPosition());
        File file = null;
        if (imageUri != null) {
            file = FileUtils.getResizedBitmap(this, new File(FileUtils.getRealPathFromURI(this, imageUri)));
        }

        return new User(username, name, mobile, gender, birthday, file == null ? null : Uri.fromFile(file));
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

    private void showDatePickerDialog() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_BIRTHDATE, new Locale("th"));
        currentSelectedDate = sdf.format(myCalendar.getTime());
        tvBirthday.setText(currentSelectedDate);
    }
}
