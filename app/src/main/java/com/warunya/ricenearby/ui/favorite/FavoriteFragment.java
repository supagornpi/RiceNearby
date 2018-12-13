package com.warunya.ricenearby.ui.favorite;

import android.view.View;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;

import org.jetbrains.annotations.NotNull;

public class FavoriteFragment extends AbstractFragment implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter = new FavoritePresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void setupView(@NotNull View view) {

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
