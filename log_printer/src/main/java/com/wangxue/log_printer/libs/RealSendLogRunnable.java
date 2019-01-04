package com.wangxue.log_printer.libs;

import android.text.TextUtils;

import com.dianping.logan.Logan;
import com.dianping.logan.SendLogRunnable;
import com.wangxue.log_printer.api.ISendLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by wangxue on 2018/12/29.
 */
public class RealSendLogRunnable extends SendLogRunnable implements ISendLog {

    /**
     * 超时时间 10s
     */
    private static final int TIME_OUT = 10000;

    private static final String TAG = RealSendLogRunnable.class.getSimpleName();

    private SendLogBuilder builder;

    public RealSendLogRunnable(SendLogBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void sendLog(File logFile) {
        if (logFile != null && logFile.exists() && logFile.length() > 0) {
            try {
                doNetWork(new FileInputStream(logFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DebugLog.e(TAG, "Log file is not found!");
            }
        } else {
            DebugLog.e(TAG, "Log file is not exist!");
        }
    }

    /**
     * 发送Log时调用此方法
     * 请不要手动调用{@link #sendLog(File)}
     */
    @Override
    public void doIT() {
        if (LogConfig.LOG_FILE_PATH != null) {
            File dir = new File(LogConfig.LOG_FILE_PATH);
            if (dir.exists() && dir.listFiles().length > 0) {
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                File[] files = dir.listFiles();
                String[] temp = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    DebugLog.i(TAG, "send log $$ files name = " + files[i].getName());
                    temp[i] = dataFormat.format(new Date(Long.parseLong(files[i].getName())));
                }
                Logan.s(temp, this);
            }
        }
    }

    private void doNetWork(InputStream inputData) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        ByteArrayOutputStream bos = null;
        try {
            byte[] buffer = new byte[2048];
            URL url = new URL(builder.url);
            connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                if (builder.sslSocketFactory != null) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(builder.sslSocketFactory);
                } else {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(HttpsUtils.getSslSocketFactory(null, null, null));
                }
            }
            if (builder.headers.size() > 0) {
                for (String key : builder.headers.keySet()) {
                    connection.addRequestProperty(key, builder.headers.get(key));
                }
            }
            if (builder.parameters.size() > 0) {
                for (String key : builder.parameters.keySet()) {
                    connection.addRequestProperty(key, builder.parameters.get(key));
                }
            }
            connection.setConnectTimeout(TIME_OUT);
            connection.setReadTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(builder.method);
            outputStream = connection.getOutputStream();
            int end;
            while ((end = inputData.read(buffer)) != -1) {
                outputStream.write(buffer, 0, end);
            }
            outputStream.flush();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                bos = new ByteArrayOutputStream();
                inputStream = connection.getInputStream();
                while ((end = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, end);
                }
                handleBackData(bos.toByteArray());
            } else {
                DebugLog.e(TAG, "send fail! code = " + responseCode);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputData != null) {
                try {
                    inputData.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void handleBackData(byte[] backData) {
        if (backData != null) {
            try {
                String response = new String(backData, "utf-8");
                if (!TextUtils.isEmpty(response)) {
                    DebugLog.i(TAG, "send success! response result = " + response);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                DebugLog.e(TAG, "response result exception! message = " + e.toString());
            }
        }
    }
}
