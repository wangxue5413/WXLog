package com.wangxue.wxlog;

import android.app.Application;

import com.wangxue.log_printer.libs.LogConfig;

/**
 * Created by wangxue on 2018/11/22.
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogConfig.init(this, BuildConfig.PLANT_DEBUG, Constants.ENCRYPT_KEY16, Constants.ENCRYPT_IV16, BuildConfig.ENABLE_LOG_TEST);
    }
}
