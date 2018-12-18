package com.warunya.ricenearby.customs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter<T> extends RecyclerView.Adapter<CustomAdapter<T>.ViewHolder> {

    private List<T> list = new ArrayList<>();
    private OnBindViewListener onBindViewListener;

    public CustomAdapter(OnBindViewListener onBindViewListener) {
        this.onBindViewListener = onBindViewListener;
    }

    @Override
    public CustomAdapter<T>.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = onBindViewListener.onCreateView(parent);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter<T>.ViewHolder holder, int position) {
        if (list.size() == 0) {
            return;
        }
        T item = list.get(position);
        onBindViewListener.onBindViewHolder(item, holder.itemView, holder.getItemViewType(), position);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnBindViewListener {
        <T> void onBindViewHolder(T item, View itemView, int viewType, int position);

        View onCreateView(ViewGroup parent);
    }

    public void setListItem(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addListItem(List<T> list) {
        int notifyStart = this.list.size();
        this.list.addAll(list);
        notifyItemRangeChanged(notifyStart, (notifyStart + list.size()));
    }

    public void addItem(T item) {
        int notifyStart = this.list.size();
        this.list.add(item);
        notifyItemRangeChanged(notifyStart, (notifyStart + list.size()));
    }

    public void editItemAt(int position, T item) {
        int notifyStart = this.list.size();
        this.list.set(position, item);
        notifyItemRangeChanged(notifyStart, (notifyStart + list.size()));
    }

    public void deleteItemAt(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getList() {
        return list;
    }
}
