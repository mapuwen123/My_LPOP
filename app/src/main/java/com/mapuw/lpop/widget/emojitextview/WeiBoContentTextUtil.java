package com.mapuw.lpop.widget.emojitextview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuw.lpop.MyApplication;
import com.mapuw.lpop.R;
import com.mapuw.lpop.bean.ShortUrlBean;
import com.mapuw.lpop.network.RetrofitService;
import com.mapuw.lpop.ui.userhome.UserHomeActivity;
import com.mapuw.lpop.ui.website.WebSiteActivity;
import com.mapuw.lpop.utils.AccessTokenKeeper;
import com.mapuw.lpop.utils.DensityUtil;
import com.mapuw.lpop.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mapuw on 2016/12/20.
 */
public class WeiBoContentTextUtil {

    private static final String AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";// @人
    private static final String TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";// ##话题
    private static final String ALLTEXT = "[全][文][\\s\\S]*[a-zA-z]+://[m][^\\s]*";//全文
    //    private static final String URL = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    private static final String URL = "[http|https]+://[t][^\\s][//a-zA-z0-9$]+";// url
    private static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情

    private static final String ALL = "(" + AT + ")" + "|" +
            "(" + TOPIC + ")" + "|" +
            "(" + ALLTEXT + ")" + "|" +
            "(" + URL + ")" + "|" +
            "(" + EMOJI + ")";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static SpannableStringBuilder getWeiBoContent(String source, final Context context, TextView textView) {
        LogUtil.d("INAGEURL", source);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile(ALL);
        Matcher matcher = pattern.matcher(spannableStringBuilder);

        if (matcher.find()) {
            if (!(textView instanceof EditText)) {
                textView.setMovementMethod(ClickableMovementMethod.getInstance());
                textView.setFocusable(false);
                textView.setClickable(false);
                textView.setLongClickable(false);
            }
            matcher.reset();
        }

        while (matcher.find()) {
            final String at = matcher.group(1);
            final String topic = matcher.group(2);
            final String alltext = matcher.group(3);
            final String url = matcher.group(4);
            final String emoji = matcher.group(5);

            //处理@用户
            if (at != null) {
                int start = matcher.start(1);
                int end = start + at.length();
                WeiBoContentClickableSpan myClickableSpan = new WeiBoContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
//                        Intent intent = new Intent(context, UserHomeActivity.class);
//                        String screen_name = at.substring(1);
//                        intent.putExtra("screenName", screen_name);
//                        context.startActivity(intent);
                        Toast.makeText(context, "点击了用户：" + at, Toast.LENGTH_SHORT).show();
                    }
                };
                spannableStringBuilder.setSpan(myClickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            //处理##话题
            if (topic != null) {
                int start = matcher.start(2);
                int end = start + topic.length();
                WeiBoContentClickableSpan clickableSpan = new WeiBoContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "点击了话题：" + topic, Toast.LENGTH_LONG).show();
                    }
                };
                spannableStringBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            //处理查看全文
            if (alltext != null) {
                LogUtil.d("INAGEURL", alltext);
                int start = matcher.start(3);
                int end = start + alltext.length();
                String alltexts[] = alltext.split("： ");
                WeiBoContentClickableSpan clickableSpan = new WeiBoContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(context, WebSiteActivity.class);
                        intent.putExtra("WEBSITE", alltexts[1]);
                        context.startActivity(intent);
                    }
                };
                spannableStringBuilder.replace(start, end, alltexts[0]);
                spannableStringBuilder.setSpan(clickableSpan, start, start + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            // 处理url地址
            if (url != null) {
//                LogUtil.d("INAGEURL", url);
                int start = matcher.start(4);
                int end = start + url.length();
//                String access_token = AccessTokenKeeper.readAccessToken(context).getToken();
//                Observable<ShortUrlBean> observable = MyApplication.retrofitService.short2LongUrl(
//                        access_token,
//                        url);
//                observable.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                shortUrlBean -> {
//                                    if (shortUrlBean.getUrls().get(0).getType() == 1) {
//
//                                    }
//                                }
//                        );
                Drawable websiteDrawable = context.getResources().getDrawable(R.mipmap.button_web);
                websiteDrawable.setBounds(0, 0, websiteDrawable.getIntrinsicWidth(), websiteDrawable.getIntrinsicHeight());
                ClickableImageSpan imageSpan = new ClickableImageSpan(websiteDrawable, ImageSpan.ALIGN_BOTTOM) {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(context, WebSiteActivity.class);
                        intent.putExtra("WEBSITE", url);
                        context.startActivity(intent);
                    }

                    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                        Drawable b = getDrawable();
                        canvas.save();
                        int transY = bottom - b.getBounds().bottom;
                        transY -= paint.getFontMetricsInt().descent / 2;
                        canvas.translate(x, transY);
                        b.draw(canvas);
                        canvas.restore();
                    }

                };

                spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            //emoji
            if (emoji != null) {
                int start = matcher.start(5);
                int end = start + emoji.length();
                String imgName = Emoticons.getImgName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
                    Drawable emojiDrawable = context.getResources().getDrawable(resId, null);
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtil.sp2px(context, 17), DensityUtil.sp2px(context, 17));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
        }
        return spannableStringBuilder;
    }
}
