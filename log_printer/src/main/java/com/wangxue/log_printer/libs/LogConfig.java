package com.wangxue.log_printer.libs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;

import java.io.File;

import timber.log.Timber;

/**
 * Created by wangxue on 2018/11/22.
 */
public class LogConfig {
    private static final String TAG = LogConfig.class.getSimpleName();

    private static final String LOG_FILE_NAME = "log_printer";

    protected static String LOG_FILE_PATH = null;

    public static void init(@NonNull Context context, boolean isDebug, @NonNull String key16, @NonNull String iv16) {
        init(context, isDebug, key16, iv16, false);
    }

    /**
     * 日志初始化设置
     *
     * @param context
     * @param isDebug    是否为debug版本，是：true ， 否：false；注意：正式版本应当设置为false
     * @param key16      key16（16位字符）
     * @param iv16       iv16（16位字符）
     * @param testEnable 是否开启日志功能测试，正式版本默认关闭
     */
    public static void init(@NonNull Context context, boolean isDebug, @NonNull String key16, @NonNull String iv16, boolean testEnable) {
        if (key16 == null || iv16 == null)
            throw new IllegalArgumentException("The key16 and iv16 must be NoNull!");
        if (isDebug) {
            if (testEnable) {
                initLogConfig(context, key16, iv16, testEnable);
                Timber.plant(new ReleaseTree(testEnable));
            } else {
                enableDebugLog(true);
                Timber.plant(new DebugTree());
            }
        } else {
            enableDebugLog(false);
            initLogConfig(context, key16, iv16, false);
            Timber.plant(new ReleaseTree());
        }
    }

    private static void initLogConfig(Context context, String key16, String iv16, boolean testEnable) {
        LOG_FILE_PATH = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + LOG_FILE_NAME;
        LoganConfig loganConfig = new LoganConfig.Builder()
                .setCachePath(context.getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(LOG_FILE_PATH)
                .setEncryptKey16(key16.getBytes())
                .setEncryptIV16(iv16.getBytes())
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
