package com.mapuw.lpop.ui.originalimg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.databinding.ActivityOriginalImgBinding;
import com.mapuw.lpop.ui.main.adapter.WeiBoImageAdapter;
import com.mapuw.lpop.ui.originalimg.adapter.ImagePagerAdapter;
import com.shizhefei.view.largeimage.LargeImageView;

import java.util.ArrayList;
import java.util.List;

public class OriginalIMGActivity extends BaseActivity {

    private ActivityOriginalImgBinding binding;

    private List<View> image_views;
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

        image_views = new ArrayList<>();
        View itemView = getLayoutInflater().inflate(R.layout.image_pager, null);
        for (int i = 0; i < image_types.size(); i ++) {
//            switch (image_types.get(i)) {
//                case WeiBoImageAdapter.IMAGE_GIF:
//                    image_views.add(new ImageView(this));
//                    break;
//                case WeiBoImageAdapter.IMAGE_LONG:
//                    image_views.add(new LargeImageView(this));
//                    break;
//                case WeiBoImageAdapter.IMAGE_COMMON:
//                    image_views.add(new ImageView(this));
//                    break;
//            }
            image_views.add(new LargeImageView(this));
        }

        ipa = new ImagePagerAdapter(this, image_urls, image_views, image_types);
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
