package com.warunya.ricenearby.ui.home;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodGridView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Food;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFragment extends AbstractFragment implements HomeContract.View {

    private HomeContract.Presenter presenter = new HomePresenter(this);
    private CustomAdapter<Food> adapter;

    private RecyclerViewProgress recyclerViewProgress;
    private EditText edtSearch;

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupView(@NotNull View view) {
        bindView(view);
        initRecyclerView();
        initSearchView();

        presenter.start();
    }

    private void bindView(View view) {
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);
        edtSearch = view.findViewById(R.id.edt_search);

    }

    private void initRecyclerView() {
        recyclerViewProgress.recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));

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
    public void fetchFoods(List<Food> Foods) {
        adapter.setListItem(Foods);
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
        recyclerViewProgress.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
    }
}
