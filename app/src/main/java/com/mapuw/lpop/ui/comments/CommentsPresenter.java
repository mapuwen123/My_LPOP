package com.mapuw.lpop.ui.comments;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.CommentList;

/**
 * Created by mapuw on 2017/2/19.
 */

public class CommentsPresenter implements RequestListener {
    private CommentsModel commentsModel;
    private CommentsView commentsView;

    public CommentsPresenter(Context context, CommentsView commentsView) {
        this.commentsModel = new CommentsModel(context);
        this.commentsView = commentsView;
    }

    public void getComments(long id, int page) {
        commentsModel.getComments(id, page, this);
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
