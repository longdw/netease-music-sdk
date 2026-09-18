package com.netease.httpdns.cache;

import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DNSRequestUrl;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class DomainCacheManager implements DNSRequestUrl {
    private static final int INIT_LIST_SIZE = 8;
    private static final Map<String, Map<String, DomainInfo>> DOMAIN_CACHE = new HashMap(8);

    private DomainCacheManager() {
    }

    public static DomainCacheManager getInstance() {
        return Holder.INSTANCE;
    }

    public void clearDomainCache() {
        DOMAIN_CACHE.clear();
    }

    public DomainInfo getDomainCache(String domain) {
        if (TextUtils.isEmpty(domain)) {
            return null;
        } else {
            String netType = NetworkUtil.getNetworkType();
            Map<String, DomainInfo> dnsCache = (Map)DOMAIN_CACHE.get(netType);
            if (dnsCache != null) {
                DomainInfo domainInfo = (DomainInfo)dnsCache.get(domain);
                if (domainInfo != null) {
                    return new DomainInfo(domainInfo);
                }
            }

            return null;
        }
    }

    public synchronized void setDomainInfoCache(Map<String, Map<String, DomainInfo>> newCache) {
        if (newCache != null) {
            DOMAIN_CACHE.clear();
            DOMAIN_CACHE.putAll(newCache);
        }

    }

    public synchronized void addDomainCache(String domain, DomainInfo domainInfo) {
        if (!TextUtils.isEmpty(domain) && domainInfo != null) {
            String netType = domainInfo.getNetworkType();
            Map<String, DomainInfo> dnsCache = (Map)DOMAIN_CACHE.get(netType);
            if (dnsCache == null) {
                dnsCache = new ConcurrentHashMap();
                DOMAIN_CACHE.put(netType, dnsCache);
            }

            dnsCache.remove(domain);
            dnsCache.put(domain, domainInfo);
        }
    }

    public synchronized void finishCurrentDomainScore(String currentNetwork) {
        Map<String, DomainInfo> dnsCache = (Map)DOMAIN_CACHE.get(currentNetwork);
        if (dnsCache != null && !dnsCache.isEmpty()) {
            for(DomainInfo info : dnsCache.values()) {
                if (info != null) {
                    info.setPending(false);
                }
            }

        }
    }

    public synchronized void deleteDomainCache(String domain) {
        if (!TextUtils.isEmpty(domain)) {
            String netType = NetworkUtil.getNetworkType();
            Map<String, DomainInfo> dnsCache = (Map)DOMAIN_CACHE.get(netType);
            if (dnsCache != null) {
                dnsCache.remove(domain);
            }
        }
    }

    public synchronized List<String> updateWhenNetworkChange(String preNetworkType) {
        Set<String> domainSet = new HashSet();
        Map<String, Map<String, DomainInfo>> cacheMap = this.getCacheMap();
        List<String> hotDomains = HttpDnsService.getInstance().getOptions().getHotNameList();
        if (hotDomains != null && !hotDomains.isEmpty()) {
            domainSet.addAll(hotDomains);
        }

        if (cacheMap != null && !cacheMap.isEmpty()) {
            Map<String, DomainInfo> preDomains = (Map)cacheMap.get(preNetworkType);
            Map<String, DomainInfo> nowDomains = (Map)cacheMap.get(NetworkUtil.getNetworkType());
            if (preDomains != null && !preDomains.isEmpty()) {
                domainSet.addAll(preDomains.keySet());
            }

            if (nowDomains != null && !nowDomains.isEmpty()) {
                domainSet.addAll(nowDomains.keySet());
            }
        }

        Iterator<String> iterator = domainSet.iterator();

        while(iterator.hasNext()) {
            String domain = (String)iterator.next();
            if (this.isDomainInBlackList(domain)) {
                iterator.remove();
            }
        }

        return new ArrayList(domainSet);
    }

    public Map<String, Map<String, DomainInfo>> getCacheMap() {
        return DOMAIN_CACHE;
    }

    public boolean isDomainInBlackList(String domain) {
        List<String> blackList = HttpDnsService.getInstance().getOptions().getBlackNameList();
        if (blackList != null && blackList.contains(domain)) {
            DNSLog.i(domain + " is in domainBlackList");
            return true;
        } else if (HttpDnsService.getInstance().getOptions().isDomainNeedFilter(domain)) {
            DNSLog.i(domain + " is in blackListRegexValueSet");
            return true;
        } else {
            return false;
        }
    }

    public void setDomainCache(DomainInfo domainInfo) {
        if (domainInfo != null && !TextUtils.isEmpty(domainInfo.getHost())) {
            DomainInfo cacheInfo = new DomainInfo(domainInfo);
            this.addDomainCache(cacheInfo.getHost(), cacheInfo);
        }
    }

    public Set<String> getDomainFromIp(String ip) {
        try {
            String netType = NetworkUtil.getNetworkType();
            Map<String, DomainInfo> currentCache = (Map)this.getCacheMap().get(netType);
            Set<String> domains = new HashSet(8);
            if (currentCache != null && currentCache.size() != 0) {
                for(String key : currentCache.keySet()) {
                    DomainInfo domainInfo = (DomainInfo)currentCache.get(key);
                    if (domainInfo != null && domainInfo.contains(ip)) {
                        domains.add(domainInfo.getHost());
                    }
                }

                return domains;
            } else {
                return domains;
            }
        } catch (Exception e) {
            DNSLog.e("getDomainFromIp error : " + e.getMessage());
            return Collections.emptySet();
        }
    }

    public List<String> getAdventDomain() {
        String netType = NetworkUtil.getNetworkType();
        Map<String, DomainInfo> currentCache = (Map)this.getCacheMap().get(netType);
        if (currentCache != null && currentCache.size() != 0) {
            List<String> adventDomain = new ArrayList(currentCache.size());

            for(String key : currentCache.keySet()) {
                DomainInfo domainInfo = (DomainInfo)currentCache.get(key);
                if (domainInfo != null && domainInfo.isCacheAdvent()) {
                    adventDomain.add(domainInfo.getHost());
                }
            }

            return adventDomain;
        } else {
            return Collections.emptyList();
        }
    }

    public JSONObject getDomainRequestBody(List<String> domains) {
        if (CollectionUtil.isEmpty(domains)) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject();

            try {
                for(String domain : domains) {
                    DomainInfo domainInfo = this.getDomainCache(domain);
                    if (domainInfo == null) {
                        jsonObject.put(domain, "");
                    } else {
                        String rawJson = domainInfo.getResponseRawJson();
                        jsonObject.put(domain, new JSONObject(rawJson));
                    }
                }
            } catch (Exception e) {
                if (S.LOG.showLog()) {
                    S.LOG.e("[getDomainRequestBody]error: " + e.getMessage());
                }
            }

            return jsonObject;
        }
    }

    private static class Holder {
        private static final DomainCacheManager INSTANCE = new DomainCacheManager();
    }
}