package com.wangxue.log_printer.upload;

import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.dianping.logan.Logan;
import com.dianping.logan.SendLogRunnable;
import com.wangxue.log_printer.api.ISendLog;
import com.wangxue.log_printer.libs.DebugLog;
import com.wangxue.log_printer.libs.HttpsUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

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
        try {
            doNetWork(new FileInputStream(logFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DebugLog.e(TAG, "Log file is not found!");
        }
    }

    /**
     * 发送Log时调用此方法
     * 请不要手动调用{@link #sendLog(File)}
     */
    @Override
    public void doIT() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        String d = dataFormat.format(new Date(System.currentTimeMillis()));
        String[] temp = new String[1];
        temp[0] = d;
        Logan.s(temp, this);
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

    public static class SendLogBuilder {
        /**
         * header集，如 key=Content-Type,value=application/json是一对
         */
        private HashMap<String, String> headers = new HashMap<>();

        /**
         * 请求参数，类似于header集，每个key跟value是一对
         */
        private HashMap<String, String> parameters = new HashMap<>();

        /**
         * https证书，可是使用本项目提供的{@link HttpsUtils}进行生成
         */
        private SSLSocketFactory sslSocketFactory;

        /**
         * 请求地址
         */
        private String url;

        /**
         * 请求方法,只支持 GET 和 POST
         */
        private
        @RequestType
        String method;

        public SendLogBuilder setHeaders(HashMap<String, String> headers) {
            if (headers != null && headers.size() > 0) {
                this.headers = headers;
            }
            return this;
        }

        public SendLogBuilder setParameters(HashMap<String, String> parameters) {
            if (parameters != null && parameters.size() > 0) {
                this.parameters = parameters;
            }
            return this;
        }

        public SendLogBuilder setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public SendLogBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public SendLogBuilder setMethod(@RequestType String method) {
            this.method = method;
            return this;
        }

        public RealSendLogRunnable build() {
            if (this.url == null || this.method == null) {
                throw new SendLogException("please specify method and url");
            }
            return new RealSendLogRunnable(this);
        }


        public static final String GET = "GET";

        public static final String POST = "POST";

        @Retention(RetentionPolicy.SOURCE)
        @Target({ElementType.FIELD, ElementType.PARAMETER})
        @StringDef({GET, POST})
        public @interface RequestType {
        }

        public class SendLogException extends RuntimeException {
            public SendLogException(String msg) {
                super(msg);
            }
        }
    }
}
