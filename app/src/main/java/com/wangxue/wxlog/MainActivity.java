package com.wangxue.wxlog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wangxue.log_printer.LogPrinter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_parse_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseLog();
            }
        });
        findViewById(R.id.btn_print_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    LogPrinter.e("wangxue", "this is number %d", i);
                }
            }
        });
    }

    public void parseLog() {
        new DecryptExecutor("abcdefghijkmlnop", FileUtil.getInputFile(this.getApplicationContext()), FileUtil.getOutPutFile(this.getApplicationContext())).parseLog();
    }
}
