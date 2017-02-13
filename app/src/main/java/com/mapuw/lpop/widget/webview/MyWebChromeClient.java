package com.mapuw.lpop.widget.webview;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by mapuw on 2017/2/7.
 */

public class MyWebChromeClient extends WebChromeClient {
    private Toolbar toolbar;
    private ProgressBar progressBar;

    public MyWebChromeClient(Toolbar toolbar, ProgressBar progressBar) {
        this.toolbar = toolbar;
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            this.progressBar.setVisibility(View.GONE);
        } else {
            if (this.progressBar.getVisibility() == View.GONE)
                this.progressBar.setVisibility(View.VISIBLE);
            this.progressBar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
        this.toolbar.setTitle(s);
    }



}
