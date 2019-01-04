package com.wangxue.wxlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wangxue.log_printer.LogPrinter;
import com.wangxue.log_printer.libs.SendLogBuilder;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_parse_log).setOnClickListener((view) -> parseLog());
        findViewById(R.id.btn_print_log).setOnClickListener((view) -> printLog());
        findViewById(R.id.btn_upload_log).setOnClickListener((view) -> uploadLog());
    }

    private void parseLog() {
        new DecryptExecutor(FileUtil.getInputFile(this.getApplicationContext()), FileUtil.getOutPutFile(this.getApplicationContext())).parseLog();
    }

    private void printLog() {
        for (int i = 0; i < 100; i++) {
            LogPrinter.e(TAG, "this is number %d", i);
        }
    }

    private void uploadLog() {
        if (BuildConfig.ENABLE_LOG_TEST || !BuildConfig.PLANT_DEBUG) {
            String url = "http://192.168.138.30:8080/test/logupload";//换成自己的url
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/octet-stream"); //二进制流
            headers.put("client", "android");
            new SendLogBuilder()
                    .setHeaders(headers)
                    .setMethod(SendLogBuilder.POST)
                    .setUrl(url)
                    .build()
                    .doIT();
        }
    }
}
