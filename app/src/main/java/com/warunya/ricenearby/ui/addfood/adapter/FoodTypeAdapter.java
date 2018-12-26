package com.warunya.ricenearby.ui.addfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.FoodType;

import java.util.ArrayList;
import java.util.List;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.ViewHolder> {

    private List<FoodType> list;
    private List<Integer> selectedList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox_food_type);
        }
    }

    public FoodTypeAdapter(List<FoodType> dataset) {
        this.list = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_type, parent, false);
//        ResolutionUtils.setViewSize(parent.getContext(), 4, 4, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (list == null) {
            return;
        }
        viewHolder.checkBox.setText(list.get(position).typeName);

        viewHolder.checkBox.setChecked(list.get(position).isSelected);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                if (onItemClickListener == null) return;
                onItemClickListener.onItemClicked(viewHolder, position, b);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public List<FoodType> getItemList() {
        return this.list;
    }


    public void setList(List<FoodType> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setSelectedItem(int position, boolean isSelected) {
        list.get(position).isSelected = isSelected;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    public interface OnItemClickListener {
        void onItemClicked(ViewHolder viewHolder, int position, boolean isSelected);
    }
}