package com.mapuw.lpop.ui.website;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.mapuw.lpop.widget.webview.ProgressWebView;

/**
 * Created by mapuw on 2017/2/8.
 */

public class WebSiteModel {

    /**
     * 下载任务
     * @param context
     * @param url
     * @param webview
     * @param operation
     */
    void doDownload(Context context, String url, ProgressWebView webview, WebSiteView.OperationListener operation) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir("LPOP/Download", url.split("/")[url.split("/").length - 1]);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long downloadId = downloadManager.enqueue(request);
        webview.goBack();
        operation.onStart("开始下载任务...");
        DownloadReceiver downloadReceiver = new DownloadReceiver(operation);
        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    class DownloadReceiver extends BroadcastReceiver {

        private WebSiteView.OperationListener operation;

        public DownloadReceiver(WebSiteView.OperationListener operation) {
            this.operation = operation;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.operation.onComplete("下载完成");
        }
    }

}
