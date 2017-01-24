package com.mapuw.lpop.ui.website;

import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;

import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityWebSiteBinding;
import com.mapuw.lpop.widget.webview.MyWebViewClient;
import com.tencent.smtt.sdk.WebSettings;

public class WebSiteActivity extends BaseActivity {

    private ActivityWebSiteBinding binding;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_site);
    }

    @Override
    protected void appBarInit() {
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("微博");
        binding.toolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

//        binding.activityWebSite.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    protected void dataInit() {
        url = getIntent().getStringExtra("WEBSITE");

        WebSettings settings = binding.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setGeolocationEnabled(true);
        settings.setSupportZoom(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        binding.webview.setWebViewClient(new MyWebViewClient());
        binding.webview.loadUrl(url);
    }

    @Override
    protected void eventInit() {

    }

    @Override
    public void onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack();
            return;
        }
        binding.webview.destroy();
        super.onBackPressed();
    }
}
