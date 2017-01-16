package com.mapuw.lpop.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.mapuw.lpop.config.AppConfig;

import static com.bumptech.glide.load.engine.cache.DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE;

/**
 * Created by mapuw on 2017/1/10.
 */

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(AppConfig.AppDir + "/IMGCache", DEFAULT_DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
