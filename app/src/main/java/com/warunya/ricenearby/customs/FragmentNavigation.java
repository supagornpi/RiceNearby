package com.warunya.ricenearby.customs;

import android.support.v4.app.Fragment;

public interface FragmentNavigation {

    void open(Fragment fragment);

    void replace(Fragment fragment, boolean addToBackStack);

    void navigateBack();
}
