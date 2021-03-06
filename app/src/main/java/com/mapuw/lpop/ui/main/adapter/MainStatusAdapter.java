package com.mapuw.lpop.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mapuw.lpop.R;
import com.mapuw.lpop.bean.ShortUrlBean;
import com.mapuw.lpop.bean.Status;
import com.mapuw.lpop.ui.comments.CommentsActivity;
import com.mapuw.lpop.utils.TimeUtils;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.mapuw.lpop.widget.emojitextview.WeiBoContentTextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mapuw on 2016/12/20.
 */

public class MainStatusAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    private Context context;

    public MainStatusAdapter(Context context, int layoutResId, List<Status> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder baseViewHolder, Status status) {

        //当前微博
        //文字
        SpannableStringBuilder this_ss = WeiBoContentTextUtil.getWeiBoContent(status.text,
                context,
                baseViewHolder.getView(R.id.weibo_Content));
        baseViewHolder.setText(R.id.weibo_Content, this_ss);
        //头像
        Glide.with(context)
                .load(status.user.profile_image_url)
                .transform(new GlideCircleTransform(context))
                .priority(Priority.HIGH)
                .into((ImageView) baseViewHolder.getView(R.id.profile_img));
        //昵称
        baseViewHolder.setText(R.id.profile_name, status.user.screen_name);
        //时间
        baseViewHolder.setText(R.id.profile_time, TimeUtils.instance(context).buildTimeString(status.created_at) + "  ");
        //来源
        if (status.source != null && status.source.length() > 0) {
            baseViewHolder.setText(R.id.weiboComeFrom, "来自 " + status.source.split(">")[1].substring(0,
                    status.source.split(">")[1].length() - 3));
        } else {
            baseViewHolder.setText(R.id.weiboComeFrom, "");
        }
        //图片
        RecyclerView weiboIMGView = baseViewHolder.getView(R.id.weibo_image);
        imageAdapterInit(context, weiboIMGView, status.bmiddle_urls, status.original_urls);
        //转发 评论 点赞
        baseViewHolder.setText(R.id.redirect, status.reposts_count + "");
        baseViewHolder.setText(R.id.comment, status.comments_count + "");
        baseViewHolder.setText(R.id.feedlike, status.attitudes_count + "");
        //转发 评论 点击事件
        baseViewHolder.getView(R.id.bottombar_comment)
                .setOnClickListener(new OnCommentsClickListener(status));
        baseViewHolder.getView(R.id.origin_weibo_layout)
                .setOnClickListener(new OnCommentsClickListener(status));

        //转发原文
        if (status.retweeted_status != null) {
            baseViewHolder.getView(R.id.retweetStatus_layout).setVisibility(View.VISIBLE);
            //文字
            SpannableStringBuilder origin_ss;
            //转发原文被删除的情况下做判空处理
            if (status.retweeted_status.user != null) {
                origin_ss = WeiBoContentTextUtil.getWeiBoContent(
                        "@" + status.retweeted_status.user.screen_name + ":" + status.retweeted_status.text,
                        context,
                        baseViewHolder.getView(R.id.origin_nameAndcontent));
            } else {
                origin_ss = WeiBoContentTextUtil.getWeiBoContent(
                        status.retweeted_status.text,
                        context,
                        baseViewHolder.getView(R.id.origin_nameAndcontent));
            }
            baseViewHolder.setText(R.id.origin_nameAndcontent, origin_ss);
            //图片
            RecyclerView origin_IMGView = baseViewHolder.getView(R.id.origin_imageList);
            imageAdapterInit(context, origin_IMGView, status.retweeted_status.bmiddle_urls, status.retweeted_status.original_urls);
            //转发 评论 点赞
            baseViewHolder.setText(R.id.redirect, status.retweeted_status.reposts_count + "");
            baseViewHolder.setText(R.id.comment, status.retweeted_status.comments_count + "");
            baseViewHolder.setText(R.id.feedlike, status.retweeted_status.attitudes_count + "");
            //转发 评论 点击事件
            baseViewHolder.getView(R.id.bottombar_comment)
                    .setOnClickListener(new OnCommentsClickListener(status.retweeted_status));
            baseViewHolder.getView(R.id.origin_weibo_layout)
                    .setOnClickListener(new OnCommentsClickListener(status.retweeted_status));
        } else {
            baseViewHolder.getView(R.id.retweetStatus_layout).setVisibility(View.GONE);
        }
    }

    /**
     * 九宫格图片适配器初始化
     *
     * @param IMGSView（容器View）
     * @param pic_urls（缩略图列表）
     * @param bmiddle_urls（原图列表）
     */
    public static void imageAdapterInit(Context context, RecyclerView IMGSView, List<String> pic_urls, List<String> bmiddle_urls) {
        int line = 3;
        WeiBoImageAdapter weiBoImageAdapter;
        if (pic_urls != null && pic_urls.size() > 0) {
            if (pic_urls.size() == 1) {
                line = 1;
            } else if (pic_urls.size() == 2 || pic_urls.size() == 4) {
                line = 2;
            } else {
                line = 3;
            }
            weiBoImageAdapter = new WeiBoImageAdapter(context, R.layout.mainfragment_weiboitem_imageitem, pic_urls, bmiddle_urls);
        } else {
            pic_urls = new ArrayList<>();
            bmiddle_urls = new ArrayList<>();
            weiBoImageAdapter = new WeiBoImageAdapter(context, R.layout.mainfragment_weiboitem_imageitem, pic_urls, bmiddle_urls);
        }
        IMGSView.setLayoutManager(new GridLayoutManager(context, line));
        IMGSView.setAdapter(weiBoImageAdapter);
    }

    public class OnCommentsClickListener implements View.OnClickListener {
        private Status status;

        public OnCommentsClickListener(Status status) {
            this.status = status;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CommentsActivity.class);
            ShortUrlBean s = new ShortUrlBean();
            intent.putExtra("STATUS", status);
            context.startActivity(intent);
        }
    }
}
