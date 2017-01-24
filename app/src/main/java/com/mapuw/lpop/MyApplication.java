package com.mapuw.lpop;

import android.app.Application;

import com.mapuw.lpop.base.BaseRetrofit;
import com.mapuw.lpop.config.AppConfig;
import com.mapuw.lpop.utils.SDCardUtil;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;

import retrofit2.Retrofit;

/**
 * Created by mapuw on 2016/12/12.
 */

public class MyApplication extends Application {

    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = BaseRetrofit.getRetrofit(this);
        QbSdk.initX5Environment(this, null);
        if (SDCardUtil.isSDCardEnable()) {
            File file = new File(AppConfig.AppDir);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }
}
