package com.warunya.ricenearby.ui.address;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.AddressView;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.model.Address;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddressActivity extends AbstractActivity implements AddressContract.View {


    public static final int PLACE_PICKER_REQUEST = 129;
    public static final int PLACE_EDIT_REQUEST = 130;

    private AddressContract.Presenter presenter = new AddressPresenter(this);
    private CustomAdapter<Address> adapter;

    private RecyclerView recyclerView;
    private Button btnAddAddress;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, AddressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_address;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle(R.string.title_address);
        showBackButton();
        bindView();
        bindAction();

        presenter.start();

    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAddAddress = findViewById(R.id.btn_add_address);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                Address address = ((Address) item);
                if (address == null) return;
                ((AddressView) itemView).bindAction(AddressActivity.this);
                ((AddressView) itemView).fetch(address, position);
                ((AddressView) itemView).addOnActionListener(new AddressView.OnActionListener() {
                    @Override
                    public void deleteItem() {
                        DialogAlert.Companion.show(AddressActivity.this, "คุณต้องการลบที่อยู่นี้ใช่หรือไม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.deleteItemAt(position);
                                presenter.submit(adapter.getList());
                            }
                        });
                    }

                    @Override
                    public void editItem() {

                    }
                });

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new AddressView(parent.getContext());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddressActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void fetchAddress(List<Address> addresses) {
        adapter.setListItem(addresses);
        btnAddAddress.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            String toastMsg = String.format("Place: %s", data.getAction());
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

            String addressName = place.getName() + "\n" + place.getAddress().toString();
            Double latitude = place.getLatLng().latitude;
            Double longitude = place.getLatLng().longitude;

            Address address = new Address(addressName, latitude, longitude);
            if (requestCode == PLACE_PICKER_REQUEST) {
                adapter.addItem(address);
            } else {
                adapter.editItemAt(requestCode, address);
            }

            btnAddAddress.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);

            presenter.submit(adapter.getList());
        }
    }
}
