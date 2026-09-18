package com.netease.httpdns.cache;

import com.netease.httpdns.configuration.DNSRequestUrl;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.module.ServerAddress;
import com.netease.httpdns.module.ServerDiscoveryResponse;
import com.netease.httpdns.provider.dal.manager.DNSServerManager;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ServerCacheManager implements DNSRequestUrl {
    private static final String TAG = "[" + ServerCacheManager.class.getSimpleName() + "]";
    private static final int INIT_LIST_SIZE = 8;
    private static final String SERVER_PREFER_IPV6 = "ipv6";
    private static final String SERVER_PREFER_IPV4 = "ipv4";
    private static final long MAX_CACHE_TIME = 1800000L;
    private static long cacheRefreshTime = -1L;
    private final List<ServerAddress> localServerListIpv4;
    private final List<ServerAddress> localServerListIpv6;
    private final List<ServerAddress> serverListIpv4;
    private final List<ServerAddress> serverListIpv6;
    private String sessionId;
    private long updateTime;
    private boolean useHttp;

    private ServerCacheManager() {
        this.localServerListIpv4 = new ArrayList(8);
        this.localServerListIpv6 = new ArrayList(8);
        this.serverListIpv4 = new ArrayList(8);
        this.serverListIpv6 = new ArrayList(8);
        this.sessionId = "";
        this.updateTime = 0L;
        this.useHttp = false;
        this.initIpv4Address();
        this.initIpv6Address();
    }

    public static ServerCacheManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initIpv4Address() {
        for(String s : REQUEST_URL_IPV4) {
            this.localServerListIpv4.add(new ServerAddress(s, "443"));
        }

    }

    private void initIpv6Address() {
        for(String s : REQUEST_URL_IPV6) {
            this.localServerListIpv6.add(new ServerAddress(s, "443"));
        }

    }

    private synchronized int randomUrlIndex(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    public synchronized String getLocalRequestUrl(boolean isIpv6Request) {
        return isIpv6Request ? this.getLocalServerIpv6() : this.getLocalServerIpv4();
    }

    private String getLocalServerIpv4() {
        int size = this.localServerListIpv4.size();
        if (size == 0) {
            return REQUEST_URL_IPV4[0] + ":" + "443";
        } else {
            int index = this.randomUrlIndex(size);
            ServerAddress address = (ServerAddress)this.localServerListIpv4.get(index);
            return address == null ? REQUEST_URL_IPV4[0] + ":" + "443" : address.getRequestUrl(false);
        }
    }

    private String getLocalServerIpv6() {
        int size = this.localServerListIpv6.size();
        if (size == 0) {
            return "[" + REQUEST_URL_IPV6[0] + "]:" + "443";
        } else {
            int index = this.randomUrlIndex(size);
            ServerAddress address = (ServerAddress)this.localServerListIpv6.get(index);
            return address == null ? "[" + REQUEST_URL_IPV6[0] + "]:" + "443" : address.getRequestUrl(true);
        }
    }

    public synchronized String getRequestUrl(boolean isIpv6) {
        List<ServerAddress> addresses = isIpv6 ? this.serverListIpv6 : this.serverListIpv4;
        int size = addresses.size();
        if (size == 0) {
            return null;
        } else {
            int index = this.randomUrlIndex(size);
            ServerAddress address = (ServerAddress)addresses.get(index);
            return address == null ? null : address.getRequestUrl(isIpv6);
        }
    }

    public synchronized String getRequestUrl(boolean isIpv6Request, List<String> usedUrl) {
        if (CollectionUtil.isEmpty(usedUrl)) {
            return this.getLocalRequestUrl(isIpv6Request);
        } else {
            List<ServerAddress> addresses = isIpv6Request ? this.localServerListIpv6 : this.localServerListIpv4;
            int size = addresses.size();
            if (size == 0) {
                return null;
            } else {
                List<ServerAddress> unusedAddress = new ArrayList();

                for(ServerAddress address : addresses) {
                    if (!usedUrl.contains(address.getRequestUrl(isIpv6Request))) {
                        unusedAddress.add(address);
                    }
                }

                if (unusedAddress.isEmpty()) {
                    return null;
                } else {
                    Collections.shuffle(unusedAddress);
                    ServerAddress address = (ServerAddress)unusedAddress.get(0);
                    if (address == null) {
                        return null;
                    } else {
                        return address.getRequestUrl(isIpv6Request);
                    }
                }
            }
        }
    }

    public synchronized String getSurveyUrl(IpEnvironment ipStackType) {
        String surveyUrl = "";
        switch (ipStackType) {
            case IPV4:
                surveyUrl = this.getIpv4SurveyUrl();
                break;
            case IPV6:
                surveyUrl = this.getIpv6SurveyUrl();
        }

        return surveyUrl;
    }

    private String getIpv4SurveyUrl() {
        int size = this.localServerListIpv4.size();
        if (size == 0) {
            return REQUEST_URL_IPV4[0];
        } else {
            int index = this.randomUrlIndex(size);
            ServerAddress address = (ServerAddress)this.localServerListIpv4.get(index);
            return address == null ? REQUEST_URL_IPV4[0] : address.getUrl();
        }
    }

    private String getIpv6SurveyUrl() {
        int size = this.localServerListIpv6.size();
        if (size == 0) {
            return REQUEST_URL_IPV6[0];
        } else {
            int index = this.randomUrlIndex(size);
            ServerAddress address = (ServerAddress)this.localServerListIpv6.get(index);
            return address == null ? REQUEST_URL_IPV6[0] : address.getUrl();
        }
    }

    public void updateServerCacheFromDataBase() {
        DNSServer dnsServer = DNSServerManager.getInstance().query(NetworkUtil.getNetworkType());
        if (dnsServer == null) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "updateServerCacheFromDataBase, dnsServer is empty.");
            }

        } else {
            long currentTime = System.currentTimeMillis();
            long serverCreatedAt = dnsServer.getCreatedAt();
            if (serverCreatedAt >= this.getCacheRefreshTime() && serverCreatedAt + 1800000L >= currentTime) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "updateServerCacheFromDataBase");
                }

                this.saveDnsServer(dnsServer);
                HttpDnsRequestManager.getInstance().onServerRequestSuccess();
            } else {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "updateServerCacheFromDataBase, dnsServer is old.");
                }

            }
        }
    }

    public synchronized boolean saveDnsServer(DNSServer dnsServer) {
        try {
            if (dnsServer == null) {
                return false;
            } else {
                IpEnvironment ipStackType = dnsServer.getIpEnvironment();
                IpEnvironment useIpType = dnsServer.getUseIpType();
                HttpDnsRequestManager.getInstance().setCurrentIpStackType(ipStackType);
                HttpDnsRequestManager.getInstance().setUseIpType(useIpType);
                if (useIpType == IpEnvironment.DOMAIN) {
                    this.refreshCacheTime();
                    return true;
                } else {
                    String content = dnsServer.getContentJson();
                    ServerDiscoveryResponse response = ServerDiscoveryResponse.parse(content);
                    if (response == null) {
                        if (S.LOG.showLog()) {
                            S.LOG.i("[ServerCacheManager]saveDnsServer, response is null.");
                        }

                        return false;
                    } else {
                        switch (ipStackType) {
                            case IPV4:
                                this.refreshIpv4ServerAddresses(response.getServersIpv4());
                                break;
                            case IPV6:
                                this.refreshIpv6ServerAddresses(response.getServersIpv6());
                                break;
                            case IP_DUAL_STACK:
                                this.refreshIpv4ServerAddresses(response.getServersIpv4());
                                this.refreshIpv6ServerAddresses(response.getServersIpv6());
                        }

                        this.refreshCacheTime();
                        String prefer = response.getPrefer();
                        return true;
                    }
                }
            }
        } catch (Throwable throwable) {
            S.LOG.e(TAG + "saveDnsServer, error: ", throwable);
            return false;
        }
    }

    public synchronized void refreshIpv4ServerAddresses(List<ServerAddress> addresses) {
        if (addresses != null && !addresses.isEmpty()) {
            this.serverListIpv4.clear();
            this.serverListIpv4.addAll(addresses);
        }

    }

    public synchronized void refreshIpv6ServerAddresses(List<ServerAddress> addresses) {
        if (addresses != null && !addresses.isEmpty()) {
            this.serverListIpv6.clear();
            this.serverListIpv6.addAll(addresses);
        }

    }

    public synchronized void clearAllServerAddress() {
        this.clearIpv4ServerAddress();
        this.clearIpv6ServerAddress();
    }

    public synchronized void clearIpv4ServerAddress() {
        this.serverListIpv4.clear();
    }

    public synchronized void clearIpv6ServerAddress() {
        this.serverListIpv6.clear();
    }

    public synchronized List<ServerAddress> getServerListIpv4() {
        return new ArrayList(this.serverListIpv4);
    }

    public synchronized List<ServerAddress> getServerListIpv6() {
        return new ArrayList(this.serverListIpv6);
    }

    public void refreshCacheTime() {
        cacheRefreshTime = System.currentTimeMillis();
    }

    public long getCacheRefreshTime() {
        return cacheRefreshTime;
    }

    private static class Holder {
        private static final ServerCacheManager INSTANCE = new ServerCacheManager();
    }
}