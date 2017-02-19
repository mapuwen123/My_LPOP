package com.mapuw.lpop.ui.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.bean.Status;
import com.mapuw.lpop.databinding.ActivityCommentsBinding;
import com.mapuw.lpop.ui.comments.adapter.CommentsAdapter;
import com.mapuw.lpop.ui.main.adapter.MainStatusAdapter;
import com.mapuw.lpop.utils.LogUtil;
import com.mapuw.lpop.utils.TimeUtils;
import com.mapuw.lpop.utils.ToastUtil;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.mapuw.lpop.widget.emojitextview.WeiBoContentTextUtil;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends BaseActivity implements CommentsView {

    private ActivityCommentsBinding binding;

    private Status status;
    private List<Comment> data;

    private CommentsPresenter commentsPresenter;

    private CommentsAdapter adapter;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
    }

    @Override
    protected void appBarInit() {
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("微博正文");
        binding.toolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void dataInit() {
        status = (Status) getIntent().getSerializableExtra("STATUS");
        commentsPresenter = new CommentsPresenter(this, this);

//        //文字
//        SpannableStringBuilder this_ss = WeiBoContentTextUtil.getWeiBoContent(status.text,
//                this,
//                binding.msg.weiboContent);
//        binding.msg.weiboContent.setText(this_ss);
//        //头像
//        Glide.with(this)
//                .load(status.user.profile_image_url)
//                .transform(new GlideCircleTransform(this))
//                .priority(Priority.HIGH)
//                .into(binding.msg.titlebar.headImg.profileImg);
//        //昵称
//        binding.msg.titlebar.profileName.setText(status.user.screen_name);
//        //时间
//        binding.msg.titlebar.profileTime.setText(TimeUtils.instance(this).buildTimeString(status.created_at) + "  ");
//        //来源
//        if (status.source != null && status.source.length() > 0) {
//            binding.msg.titlebar.weiboComeFrom.setText("来自 " + status.source.split(">")[1].substring(0,
//                    status.source.split(">")[1].length() - 3));
//        } else {
//            binding.msg.titlebar.weiboComeFrom.setText("");
//        }
//        //图片
//        MainStatusAdapter.imageAdapterInit(this, binding.msg.weiboImage, status.bmiddle_urls, status.original_urls);

        data = new ArrayList<Comment>();
        adapter = new CommentsAdapter(this, R.layout.comments_item, data);
        adapter.openLoadAnimation();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        binding.commentsRlv.setLayoutManager(linearLayoutManager);
        binding.commentsRlv.setHasFixedSize(true);
        binding.commentsRlv.setNestedScrollingEnabled(false);
        binding.commentsRlv.setAdapter(adapter);
        binding.commentsRlv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView滑动状态，滑动停止时加载图片
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Glide.with(CommentsActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Glide.with(CommentsActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Glide.with(CommentsActivity.this).pauseRequests();
                        break;
                }
            }
        });

        commentsPresenter.getComments(Long.parseLong(status.id), 1);
        onRefresh();
    }

    @Override
    protected void eventInit() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            page = 1;
            commentsPresenter.getComments(Long.parseLong(status.id), page);
        });

        adapter.setOnLoadMoreListener(() -> {
            page = ++page;
            commentsPresenter.getComments(Long.parseLong(status.id), page);
        });
    }

    @Override
    public void updateCommentsList(List<Comment> data) {
        if (data != null) {
            if (page == 1) {
                this.data.clear();
                this.data.addAll(data);
                adapter.notifyDataSetChanged();
            } else {
                adapter.addData(data);
            }
            if (data.size() < 50) {
                adapter.loadMoreEnd();
            } else {
                adapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void showError(String msg) {
        LogUtil.d("ERROR", "ERROR:" + msg);
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(true);
    }

    @Override
    public void offRefresh() {
        binding.swipeRefresh.setRefreshing(false);
    }
}
