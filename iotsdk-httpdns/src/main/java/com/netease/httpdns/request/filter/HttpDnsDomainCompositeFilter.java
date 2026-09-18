package com.netease.httpdns.request.filter;

import com.netease.httpdns.request.filter.chainhandler.CompositeHandler;
import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.CollectionUtil;
import java.util.List;

public class HttpDnsDomainCompositeFilter extends CompositeHandler<List<String>, List<String>> implements IDomainCompositeFilter {
    private static volatile HttpDnsDomainCompositeFilter instance;

    public static HttpDnsDomainCompositeFilter getInstance() {
        if (instance == null) {
            synchronized(HttpDnsDomainCompositeFilter.class) {
                if (instance == null) {
                    instance = new HttpDnsDomainCompositeFilter();
                }
            }
        }

        return instance;
    }

    private HttpDnsDomainCompositeFilter() {
        this.addChainHandler(new PreconditionsFilter());
        this.addChainHandler(new IpFilter());
        this.addChainHandler(new BlackListFilter());
        this.addChainHandler(new RequestPoolFilter());
    }

    public void release(List<String> domains) throws Exception {
        if (!CollectionUtil.isEmpty(domains)) {
            for(IChainHandler<List<String>, List<String>> filterHandler : this.getChainHandlers()) {
                ((IDomainFilterHandler)filterHandler).release(domains);
            }

        }
    }
}