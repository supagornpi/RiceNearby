package com.warunya.ricenearby.base;

public interface BaseView {

    void showProgress();

    void hideProgress();

    void showNotFound();

    void hideNotFound();

    interface Progress {
        void updateProgress(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void error(String message);
    }
}
