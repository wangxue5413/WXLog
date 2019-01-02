package com.wangxue.log_printer.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;
import com.wangxue.log_printer.libs.DebugLog;
import com.wangxue.log_printer.libs.DebugTree;
import com.wangxue.log_printer.libs.ReleaseTree;

import java.io.File;

import timber.log.Timber;

/**
 * Created by wangxue on 2018/11/22.
 */
public class LogConfig {
    private static final String TAG = LogConfig.class.getSimpleName();

    public static final String LOG_FILE_NAME = "log_printer";

    private static final String ENCRYPT_PASSWORD = "abcdefghijkmlnop";

    /**
     * 日志初始化设置
     *
     * @param context
     * @param isDebug     是否为debug版本，是：true ， 否：false；注意：正式版本应当设置为false
     * @param logFileName log文件存储位置的父目录名称（不带扩展名），默认值为{@link #LOG_FILE_NAME}
     * @param encryptPwd  日志加密密码（16位字符），默认值为{@link #ENCRYPT_PASSWORD}
     * @param testEnable  是否开启日志功能测试，正式版本默认关闭
     */
    public static void init(@NonNull Context context, boolean isDebug, String logFileName, String encryptPwd, boolean testEnable) {
        if (isDebug) {
            if(testEnable) {
                initLogConfig(context, logFileName, encryptPwd, testEnable);
                Timber.plant(new ReleaseTree());
            } else {
                enableDebugLog(true);
                Timber.plant(new DebugTree());
            }
        } else {
            enableDebugLog(false);
            initLogConfig(context, logFileName, encryptPwd, false);
            Timber.plant(new ReleaseTree());
        }
    }

    private static void initLogConfig(Context context, String logFileName, String encryptPwd, boolean testEnable) {
        LoganConfig loganConfig = new LoganConfig.Builder()
                .setCachePath(context.getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + (TextUtils.isEmpty(logFileName) ? LOG_FILE_NAME : logFileName))
                .setEncryptKey16(TextUtils.isEmpty(encryptPwd) ? ENCRYPT_PASSWORD.getBytes() : encryptPwd.getBytes())
                .setEncryptIV16(TextUtils.isEmpty(encryptPwd) ? ENCRYPT_PASSWORD.getBytes() : encryptPwd.getBytes())
                .build();
        Logan.init(loganConfig);
        if (testEnable) {
            enableDebugLog(testEnable);
            Logan.setDebug(testEnable);
            Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
                @Override
                public void loganProtocolStatus(String cmd, int code) {
                    DebugLog.i(TAG, "clogan > cmd : " + cmd + " | " + "code : " + code);
                }
            });
        }
    }

    private static void enableDebugLog(boolean enable) {
        DebugLog.ENABLE_PRINT_LOG = enable;
    }
}
