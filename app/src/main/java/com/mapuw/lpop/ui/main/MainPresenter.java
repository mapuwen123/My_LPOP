package com.mapuw.lpop.ui.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.mapuw.lpop.bean.StatusList;
import com.mapuw.lpop.utils.NetUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mapuw on 2016/12/14.
 */

public class MainPresenter implements RequestListener {

    private MainView mainView;
    private MainModel mainModel;

    MainPresenter(MainView mainView) {
        this.mainModel = new MainModel();
        this.mainView = mainView;
    }

    void getUserMSG(Oauth2AccessToken mAccessToken, UsersAPI usersAPI) {
        if (NetUtil.isConnected((Context) mainView)) {
            mainModel.getUserMSG(mAccessToken, usersAPI, this);
        }
    }

    void getStatusList(StatusesAPI statusesAPI, int pageNum, View view) {
        if (NetUtil.isConnected((Context) mainView)) {
            mainModel.getStatusList(statusesAPI, pageNum, this);
            mainView.onRefresh();
        } else {
            NetUtil.showNetSetSnack((Context) mainView, view);
            mainView.offRefresh();
        }
    }

    @Override
    public void onComplete(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                if (new JSONObject(s).isNull("name")) {
                    StatusList statusList = StatusList.parse(s);
                    if (statusList != null) {
                        mainView.offRefresh();
                        mainView.updateStatusList(statusList.statusList);
                    }else {
                        mainView.offRefresh();
                        mainView.showError(s);
                    }
                } else {
                    // 调用 User#parse 将JSON串解析成User对象
                    User user = User.parse(s);
                    if (user != null) {
                        mainView.updateView(user);
                    } else {
                        mainView.showError(s);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        mainView.offRefresh();
        mainView.showError(e.getMessage());
    }
}
