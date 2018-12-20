package com.warunya.ricenearby.utils;

import android.app.Activity;
import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class FacebookUtils {
    /* use Profile to get profile facebook */
    public static void sharedFacebook(Activity activity, String title, String detail, String imageUrl) {
        ShareDialog shareDialog = new ShareDialog(activity);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(title + "\n" + detail)
                .setContentUrl(Uri.parse("http://www.google.com/"))
                .setImageUrl(Uri.parse(imageUrl))
                .build();
        shareDialog.show(linkContent);
    }

    public static CallbackManager initFacebookLogin(LoginButton facebookButton, final OnCallbackFacebook onCallbackFacebook) {
        CallbackManager callbackFacebookManager = CallbackManager.Factory.create();
        facebookButton.setReadPermissions("email, public_profile");
        // Callback registration
        facebookButton.registerCallback(callbackFacebookManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onCallbackFacebook.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                onCallbackFacebook.onCancel();
            }

            @Override
            public void onError(FacebookException exception) {
                onCallbackFacebook.onError(exception);
            }
        });
        return callbackFacebookManager;
    }

    public static boolean isLoginFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void loginFacebook(LoginButton loginButton) {
        if (isLoginFacebook()) {
            logoutFacebook();
        }
        loginButton.performClick();
    }

    public static void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    public static void deletePermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newDeleteObjectRequest(accessToken,
                    "/" + Profile.getCurrentProfile().getId() + "/permissions", new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {

                        }
                    });
            request.executeAsync();
        }
    }

    // listener
    public interface OnCallbackFacebook {
        void onSuccess(LoginResult loginResult);

        void onCancel();

        void onError(FacebookException exception);
    }
}
