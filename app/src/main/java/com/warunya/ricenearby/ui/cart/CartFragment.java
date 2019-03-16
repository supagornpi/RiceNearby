package com.warunya.ricenearby.ui.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.CartView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.ui.address.AddressActivity;
import com.warunya.ricenearby.ui.confirmorder.ConfirmOrderActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartFragment extends AbstractFragment implements CartContract.View {

    private CartContract.Presenter presenter = new CartPresenter(this);
    private CustomAdapter<Cart> adapter;
    private Address currentAddress;

    private TextView tvConfirmOrder;
    private RecyclerViewProgress recyclerViewProgress;
    private TextView tvFoodPrice;
    private TextView tvDeliveryPrice;
    private TextView tvTotalPrice;
    private TextView tvAddress;
    private TextView tvEditAddress;
    private EditText edtAdditionalAddress;
    private RelativeLayout actionBar;

    public static CartFragment getInstance(boolean isActionBarEnable) {
        CartFragment cartFragment = new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isActionBarEnable", isActionBarEnable);
        cartFragment.setArguments(bundle);
        return cartFragment;
    }

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_cart;
    }


    @Override
    protected void setupView(@NotNull View view) {
        bindView(view);
        bindAction();

        if (getArguments() != null) {
            boolean isActionBarEnable = getArguments().getBoolean("isActionBarEnable", false);
            actionBar.setVisibility(isActionBarEnable ? View.VISIBLE : View.GONE);
        }
    }


    private void bindView(View view) {
        actionBar = view.findViewById(R.id.actionbar);
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);
        tvConfirmOrder = view.findViewById(R.id.tv_confirm_order);
        tvFoodPrice = view.findViewById(R.id.tv_food_price);
        tvDeliveryPrice = view.findViewById(R.id.tv_delivery_price);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        tvAddress = view.findViewById(R.id.tv_address_name);
        tvEditAddress = view.findViewById(R.id.tv_edit_address);
        edtAdditionalAddress = view.findViewById(R.id.edt_additional_address);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                final Cart cart = ((Cart) item);
                if (cart == null) return;
                ((CartView) itemView).bindAction();
                ((CartView) itemView).bind(cart, true);
                ((CartView) itemView).setOnButtonClickListener(new CartView.OnButtonClickListener() {
                    @Override
                    public void onClickedPlus() {
                        calculatePrice();
                        presenter.editAmount(cart.key, cart.amount);
                    }

                    @Override
                    public void onClickedMinus() {
                        calculatePrice();
                        presenter.editAmount(cart.key, cart.amount);
                    }

                    @Override
                    public void onClickedDelete() {
                        DialogAlert.show(getActivity(), "คุณต้องการลบใช่หรือไม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.deleteItemAt(position);
                                presenter.removeCart(cart.key);
                            }
                        });
                    }
                });
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new CartView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProgress.recyclerView.setAdapter(adapter);
        recyclerViewProgress.setTextNotFound("ไม่มีอาหารที่คุณเลือกในตะกร้า");
        recyclerViewProgress.setBackgroundNotFound(R.drawable.ic_not_found_cart);

        tvDeliveryPrice.setText(AppInstance.DELIVERY_PRICE + "฿");

    }

    private void bindAction() {
        tvConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAddress == null) {
                    //need to add address
                    DialogAlert.show(getActivity(), "คุณต้องเพิ่มที่อยู่ก่อนทำการยืนยันออเดอร์",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AddressActivity.start();
                                }
                            });
                } else {
                    //confirm order
                    DialogAlert.show(getActivity(), R.string.dialog_title_confirm_order,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    presenter.confirmOrder(adapter.getList(), getTotalPrice(),
                                            currentAddress, edtAdditionalAddress.getText().toString());
                                }
                            });
                }
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
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void fetchCart(List<Cart> carts) {
        adapter.setListItem(carts);
        calculatePrice();
    }

    @Override
    public void fetchAddress(List<Address> addresses) {
        currentAddress = addresses.get(0);
        tvAddress.setText(addresses.get(0).name);
    }

    @Override
    public void goToConfirmOrderActivity(String key) {
        ConfirmOrderActivity.start(key);
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
        tvConfirmOrder.setEnabled(false);
        if (isAdded()) {
            tvConfirmOrder.setBackgroundColor(getResources().getColor(R.color.color_gray));
        }
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
        tvConfirmOrder.setEnabled(true);
        if (isAdded()) {
            tvConfirmOrder.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void calculatePrice() {
        tvFoodPrice.setText(String.valueOf(getFoodPrice()) + "฿");
        tvTotalPrice.setText(String.valueOf(getTotalPrice()) + "฿");
    }

    private int getFoodPrice() {
        int price = 0;
        for (Cart cart : adapter.getList()) {
            for (Meal meal : cart.meals) {
                price += cart.food.price * meal.amount;
            }
        }
        return price;
    }

    private int getTotalPrice() {
        return getFoodPrice() + AppInstance.DELIVERY_PRICE;
    }
}
