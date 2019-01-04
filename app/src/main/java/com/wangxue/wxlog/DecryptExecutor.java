package com.wangxue.wxlog;

import android.os.AsyncTask;

import com.wangxue.log_printer.LogPrinter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by wangxue on 2019/1/2.
 */
public class DecryptExecutor {

    private File inputFile;

    private File outputFile;

    public DecryptExecutor( File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void parseLog() {
        new DecryptAsyncTask(inputFile, outputFile).execute();
    }

    public static class DecryptAsyncTask extends AsyncTask<String, String, Boolean> {

        private static final String TAG = DecryptAsyncTask.class.getSimpleName();

        private File inputFile;

        private File outputFile;

        public DecryptAsyncTask(File inputFile, File outputFile) {
            this.inputFile = inputFile;
            this.outputFile = outputFile;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean r = false;
            try {
                for (File file : inputFile.listFiles()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile, file.getName() + "_result.log"));
                    new LoganParser(Constants.ENCRYPT_KEY16.getBytes(), Constants.ENCRYPT_IV16.getBytes()).parse(fileInputStream, fileOutputStream);
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
            LogPrinter.i(TAG, "onPostExecute result = " + result);
        }
    }
}
