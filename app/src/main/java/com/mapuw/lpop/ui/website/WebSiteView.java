package com.mapuw.lpop.ui.website;

/**
 * Created by mapuw on 2017/2/8.
 */

public interface WebSiteView {
    void showMessage(String msg);
    void showError(String e);
    interface OperationListener {
        void onStart(String status);
        void onComplete(String status);
    }
}
