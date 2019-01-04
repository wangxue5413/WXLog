package com.wangxue.log_printer.libs;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by wangxue on 2019/1/2.
 */
public class DebugTree extends Timber.Tree {

    protected DebugTree() {
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        switch (priority) {
            case Log.VERBOSE:
                DebugLog.v(tag, message);
                break;
            case Log.DEBUG:
                DebugLog.d(tag, message);
                break;
            case Log.INFO:
                DebugLog.i(tag, message);
                break;
            case Log.WARN:
                DebugLog.w(tag, message);
                break;
            case Log.ERROR:
                DebugLog.e(tag, message);
                break;
        }
    }
}
