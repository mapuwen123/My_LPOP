package com.mapuw.lpop.ui.originalimg.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.sina.weibo.sdk.openapi.legacy.CommonAPI.CAPITAL.l;

/**
 * Created by mapuw on 2017/1/9.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> data;
    private List<View> views;

    public ImagePagerAdapter(Context context, List<String> data, List<View> views) {
        this.context = context;
        this.data = data;
        this.views = views;
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
        container.removeView(this.views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Glide.with(context)
                .load(this.data.get(position))
                .into((ImageView) views.get(position));
        container.addView(views.get(position));
        return container.getChildAt(position);
    }
}
