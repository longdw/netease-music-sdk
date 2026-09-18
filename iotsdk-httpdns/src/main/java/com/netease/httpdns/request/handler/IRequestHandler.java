package com.netease.httpdns.request.handler;

import com.netease.httpdns.module.NAHttpEntity;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.DomainRequestTask;
import com.netease.httpdns.request.ServerRequestTask;

public interface IRequestHandler {
    DNSServer requestServerDiscovery(ServerRequestTask var1);

    NAHttpEntity requestDomainResolve(DomainRequestTask var1) throws Exception;
}