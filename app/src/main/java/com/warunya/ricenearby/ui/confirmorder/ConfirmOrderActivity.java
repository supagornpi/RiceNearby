package com.warunya.ricenearby.ui.confirmorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.CartView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.ui.address.AddressActivity;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class ConfirmOrderActivity extends AbstractActivity implements ConfirmOrderContract.View {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private ConfirmOrderContract.Presenter presenter = new ConfirmOrderPresenter(this);
    private CustomAdapter<Cart> adapter;

    private TextView tvConfirmPayment;
    private RecyclerViewProgress recyclerViewProgress;
    private TextView tvTotalPrice;
    private TextView tvAddress;
    private TextView tvEditAddress;

    public static void start(String key) {
        Intent intent = new Intent(MyApplication.applicationContext, ConfirmOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_KEY, key);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        bindView();
        bindAction();
        showBackButton();
        setTitle("การสั่งซื้ออาหารของท่าน");
        String key = getIntent().getStringExtra(EXTRA_KEY);
        presenter.findOrder(key);
        presenter.start();
    }

    private void bindView() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        tvConfirmPayment = findViewById(R.id.tv_confirm_payment);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvAddress = findViewById(R.id.tv_address_name);
        tvEditAddress = findViewById(R.id.tv_edit_address);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                final Cart cart = ((Cart) item);
                if (cart == null) return;
                ((CartView) itemView).bindAction();
                ((CartView) itemView).bind(cart, false);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new CartView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProgress.recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        tvConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });

        tvEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressActivity.start();
            }
        });

    }

    @Override
    public void fetchCart(List<Cart> carts) {
        adapter.setListItem(carts);
        calculatePrice();
    }

    @Override
    public void fetchAddress(List<Address> addresses) {
        tvAddress.setText(addresses.get(0).name);
    }

    @Override
    public void paymentSuccess() {
        DialogAlert.Companion.showOnlyPossitive(this, "ยืนยันการชำระเงินสำเร็จแล้ว",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
    }

    @Override
    public void showProgress() {
        recyclerViewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        recyclerViewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        recyclerViewProgress.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
    }

    private void calculatePrice() {
        int price = 0;
        for (Cart cart : adapter.getList()) {
            price += cart.food.price * cart.amount;
        }
        tvTotalPrice.setText(String.valueOf(price + AppInstance.DELIVERY_PRICE) + "฿");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                File file = null;
                if (data.getData() != null) {
                    file = FileUtils.getResizedBitmap(this, new File(FileUtils.getRealPathFromURI(this, data.getData())));
                }
                Uri uri = Uri.fromFile(file);
                presenter.confirmPayment(uri);
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
