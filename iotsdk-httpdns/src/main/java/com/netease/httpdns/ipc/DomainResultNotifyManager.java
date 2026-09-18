package com.netease.httpdns.ipc;

import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCDefaultObservableService;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.cache.DomainCacheManager;
import com.netease.httpdns.listener.MultiHttpDnsListener;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.module.IPCDomainRequest;
import com.netease.httpdns.util.DeviceUtil;
import com.netease.httpdns.util.S;
import com.netease.httpdns.util.S.Service;

public class DomainResultNotifyManager {
    private IPCDefaultObservableService<DomainInfo> domainResultNotifyService;
    private IPCDefaultObservableService<IPCDomainRequest> domainRequestNotifyService;

    public void register() throws Exception {
        this.domainResultNotifyService = new IPCDefaultObservableService(Service.SUID_IPC_OBSERVABLE_DOMAINN_INFO);
        this.domainResultNotifyService.subscribe(new IPCServiceSubscriber<DomainInfo>() {
            public void onReceive(DomainInfo domainInfo, IPCRoute ipcRoute) {
                if (S.LOG.showLog()) {
                    S.LOG.i("[DomainResultNotifyManager]domainResultNotifyService onReceive");
                }

                DomainCacheManager.getInstance().setDomainCache(domainInfo);
            }
        });
        ResultNotifyService.getInstance().getServiceKeeperController().register(this.domainResultNotifyService);
        this.domainRequestNotifyService = new IPCDefaultObservableService(Service.SUID_IPC_OBSERVABLE_DOMAIN_REQUEST);
        this.domainRequestNotifyService.subscribe(new IPCServiceSubscriber<IPCDomainRequest>() {
            public void onReceive(IPCDomainRequest ipcDomainRequest, IPCRoute ipcRoute) {
                if (!DeviceUtil.isUIProcess()) {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[DomainResultNotifyManager]domainRequestNotifyService , not ui process");
                    }

                } else {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[DomainResultNotifyManager]domainRequestNotifyService , receive on ui process");
                    }

                    if (ipcDomainRequest != null) {
                        HttpDnsService.getInstance().getDomainAvailableCache(ipcDomainRequest.getDomains(), (MultiHttpDnsListener)null);
                    }

                }
            }
        });
        ResultNotifyService.getInstance().getServiceKeeperController().register(this.domainRequestNotifyService);
    }

    public void unregister() throws Exception {
        if (null != this.domainResultNotifyService) {
            ResultNotifyService.getInstance().getServiceKeeperController().unregister(this.domainResultNotifyService);
        }

    }

    public void notifyDomainInfo(DomainInfo domainInfo) {
        if (this.domainResultNotifyService != null) {
            this.domainResultNotifyService.sendOrDispatch(domainInfo);
        }

    }

    public void sendDomainRequest(IPCDomainRequest ipcDomainRequest) {
        if (this.domainRequestNotifyService != null) {
            this.domainRequestNotifyService.sendOrDispatch(ipcDomainRequest);
        }

    }

    public IPCDefaultObservableService<DomainInfo> getDomainResultNotifyService() {
        return this.domainResultNotifyService;
    }
}