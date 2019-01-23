package com.warunya.ricenearby.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.warunya.ricenearby.R;

public class AddAmountDialog extends Dialog {

    private int amount = 1;
    private OnClickListener onClickListener;

    private ImageButton btnClose;
    private TextView tvPlus;
    private TextView tvMinus;
    private TextView tvAmount;
    private Button btnAdd;

    public AddAmountDialog(@NonNull Context context) {
        super(context);
    }

    public static void show(Context context, OnClickListener onclickListener) {
        AddAmountDialog addCartDialog = new AddAmountDialog(context).setOnclickListener(onclickListener);
        addCartDialog.show();
    }

    public AddAmountDialog setOnclickListener(OnClickListener onclickListener) {
        this.onClickListener = onclickListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_amount);
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
        btnAdd = findViewById(R.id.btn_add);

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
                if (amount <= 1) {
                    return;
                } else {
                    amount--;
                }
                tvAmount.setText(String.valueOf(amount));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener == null) return;
                onClickListener.onClickedAddToCart(amount);
                dismiss();
            }
        });
    }

    public interface OnClickListener {
        void onClickedAddToCart(int amount);
    }
}
