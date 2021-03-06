package com.warunya.ricenearby.ui.address;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.User;

import java.util.List;

public class AddressPresenter implements AddressContract.Presenter {

    private AddressContract.View view;

    AddressPresenter(AddressContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        UserManager.getUserProfile(new UserManager.OnValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                if (user.addresses == null) return;
                view.fetchAddress(user.addresses);

            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void submit(List<Address> addresses) {
        view.showProgressDialog();
        UserManager.editAddress(addresses, new UserManager.Handler() {
            @Override
            public void onComplete() {
                view.hideProgressDialog();
            }
        });

        if (addresses.size() > 0) {
            //save my location
            Location location = new Location("");
            location.setLatitude(addresses.get(0).latitude);
            location.setLongitude(addresses.get(0).longitude);
            AppInstance.getInstance().setMyLocation(location);

            AppInstance.getInstance().setCurrentLocation(location);

        }
    }
}
