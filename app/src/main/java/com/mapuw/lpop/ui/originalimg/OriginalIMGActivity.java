package com.mapuw.lpop.ui.originalimg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityOriginalImgBinding;
import com.mapuw.lpop.ui.main.adapter.WeiBoImageAdapter;
import com.mapuw.lpop.ui.originalimg.adapter.ImagePagerAdapter;
import com.mapuw.lpop.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class OriginalIMGActivity extends BaseActivity {

    private ActivityOriginalImgBinding binding;

    private List<View> image_views;
    private List<String> image_urls;
    private int position;
    private int image_type;

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
        position = getIntent().getExtras().getInt("POSITION");
        image_type = getIntent().getExtras().getInt("IMAGE_TYPE");

        image_views = new ArrayList<>();
        for (int i = 0; i < image_urls.size(); i ++) {
            if (i == position) {
                if (image_type == WeiBoImageAdapter.IMAGE_COMMON) {
                    image_views.add(new ImageView(this));
                } else if (image_type == WeiBoImageAdapter.IMAGE_GIF) {
                    image_views.add(new ImageView(this));
                } else {
                    image_views.add(new SubsamplingScaleImageView(this));
                }
            } else {
                image_views.add(new ImageView(this));
            }
        }

        ipa = new ImagePagerAdapter(this, image_urls, image_views);
        binding.originalImgs.setAdapter(ipa);
        binding.originalImgs.setCurrentItem(position);
    }

    @Override
    protected void eventInit() {

    }

//    private void createImageView(int ) {
//
//    }
}
