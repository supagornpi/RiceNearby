package com.warunya.ricenearby.ui.favorite;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FollowView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Follow;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteFragment extends AbstractFragment implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter = new FavoritePresenter(this);
    private CustomAdapter<Follow> adapter;

    private RecyclerViewProgress recyclerViewProgress;

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void setupView(@NotNull View view) {
        setTitle("Following");
        bindView(view);
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    private void bindView(View view) {
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);
    }

    private void initRecyclerView() {
        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                ((FollowView) itemView).bind(((Follow) item));
                ((FollowView) itemView).bindAction(new FollowView.OnItemClickListener() {
                    @Override
                    public void onFollowBtnClicked(String uidSeller, boolean isFollowing) {
                        if (isFollowing) {
                            presenter.follow(uidSeller);
                        } else {
                            presenter.unFollow(uidSeller);
                            adapter.deleteItemAt(position);
                            adapter.notifyDataSetChanged();

                            if (adapter.getItemCount() == 0) {
                                recyclerViewProgress.showNotFound();
                            }
                        }
                    }
                });
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FollowView(parent.getContext());
            }
        });
        recyclerViewProgress.setTextNotFound("คุณยังไม่ได้ติดตามร้านค้า");
        recyclerViewProgress.recyclerView.setAdapter(adapter);

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

    @Override
    public void fetchFollower(List<Follow> follows) {
        adapter.setListItem(follows);
    }
}
