package com.netease.httpdns.request.filter;

import com.netease.android.extension.func.NFunc0;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.ipc.ResultNotifyService;
import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IPCRequestFilter implements IDomainFilterHandler {
    public void release(List<String> domains) throws Exception {
        if (!CollectionUtil.isEmpty(domains)) {
            int size = domains.size();

            for(int i = 0; i < size; ++i) {
                String domain = (String)domains.get(i);

                try {
                    ResultNotifyService.getInstance().release(domain);
                } catch (Exception e) {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[IPCRequestFilter]release exception: " + e.getMessage());
                    }
                }
            }

        }
    }

    public List<String> handler(IChainHandler.Chain<List<String>, List<String>> chain) throws Exception {
        List<String> filterObjList = (List)chain.handlerObj();
        final List<String> realRequestDomains = new ArrayList();
        int size = filterObjList.size();
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        final CountDownLatch countDownLatch = new CountDownLatch(size);

        for(int i = 0; i < size; ++i) {
            int timeout = options == null ? 12000 : options.getHttpTimeOut();
            final String domain = (String)filterObjList.get(i);
            ResultNotifyService.getInstance().tryDomainRequest(domain, timeout, new NFunc0() {
                public void call() {
                    realRequestDomains.add(domain);
                    countDownLatch.countDown();
                }
            }, new NFunc0() {
                public void call() {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            S.LOG.e("[IPCRequestFilter]handler error: " + e.getMessage());
        }

        if (S.LOG.showLog()) {
            S.LOG.i("[IPCRequestFilter]after filter : " + realRequestDomains);
        }

        return (List)chain.proceed(realRequestDomains);
    }
}
