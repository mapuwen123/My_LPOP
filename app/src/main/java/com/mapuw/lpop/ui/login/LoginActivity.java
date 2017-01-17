package com.mapuw.lpop.ui.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.mapuw.lpop.R;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mapuw.lpop.base.BaseActivity;
import com.mapuw.lpop.config.Constants;
import com.mapuw.lpop.databinding.ActivityLoginBinding;
import com.mapuw.lpop.ui.main.MainActivity;
import com.mapuw.lpop.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class LoginActivity extends BaseActivity implements LoginView {

    private ActivityLoginBinding binding;

    private LoginPresenter loginPresenter;

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }

    @Override
    protected void dataBindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected void appBarInit() {

    }

    @Override
    protected void dataInit() {
        loginPresenter = new LoginPresenter(this);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        } else {
            binding.login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void eventInit() {
        binding.login.setOnClickListener(view -> loginPresenter.doLogining(this, mSsoHandler));
    }

    @Override
    public void updateView() {
        Toast.makeText(this, getString(R.string.weibosdk_demo_toast_auth_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
