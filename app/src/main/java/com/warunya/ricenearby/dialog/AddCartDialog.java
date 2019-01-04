package com.warunya.ricenearby.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.warunya.ricenearby.R;

public class AddCartDialog extends Dialog {

    private int amount = 1;

    private ImageButton btnClose;
    private TextView tvPlus;
    private TextView tvMinus;
    private TextView tvAmount;

    public AddCartDialog(@NonNull Context context) {
        super(context);
    }

    public static void show(Context context) {
        AddCartDialog addCartDialog = new AddCartDialog(context);
        addCartDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_cart);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        bindView();
        bindAction();
    }

    private void bindView() {
        btnClose = findViewById(R.id.btn_close);
        tvPlus = findViewById(R.id.tv_plus);
        tvMinus = findViewById(R.id.tv_minus);
        tvAmount = findViewById(R.id.tv_amount);

        tvAmount.setText(String.valueOf(amount));
    }

    private void bindAction() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount++;
                tvAmount.setText(String.valueOf(amount));
            }
        });

        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount - 1 <= 0) {
                    amount = 1;
                } else {
                    amount--;
                }
                tvAmount.setText(String.valueOf(amount));
            }
        });
    }
}
