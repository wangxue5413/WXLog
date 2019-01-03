package com.wangxue.wxlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wangxue.log_printer.LogPrinter;
import com.wangxue.log_printer.upload.RealSendLogRunnable;

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
        new DecryptExecutor("abcdefghijkmlnop", FileUtil.getInputFile(this.getApplicationContext()), FileUtil.getOutPutFile(this.getApplicationContext())).parseLog();
    }

    private void printLog() {
        for (int i = 0; i < 100; i++) {
            LogPrinter.e(TAG, "this is number %d", i);
        }
    }

    private void uploadLog() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "binary/octet-stream"); //二进制上传
        headers.put("client", "android");
        new RealSendLogRunnable.SendLogBuilder()
                .setHeaders(headers)
                .setMethod(RealSendLogRunnable.SendLogBuilder.POST)
                .setUrl("http://192.168.1.7:3000/logupload")
                .build()
                .doIT();
    }
}
