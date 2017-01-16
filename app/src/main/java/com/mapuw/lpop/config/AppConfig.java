package com.mapuw.lpop.config;

import com.mapuw.lpop.utils.SDCardUtil;

/**
 * Created by mapuw on 2016/12/12.
 */

public class AppConfig {
    public static final String AppDir = SDCardUtil.getSDCardPath() + "/LPOP";
    public static long CONNECT_TIME_OUT = 30;
    public static String HOST = "https://api.weibo.com/2/";

}
