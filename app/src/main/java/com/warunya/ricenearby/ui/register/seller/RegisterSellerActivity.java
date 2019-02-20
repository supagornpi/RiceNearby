package com.warunya.ricenearby.ui.register.seller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.RequireField;
import com.warunya.ricenearby.model.RegisterEntity;
import com.warunya.ricenearby.model.Seller;
import com.warunya.ricenearby.ui.register.RegisterContract;
import com.warunya.ricenearby.ui.register.RegisterPresenter;
import com.warunya.ricenearby.utils.DismissKeyboardListener;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;
import com.warunya.ricenearby.utils.SpinnerUtils;
import com.warunya.ricenearby.utils.ValidatorUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class RegisterSellerActivity extends AbstractActivity implements RegisterContract.View {

    private static final int REQUEST_IMAGE_GALLERY_ID_CARD = 45;
    private static final int REQUEST_IMAGE_GALLERY_BOOK_BANK = 46;

    private LinearLayout rootView;
    private EditText edtIdentityId;
    private EditText edtBankAccount;
    private EditText edtBankName;
    private EditText edtBranch;
    private EditText edtSpinnerError;

    private Button btnCopyIdCard;
    private Button btnCopyBookBank;
    private Button btnRegister;
    private Spinner spnBank;

    private RegisterContract.Presenter presenter = new RegisterPresenter(this);

    public static void Start() {
        Intent intent = new Intent(MyApplication.applicationContext, RegisterSellerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_register_seller;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle("ลงทะเบียนเป็นผู้ขายอาหาร");
        showBackButton();
        bindView();
        bindAction();

    }

    private void bindView() {
        rootView = findViewById(R.id.rootView);
        edtIdentityId = findViewById(R.id.edt_identity_id);
        edtBankAccount = findViewById(R.id.edt_bank_account);
        edtBankName = findViewById(R.id.edt_bank_name);
        edtBranch = findViewById(R.id.edt_branch);
        btnCopyIdCard = findViewById(R.id.btn_copy_id_card);
        btnCopyBookBank = findViewById(R.id.btn_copy_book_bank);
        btnRegister = findViewById(R.id.btn_register);
        spnBank = findViewById(R.id.spinner_bank);
        edtSpinnerError = findViewById(R.id.edt_spinner_error);
        SpinnerUtils.setSpinner(getApplicationContext(), spnBank, R.array.bank_list, true);

        spnBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    //clear error
                    edtSpinnerError.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void bindAction() {
        rootView.setOnTouchListener(new DismissKeyboardListener(this));

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register
                presenter.registerSeller(getRegisterEntity());
            }
        });

        btnCopyIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent(REQUEST_IMAGE_GALLERY_ID_CARD);
            }
        });

        btnCopyBookBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent(REQUEST_IMAGE_GALLERY_BOOK_BANK);
            }
        });
    }

    @Override
    public void requireField(RequireField requireField) {
        EditText editText = null;
        Button button = null;
        switch (requireField) {
            case IdentityId:
                editText = edtIdentityId;
                break;
            case BankAccount:
                editText = edtBankAccount;
                break;
            case BankName:
                editText = edtBankName;
                break;
            case BankBranch:
                editText = edtBranch;
                break;
            case CopyIDCard:
                button = btnCopyIdCard;
                break;
            case CopyBookBank:
                button = btnCopyBookBank;
                break;
        }
        if (editText != null) {
            ValidatorUtils.setErrorInput(getApplicationContext(), editText, R.string.error_please_fill);
        } else if (button != null) {
            button.setTextColor(getResources().getColor(R.color.color_red));
        } else if (requireField == RequireField.Bank) {
            edtSpinnerError.setError(getResources().getString(R.string.error_please_fill));
        }
    }

    @Override
    public void showUsernameInvalid() {

    }

    @Override
    public void showEmailInvalid() {

    }

    @Override
    public void showPasswordInvalid() {
        edtSpinnerError.setTextColor(getResources().getColor(R.color.color_red));

    }

    @Override
    public void showConfirmPasswordNotMatch() {

    }

    @Override
    public void registerSuccess() {
        finish();
    }

    @Override
    public void registerFailed() {
//        DialogAlert.Companion.show(this, R.string.dialog_register_failed);
    }

    private RegisterEntity getRegisterEntity() {
        String idCard = edtIdentityId.getText().toString().trim();
        String bankAccount = edtBankAccount.getText().toString().trim();
        String bankName = edtBankName.getText().toString().trim();
        String bank = spnBank.getSelectedItemPosition() == 0 ? "" : spnBank.getSelectedItem().toString();
        String branch = edtBranch.getText().toString().trim();
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.seller = new Seller();
        registerEntity.seller.idCard = idCard;
        registerEntity.seller.bankAccount = bankAccount;
        registerEntity.seller.bankName = bankName;
        registerEntity.seller.bank = bank;
        registerEntity.seller.bankBranch = branch;
        return registerEntity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                File file = null;
                if (data.getData() != null) {
                    file = FileUtils.getResizedBitmap(this, new File(FileUtils.getRealPathFromURI(this, data.getData())));
                }
                Uri uri = Uri.fromFile(file);
                if (requestCode == REQUEST_IMAGE_GALLERY_ID_CARD) {
                    btnCopyIdCard.setTextColor(getResources().getColor(android.R.color.black));
                    btnCopyIdCard.setText(getString(R.string.seller_add_copy_id_card) + "\n(" + uri.getLastPathSegment() + ")");
                    //set uri to presenter
                    presenter.setCopyIdCardUri(uri);
                } else if (requestCode == REQUEST_IMAGE_GALLERY_BOOK_BANK) {
                    btnCopyBookBank.setTextColor(getResources().getColor(android.R.color.black));
                    btnCopyBookBank.setText(getString(R.string.seller_add_copy_book_bank) + "\n (" + uri.getLastPathSegment() + ")");
                    //set uri to presenter
                    presenter.setCopyBookBankUri(uri);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.Companion.getPERMISSION_READ_EXTERNAL_STORAGE()) {
            if (PermissionUtils.Companion.isGrantAll(permissions)) {
                openGalleryIntent(requestCode);
            }
        }
    }

    private void openGalleryIntent(int requestCode) {
        IntentUtils.INSTANCE.startIntentGallery(this, requestCode);
    }

    @Override
    public void error(@NotNull String message) {

    }
}
