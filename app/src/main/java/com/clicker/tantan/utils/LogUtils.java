package com.clicker.tantan.utils;

import android.util.Log;

import com.clicker.tantan.APP;

/**
 * Created by hao on 2017/4/10.
 */

public class LogUtils {
    public static void e(String tag, String msg) {
        if (APP.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (APP.DEBUG) {
            Log.e("info", msg);
        }
    }
}
