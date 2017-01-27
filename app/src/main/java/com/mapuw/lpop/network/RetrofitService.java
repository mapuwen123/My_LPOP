package com.mapuw.lpop.network;

import com.mapuw.lpop.bean.ShortUrlBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mapuw on 2017/1/26.
 */

public interface RetrofitService {

    @GET("short_url/expand.json")
    Observable<ShortUrlBean> short2LongUrl(@Query("access_token") String access_token,
                                           @Query("url_short") String url_short);

}
