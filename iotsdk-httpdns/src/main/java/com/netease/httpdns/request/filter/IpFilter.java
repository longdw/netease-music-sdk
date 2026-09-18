package com.netease.httpdns.request.filter;

import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.IpTypeUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.List;

public class IpFilter implements IDomainFilterHandler {
    private static final String TAG = "[" + IpFilter.class.getSimpleName() + "]";

    public void release(List<String> domains) throws Exception {
    }

    public List<String> handler(IChainHandler.Chain<List<String>, List<String>> chain) throws Exception {
        List<String> filterObjList = (List)chain.handlerObj();
        List<String> realRequestDomains = new ArrayList();
        int size = filterObjList.size();

        for(int i = 0; i < size; ++i) {
            String domain = (String)filterObjList.get(i);
            if (!IpTypeUtil.isIpv4(domain) && !IpTypeUtil.isIpv6(domain)) {
                realRequestDomains.add(domain);
            }
        }

        if (S.LOG.showLog()) {
            S.LOG.i(TAG + "after filter : " + realRequestDomains);
        }

        return (List)chain.proceed(realRequestDomains);
    }
}