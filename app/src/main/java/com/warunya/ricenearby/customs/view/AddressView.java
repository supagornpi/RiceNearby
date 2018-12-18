package com.warunya.ricenearby.customs.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.ui.address.AddressActivity;

public class AddressView extends LinearLayout {

    private int position = 0;
    private Address address;
    private OnActionListener onActionListener;

    private LinearLayout layoutItem;
    private TextView tvAddress;
    private TextView tvEdit;
    private TextView tvDelete;

    public AddressView(Context context) {
        super(context);
        init();
    }

    public AddressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AddressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_address, this);

        tvAddress = findViewById(R.id.tv_address_name);
        tvEdit = findViewById(R.id.tv_edit);
        tvDelete = findViewById(R.id.tv_delete);
    }

    public void fetch(Address address, int position) {
        this.address = address;
        this.position = position;
        tvAddress.setText(address.name);
    }

    public void bindAction(final Activity activity) {
        tvEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    activity.startActivityForResult(builder.build(activity),
                            position);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                if (onActionListener == null) return;
                onActionListener.editItem();
            }
        });

        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onActionListener == null) return;
                onActionListener.deleteItem();
            }
        });
    }

    public AddressView addOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
        return this;
    }

    public interface OnActionListener {
        void deleteItem();

        void editItem();
    }
}
