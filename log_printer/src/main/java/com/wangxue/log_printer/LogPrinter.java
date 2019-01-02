package com.wangxue.log_printer;

import timber.log.Timber;

/**
 * Created by wangxue on 2019/1/2.
 */
public class LogPrinter {
    public static void v(String tag, String log, Object... args) {
        Timber.tag(tag);
        Timber.v(log, args);
    }

    public static void d(String tag, String log, Object... args) {
        Timber.tag(tag);
        Timber.d(log, args);
    }

    public static void i(String tag, String log, Object... args) {
        Timber.tag(tag);
        Timber.i(log, args);
    }

    public static void w(String tag, String log, Object... args) {
        Timber.tag(tag);
        Timber.w(log, args);
    }

    public static void e(String tag, String log, Object... args) {
        Timber.tag(tag);
        Timber.e(log, args);
    }
}
