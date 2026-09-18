package com.netease.httpdns.http;

import android.text.TextUtils;
import com.netease.httpdns.log.DNSLog;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpDnsHostnameVerifier implements HostnameVerifier {
    private String bizHost;

    public HttpDnsHostnameVerifier(String bizHost) {
        this.bizHost = bizHost;
    }

    public boolean verify(String hostname, SSLSession session) {
        try {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return defaultHostnameVerifier.verify(this.bizHost, session) || defaultHostnameVerifier.verify("httpdns.n.netease.com", session);
        } catch (Exception e) {
            DNSLog.e(e.getMessage());
            return false;
        }
    }

    public boolean equals(Object o) {
        if (!TextUtils.isEmpty(this.bizHost) && o instanceof HttpDnsHostnameVerifier) {
            String thatHost = ((HttpDnsHostnameVerifier)o).bizHost;
            return TextUtils.isEmpty(thatHost) ? false : this.bizHost.equals(thatHost);
        } else {
            return false;
        }
    }
}