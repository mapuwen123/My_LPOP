package com.mapuw.lpop.ui.originalimg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import java.io.File;
import java.util.List;

/**
 * Created by mapuw on 2017/1/9.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> data;
    private List<View> views;
    private List<Integer> types;

    public ImagePagerAdapter(Context context, List<String> data, List<View> views, List<Integer> types) {
        this.context = context;
        this.data = data;
        this.views = views;
        this.types = types;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        switch (types.get(position)) {
//            case WeiBoImageAdapter.IMAGE_GIF:
//                Glide.with(context)
//                        .load(this.data.get(position))
//                        .asGif()
//                        .into((ImageView) views.get(position));
//                break;
//            case WeiBoImageAdapter.IMAGE_LONG:
//                setLongIMG((LargeImageView) views.get(position), data.get(position));
//                break;
//            case WeiBoImageAdapter.IMAGE_COMMON:
//                Glide.with(context)
//                        .load(this.data.get(position))
//                        .into((ImageView) views.get(position));
//                break;
//        }
        setLongIMG((LargeImageView) views.get(position), data.get(position));
        container.addView(views.get(position));
        return container.getChildAt(position);
    }

    private void setLongIMG(LargeImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        imageView.setImage(new FileBitmapDecoderFactory(resource));
                    }
                });
    }
}
