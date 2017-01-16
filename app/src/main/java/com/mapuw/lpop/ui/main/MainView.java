package com.mapuw.lpop.ui.main;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.List;

/**
 * Created by mapuw on 2016/12/15.
 */

public interface MainView {
    void updateView(User user);
    void updateStatusList(List<Status> data);
    void showError(String msg);
    void onRefresh();
    void offRefresh();
}
