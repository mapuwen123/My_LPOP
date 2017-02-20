package com.mapuw.lpop.ui.comments;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.mapuw.lpop.utils.NetUtil;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.CommentList;

/**
 * Created by mapuw on 2017/2/19.
 */

public class CommentsPresenter implements RequestListener {
    private CommentsModel commentsModel;
    private CommentsView commentsView;

    public CommentsPresenter(CommentsView commentsView) {
        this.commentsModel = new CommentsModel((Context) commentsView);
        this.commentsView = commentsView;
    }

    public void getComments(long id, int page, View view) {
        if (NetUtil.isConnected((Context) commentsView)) {
            commentsModel.getComments(id, page, this);
            commentsView.onRefresh();
        } else {
            NetUtil.showNetSetSnack((Context) commentsView, view);
            commentsView.offRefresh();
        }
    }

    @Override
    public void onComplete(String s) {
        if (!TextUtils.isEmpty(s)) {
            CommentList commentList = CommentList.parse(s);
            if (commentList != null) {
                commentsView.offRefresh();
                commentsView.updateCommentsList(commentList.commentList);
            } else {
                commentsView.offRefresh();
                commentsView.showError(s);
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        commentsView.offRefresh();
        commentsView.showError(e.getMessage());
    }
}
