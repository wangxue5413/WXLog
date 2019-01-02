package com.wangxue.wxlog;

import android.os.AsyncTask;

import com.wangxue.log_printer.libs.DebugLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangxue on 2019/1/2.
 */
public class DecryptExecutor {

    private String password;

    private File inputFile;

    private File outputFile;

    public DecryptExecutor(String password, File inputFile, File outputFile) {
        this.password = password;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void parseLog() {
        new DecryptAsyncTask(password, inputFile, outputFile).execute();
    }

    public static class DecryptAsyncTask extends AsyncTask<String, String, Boolean> {


        private String password;

        private File inputFile;

        private File outputFile;

        public DecryptAsyncTask(String password, File inputFile, File outputFile) {
            this.password = password;
            this.inputFile = inputFile;
            this.outputFile = outputFile;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean r = false;
            try {
                for (File file : inputFile.listFiles()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    String d = dataFormat.format(new Date(System.currentTimeMillis()));
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile, d + "_result.log"));
                    new LoganParser(password.getBytes(), password.getBytes()).parse(fileInputStream, fileOutputStream);
                    r = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            DebugLog.i("wangxue", "onPostExecute result = " + result);
        }
    }
}
