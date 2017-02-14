package com.mapuw.lpop.ui.originalimg.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.mapuw.lpop.R;
import com.mapuw.lpop.ui.main.adapter.WeiBoImageAdapter;
import com.mapuw.lpop.utils.LogUtil;
import com.mapuw.lpop.utils.ScreenUtil;
import com.mapuw.lpop.utils.ToastUtil;
import com.mapuw.lpop.utils.glide.integration.okhttp.ProgressModelLoader;

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
        RelativeLayout progress_layout = (RelativeLayout) view.findViewById(R.id.progress_layout);
        ProgressBar progressbar = (ProgressBar) view.findViewById(R.id.progressbar);

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
                setGifIMG(gifIMG, data.get(position), progress_layout, progressbar);
                break;
            case WeiBoImageAdapter.IMAGE_LONG:
                longIMG.setVisibility(View.VISIBLE);
                norIMG.setVisibility(View.GONE);
                gifIMG.setVisibility(View.GONE);
                setLongIMG(longIMG, data.get(position), progress_layout, progressbar);
                break;
            case WeiBoImageAdapter.IMAGE_COMMON:
                longIMG.setVisibility(View.GONE);
                norIMG.setVisibility(View.VISIBLE);
                gifIMG.setVisibility(View.GONE);
                setNorIMG(norIMG, data.get(position), progress_layout, progressbar);
                break;
        }

        container.addView(view);
        return view;
    }

    /**
     * 加载长图
     *
     * @param imageView
     * @param url
     */
    private void setLongIMG(SubsamplingScaleImageView imageView, String url, RelativeLayout progress_layout, ProgressBar progressbar) {
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
                        progress_layout.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param url
     */
    private void setNorIMG(SubsamplingScaleImageView imageView, String url, RelativeLayout progress_layout, ProgressBar progressbar) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        progress_layout.setVisibility(View.GONE);
                        imageView.setImage(ImageSource.bitmap(resource), new ImageViewState(0, new PointF(0, 0), 0));
                    }
                });
    }

    /**
     * 加载GIF
     *
     * @param imageView
     * @param url
     */
    private void setGifIMG(ImageView imageView, String url, RelativeLayout progress_layout, ProgressBar progressbar) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String items[] = {"保存图片", "分享图片", "复制图片链接"};
        builder.setItems(items, new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ToastUtil.showShort(context, items[which]);
                        break;
                    case 1:
                        ToastUtil.showShort(context, items[which]);
                        break;
                    case 2:
                        ToastUtil.showShort(context, items[which]);
                        break;
                }
            }
        });
        builder.create().show();
        return false;
    }

//    public class ProgressHandler extends Handler {
//        private  ProgressBar progressbar;
//
//        public ProgressHandler(ProgressBar progressbar) {
//            this.progressbar = progressbar;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    int progress = msg.arg1*100/msg.arg2;
//                    LogUtil.d("PROGRESS", progress + "");
//                    progressbar.setProgress(progress);
//                    break;
//            }
//        }
//    }
}
