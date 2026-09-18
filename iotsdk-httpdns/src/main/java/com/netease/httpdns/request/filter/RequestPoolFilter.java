package com.netease.httpdns.request.filter;

import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestPoolFilter implements IDomainFilterHandler {
    private final Map<String, Long> requestingPool = new HashMap();
    private static final long MAX_CACHE_TIME = 12000L;

    public void release(List<String> domains) throws Exception {
        if (!CollectionUtil.isEmpty(domains)) {
            synchronized(this.requestingPool) {
                for(String domain : domains) {
                    this.requestingPool.remove(domain);
                }

            }
        }
    }

    public List<String> handler(IChainHandler.Chain<List<String>, List<String>> chain) throws Exception {
        List<String> domains = (List)chain.handlerObj();
        List<String> realDomains = new ArrayList();
        synchronized(this.requestingPool) {
            for(String domain : domains) {
                long nowTime = System.currentTimeMillis();
                if (this.requestingPool.containsKey(domain)) {
                    long requestTime = (Long)this.requestingPool.get(domain);
                    if (nowTime - requestTime >= 12000L) {
                        this.requestingPool.put(domain, nowTime);
                        realDomains.add(domain);
                    }
                } else {
                    this.requestingPool.put(domain, nowTime);
                    realDomains.add(domain);
                }
            }
        }

        if (S.LOG.showLog()) {
            S.LOG.i("[RequestPoolFilter]after filter : " + realDomains);
        }

        return realDomains;
    }
}
