package com.warunya.ricenearby.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Address;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.address.AddressActivity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SearchActivity extends AbstractActivity implements SearchContract.View {

    private SearchContract.Presenter presenter;

    private CustomAdapter<Food> adapter;
    private RecyclerViewProgress recyclerViewProgress;
    private EditText edtSearch;
    private TextView tvAddressName;
    private LinearLayout layoutAddress;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        showBackButton();
        setTitle("ค้นหา");
        bindView();
        bindAction();
        initRecyclerViewFood();
        initSearchView();
        presenter = new SearchPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCurrentAddress();
        presenter.filterFoods(edtSearch.getText().toString().trim());
    }

    private void bindView() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        edtSearch = findViewById(R.id.edt_search);
        tvAddressName = findViewById(R.id.tv_address_name);
        layoutAddress = findViewById(R.id.layout_address);
    }

    private void bindAction() {
        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressActivity.start();
            }
        });
    }

    private void initRecyclerViewFood() {
        recyclerViewProgress.recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodGridView) itemView).bind(((Food) item));
                ((FoodGridView) itemView).bindAction();

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodGridView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setAdapter(adapter);
    }

    private void initSearchView() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    presenter.filterFoods(charSequence.toString());
                } else {
                    presenter.start();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void fetchFoods(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void fetchAddress(Address address) {
        tvAddressName.setText(address.name);
    }

    @Override
    public void showProgress() {
        recyclerViewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        recyclerViewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        recyclerViewProgress.setTextNotFound("ไม่มีอาหารประเภทนี้");
        recyclerViewProgress.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
    }
}
