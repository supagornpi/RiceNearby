package com.warunya.ricenearby.ui.seller.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.utils.GlideLoader;

import java.util.List;

public class SellerProfileActivity extends AbstractActivity implements SellerProfileContract.View {

    private SellerProfileContract.Presenter presenter;
    private CustomAdapter<Food> adapter;
    private boolean hasFollow = false;

    private RecyclerViewProgress recyclerViewRelate;
    private Button btnFollow;
    private ProgressBar progressBarFollow;
    private TextView tvSellerName;
    private ImageView ivSellerProfile;

    public static void start(String uid) {
        Intent intent = new Intent(MyApplication.applicationContext, SellerProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("uid_seller", uid);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_seller_profile;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle("");
        showBackButton();
        setToolbarColor(android.R.color.transparent);
        bindView();
        bindAction();

        initRecyclerView();
        String uid = getIntent().getStringExtra("uid_seller");
        presenter = new SellerProfilePresenter(this, uid);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        presenter.findRelateFood();
        presenter.checkFollow();
    }

    private void bindView() {
        recyclerViewRelate = findViewById(R.id.recyclerView_relate);
        btnFollow = findViewById(R.id.btn_follow);
        progressBarFollow = findViewById(R.id.progress_follow);
        tvSellerName = findViewById(R.id.tv_seller_name);
        ivSellerProfile = findViewById(R.id.iv_seller_profile);

    }

    private void bindAction() {
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasFollow) {
                    hasFollow = false;
                    presenter.unFollow();
                } else {
                    hasFollow = true;
                    presenter.follow();
                }
                btnFollow.setText(hasFollow ? "ยกเลิกติดตาม" : "ติดตาม");
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewRelate.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodGridView) itemView).setHorizontalItem();
                ((FoodGridView) itemView).bind(((Food) item));
                ((FoodGridView) itemView).bindAction();

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodGridView(parent.getContext());
            }
        });
        recyclerViewRelate.recyclerView.setAdapter(adapter);
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

    @Override
    public void fetchSellerInfo(User user) {
        tvSellerName.setText(user.name == null ? user.username : user.name);
        if (user.image != null) {
            GlideLoader.loadImageCircle(user.image.url, ivSellerProfile);
        }
    }

    @Override
    public void fetchRelateFood(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void updateFallow(boolean hasFollow) {
        this.hasFollow = hasFollow;
        btnFollow.setText(hasFollow ? "ยกเลิกติดตาม" : "ติดตาม");
    }

    @Override
    public void showProgressFollow() {
        progressBarFollow.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressFollow() {
        progressBarFollow.setVisibility(View.GONE);
    }
}
