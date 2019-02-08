package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.firebase.FollowManager;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Follow;
import com.warunya.ricenearby.model.User;
import com.warunya.ricenearby.ui.seller.profile.SellerProfileActivity;
import com.warunya.ricenearby.utils.GlideLoader;

public class FollowView extends LinearLayout {

    private Follow follow;

    private TextView tvSellerName;
    private Button btnFollow;
    private ImageView ivSellerProfile;

    public FollowView(Context context) {
        super(context);
        init();
    }

    public FollowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FollowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_follow, this);

        tvSellerName = findViewById(R.id.tv_seller_name);
        btnFollow = findViewById(R.id.btn_follow);
        ivSellerProfile = findViewById(R.id.iv_seller_profile);
    }

    public void bind(final Follow follow) {
        if (follow == null) return;
        this.follow = follow;
        if (follow.user == null) {
            //get user info
            UserManager.getUserProfile(follow.uidSeller, new UserManager.OnValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        follow.user = user;
                        FollowView.this.follow = follow;
                        tvSellerName.setText(follow.user.name == null ? follow.user.username : follow.user.name);
                        if (follow.user.image != null) {
                            GlideLoader.Companion.loadImageCircle(follow.user.image.url, ivSellerProfile);
                        }
                    }
                }
            });

            FollowManager.hasFollow(follow.uidSeller, new FollowManager.HandlerFollow() {
                @Override
                public void onComplete(boolean isFollowing) {
                    follow.isFollowing = isFollowing;
                    btnFollow.setText(isFollowing ? "ยกเลิกติดตาม" : "ติดตาม");
                }
            });
        } else {
            //has user (after got from server)
            tvSellerName.setText(follow.user.name == null ? follow.user.username : follow.user.name);
            if (follow.user.image != null) {
                GlideLoader.Companion.loadImageCircle(follow.user.image.url, ivSellerProfile);
            }
            btnFollow.setText(follow.isFollowing ? "ยกเลิกติดตาม" : "ติดตาม");
        }
    }

    public void bindAction(final OnItemClickListener onItemClickListener) {
        btnFollow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow.isFollowing) {
                    follow.isFollowing = false;
//                    presenter.unFollow();
                } else {
                    follow.isFollowing = true;
//                    presenter.follow();
                }
                onItemClickListener.onFollowBtnClicked(follow.uidSeller, follow.isFollowing);
                btnFollow.setText(follow.isFollowing ? "ยกเลิกติดตาม" : "ติดตาม");
            }
        });

        ivSellerProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerProfileActivity.start(follow.uidSeller);
            }
        });

        tvSellerName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerProfileActivity.start(follow.uidSeller);
            }
        });
    }

    public interface OnItemClickListener {
        void onFollowBtnClicked(String uidSeller, boolean isFollowing);
    }
}
