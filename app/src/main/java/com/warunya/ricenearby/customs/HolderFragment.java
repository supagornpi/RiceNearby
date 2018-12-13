package com.warunya.ricenearby.customs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.Tabs;
import com.warunya.ricenearby.ui.favorite.FavoriteFragment;
import com.warunya.ricenearby.ui.history.HistoryFragment;
import com.warunya.ricenearby.ui.home.HomeFragment;
import com.warunya.ricenearby.ui.profile.ProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HolderFragment extends Fragment implements FragmentNavigation {
    private final static String TAG = HolderFragment.class.getName();

    FrameLayout holderFrame;

    public static HolderFragment newInstance(Tabs tabs) {
        HolderFragment fragment = new HolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Tabs.class.getSimpleName(), tabs.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }

    public HolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        holderFrame = view.findViewById(R.id.holderFrame);
        Tabs tabs = Tabs.parse(getArguments().getInt(Tabs.class.getSimpleName(), 0));

        if (getChildFragmentManager().findFragmentById(R.id.holderFrame) == null) {
            Fragment fragment = null;
            switch (tabs) {
                case Home:
                    fragment = new HomeFragment();
                    break;
                case Favorite:
                    fragment = new FavoriteFragment();
                    break;
                case History:
                    fragment = new HistoryFragment();
                    break;
                case Profile:
                    fragment = new ProfileFragment();
                    break;
                default:
                    break;
            }

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.holderFrame, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void open(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .replace(R.id.holderFrame, fragment)
                .commit();
    }

    @Override
    public void replace(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getChildFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .replace(R.id.main_content, fragment)
                    .commitAllowingStateLoss();
        } else {
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_content, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void navigateBack() {
        getActivity().onBackPressed();
    }
}
