package com.mapuw.lpop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by mapuw on 2016/12/6.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeEdgePercent(0.2f);
        dataBindingView();
        appBarInit();
        dataInit();
        eventInit();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);

    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract void dataBindingView();

    protected abstract void appBarInit();

    protected abstract void dataInit();

    protected abstract void eventInit();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);

    }

    /**
     * 退出应用
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
