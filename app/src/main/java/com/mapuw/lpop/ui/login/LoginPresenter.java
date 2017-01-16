package com.mapuw.lpop.ui.login;

import android.content.Context;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by mapuw on 2016/12/14.
 */

public class LoginPresenter implements WeiboAuthListener {

    private LoginModel loginModel;
    private LoginView loginView;
    private Context context;

    public LoginPresenter(LoginView loginView) {
        this.loginModel = new LoginModel();
        this.loginView = loginView;
    }

    public interface onSaveTokenListener {
        void success();
        void error(String msg);
    }

    public void doLogining(Context context, SsoHandler ssoHandler) {
        this.context = context;
        loginModel.doLogin(ssoHandler, this);
    }

    @Override
    public void onComplete(Bundle bundle) {
        loginModel.saveToken(this.context, bundle, new onSaveTokenListener() {
            @Override
            public void success() {
                loginView.updateView();
            }

            @Override
            public void error(String msg) {
                loginView.showError(msg);
            }
        });
    }

    @Override
    public void onWeiboException(WeiboException e) {
        loginView.showError(e.getMessage());
    }

    @Override
    public void onCancel() {
        loginView.showError("授权取消");
    }
}
