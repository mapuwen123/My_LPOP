package com.mapuw.lpop.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by mapuw on 2017/2/21.
 */

public class SnackUtil {

    private SnackUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    public static Snackbar showShort(Context context, View view, CharSequence message) {
        if (isShow) {
            return Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        }

        return null;
    }

    public static Snackbar showLong(Context context, View view, CharSequence message) {
        if (isShow) {
            return Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        }

        return null;
    }

}
