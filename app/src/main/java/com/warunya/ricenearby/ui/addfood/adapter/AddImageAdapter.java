package com.warunya.ricenearby.ui.addfood.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {

    private List<Upload> images = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_image);
        }
    }

    public AddImageAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_photo, parent, false);
        ResolutionUtils.setViewSize(parent.getContext(), 4, 4, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (images == null) {
            return;
        }
        if (images.size() > 0 && position != images.size() && images.get(position).uri != null) {
            GlideLoader.Companion.load(images.get(position).uri, viewHolder.ivImage);
        }


        if (images.size() > 0 && position != images.size() && images.get(position).url != null) {
            GlideLoader.Companion.load(images.get(position).url, viewHolder.ivImage);
        }

        if (images.size() != 4 && position == images.size() || images.size() == 0 && position == 0) {
            viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) return;
                    onItemClickListener.onItemClicked();
                }
            });
        } else {
            viewHolder.ivImage.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return (this.images.size() == 4) ? this.images.size() : this.images.size() + 1;
    }

    public void addImageUri(Uri uri) {
        this.images.add(new Upload(uri));
        notifyDataSetChanged();
    }

    public void setImages(List<Upload> foodImages) {
        this.images = foodImages;
        notifyDataSetChanged();
    }

    public List<Upload> getFoodImages() {
        return this.images;
    }

    public interface OnItemClickListener {
        void onItemClicked();
    }
}