package com.mapuw.lpop.ui.main;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;

/**
 * Created by mapuw on 2016/12/14.
 */

public class MainModel {

    void getUserMSG(Oauth2AccessToken mAccessToken, UsersAPI usersAPI, RequestListener requestListener) {
        usersAPI.show(Long.parseLong(mAccessToken.getUid()),
                requestListener);
    }

    void getStatusList(StatusesAPI statusesAPI, int pageNum, RequestListener requestListener) {
        statusesAPI.friendsTimeline(0,
                0,
                20,
                pageNum,
                false,
                0,
                false,
                requestListener);
    }

}
