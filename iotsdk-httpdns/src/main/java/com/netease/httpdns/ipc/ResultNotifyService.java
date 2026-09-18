package com.netease.httpdns.ipc;

import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.func.NFunc0;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.service.ipc.listener.OnIPCConnectListener;
import com.netease.android.extension.usage.NLazy;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.module.IPCDomainRequest;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.util.S;

public class ResultNotifyService {
    private static ResultNotifyService instance;
    private ServerResultNotifyManager serverResultNotifyManager;
    private DomainResultNotifyManager domainResultNotifyManager;
    private LockManager lockManager;
    private static NLazy<HttpDnsServiceKeeperController> serviceKeeperControllerLazy = new NLazy(new NFunc0R<HttpDnsServiceKeeperController>() {
        public HttpDnsServiceKeeperController call() {
            return new HttpDnsServiceKeeperController(HttpDnsService.getInstance().getContext().getApplicationContext(), "SCOPE_UNIQUE_ID_HTTP_DNS_SDK");
        }
    });

    public static ResultNotifyService getInstance() {
        if (instance == null) {
            synchronized(ResultNotifyService.class) {
                if (instance == null) {
                    instance = new ResultNotifyService();
                }
            }
        }

        return instance;
    }

    public void start(OnIPCConnectListener listener) {
        if (S.LOG.showLog()) {
            S.LOG.i("[ResultNotifyService]start");
        }

        this.destroy();
        HttpDnsServiceKeeperController serviceKeeperController = (HttpDnsServiceKeeperController)serviceKeeperControllerLazy.get();
        serviceKeeperController.setOnIPCConnectListener(listener);
        serviceKeeperController.initialize();
        this.serverResultNotifyManager = new ServerResultNotifyManager();
        this.domainResultNotifyManager = new DomainResultNotifyManager();
        this.lockManager = new LockManager();

        try {
            this.serverResultNotifyManager.register();
            this.domainResultNotifyManager.register();
            this.lockManager.start();
        } catch (Exception e) {
            S.LOG.e("[ResultNotifyService]start error : " + e.getMessage());
        }

    }

    public void destroy() {
        try {
            if (this.serverResultNotifyManager != null) {
                this.serverResultNotifyManager.unregister();
            }

            if (this.domainResultNotifyManager != null) {
                this.domainResultNotifyManager.unregister();
            }

            if (this.lockManager != null) {
                this.lockManager.destroy();
            }

            HttpDnsServiceKeeperController serviceKeeperController = (HttpDnsServiceKeeperController)serviceKeeperControllerLazy.get();
            serviceKeeperController.destroy();
        } catch (Exception e) {
            S.LOG.e("[ResultNotifyService]destroy error : " + e.getMessage());
        }

    }

    public IServiceKeeperController getServiceKeeperController() {
        return (IServiceKeeperController)serviceKeeperControllerLazy.get();
    }

    public void notifyServerResult(DNSServer dnsServer) {
        if (ObjectExt.allNonNull(new Object[]{this.serverResultNotifyManager, dnsServer})) {
            this.serverResultNotifyManager.notifyServerAddress(dnsServer);
        }

    }

    public void notifyDomainResult(DomainInfo domainInfo) {
        if (ObjectExt.allNonNull(new Object[]{this.domainResultNotifyManager, domainInfo})) {
            this.domainResultNotifyManager.notifyDomainInfo(domainInfo);
        }

    }

    public void sendDomainRequest(IPCDomainRequest ipcDomainRequest) {
        if (ObjectExt.allNonNull(new Object[]{this.domainResultNotifyManager, ipcDomainRequest})) {
            this.domainResultNotifyManager.sendDomainRequest(ipcDomainRequest);
        }

    }

    public void tryServerRequest(int timeoutMillis, NFunc0 nFuncGetLock, NFunc0 nFuncNoGetLock) {
        if (this.lockManager != null) {
            this.lockManager.tryServerRequest(timeoutMillis, nFuncGetLock, nFuncNoGetLock);
        }

    }

    public void tryDomainRequest(String domain, int timeoutMillis, NFunc0 nFuncGetLock, NFunc0 nFuncNoGetLock) {
        if (this.lockManager != null) {
            this.lockManager.tryDomainRequest(domain, timeoutMillis, nFuncGetLock, nFuncNoGetLock);
        }

    }

    public void release(String domain) {
        if (this.lockManager != null) {
            this.lockManager.release(domain);
        }

    }
}
