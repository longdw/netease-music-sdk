package com.netease.httpdns.http;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SDKSNISocketFactory extends SSLSocketFactory {
    private final String TAG = SDKSNISocketFactory.class.getSimpleName();
    private String bizHost;
    private boolean exceptionOccur = false;

    public SDKSNISocketFactory(String bizHost) {
        this.bizHost = bizHost;
    }

    public Socket createSocket() {
        return null;
    }

    public Socket createSocket(String host, int port) {
        return null;
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return null;
    }

    public Socket createSocket(InetAddress host, int port) {
        return null;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return null;
    }

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    public Socket createSocket(Socket plainSocket, String host, int port, boolean autoClose) {
        if (TextUtils.isEmpty(this.bizHost)) {
            return null;
        } else {
            try {
                if (VERSION.SDK_INT < 17) {
                    SSLSessionCache sslSessionCache = new SSLSessionCache(HttpDnsService.getInstance().getContext());
                    SSLCertificateSocketFactory sslSocketFactory = (SSLCertificateSocketFactory)SSLCertificateSocketFactory.getInsecure(10000, sslSessionCache);
                    SSLSocket ssl = (SSLSocket)sslSocketFactory.createSocket(plainSocket, this.bizHost, port, autoClose);
                    ssl.setEnabledProtocols(ssl.getSupportedProtocols());
                    Method setHostnameMethod = ssl.getClass().getMethod("setHostname", String.class);
                    setHostnameMethod.invoke(ssl, this.bizHost);
                    ssl.startHandshake();
                    return ssl;
                } else {
                    SSLSessionCache sessionCache = new SSLSessionCache(HttpDnsService.getInstance().getContext());
                    SSLCertificateSocketFactory sslSocketFactory = (SSLCertificateSocketFactory)SSLCertificateSocketFactory.getInsecure(10000, sessionCache);
                    SSLSocket ssl = (SSLSocket)sslSocketFactory.createSocket(plainSocket, this.bizHost, port, autoClose);
                    sslSocketFactory.setUseSessionTickets(ssl, true);
                    ssl.setEnabledProtocols(ssl.getSupportedProtocols());
                    sslSocketFactory.setHostname(ssl, this.bizHost);
                    ssl.startHandshake();
                    return ssl;
                }
            } catch (Throwable var9) {
                this.exceptionOccur = true;
                return null;
            }
        }
    }

    public boolean equals(Object o) {
        if (!TextUtils.isEmpty(this.bizHost) && o instanceof SDKSNISocketFactory) {
            String thatHost = ((SDKSNISocketFactory)o).bizHost;
            return TextUtils.isEmpty(thatHost) ? false : this.bizHost.equals(thatHost);
        } else {
            return false;
        }
    }
}