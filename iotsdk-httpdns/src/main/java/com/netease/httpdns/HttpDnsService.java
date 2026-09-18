package com.netease.httpdns;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.func.NFunc1;
import com.netease.android.extension.servicekeeper.service.ipc.listener.OnIPCConnectListener;
import com.netease.httpdns.cache.DomainCacheManager;
import com.netease.httpdns.cache.IpStackTypeListener;
import com.netease.httpdns.cache.ServerCacheManager;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.configuration.ThreadPool;
import com.netease.httpdns.configuration.DnsOptions.Builder;
import com.netease.httpdns.ipc.ResultNotifyService;
import com.netease.httpdns.listener.MultHttpDnsListener;
import com.netease.httpdns.listener.MultiHttpDnsListener;
import com.netease.httpdns.listener.RequestStatusListener;
import com.netease.httpdns.listener.SingleHttpDnsListener;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.provider.dal.DNSCacheOpenHelper;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.score.ScoreSort;
import com.netease.httpdns.score.socketScore.DetectIpsListener;
import com.netease.httpdns.score.socketScore.RttScoreManager;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.NetworkMonitor;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpDnsService implements OnIPCConnectListener {
    private static final String TAG = "[" + HttpDnsService.class.getSimpleName() + "]";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static boolean isFirstStart = true;
    private static boolean isSDKStart = false;
    private static HttpDnsService instance;
    private RequestStatusListener listener;
    private DnsOptions options;
    private String preNetworkType = "";
    private String currentNetworkType = "";
    private Context context;
    private NetworkMonitor monitor;

    private HttpDnsService() {
    }

    public static HttpDnsService getInstance() {
        if (instance == null) {
            synchronized(HttpDnsService.class) {
                if (instance == null) {
                    instance = new HttpDnsService();
                }
            }
        }

        return instance;
    }

    public void init(Context context, DnsOptions options) {
        try {
            if (context != null) {
                this.context = context.getApplicationContext();
                if (options != null) {
                    this.options = options;
                } else if (this.options == null) {
                    this.options = Builder.createSimpler();
                }

                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "options: " + this.options.toString());
                }

                if (this.options.getCustomSort() == null && this.options.isOpenScore()) {
                    ScoreSort.getInstance().init(context);
                }

                (new DNSCacheOpenHelper(this.context)).init();
                ResultNotifyService.getInstance().start(this);
                this.monitor = new NetworkMonitor();
                this.monitor.start(context);
                isSDKStart = true;
            }
        } catch (Exception e) {
            S.LOG.e(TAG + "init, error: ", e);
        }

    }

    public void uninit() {
        ResultNotifyService.getInstance().destroy();
        this.stopNetworkMonitor();
        isSDKStart = false;
    }

    public void onIPCConnected() {
    }

    public void onIPCAttached() {
        ThreadPool.submit(new Runnable() {
            public void run() {
                HttpDnsRequestManager.getInstance().detectNetworkEnv();
            }
        });
    }

    public void onIPCDetached() {
    }

    public void onIPCDisconnected() {
    }

    public boolean isSDKStart() {
        return isSDKStart;
    }

    public void init(Context context) {
        this.init(context, (DnsOptions)null);
    }

    public void setRequestStatusListener(RequestStatusListener listener) {
        if (listener != null) {
            this.listener = listener;
        }

    }

    public void removeRequestStatusListener() {
        this.listener = null;
    }

    public RequestStatusListener getRequestStatusListener() {
        return this.listener;
    }

    public DnsOptions getOptions() {
        if (this.options == null) {
            this.options = Builder.createSimpler();
        }

        return this.options;
    }

    public Context getContext() {
        return this.context;
    }

    public void setOptions(DnsOptions option) {
        if (option != null) {
            this.options = option;
        }

    }

    public void clearDNSCache() {
        DomainCacheManager.getInstance().clearDomainCache();
    }

    public void clearServerCache() {
        ServerCacheManager.getInstance().clearIpv4ServerAddress();
        ServerCacheManager.getInstance().clearIpv6ServerAddress();
    }

    public void stopNetworkMonitor() {
        if (this.monitor != null) {
            this.monitor.stop();
        }

    }

    public String getSingleIpByAsync(String domain, SingleHttpDnsListener listener) {
        List<String> ips = this.getDomainServerIpList(domain, listener);
        String ip = null;
        if (!CollectionUtil.isEmpty(ips)) {
            ip = (String)ips.get(0);
        }

        return ip;
    }

    public Map<String, DomainInfo> getDomainAvailableCache(List<String> domainList, final MultiHttpDnsListener listener) {
        if (CollectionUtil.isEmpty(domainList)) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "getSingleIpByAsync domainList isEmpty.");
            }

            return Collections.emptyMap();
        } else {
            final Map<String, DomainInfo> availableCacheMap = new HashMap();
            final Set<String> needRequestDomainSet = new HashSet();
            this.DomainResolvePreprocessing(domainList, new NFunc1<DomainInfo>() {
                public void call(DomainInfo domainInfo) {
                    if (domainInfo != null) {
                        availableCacheMap.put(domainInfo.getHost(), domainInfo);
                    }

                }
            }, new NFunc1<String>() {
                public void call(String domain) {
                    needRequestDomainSet.add(domain);
                }
            });
            final DnsOptions dnsOptions = this.getOptions();
            if (!CollectionUtil.isEmpty(needRequestDomainSet)) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "needRequestDomainSet is :" + needRequestDomainSet.toString());
                }

                ThreadPool.submit(new Runnable() {
                    public void run() {
                        if (dnsOptions != null && dnsOptions.isRefreshExpiringCache()) {
                            List<String> adventDomainList = DomainCacheManager.getInstance().getAdventDomain();
                            if (!CollectionUtil.isEmpty(adventDomainList)) {
                                needRequestDomainSet.addAll(adventDomainList);
                            }
                        }

                        List<DomainInfo> domainResultList = HttpDnsRequestManager.getInstance().handlerMultiHttpDNS(new ArrayList(needRequestDomainSet));
                        Map<String, DomainInfo> finishedDomainInfo = new HashMap(availableCacheMap);
                        if (!CollectionUtil.isEmpty(domainResultList)) {
                            for(DomainInfo info : domainResultList) {
                                if (info != null && !TextUtils.isEmpty(info.getHost())) {
                                    if (dnsOptions != null && dnsOptions.isDomainNeedMergeLocalDNS(info.getHost())) {
                                        info.mergeLocalDNSResult();
                                    }

                                    finishedDomainInfo.put(info.getHost(), info);
                                }
                            }
                        }

                        if (listener != null) {
                            listener.onIpsParsed(finishedDomainInfo);
                        }

                    }
                });
            }

            return availableCacheMap;
        }
    }

    private void DomainResolvePreprocessing(@Nullable List<String> domainList, @NonNull NFunc1<DomainInfo> onAvailableCacheReceive, @NonNull NFunc1<String> onNeedRequestReceive) {
        if (CollectionUtil.isEmpty(domainList)) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "getDomainDispatchEntity domainList isEmpty.");
            }

        } else {
            DnsOptions options = this.getOptions();

            for(String domain : domainList) {
                if (TextUtils.isEmpty(domain)) {
                    if (S.LOG.showLog()) {
                        S.LOG.i(TAG + "getSingleIpByAsync domain :" + domain + "isEmpty.");
                    }
                } else if (DomainCacheManager.getInstance().isDomainInBlackList(domain)) {
                    if (S.LOG.showLog()) {
                        S.LOG.i(TAG + "getSingleIpByAsync domain :" + domain + "isDomainInBlackList.");
                    }
                } else {
                    DomainInfo domainInfoCache = DomainCacheManager.getInstance().getDomainCache(domain);
                    if (domainInfoCache != null && !domainInfoCache.isUseless() && !domainInfoCache.isCacheExpires()) {
                        if (domainInfoCache.isWaiting()) {
                            if (S.LOG.showLog()) {
                                S.LOG.i(TAG + "DomainResolvePreprocessing domain :" + domain + " isWaiting.");
                            }
                        } else {
                            if (options.isDomainNeedMergeLocalDNS(domain)) {
                                domainInfoCache.mergeLocalDNSResult();
                            }

                            onAvailableCacheReceive.call(domainInfoCache);
                        }
                    } else {
                        if (S.LOG.showLog()) {
                            S.LOG.i(TAG + "DomainResolvePreprocessing domain :" + domain + " 加到待请求队列中.");
                        }

                        onNeedRequestReceive.call(domain);
                    }
                }
            }

        }
    }

    public List<String> getIpListByAsync(String domain, SingleHttpDnsListener listener) {
        return this.getDomainServerIpList(domain, listener);
    }

    private List<String> getDomainServerIpList(final String domain, final SingleHttpDnsListener listener) {
        String tempDomain = this.modifyDomain(domain);
        List<String> ips = new ArrayList();
        List<String> domainList = new ArrayList();
        domainList.add(tempDomain);
        Map<String, DomainInfo> domainInfoMap = this.getDomainAvailableCache(domainList, new MultiHttpDnsListener() {
            public void onIpsParsed(Map<String, DomainInfo> infoMap) {
                List<String> ipsAsync = new ArrayList();
                if (infoMap != null && infoMap.size() > 0) {
                    DomainInfo domainInfo = (DomainInfo)infoMap.get(domain);
                    if (domainInfo != null) {
                        ipsAsync = domainInfo.getAvailableIps();
                    }

                    if (listener != null) {
                        if (CollectionUtil.isEmpty(ipsAsync)) {
                            listener.onIpsParsed((List)null);
                            listener.onIpParsed((String)null);
                        } else {
                            listener.onIpsParsed(ipsAsync);
                            listener.onIpParsed((String)ipsAsync.get(0));
                        }
                    }
                }

            }
        });
        if (domainInfoMap != null && domainInfoMap.size() > 0) {
            DomainInfo domainInfo = (DomainInfo)domainInfoMap.get(domain);
            if (domainInfo != null) {
                ips = domainInfo.getAvailableIps();
                if (S.LOG.showLog()) {
                    S.LOG.i("[getDomainServerIpList] 获取有效缓存, domain: " + domain + "ips: " + ips);
                }
            }
        }

        return ips;
    }

    public List<DomainInfo> getMultIpsWithAsync(List<String> domains, final MultHttpDnsListener listener) {
        if (domains != null && !domains.isEmpty()) {
            List<DomainInfo> domainInfoList = new ArrayList();
            Map<String, DomainInfo> domainInfoMap = this.getDomainAvailableCache(domains, new MultiHttpDnsListener() {
                public void onIpsParsed(Map<String, DomainInfo> infoMap) {
                    if (listener != null && infoMap != null && infoMap.size() > 0) {
                        listener.onIpsParsed(new ArrayList(infoMap.values()));
                    }

                }
            });
            if (domainInfoMap != null && domainInfoMap.size() > 0) {
                domainInfoList.addAll(new ArrayList(domainInfoMap.values()));
            }

            return domainInfoList;
        } else {
            return Collections.emptyList();
        }
    }

    public Set<String> getDomainFromIp(String ip) {
        return DomainCacheManager.getInstance().getDomainFromIp(ip);
    }

    public void updateCached() {
        String networkType = NetworkUtil.getNetworkType();
        if (isFirstStart) {
            isFirstStart = false;
            this.currentNetworkType = networkType;
        } else if (!TextUtils.isEmpty(networkType) && !networkType.equals(this.currentNetworkType)) {
            DNSLog.i("networkType : " + networkType + "  preNetworkType : " + this.currentNetworkType);
            this.preNetworkType = this.currentNetworkType;
            this.currentNetworkType = networkType;
            this.finishCurrentScore(this.preNetworkType);
            ServerCacheManager.getInstance().clearAllServerAddress();
            HttpDnsRequestManager.getInstance().onNetChange();
        } else {
            DNSLog.i("networkType : " + networkType + "，当前无网或和上次网络相同");
        }
    }

    private void finishCurrentScore(String netType) {
        if (!isFirstStart) {
            RttScoreManager.getInstance().finishCurrentScore(netType);
        }

    }

    public void refreshPreDomain() {
        final List<String> preDomainList = DomainCacheManager.getInstance().updateWhenNetworkChange(this.preNetworkType);
        ThreadPool.submit(new Runnable() {
            public void run() {
                HttpDnsRequestManager.getInstance().handlerMultiHttpDNS(preDomainList);
            }
        });
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

    public void setIpStackTypeListener(IpStackTypeListener listener) {
        HttpDnsRequestManager.getInstance().setIpStackTypeListener(listener);
    }

    public void removeIpStackTypeListener() {
        HttpDnsRequestManager.getInstance().removeIpStackTypeListener();
    }

    public void setDetectIpsListener(DetectIpsListener listener) {
        RttScoreManager.getInstance().setDetectIpsListener(listener);
    }

    public void removeDetectIpsListener() {
        RttScoreManager.getInstance().removeDetectIpsListener();
    }

    public void detectIps(List<String> ips, DetectIpsListener listener) {
        RttScoreManager.getInstance().detectIps(ips, listener);
    }
}