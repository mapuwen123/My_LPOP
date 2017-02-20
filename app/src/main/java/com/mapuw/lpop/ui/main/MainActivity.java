package com.mapuw.lpop.ui.main;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mapuw.lpop.R;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.bean.Status;
import com.mapuw.lpop.config.Constants;
import com.mapuw.lpop.databinding.ActivityMainBinding;
import com.mapuw.lpop.ui.login.LoginActivity;
import com.mapuw.lpop.ui.main.adapter.MainStatusAdapter;
import com.mapuw.lpop.ui.userhome.UserHomeActivity;
import com.mapuw.lpop.utils.AccessTokenKeeper;
import com.mapuw.lpop.utils.RecycleViewDivider;
import com.mapuw.lpop.utils.ToastUtil;
import com.mapuw.lpop.utils.glide.GlideCircleTransform;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainView {

    public static ActivityMainBinding binding;

    private ActionBarDrawerToggle mDrawerToggle;

    private MainPresenter mainPresenter;

    private Oauth2AccessToken mAccessToken;

    private User user;

    private MainStatusAdapter mainStatusAdapter;
    private List<Status> data;
    private int pageNum = 1;
    private int count = 0;
    private long firClick;
    private long secClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void appBarInit() {
        setSupportActionBar(binding.appBarMain.toolbar.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawer,
                binding.appBarMain.toolbar.toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        binding.drawer.addDrawerListener(mDrawerToggle);
        binding.appBarMain.toolbar.toolbar.setNavigationOnClickListener(v -> binding.drawer.openDrawer(Gravity.LEFT));
    }

    @Override
    protected void dataInit() {
        mainPresenter = new MainPresenter(this);

        data = new ArrayList<>();
        mainStatusAdapter = new MainStatusAdapter(this, R.layout.home_weiboitem_original_pictext, data);
        mainStatusAdapter.setEnableLoadMore(true);
        mainStatusAdapter.openLoadAnimation();
        binding.appBarMain.contentMain.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.appBarMain.contentMain.recycler.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        binding.appBarMain.contentMain.recycler.setAdapter(mainStatusAdapter);
        binding.appBarMain.contentMain.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断RecyclerView滑动状态，滑动停止时加载图片
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Glide.with(MainActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Glide.with(MainActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Glide.with(MainActivity.this).pauseRequests();
                        break;
                }
            }
        });

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            mainPresenter.getUserMSG(mAccessToken, new UsersAPI(this, Constants.APP_KEY, mAccessToken));
            mainPresenter.getStatusList(new StatusesAPI(this, Constants.APP_KEY, mAccessToken), 1, binding.appBarMain.appBarMain);
        } else {
            showError("\"Access Token 不存在，请先登录\"");
        }
    }

    @Override
    protected void eventInit() {
        //toolbar单击置顶，双击刷新
        binding.appBarMain.toolbar.toolbar.setOnClickListener(v -> {
            count++;
            if (count == 1) {
                firClick = System.currentTimeMillis();
                binding.appBarMain.contentMain.recycler.scrollToPosition(0);
            } else if (count == 2) {
                secClick = System.currentTimeMillis();
                if (secClick - firClick < 1000) {
                    mainPresenter.getStatusList(new StatusesAPI(this, Constants.APP_KEY, mAccessToken), 1, binding.appBarMain.appBarMain);
                    onRefresh();
                    count = 0;
                } else {
                    count = 1;
                    firClick = System.currentTimeMillis();
                    binding.appBarMain.contentMain.recycler.scrollToPosition(0);
                }
            }

        });
        //抽屉头部点击
        binding.menu.getHeaderView(0).findViewById(R.id.user_heard)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, UserHomeActivity.class);
                    intent.putExtra("USER", this.user);
                    startActivity(intent);
                });
        //刷新
        binding.appBarMain.contentMain.swipeRefresh.setOnRefreshListener(() -> {
            pageNum = 1;
            mainPresenter.getStatusList(new StatusesAPI(MainActivity.this, Constants.APP_KEY, mAccessToken), 1, binding.appBarMain.appBarMain);
        });
        //加载更多
        mainStatusAdapter.setOnLoadMoreListener(() -> {
            pageNum = ++pageNum;
            mainPresenter.getStatusList(new StatusesAPI(MainActivity.this, Constants.APP_KEY, mAccessToken), pageNum, binding.appBarMain.appBarMain);
        });
        //抽屉item点击事件
        binding.menu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_send:
                    AccessTokenKeeper.clear(this);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void updateView(User user) {
        this.user = user;
        ((TextView) binding.menu.getHeaderView(0).findViewById(R.id.username)).setText(user.screen_name);
        Glide.with(this)
                .load(user.avatar_hd)
                .transform(new GlideCircleTransform(this))
                .into((ImageView) binding.menu.getHeaderView(0).findViewById(R.id.user_heard));
        Glide.with(this)
                .load(user.cover_image_phone)
                .into((ImageView) binding.menu.getHeaderView(0).findViewById(R.id.heard_back_ground));
    }

    @Override
    public void updateStatusList(List<Status> data) {
        if (data != null) {
            if (pageNum == 1) {
                this.data.clear();
                this.data.addAll(data);
                mainStatusAdapter.notifyDataSetChanged();
            } else {
                mainStatusAdapter.addData(data);
            }
            if (data.size() < 20) {
                mainStatusAdapter.loadMoreEnd();
            } else {
                mainStatusAdapter.loadMoreComplete();
            }
        } else {
            mainStatusAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onRefresh() {
        binding.appBarMain.contentMain.swipeRefresh.setRefreshing(true);
    }

    @Override
    public void offRefresh() {
        binding.appBarMain.contentMain.swipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            onRefresh();
            pageNum = 1;
            mainPresenter.getStatusList(new StatusesAPI(MainActivity.this, Constants.APP_KEY, mAccessToken), pageNum, binding.appBarMain.appBarMain);
        }
    }
}
