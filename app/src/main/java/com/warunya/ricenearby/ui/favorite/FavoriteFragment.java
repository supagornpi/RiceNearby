package com.warunya.ricenearby.ui.favorite;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;

public class FavoriteFragment extends AbstractFragment implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter = new FavoritePresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_favorite;
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
