package com.warunya.ricenearby.ui.profile;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;

public class ProfileFragment extends AbstractFragment implements ProfileContract.View {

    private ProfileContract.Presenter presenter = new ProfilePresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void setupView() {

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
}
