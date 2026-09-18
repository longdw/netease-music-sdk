package com.netease.httpdns.request.handler;

import com.netease.httpdns.cache.ServerCacheManager;
import com.netease.httpdns.module.NAHttpEntity;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.DomainRequestTask;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.request.ServerRequestTask;

public class IpDualStackRequestHandler extends AbstractRequestHandler {
    protected String getServerDiscoveryHost(ServerRequestTask serverRequestTask) {
        String localRequestUrl = ServerCacheManager.getInstance().getRequestUrl(true, serverRequestTask.getUsedListIpv6());
        serverRequestTask.addUsedIpv6(localRequestUrl);
        return localRequestUrl;
    }

    public DNSServer requestServerDiscovery(ServerRequestTask serverRequestTask) {
        return this.requestServerDiscoveryInternal(serverRequestTask);
    }

    public NAHttpEntity requestDomainResolve(DomainRequestTask domainRequestTask) throws Exception {
        return this.requestDomainResolveInternal(domainRequestTask);
    }

    protected void onHttpHijacking(boolean isHttp) {
        if (isHttp) {
            HttpDnsRequestManager.getInstance().onHttpHijacking(true);
        }

    }

    protected String getHostHeader() {
        return "httpdns.n.netease.com";
    }

    protected String getDomainResolveHost(boolean isHttp) {
        String host = ServerCacheManager.getInstance().getRequestUrl(true);
        return isHttp ? host + ":80" : host + ":443";
    }
}