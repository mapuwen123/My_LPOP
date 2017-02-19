package com.mapuw.lpop.ui.comments.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mapuw.lpop.R;
import com.mapuw.lpop.utils.TimeUtils;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.mapuw.lpop.widget.emojitextview.WeiBoContentTextUtil;
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
        //昵称
        baseViewHolder.setText(R.id.profile_name, comment.user.screen_name);
        //时间
        baseViewHolder.setText(R.id.profile_time, TimeUtils.instance(context).buildTimeString(comment.created_at) + "  ");
        //来源
        if (comment.source != null && comment.source.length() > 0) {
            baseViewHolder.setText(R.id.weiboComeFrom, "来自 " + comment.source.split(">")[1].substring(0,
                    comment.source.split(">")[1].length() - 3));
        } else {
            baseViewHolder.setText(R.id.weiboComeFrom, "");
        }
        //文字
        SpannableStringBuilder this_ss = WeiBoContentTextUtil.getWeiBoContent(comment.text,
                context,
                baseViewHolder.getView(R.id.weibo_Content));
        baseViewHolder.setText(R.id.weibo_Content, this_ss);
        if (comment.reply_comment != null) {
            baseViewHolder.getView(R.id.retweetStatus_layout).setVisibility(View.VISIBLE);
            //文字
            SpannableStringBuilder origin_ss;
            //转发原文被删除的情况下做判空处理
            if (comment.reply_comment.user != null) {
                origin_ss = WeiBoContentTextUtil.getWeiBoContent(
                        "@" + comment.reply_comment.user.screen_name + ":" + comment.reply_comment.text,
                        context,
                        baseViewHolder.getView(R.id.origin_nameAndcontent));
            } else {
                origin_ss = WeiBoContentTextUtil.getWeiBoContent(
                        comment.reply_comment.text,
                        context,
                        baseViewHolder.getView(R.id.origin_nameAndcontent));
            }
        } else {
            baseViewHolder.getView(R.id.retweetStatus_layout).setVisibility(View.GONE);
        }
    }
}
