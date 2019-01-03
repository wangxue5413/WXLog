package com.wangxue.wxlog;

import android.app.Application;

import com.wangxue.log_printer.config.LogConfig;

/**
 * Created by wangxue on 2018/11/22.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogConfig.init(this, BuildConfig.PLANT_DEBUG, null, null, BuildConfig.ENABLE_LOG_TEST);
    }
}
