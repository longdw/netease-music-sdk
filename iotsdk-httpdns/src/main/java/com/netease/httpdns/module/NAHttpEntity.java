package com.netease.httpdns.module;

import android.text.TextUtils;
import java.util.List;
import java.util.Map;

public class NAHttpEntity {
    public static final int OK_CODE = 0;
    public static final int ERROR_CODE_COMMON = -1;
    public static final int ERROR_CODE_DNS = -2;
    public static final int REQUEST_SUCCESS = 200;
    private String url;
    private int errorCode = 0;
    private String errorMsg;
    private int responseCode;
    private String response;
    private long requestTime;
    private Map<String, List<String>> headers;

    public NAHttpEntity(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NAHttpEntity(String url, String response, int responseCode) {
        this.url = url;
        this.response = response;
        this.responseCode = responseCode;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(this.response) && 200 == this.responseCode;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public boolean isDnsError() {
        return this.errorCode == -2;
    }
}