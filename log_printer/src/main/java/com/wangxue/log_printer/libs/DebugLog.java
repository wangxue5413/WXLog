package com.wangxue.log_printer.libs;

import android.util.Log;

/**
 * Created by wangxue on 2019/1/2.
 */
public class DebugLog {

    public static boolean ENABLE_PRINT_LOG = false;

    public static void v(String tag, String log) {
        if (ENABLE_PRINT_LOG) {
            Log.v(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (ENABLE_PRINT_LOG) {
            Log.d(tag, log);
        }
    }

    public static void i(String tag, String log) {
        if (ENABLE_PRINT_LOG) {
            Log.i(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (ENABLE_PRINT_LOG) {
            Log.w(tag, log);
        }
    }

    public static void e(String tag, String log) {
        if (ENABLE_PRINT_LOG) {
            Log.e(tag, log);
        }
    }
}
