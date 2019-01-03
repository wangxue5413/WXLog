package com.wangxue.wxlog;

import android.content.Context;

import java.io.File;

/**
 * Created by wangxue on 2019/1/2.
 */
public class FileUtil {
    public static File getInputFile(Context context) {
        File inputFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_printer");
        if (!inputFile.exists()) inputFile.mkdirs();
        return inputFile;
    }

    public static File getOutPutFile(Context context) {
        File outputFile = new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "log_parse" + File.separator + "outputFile");
        if (!outputFile.exists()) outputFile.mkdirs();
        return outputFile;
    }
}
