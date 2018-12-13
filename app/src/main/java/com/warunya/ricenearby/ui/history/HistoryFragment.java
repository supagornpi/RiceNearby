package com.warunya.ricenearby.ui.history;

import android.view.View;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;

import org.jetbrains.annotations.NotNull;

public class HistoryFragment extends AbstractFragment implements HistoryContract.View {

    private HistoryContract.Presenter presenter = new HistoryPresenter(this);

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_home;
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
