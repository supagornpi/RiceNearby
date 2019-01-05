package com.warunya.ricenearby.customs;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SimplePagerAdapter<T> extends PagerAdapter {

    private List<T> listItems = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnBindViewListener onBindViewListener;
    private OnInflateViewListener onInflateViewListener;

    public SimplePagerAdapter<T> setOnBindViewListener(OnBindViewListener onBindViewListener) {
        this.onBindViewListener = onBindViewListener;
        return this;
    }

    public SimplePagerAdapter<T> setOnInflateViewListener(OnInflateViewListener onInflateViewListener) {
        this.onInflateViewListener = onInflateViewListener;
        return this;
    }

    public SimplePagerAdapter<T> setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getCount() {
        return (listItems == null) ? 1 : listItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        Context mContext = container.getContext();
        View view = null;
        T item = null;
        if (listItems != null) {
            item = listItems.get(position);
        }

        //create view and bind item
        if (onBindViewListener != null) {
            view = onBindViewListener.onCreateView();
            onBindViewListener.onBindViewHolder(item, view, position);
        } else if (onInflateViewListener != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(onInflateViewListener.getLayout(), null);
            onInflateViewListener.onBindViewHolder(item, view, position);
        }

        if (onItemClickListener != null && view != null) {
            final T finalItem = item;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClicked(finalItem);
                }
            });
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnBindViewListener {
        <T> void onBindViewHolder(T item, View itemView, int position);

        View onCreateView();
    }

    public interface OnInflateViewListener {
        <T> void onBindViewHolder(T item, View itemView, int position);

        int getLayout();
    }

    public void setListItems(List<T> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        <T> void onItemClicked(T item);
    }
}