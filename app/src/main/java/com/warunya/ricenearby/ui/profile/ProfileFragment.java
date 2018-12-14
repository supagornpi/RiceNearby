package com.warunya.ricenearby.ui.profile;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.ui.login.LoginActivity;
import com.warunya.ricenearby.ui.profile.edit.EditProfileActivity;
import com.warunya.ricenearby.ui.seller.SellerActivity;

public class ProfileFragment extends AbstractFragment implements ProfileContract.View {

    private Button btnEditProfile;
    private Button btnSeller;
    private Button btnLogout;

    private ProfileContract.Presenter presenter = new ProfilePresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void setupView(@NonNull View view) {
        bindView(view);
        bindAction();
    }

    private void bindView(View view) {
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnSeller = view.findViewById(R.id.btn_seller);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void bindAction() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivity.start();
            }
        });

        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerActivity.start();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAlert.Companion.show(getActivity(), R.string.dialog_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManager.logout();
                        LoginActivity.start();
                    }
                });
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
