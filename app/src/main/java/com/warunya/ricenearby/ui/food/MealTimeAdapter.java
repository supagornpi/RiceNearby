package com.warunya.ricenearby.ui.food;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.warunya.ricenearby.customs.view.MealTimeView;
import com.warunya.ricenearby.model.Meal;

import java.util.List;

public class MealTimeAdapter extends RecyclerView.Adapter<MealTimeAdapter.ViewHolder> {

    private int selectedItem = 0;
    private List<Meal> list;
    private OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }

    public MealTimeAdapter() {
    }

    public MealTimeAdapter(List<Meal> dataset) {
        this.list = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_meal_time, parent, false);
        View view = new MealTimeView(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list == null) {
            return;
        }
        ((MealTimeView) viewHolder.itemView).bind(list.get(position));
        ((MealTimeView) viewHolder.itemView).setSelected(selectedItem == position);
        ((MealTimeView) viewHolder.itemView).bindAction(new MealTimeView.OnItemClickListener() {
            @Override
            public void onClicked() {
                selectedItem = position;
                notifyDataSetChanged();
                if (onItemClickListener == null) return;
                onItemClickListener.onItemClicked(viewHolder, list.get(position), position, selectedItem == position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public List<Meal> getItemList() {
        return this.list;
    }


    public void setList(List<Meal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(ViewHolder viewHolder, Meal meal, int position, boolean isSelected);
    }
}