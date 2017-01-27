package com.mapuw.lpop.widget.webview;

import android.support.v4.widget.NestedScrollView;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by mapuw on 2017/1/17.
 */

public class MyWebViewClient extends WebViewClient {
    private NestedScrollView nsv;

    public MyWebViewClient(NestedScrollView nsv) {
        this.nsv = nsv;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        webView.loadUrl(s);
        return true;
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        int height = webView.getHeight();
        android.support.v4.widget.NestedScrollView.LayoutParams params = (NestedScrollView.LayoutParams) this.nsv.getLayoutParams();
        params.height = height;
        this.nsv.setLayoutParams(params);
        super.onPageFinished(webView, s);
    }
}
