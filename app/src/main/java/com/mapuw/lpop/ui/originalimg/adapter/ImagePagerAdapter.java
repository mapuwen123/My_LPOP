package com.mapuw.lpop.ui.originalimg.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.mapuw.lpop.R;
import com.mapuw.lpop.ui.main.adapter.WeiBoImageAdapter;

import java.io.File;
import java.util.List;

import static com.mapuw.lpop.R.id.longImg;

/**
 * Created by mapuw on 2017/1/9.
 */

public class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<String> data;
    private List<Integer> types;

    public ImagePagerAdapter(Context context, List<String> data, List<Integer> types) {
        this.context = context;
        this.data = data;
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
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_pager, null);
        final SubsamplingScaleImageView longIMG = (SubsamplingScaleImageView) view.findViewById(longImg);
        final SubsamplingScaleImageView norIMG = (SubsamplingScaleImageView) view.findViewById(R.id.norImg);
        final ImageView gifIMG = (ImageView) view.findViewById(R.id.gifView);

        longIMG.setOnClickListener(this);
        norIMG.setOnClickListener(this);
        gifIMG.setOnClickListener(this);
        longIMG.setOnLongClickListener(this);
        norIMG.setOnLongClickListener(this);
        gifIMG.setOnLongClickListener(this);

        switch (types.get(position)) {
            case WeiBoImageAdapter.IMAGE_GIF:
                longIMG.setVisibility(View.GONE);
                norIMG.setVisibility(View.GONE);
                gifIMG.setVisibility(View.VISIBLE);
                setGifIMG(gifIMG, data.get(position));
                break;
            case WeiBoImageAdapter.IMAGE_LONG:
                longIMG.setVisibility(View.VISIBLE);
                norIMG.setVisibility(View.GONE);
                gifIMG.setVisibility(View.GONE);
                setLongIMG(longIMG, data.get(position));
                break;
            case WeiBoImageAdapter.IMAGE_COMMON:
                longIMG.setVisibility(View.GONE);
                norIMG.setVisibility(View.VISIBLE);
                gifIMG.setVisibility(View.GONE);
                setNorIMG(norIMG, data.get(position));
                break;
        }

        container.addView(view);
        return view;
    }

    /**
     * 加载长图
     * @param imageView
     * @param url
     */
    private void setLongIMG(SubsamplingScaleImageView imageView, String url) {
        imageView.setQuickScaleEnabled(true);
        imageView.setZoomEnabled(true);
        imageView.setPanEnabled(true);
        imageView.setDoubleTapZoomDuration(100);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        imageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        Glide.with(context)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                        @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        imageView.setImage(ImageSource.uri(resource.getAbsolutePath()), new ImageViewState(0, new PointF(0, 0), 0));
                    }
                });
    }

    /**
     * 加载普通图片
     * @param imageView
     * @param url
     */
    private void setNorIMG(SubsamplingScaleImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .error(R.mipmap.button_web)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImage(ImageSource.bitmap(resource), new ImageViewState(0, new PointF(0, 0), 0));
                    }
                });
    }

    /**
     * 加载GIF
     * @param imageView
     * @param url
     */
    private void setGifIMG(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        ((Activity) context).finish();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
