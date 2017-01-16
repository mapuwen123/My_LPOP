package com.mapuw.lpop.base;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mapuw.lpop.config.AppConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mapuw on 2016/12/9.
 */

public abstract class BaseRetrofit extends RuntimeException {

    private static final OkHttpClient.Builder mHttpClientBuilder = new OkHttpClient.Builder();
    private static Retrofit mRetrofit;


    public static Retrofit getRetrofit(Context context) {
        if (mRetrofit == null) {

            //设定30秒超时,拦截http请求进行监控重写或重试,打印网络请求
            mHttpClientBuilder.connectTimeout(AppConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
//                    .interceptors().add(
//                    new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request originalRequest = chain.request();
//
//                            String cacheHeaderValue = isOnline(context) ?
//                                    "public, max-age=2419200" : "public, only-if-cached, max-stale=2419200" ;
//
//                            Request request = originalRequest.newBuilder().build();
//                            Response response = chain.proceed(request);
//                            return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
//                                    .header("Cache-Control", cacheHeaderValue).build();
//                        }
//                    }
//            ).

//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request request = chain.request();
//                            HttpUrl url = request.url().newBuilder().addQueryParameter(MovieDbApi.PARAM_API_KEY, BuildConfig.MOVIE_DB_API_KEY).build();
//                            request = request.newBuilder().url(url).build();
//
//                            return chain.proceed(request);
//                        }
//                    })


//                    .networkInterceptors().add(new PersistentCookieStore(context))

//
                    .addInterceptor(interceptor);

//                    .addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request()
//                            .newBuilder()
//                            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                            .addHeader("Accept-Encoding", "gzip, deflate")
//                            .addHeader("Connection", "keep-alive")
//                            .addHeader("Accept", "*/*")
//                            .addHeader("Cookie", "add cookies here")
//                            .build();
//                    return chain.proceed(request);
//                }

//            })
//                    .interceptors().add(new CookiesInterceptor(context).REWRITE_CACHE_CONTROL_INTERCEPTOR)

//                    .cache(new Cache( new File(context.getCacheDir()
//                            .getAbsolutePath(), AppConfig.OKHTTP_CACHE_DIR), AppConfig.MAX_CACHE_SIZE_INBYTES))
//                    .interceptors().add(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            OkHttpClient mOkHttpClient = mHttpClientBuilder.build();


            //构建Retrofit
            mRetrofit = new Retrofit.Builder()//配置服务器路径
                    .baseUrl(AppConfig.HOST)
                    //配置转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //配置回调库，采用RxJava
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //设置OKHttpClient为网络客户端
                    .client(mOkHttpClient)
                    .build();
        }
        return mRetrofit;
    }


    static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=60";
            }
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };

}
