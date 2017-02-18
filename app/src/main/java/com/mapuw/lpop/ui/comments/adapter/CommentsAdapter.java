package com.mapuw.lpop.ui.comments.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mapuw.lpop.R;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.List;

/**
 * Created by mapuw on 2017/2/18.
 */

public class CommentsAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    private Context context;

    public CommentsAdapter(Context context, int layoutResId, List<Comment> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Comment comment) {
        //头像
        Glide.with(context)
                .load(comment.user.profile_image_url)
                .transform(new GlideCircleTransform(context))
                .priority(Priority.HIGH)
                .into((ImageView) baseViewHolder.getView(R.id.profile_img));
    }
}
