package com.mapuw.lpop.widget.webview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.mapuw.lpop.R;
import com.tencent.smtt.sdk.WebView;

public class ProgressWebView extends WebView {

	private ProgressBar progressbar;
	
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg, null));
        addView(progressbar);
	}
	
	@Override
	public void super_onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.width = l;
        lp.height = t;
        progressbar.setLayoutParams(lp);
		super.super_onScrollChanged(l, t, oldl, oldt);
	}

    /**
     * 获取webview进度条
     * @return （ProgressBar）
     */
    public ProgressBar getProgressbar() {
        return this.progressbar;
    }
	
//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
//        lp.width = l;
//        lp.height = t;
//        progressbar.setLayoutParams(lp);
//		super.onScrollChanged(l, t, oldl, oldt);
//	}

}
