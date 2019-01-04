package com.wangxue.log_printer.libs;

import android.util.Log;

import com.dianping.logan.Logan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by wangxue on 2019/1/2.
 */
public class ReleaseTree extends Timber.Tree {

    private boolean testEnable = false;

    protected ReleaseTree() {
    }

    protected ReleaseTree(boolean testEnable) {
        this.testEnable = testEnable;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        Logan.w("TAG:" + tag + " => " + message, 1);
        Logan.f();
        if (testEnable) {
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
}
