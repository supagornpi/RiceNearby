package com.warunya.ricenearby.ui.profile;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.address.AddressActivity;
import com.warunya.ricenearby.ui.login.LoginActivity;
import com.warunya.ricenearby.ui.menu.MenuActivity;
import com.warunya.ricenearby.ui.profile.edit.EditProfileActivity;
import com.warunya.ricenearby.ui.register.seller.RegisterSellerActivity;
import com.warunya.ricenearby.ui.seller.SellerActivity;
import com.warunya.ricenearby.ui.settime.SetTimeActivity;
import com.warunya.ricenearby.utils.GlideLoader;

public class ProfileFragment extends AbstractFragment implements ProfileContract.View {

    private boolean hasAddress = false;
    private Button btnEditProfile;
    private Button btnSeller;
    private Button btnLogout;
    private TextView tvName;
    private ImageView ivProfile;
    private Button btnAddFood;
    private LinearLayout layoutSeller;
    private LinearLayout layoutMenu;
    private LinearLayout layoutSetTime;

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
        tvName = view.findViewById(R.id.tv_name);
        ivProfile = view.findViewById(R.id.iv_profile);
        btnAddFood = view.findViewById(R.id.btn_add_food);
        layoutSeller = view.findViewById(R.id.layoutSeller);
        layoutMenu = view.findViewById(R.id.layout_menu);
        layoutSetTime = view.findViewById(R.id.layout_set_time);

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
                presenter.registerSeller();
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasAddress) {
                    AddFoodActivity.start();
                } else {
                    DialogAlert.Companion.show(getActivity(), "คุณต้องเพิ่มที่อยู่ก่อน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddressActivity.start();
                        }
                    });
                }
            }
        });
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.start();
            }
        });
        layoutSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeActivity.start();
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
    public void onResume() {
        super.onResume();
        presenter.start();
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

    @Override
    public void bindUserData(User user) {
        if (user == null) return;
        tvName.setText(user.name == null || user.name.isEmpty() ? user.username : user.name);
        if (user.image == null) return;
        GlideLoader.Companion.loadImageCircle(user.image.url, ivProfile);

        if (user.addresses != null && user.addresses.size() > 0) {
            hasAddress = true;
        }
    }

    @Override
    public void enableRegisterSellerButton() {
        btnSeller.setText(getContext().getResources().getString(R.string.button_register_seller));
        btnSeller.setEnabled(true);
        layoutSeller.setVisibility(View.GONE);
    }

    @Override
    public void disableRegisterSellerButton() {
        btnSeller.setText(getContext().getResources().getString(R.string.profile_you_are_seller));
        btnSeller.setEnabled(false);
        layoutSeller.setVisibility(View.VISIBLE);
    }

    @Override
    public void openSellerActivity() {
        SellerActivity.start();
    }

    @Override
    public void openRegisterSellerActivity() {
        RegisterSellerActivity.Start();
    }
}
