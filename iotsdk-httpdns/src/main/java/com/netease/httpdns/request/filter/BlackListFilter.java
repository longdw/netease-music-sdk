package com.netease.httpdns.request.filter;

import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.List;

public class BlackListFilter implements IDomainFilterHandler {
    private static final String TAG = "[" + BlackListFilter.class.getSimpleName() + "]";

    public void release(List<String> domains) throws Exception {
    }

    public List<String> handler(IChainHandler.Chain<List<String>, List<String>> chain) throws Exception {
        List<String> filterObjList = (List)chain.handlerObj();
        List<String> realRequestDomains = new ArrayList();
        int size = filterObjList.size();
        DnsOptions options = HttpDnsService.getInstance().getOptions();

        for(int i = 0; i < size; ++i) {
            String domain = (String)filterObjList.get(i);
            if (!options.isDomainNeedFilter(domain)) {
                realRequestDomains.add(domain);
            }
        }

        if (S.LOG.showLog()) {
            S.LOG.i(TAG + "after filter : " + realRequestDomains);
        }

        return (List)chain.proceed(realRequestDomains);
    }
}