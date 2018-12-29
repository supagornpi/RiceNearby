package com.warunya.ricenearby.ui.addfood.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.utils.GlideLoader;
import com.warunya.ricenearby.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ViewHolder> {

    private List<FoodImage> images = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private ImageButton btnRemove;

        public ViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_image);
            btnRemove = view.findViewById(R.id.btn_remove);
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
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (images == null) {
            viewHolder.ivImage.setImageResource(R.drawable.ic_add);
            return;
        }

        if (images.size() > 0 && position != images.size() && images.get(position).uri != null) {
            GlideLoader.Companion.load(images.get(position).uri, viewHolder.ivImage);
        } else if (images.size() > 0 && position != images.size() && images.get(position).url != null) {
            GlideLoader.Companion.load(images.get(position).url, viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(R.drawable.ic_add);
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

        //show remove button
        viewHolder.btnRemove.setVisibility((images.size() > 0 && position != images.size()) ? View.VISIBLE : View.GONE);

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener == null) return;
                try {
                    onItemClickListener.onItemRemove(position);
                    images.get(position).isRemoved = true;
                    images.remove(position);
                    notifyDataSetChanged();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = 0;
        for (FoodImage foodImage : this.images) {
            if (!foodImage.isRemoved) {
                size++;
            }
        }
        return (size == 4) ? size : size + 1;
    }

    public void addImageUri(Uri uri) {
        this.images.add(new FoodImage(uri));
        notifyDataSetChanged();
    }

    public void setImages(List<FoodImage> foodImages) {
        this.images = foodImages;
        notifyDataSetChanged();
    }

    public List<FoodImage> getFoodImages() {
        return this.images;
    }

    public interface OnItemClickListener {
        void onItemClicked();

        void onItemRemove(int position);

    }
}