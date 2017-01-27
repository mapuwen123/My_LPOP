package com.mapuw.lpop.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mapuw.lpop.R;
import com.mapuw.lpop.ui.originalimg.OriginalIMGActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mapuw on 2016/12/28.
 */

public class WeiBoImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;
    List<String> bmiddle;

    List<Integer> image_types;
    public static final int IMAGE_GIF = 0;//gif图片
    public static final int IMAGE_LONG = 1;//长图
    public static final int IMAGE_COMMON = 2;//正常图片

    public WeiBoImageAdapter(Context context, int layoutResId, List<String> data, List<String> bmiddle) {
        super(layoutResId, data);
        this.context = context;
        this.bmiddle = bmiddle;
        this.image_types = new ArrayList<Integer>();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String string) {
        View conver = baseViewHolder.getConvertView();
        ImageView longImg = baseViewHolder.getView(R.id.longImg);
        ImageView norImg = baseViewHolder.getView(R.id.norImg);
        ImageView gifView = baseViewHolder.getView(R.id.gifView);
        ImageView imageType = baseViewHolder.getView(R.id.imageType);

        Glide.with(context)
                .load(string)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (string.endsWith(".gif")) {
                            longImg.setVisibility(View.INVISIBLE);
                            norImg.setVisibility(View.INVISIBLE);
                            gifView.setVisibility(View.VISIBLE);
                            imageType.setImageResource(R.mipmap.timeline_image_gif);
                            gifView.setImageBitmap(resource);
                            image_types.add(IMAGE_GIF);
                        } else if (isLongImg(resource)) {
                            longImg.setVisibility(View.VISIBLE);
                            norImg.setVisibility(View.INVISIBLE);
                            gifView.setVisibility(View.INVISIBLE);
                            imageType.setImageResource(R.mipmap.timeline_image_longimage);
                            longImg.setImageBitmap(resource);
                            image_types.add(IMAGE_LONG);
                        } else {
                            longImg.setVisibility(View.INVISIBLE);
                            norImg.setVisibility(View.VISIBLE);
                            gifView.setVisibility(View.INVISIBLE);
                            norImg.setImageBitmap(resource);
                            image_types.add(IMAGE_COMMON);
                        }
                    }
                });

        conver.setOnClickListener(v -> {
            Intent intent = new Intent(context, OriginalIMGActivity.class);
            intent.putStringArrayListExtra("IMG_URLS", (ArrayList<String>) this.bmiddle);
            intent.putIntegerArrayListExtra("IMG_TYPES", (ArrayList<Integer>) this.image_types);
            intent.putExtra("POSITION", baseViewHolder.getAdapterPosition());
            context.startActivity(intent);
        });

    }

    public boolean isLongImg(Bitmap bitmap) {
        if (bitmap.getHeight() > bitmap.getWidth() * 3) {
            return true;
        }
        return false;
    }

}
