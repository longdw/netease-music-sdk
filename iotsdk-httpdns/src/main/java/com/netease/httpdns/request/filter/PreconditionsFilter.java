package com.netease.httpdns.request.filter;

import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import java.util.Collections;
import java.util.List;

public class PreconditionsFilter implements IDomainFilterHandler {
    public void release(List<String> domains) throws Exception {
    }

    public List<String> handler(IChainHandler.Chain<List<String>, List<String>> chain) throws Exception {
        if (!HttpDnsService.getInstance().isSDKStart()) {
            if (S.LOG.showLog()) {
                S.LOG.i("[HttpDnsService]handlerMultiHttpDNS, sdk还未初始化。");
            }

            return Collections.emptyList();
        } else if (!HttpDnsRequestManager.getInstance().ifCanRequestDomain()) {
            return Collections.emptyList();
        } else if (NetworkUtil.isWeakNetwork()) {
            if (S.LOG.showLog()) {
                S.LOG.i("[PreconditionsFilter]handler, 当前处于弱网。");
            }

            return Collections.emptyList();
        } else {
            return (List)chain.proceed(chain.handlerObj());
        }
    }
}
