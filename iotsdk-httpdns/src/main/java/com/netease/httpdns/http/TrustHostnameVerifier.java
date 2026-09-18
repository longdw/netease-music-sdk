package com.netease.httpdns.http;

import android.text.TextUtils;
import com.netease.httpdns.log.DNSLog;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class TrustHostnameVerifier implements HostnameVerifier {
    private String bizHost;

    public TrustHostnameVerifier(String bizHost) {
        this.bizHost = bizHost;
    }

    public boolean verify(String hostname, SSLSession session) {
        try {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(this.bizHost, session);
        } catch (Exception e) {
            DNSLog.e(e.getMessage());
            return false;
        }
    }

    public boolean equals(Object o) {
        if (!TextUtils.isEmpty(this.bizHost) && o instanceof TrustHostnameVerifier) {
            String thatHost = ((TrustHostnameVerifier)o).bizHost;
            return TextUtils.isEmpty(thatHost) ? false : this.bizHost.equals(thatHost);
        } else {
            return false;
        }
    }
}