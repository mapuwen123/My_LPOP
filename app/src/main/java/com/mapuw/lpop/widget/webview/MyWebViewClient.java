package com.mapuw.lpop.widget.webview;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by mapuw on 2017/1/17.
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        webView.loadUrl(s);
        return true;
    }
}
