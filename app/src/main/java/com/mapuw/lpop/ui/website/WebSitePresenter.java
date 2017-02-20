package com.mapuw.lpop.ui.website;

import android.content.Context;

import com.mapuw.lpop.utils.NetUtil;
import com.mapuw.lpop.widget.webview.ProgressWebView;

/**
 * Created by mapuw on 2017/2/8.
 */

public class WebSitePresenter implements WebSiteView.OperationListener {

    private WebSiteView webSiteView;
    private WebSiteModel webSiteModel;

    public WebSitePresenter(WebSiteView webSiteView) {
        this.webSiteView = webSiteView;
        this.webSiteModel = new WebSiteModel();
    }

    /**
     * 下载任务
     * @param context
     * @param url
     * @param webview
     */
    void doDownload(Context context, String url, ProgressWebView webview) {

        if (NetUtil.isConnected((Context) webSiteView)) {
            this.webSiteModel.doDownload(context, url, webview, this);
        } else {
            webSiteView.showError("请检查当前网路状态!");
        }
    }

    @Override
    public void onStart(String status) {
        this.webSiteView.showMessage(status);
    }

    @Override
    public void onComplete(String status) {
        this.webSiteView.showMessage(status);
    }
}
