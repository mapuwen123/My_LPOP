package com.mapuw.lpop.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mapuw.lpop.R;
import com.mapuw.lpop.utils.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by mapuw on 2016/12/13.
 */

public class LoginModel {

    public void doLogin(SsoHandler ssoHandler, WeiboAuthListener weiboAuthListener) {
        ssoHandler.authorize(weiboAuthListener);
    }

    public void saveToken(Context context, Bundle values, LoginPresenter.onSaveTokenListener onSaveToken) {
        Oauth2AccessToken mAccessToken = new Oauth2AccessToken();
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        //从这里获取用户输入的 电话号码信息
        String  phoneNum =  mAccessToken.getPhoneNum();
        if (mAccessToken.isSessionValid()) {
            // 显示 Token
//            updateTokenView(false);

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(context, mAccessToken);
            onSaveToken.success();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = context.getString(R.string.weibosdk_demo_toast_auth_failed);
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            onSaveToken.error(message);
        }
    }

}
