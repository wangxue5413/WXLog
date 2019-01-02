package com.wangxue.log_printer.libs;

import com.dianping.logan.Logan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by wangxue on 2019/1/2.
 */
public class ReleaseTree extends Timber.Tree {

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        Logan.w(message, 1);
        Logan.f();
    }
}
