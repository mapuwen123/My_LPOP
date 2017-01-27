package com.mapuw.lpop.bean;

import java.util.List;

/**
 * Created by mapuw on 2017/1/26.
 */

public class ShortUrlBean {

    private List<UrlsBean> urls;

    public List<UrlsBean> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlsBean> urls) {
        this.urls = urls;
    }

    public static class UrlsBean {
        /**
         * url_short : http://t.cn/h4DwT1
         * url_long : http://finance.sina.com.cn/
         * type : 0
         * result : true
         */

        private String url_short;
        private String url_long;
        private int type;
        private String result;

        public String getUrl_short() {
            return url_short;
        }

        public void setUrl_short(String url_short) {
            this.url_short = url_short;
        }

        public String getUrl_long() {
            return url_long;
        }

        public void setUrl_long(String url_long) {
            this.url_long = url_long;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
