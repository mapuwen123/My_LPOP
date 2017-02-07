package com.mapuw.lpop.ui.website;

import android.app.DownloadManager;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.config.AppConfig;
import com.mapuw.lpop.databinding.ActivityWebSiteBinding;
import com.mapuw.lpop.widget.webview.MyWebChromeClient;
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
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("微博");
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
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
        binding.webview.setWebChromeClient(new MyWebChromeClient(binding.toolbar, binding.webview.getProgressbar()));
        binding.webview.loadUrl(url);
    }

    @Override
    protected void eventInit() {
        binding.webview.setDownloadListener((s, s1, s2, s3, l) -> {
            Log.i("tag", "url="+s);
            Log.i("tag", "userAgent="+s1);
            Log.i("tag", "contentDisposition="+s2);
            Log.i("tag", "mimetype="+s3);
            Log.i("tag", "contentLength="+l);
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
            request.setDestinationInExternalPublicDir("LPOP/Download", s.split("/")[s.split("/").length - 1]);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long downloadId = downloadManager.enqueue(request);
            binding.webview.goBack();
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
}
