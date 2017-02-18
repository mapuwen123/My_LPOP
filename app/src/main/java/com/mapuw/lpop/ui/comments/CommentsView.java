package com.mapuw.lpop.ui.comments;

import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.List;

/**
 * Created by mapuw on 2017/2/19.
 */

public interface CommentsView {
    void updateCommentsList(List<Comment> data);
    void showError(String msg);
    void onRefresh();
    void offRefresh();
}
