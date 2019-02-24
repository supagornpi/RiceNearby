package com.warunya.ricenearby.ui.web;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;

import org.jetbrains.annotations.Nullable;

public class WebViewActivity extends AbstractActivity {

    private WebView webView;

    public static void start(String url) {
        Intent intent = new Intent(MyApplication.applicationContext, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        showBackButton();
        bindView();
    }

    private void bindView() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
