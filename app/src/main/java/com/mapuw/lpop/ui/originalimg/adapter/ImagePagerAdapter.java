package com.mapuw.lpop.ui.originalimg.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mapuw.lpop.config.AppConfig;
import com.mapuw.lpop.ui.main.adapter.WeiBoImageAdapter;
import com.mapuw.lpop.utils.LogUtil;
import com.mapuw.lpop.utils.SaveImgUtil;
import com.mapuw.lpop.utils.ToastUtil;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import java.io.File;
import java.util.List;

/**
 * Created by mapuw on 2017/1/9.
 */

public class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener {
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
        RelativeLayout view = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ProgressBar progressBar = new ProgressBar(context);

        switch (types.get(position)) {
            case WeiBoImageAdapter.IMAGE_GIF:
                ImageView gifIMG = new ImageView(context);
                gifIMG.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.addView(gifIMG, params);
                setGifIMG(gifIMG, data.get(position), progressBar);
                break;
            case WeiBoImageAdapter.IMAGE_LONG:
                LargeImageView longIMG = new LargeImageView(context);
                view.addView(longIMG, params);
                setLongIMG(longIMG, data.get(position), progressBar);
                break;
            case WeiBoImageAdapter.IMAGE_COMMON:
                LargeImageView norIMG = new LargeImageView(context);
                view.addView(norIMG, params);
                setNorIMG(norIMG, data.get(position), progressBar);
                break;
        }

        RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params_2.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.addView(progressBar, params_2);

        container.addView(view);
        return view;
    }

    /**
     * 加载长图
     *
     * @param imageView
     * @param url
     */
    private void setLongIMG(LargeImageView imageView, String url, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setOnLongClickListener(new ImgOnLongClickListener(resource, null, url));
                        imageView.setImage(resource);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param url
     */
    private void setNorIMG(LargeImageView imageView, String url, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setOnLongClickListener(new ImgOnLongClickListener(resource, null, url));
                        imageView.setImage(resource);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 加载GIF
     *
     * @param imageView
     * @param url
     */
    private void setGifIMG(ImageView imageView, String url, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        Glide.with(context)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        File file = SaveImgUtil.create(context)
                                .saveGif(new File(AppConfig.AppDir, "Picture"), resource, url.split("/")[url.split("/").length - 1]);
                        LogUtil.d("PROGRESS", file.getAbsolutePath());
                        Glide.with(context)
                                .load(file.getAbsoluteFile())
                                .asGif()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new SimpleTarget<GifDrawable>() {
                                    @Override
                                    public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                                        imageView.setOnLongClickListener(new ImgOnLongClickListener(null, file, url));
                                        imageView.setImageDrawable(resource);
                                        ((GifDrawable) imageView.getDrawable()).start();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                });

    }

    @Override
    public void onClick(View v) {
        ((Activity) context).finish();
    }

    public class ImgOnLongClickListener implements View.OnLongClickListener {
        private Bitmap bitmap;
        private File gif;
        private String url;

        public ImgOnLongClickListener(Bitmap bitmap, File gif, String url) {
            this.bitmap = bitmap;
            this.gif = gif;
            this.url = url;
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String items[] = {"保存图片", "分享图片", "复制图片链接"};
            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        File file = null;
                        if (bitmap != null) {
                            file = SaveImgUtil.create(context)
                                    .saveImage(new File(AppConfig.AppDir, "Picture"), bitmap, url.split("/")[url.split("/").length - 1]);
                        } else {
                            file = gif;
                        }
                        ToastUtil.showLong(context, "图片已保存至" + file.getAbsolutePath());
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        Uri uri = null;
                        if (bitmap != null) {
                            uri = Uri.fromFile(SaveImgUtil.create(context)
                                    .saveImage(new File(AppConfig.AppDir, "Picture"), bitmap, url.split("/")[url.split("/").length - 1]));
                        } else {
                            uri = Uri.fromFile(gif);
                        }
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(intent, "分享到"));
                        break;
                    case 2:
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData cd = ClipData.newPlainText("URL", url);
                        cm.setPrimaryClip(cd);
                        break;
                }
            });
            builder.create().show();
            return false;
        }
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
