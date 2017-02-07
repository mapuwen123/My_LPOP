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

    @Override
    public void onLoadResource(WebView webView, String s) {
        super.onLoadResource(webView, s);
    }

    //    @Override
//    public void onPageFinished(WebView webView, String s) {
//        int height = webView.getHeight();
//        android.support.v4.widget.NestedScrollView.LayoutParams params = (NestedScrollView.LayoutParams) this.nsv.getLayoutParams();
//        params.height = height;
//        this.nsv.setLayoutParams(params);
//        super.onPageFinished(webView, s);
//    }
}
