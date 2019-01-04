package com.warunya.ricenearby.ui.cart;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.AddressView;
import com.warunya.ricenearby.customs.view.CartView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Cart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartFragment extends AbstractFragment implements CartContract.View {


    private CartContract.Presenter presenter = new CartPresenter(this);
    private CustomAdapter<Cart> adapter;
    private TextView tvConfirmOrder;
    private RecyclerViewProgress recyclerViewProgress;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, CartFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_cart;
    }


    @Override
    protected void setupView(@NotNull View view) {
        bindView(view);
        bindAction();

        presenter.start();

    }


    private void bindView(View view) {
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);
        tvConfirmOrder = view.findViewById(R.id.tv_confirm_order);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                Cart cart = ((Cart) item);
                if (cart == null) return;
                ((CartView) itemView).bindAction();
                ((CartView) itemView).bind(cart);
//                ((CartView) itemView).addOnActionListener(new AddressView.OnActionListener() {
//                    @Override
//                    public void deleteItem() {
//                        DialogAlert.Companion.show(CartFragment.this, "คุณต้องการลบที่อยู่นี้ใช่หรือไม่", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                adapter.deleteItemAt(position);
//                                presenter.submit(adapter.getList());
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void editItem() {
//
//                    }
//                });

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new AddressView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProgress.recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        tvConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void fetchCart(List<Cart> carts) {
        adapter.setListItem(carts);
    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public void updateProgress(@NotNull String message) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }
}
