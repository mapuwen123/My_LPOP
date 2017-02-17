package com.mapuw.lpop.ui.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.SpannableStringBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.bean.Status;
import com.mapuw.lpop.databinding.ActivityCommentsBinding;
import com.mapuw.lpop.ui.main.adapter.MainStatusAdapter;
import com.mapuw.lpop.utils.TimeUtils;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.mapuw.lpop.widget.emojitextview.WeiBoContentTextUtil;

public class CommentsActivity extends BaseActivity {

    private ActivityCommentsBinding binding;

    private Status status;

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
        //文字
        SpannableStringBuilder this_ss = WeiBoContentTextUtil.getWeiBoContent(status.text,
                this,
                binding.msg.weiboContent);
        binding.msg.weiboContent.setText(this_ss);
        //头像
        Glide.with(this)
                .load(status.user.profile_image_url)
                .transform(new GlideCircleTransform(this))
                .priority(Priority.HIGH)
                .into(binding.msg.titlebar.headImg.profileImg);
        //昵称
        binding.msg.titlebar.profileName.setText(status.user.screen_name);
        //时间
        binding.msg.titlebar.profileTime.setText(TimeUtils.instance(this).buildTimeString(status.created_at) + "  ");
        //来源
        if (status.source != null && status.source.length() > 0) {
            binding.msg.titlebar.weiboComeFrom.setText("来自 " + status.source.split(">")[1].substring(0,
                    status.source.split(">")[1].length() - 3));
        } else {
            binding.msg.titlebar.weiboComeFrom.setText("");
        }
        //图片
        MainStatusAdapter.imageAdapterInit(this, binding.msg.weiboImage, status.bmiddle_urls, status.original_urls);
    }

    @Override
    protected void eventInit() {

    }
}
