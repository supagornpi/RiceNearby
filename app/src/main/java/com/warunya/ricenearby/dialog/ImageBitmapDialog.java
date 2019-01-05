package com.warunya.ricenearby.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.utils.CircleAnimationUtil;


public class ImageBitmapDialog extends Dialog {

    private ImageView imageView;
    private Bitmap bitmap;
    private Activity activity;

    public ImageBitmapDialog(Activity activity, Bitmap bitmap) {
        super(activity);
        this.activity = activity;
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_image);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        imageView = (ImageView) findViewById(R.id.image);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(1000);
        imageView.setAnimation(fadeIn);
        imageView.setImageBitmap(bitmap);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                makeFlyAnimation(imageView);
            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void makeFlyAnimation(ImageView targetView) {

        RelativeLayout destView = (RelativeLayout) findViewById(R.id.cartRelativeLayout);

        CircleAnimationUtil circleAnimationUtil = new CircleAnimationUtil();
        circleAnimationUtil.setCircleDuration(100);
        circleAnimationUtil.attachActivity(activity).setTargetView(targetView)
                .setMoveDuration(300).setDestView(destView)
                .setAnimationListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
//                        Toast.makeText(getContext(), "Continue Shopping...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).startAnimation();


    }
}
