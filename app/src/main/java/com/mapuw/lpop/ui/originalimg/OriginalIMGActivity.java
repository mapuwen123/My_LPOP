package com.mapuw.lpop.ui.originalimg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityOriginalImgBinding;
import com.mapuw.lpop.ui.main.MainActivity;
import com.mapuw.lpop.ui.originalimg.adapter.ImagePagerAdapter;

import java.util.List;

public class OriginalIMGActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ActivityOriginalImgBinding binding;

    private List<String> image_urls;
    private List<Integer> image_types;
    private int position;

    private ImagePagerAdapter ipa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_original_img);
    }

    @Override
    protected void appBarInit() {

    }

    @Override
    protected void dataInit() {
        image_urls = getIntent().getStringArrayListExtra("IMG_URLS");
        image_types = getIntent().getIntegerArrayListExtra("IMG_TYPES");
        position = getIntent().getExtras().getInt("POSITION");

        if (image_urls.size() == 1) {
            binding.pageNum.setVisibility(View.GONE);
        } else {
            binding.pageNum.setText(position + 1 + "/" + image_urls.size());
        }

        ipa = new ImagePagerAdapter(this, image_urls, image_types);
        binding.originalImgs.setAdapter(ipa);
        binding.originalImgs.setCurrentItem(position);
    }

    @Override
    protected void eventInit() {
        binding.originalImgs.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        binding.pageNum.setText(position + 1 + "/" + image_urls.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //判断滑动状态，滑动停止时加载图片
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                Glide.with(OriginalIMGActivity.this).resumeRequests();
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                Glide.with(OriginalIMGActivity.this).resumeRequests();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                Glide.with(OriginalIMGActivity.this).pauseRequests();
                break;
        }
    }

//    private void createImageView(int ) {
//
//    }
}
