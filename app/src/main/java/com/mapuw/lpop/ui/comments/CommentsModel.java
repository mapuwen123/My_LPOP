package com.mapuw.lpop.ui.comments;

import android.content.Context;

import com.mapuw.lpop.config.Constants;
import com.mapuw.lpop.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;

/**
 * Created by mapuw on 2017/2/19.
 */

public class CommentsModel {
    private CommentsAPI api;

    public CommentsModel(Context context) {
        api = new CommentsAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
    }

    void getComments(long id, int page, RequestListener listener) {
        api.show(id
                , 0
                , 0
                , 25
                , page
                , 0
                , listener);
    }

}
