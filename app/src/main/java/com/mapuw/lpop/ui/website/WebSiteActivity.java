package com.mapuw.lpop.ui.website;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityWebSiteBinding;
import com.mapuw.lpop.utils.ToastUtil;
import com.mapuw.lpop.widget.webview.MyWebChromeClient;
import com.mapuw.lpop.widget.webview.MyWebViewClient;
import com.tencent.smtt.sdk.WebSettings;

public class WebSiteActivity extends BaseActivity implements WebSiteView, Toolbar.OnMenuItemClickListener {

    private ActivityWebSiteBinding binding;

    private String url = "";

    private WebSitePresenter webSitePresenter;

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
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("微博");
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void dataInit() {
        url = getIntent().getStringExtra("WEBSITE");

        webSitePresenter = new WebSitePresenter(this);

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
        binding.webview.setWebChromeClient(new MyWebChromeClient(binding.toolbar, binding.webview.getProgressbar()));
        binding.webview.loadUrl(url);
    }

    @Override
    protected void eventInit() {
        binding.webview.setDownloadListener((s, s1, s2, s3, l) -> {
            webSitePresenter.doDownload(this, s, binding.webview);
        });
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

    @Override
    public void showMessage(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void showError(String e) {
        ToastUtil.showShort(this, e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_site_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_site_refresh:
                binding.webview.reload();
                break;
            case R.id.web_site_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData cd = ClipData.newPlainText("URL", binding.webview.getUrl());
                cm.setPrimaryClip(cd);
                break;
            case R.id.web_site_cut:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(binding.webview.getUrl()));
                startActivity(intent);
                break;
        }
        return true;
    }
}
