package com.wangxue.log_printer.libs;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by wangxue on 2019/1/4.
 */
public class SendLogBuilder {
    /**
     * header集，如 key=Content-Type,value=application/json是一对
     */
    protected HashMap<String, String> headers = new HashMap<>();

    /**
     * 请求参数，类似于header集，每个key跟value是一对
     */
    protected HashMap<String, String> parameters = new HashMap<>();

    /**
     * https证书，可是使用本项目提供的{@link HttpsUtils}进行生成
     */
    protected SSLSocketFactory sslSocketFactory;

    /**
     * 请求地址
     */
    protected String url;

    /**
     * 请求方法,只支持 GET 和 POST
     */
    protected
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
