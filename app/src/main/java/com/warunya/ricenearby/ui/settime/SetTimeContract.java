package com.warunya.ricenearby.ui.settime;

import android.widget.CheckBox;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.model.Food;

import java.util.List;

public interface SetTimeContract {

    interface Presenter extends BasePresenter {
        void submit(boolean isSelectedDate, String date, List<CheckBox> checkBoxListMeal, List<Food> foodList);
    }

    interface View extends BaseView.Progress {
        void pleaseSelectedDate();

        void pleaseSelectedMeal();

        void pleaseSelectedMenu();

        void submitSuccess();
    }
}
