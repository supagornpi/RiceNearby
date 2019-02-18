package com.warunya.ricenearby.ui.schedule;

import com.warunya.ricenearby.base.BasePresenter;
import com.warunya.ricenearby.base.BaseView;
import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.model.MealByDate;

import java.util.Calendar;
import java.util.List;

public interface ScheduleContract {

    interface Presenter extends BasePresenter {
        void getOrder(Filter filter);

        void filterByDate(Calendar calendar);
    }

    interface View extends BaseView {

        void fetchSummaryOrder(List<MealByDate> mealByDates);

    }
}
