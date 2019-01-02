package com.wangxue.wxlog;

import android.content.Context;

import java.io.File;

/**
 * Created by wangxue on 2019/1/2.
 */
public class FileUtil {
    public static void generateParseDir(Context context) {
        File inputFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_parse" + File.separator + "inputFile");
        File outputFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_parse" + File.separator + "outputFile");
        if(!inputFile.exists()) {
            inputFile.mkdirs();
        }
        if(!outputFile.exists()) {
            outputFile.mkdirs();
        }
    }

    public static File getInputFile(Context context) {
        return new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_parse" + File.separator + "inputFile");
    }

    public static File getOutPutFile(Context context) {
        return new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_parse" + File.separator + "outputFile");
    }
}
