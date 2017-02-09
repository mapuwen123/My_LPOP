package com.mapuw.lpop.ui.originalimg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityOriginalImgBinding;
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
        binding.originalImgs.addOnPageChangeListener(this);
        binding.originalImgs.setCurrentItem(position);
    }

    @Override
    protected void eventInit() {

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

    }

//    private void createImageView(int ) {
//
//    }
}
