package com.netease.httpdns.request.handler;

import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.downgrade.DowngradeManager;
import com.netease.httpdns.http.HttpUtil;
import com.netease.httpdns.listener.RequestStatusListener;
import com.netease.httpdns.module.NAHttpEntity;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.DomainRequestTask;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.request.ServerRequestTask;
import com.netease.httpdns.request.filter.HttpDnsDomainCompositeFilter;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.MD5Util;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.RSASignature;
import com.netease.httpdns.util.S;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequestHandler implements IRequestHandler {
    private static final String TAG = AbstractRequestHandler.class.getSimpleName();
    private static final String SDK_VERSION = "2.0.3";
    private static final int MAX_RETRY_TIME = 2;
    private static final String X_SDK_VERSION = "X-SDK-VERSION";
    private static final String X_SESSION_ID = "X-SESSION-ID";
    private static final String X_OS_TYPE = "X-OS-TYPE";
    private static final String X_OS_TYPE_VALUE = "android";
    public static final String HTTPDNS_HOST = "httpdns.n.netease.com";
    protected static final String PORT_443 = ":443";
    protected static final String PORT_80 = ":80";
    private static final String D_DOMAIN = "/httpdns/v2/d?domain=";
    private static final String V = "/httpdns/v2/s";
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    private static final String GZIP = "gzip";
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HTTPDNS_SOURCE = "httpdns-source";
    private static final String HTTPDNS_SOURCE_NETEASE = "netease";
    private static final String HTTPDNS_NONCE = "httpdns-nonce";
    private static final String HTTPDNS_SIGN = "httpdns-sign";
    private static final String NETEASE_APP = "netease-app";
    private static final String NETEASE_SIGN = "netease-sign";
    private static final String TIMESTAMP = "timestamp";
    private static final String HOST = "Host";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON = "application/json";
    private static final String HTTPDNS_RSA_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIyYR92tGgfA18aQMOWtbkHNE/Y3w/wMsibUFYrz6Wzoh7O/vO1XOdFF40Im1Wm6emfrBksK4WCMLXUqJ21P4CcCAwEAAQ==";

    protected DNSServer requestServerDiscoveryInternal(ServerRequestTask serverRequestTask) {
        if (serverRequestTask == null) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "requestServerDiscovery, serverRequestTask is null !");
            }

            return null;
        } else if (serverRequestTask.getRetryTime() > 2) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "requestServerDiscovery, serverRequestTask is null !");
            }

            return null;
        } else {
            String host = this.getServerDiscoveryHost(serverRequestTask);
            if (TextUtils.isEmpty(host)) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "requestServerDiscovery, getServerDiscoveryHost is null !");
                }

                return null;
            } else {
                String url = "https://" + host + ":443" + "/httpdns/v2/s";
                if (TextUtils.isEmpty(url)) {
                    return null;
                } else {
                    NAHttpEntity httpEntity = HttpUtil.httpRequestWithGet(this.getHostHeader(), url, this.getGenericDnsRequestHeader(), serverRequestTask.getListener());
                    if (!httpEntity.isDnsError() && httpEntity.isSuccess() && !this.isHttpHijacking(httpEntity)) {
                        if (S.LOG.showLog()) {
                            S.LOG.i(TAG + "requestServerDiscovery, 请求成功： " + httpEntity.getResponse());
                        }

                        return new DNSServer(NetworkUtil.getNetworkType(), httpEntity.getResponse(), serverRequestTask.getNetworkEnvironment(), serverRequestTask.getUseIpType());
                    } else {
                        serverRequestTask.addRetryTime();
                        if (S.LOG.showLog()) {
                            S.LOG.i(TAG + "requestServerDiscovery, 请求失败，尝试重新请求，index： " + serverRequestTask.getRetryTime());
                        }

                        this.requestServerDiscoveryInternal(serverRequestTask);
                        return null;
                    }
                }
            }
        }
    }

    protected NAHttpEntity requestDomainResolveInternal(DomainRequestTask domainRequestTask) throws Exception {
        if (domainRequestTask == null) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "requestDomainResolve, domainRequestTask is null !");
            }

            return null;
        } else {
            List<String> realRequestDomains = (List)HttpDnsDomainCompositeFilter.getInstance().handler(domainRequestTask.getDomains());
            if (CollectionUtil.isEmpty(realRequestDomains)) {
                return null;
            } else {
                String url = this.getDomainRequestServerAddress(domainRequestTask);
                if (TextUtils.isEmpty(url)) {
                    return null;
                } else {
                    RequestStatusListener requestStatusListener = HttpDnsService.getInstance().getRequestStatusListener();
                    NAHttpEntity httpEntity = HttpUtil.httpRequestWithGet(this.getHostHeader(), url, this.getDNSResolveRequestHeader(), requestStatusListener);
                    HttpDnsDomainCompositeFilter.getInstance().release(realRequestDomains);
                    if (httpEntity.isDnsError()) {
                        if (S.LOG.showLog()) {
                            S.LOG.i("[requestDomainResolve] isDnsError.");
                        }

                        this.onDnsError();
                        return null;
                    } else if (!httpEntity.isSuccess()) {
                        long currentTime = System.currentTimeMillis();
                        DowngradeManager.getInstance().onDomainRequestFailed(currentTime);
                        return null;
                    } else if (this.isHttpHijacking(httpEntity)) {
                        if (S.LOG.showLog()) {
                            S.LOG.i("[requestDomainResolve] isHttpHijacking.");
                        }

                        this.onHttpHijacking(domainRequestTask.isHttp());
                        return null;
                    } else {
                        return httpEntity;
                    }
                }
            }
        }
    }

    protected void onDnsError() {
    }

    protected void onHttpHijacking(boolean isHttp) {
    }

    private String getDomainRequestServerAddress(DomainRequestTask domainRequestTask) {
        if (domainRequestTask == null) {
            return null;
        } else {
            String totalDomain = this.getTotalDomainString(domainRequestTask.getDomains());
            if (TextUtils.isEmpty(totalDomain)) {
                return null;
            } else {
                boolean http = domainRequestTask.isHttp();
                String scheme = http ? "http://" : "https://";
                String serverAddress = this.getDomainResolveHost(http);
                return TextUtils.isEmpty(serverAddress) ? null : scheme + serverAddress + "/httpdns/v2/d?domain=" + totalDomain;
            }
        }
    }

    private String getTotalDomainString(List<String> domains) {
        if (domains != null && !domains.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < domains.size(); ++i) {
                String domain = this.modifyDomain((String)domains.get(i));
                sb.append(domain).append(",");
            }

            String totalDomain = sb.toString();
            if (TextUtils.isEmpty(totalDomain)) {
                return null;
            } else {
                totalDomain = totalDomain.substring(0, totalDomain.length() - 1);
                return totalDomain;
            }
        } else {
            return null;
        }
    }

    private Map<String, String> getGenericDnsRequestHeader() {
        Map<String, String> header = new HashMap();
        header.put("Accept-Encoding", "gzip");
        header.put("X-SDK-VERSION", "2.0.3");
        header.put("Host", this.getHostHeader());
        header.put("Content-Type", "application/json");
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        long currentTime = System.currentTimeMillis();
        String productKey = options.getProductKey();
        header.put("netease-app", productKey);
        header.put("netease-sign", this.getAuthSign(productKey, currentTime));
        header.put("timestamp", String.valueOf(currentTime));
        return header;
    }

    protected boolean isHttpHijacking(NAHttpEntity httpEntity) {
        if (httpEntity == null) {
            return false;
        } else {
            Map<String, List<String>> headers = httpEntity.getHeaders();
            if (headers != null && !headers.isEmpty()) {
                String response = httpEntity.getResponse();
                if (TextUtils.isEmpty(response)) {
                    return false;
                } else if (!headers.containsKey("httpdns-source")) {
                    return true;
                } else {
                    List<String> sourceValues = (List)headers.get("httpdns-source");
                    if (sourceValues != null && !sourceValues.isEmpty()) {
                        String source = (String)sourceValues.get(sourceValues.size() - 1);
                        if (!TextUtils.equals(source, "netease")) {
                            return true;
                        } else if (!headers.containsKey("httpdns-nonce")) {
                            return true;
                        } else {
                            List<String> nonceValues = (List)headers.get("httpdns-nonce");
                            if (nonceValues != null && !nonceValues.isEmpty()) {
                                String nonce = (String)nonceValues.get(nonceValues.size() - 1);
                                if (TextUtils.isEmpty(nonce)) {
                                    return true;
                                } else if (!headers.containsKey("httpdns-sign")) {
                                    return true;
                                } else {
                                    List<String> signValues = (List)headers.get("httpdns-sign");
                                    if (signValues != null && !signValues.isEmpty()) {
                                        String sign = (String)signValues.get(signValues.size() - 1);
                                        if (TextUtils.isEmpty(sign)) {
                                            return true;
                                        } else {
                                            try {
                                                String content = response + nonce;
                                                return !RSASignature.verify("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIyYR92tGgfA18aQMOWtbkHNE/Y3w/wMsibUFYrz6Wzoh7O/vO1XOdFF40Im1Wm6emfrBksK4WCMLXUqJ21P4CcCAwEAAQ==", content, sign);
                                            } catch (Exception e) {
                                                if (S.LOG.showLog()) {
                                                    S.LOG.i("[isHttpHijacking] error: " + e.getMessage());
                                                }

                                                return false;
                                            }
                                        }
                                    } else {
                                        return true;
                                    }
                                }
                            } else {
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
    }

    public String getAuthSign(String productKey, long timeStamp) {
        String raw = "httpdns-" + productKey + "-" + timeStamp;
        return MD5Util.getMD5(raw);
    }

    private Map<String, String> getDNSResolveRequestHeader() {
        Map<String, String> header = this.getGenericDnsRequestHeader();
        header.put("X-OS-TYPE", "android");
        header.put("X-SESSION-ID", HttpDnsRequestManager.getInstance().getServerDiscoverySessionId());
        return header;
    }

    private String modifyDomain(String domain) {
        if (TextUtils.isEmpty(domain)) {
            return null;
        } else {
            if (domain.contains("http://")) {
                domain = domain.replace("http://", "");
            }

            if (domain.contains("https://")) {
                domain = domain.replace("https://", "");
            }

            return domain;
        }
    }

    protected abstract String getHostHeader();

    protected abstract String getDomainResolveHost(boolean var1);

    protected abstract String getServerDiscoveryHost(ServerRequestTask var1);
}