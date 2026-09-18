package com.netease.httpdns.ipc;

import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCDefaultObservableService;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.httpdns.cache.ServerCacheManager;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.util.S;
import com.netease.httpdns.util.S.Service;

public class ServerResultNotifyManager {
    private IPCDefaultObservableService<DNSServer> serverResultNotifyService;

    public void register() throws Exception {
        this.serverResultNotifyService = new IPCDefaultObservableService(Service.SUID_IPC_OBSERVABLE_SERVER_ADDRESS);
        this.serverResultNotifyService.subscribe(new IPCServiceSubscriber<DNSServer>() {
            public void onReceive(DNSServer dnsServer, IPCRoute ipcRoute) {
                if (S.LOG.showLog()) {
                    S.LOG.i("[ServerResultNotifyManager]onReceive");
                }

                ServerResultNotifyManager.this.handleServerResult(dnsServer);
            }
        });
        ResultNotifyService.getInstance().getServiceKeeperController().register(this.serverResultNotifyService);
    }

    private void handleServerResult(DNSServer dnsServer) {
        boolean isSuccess = ServerCacheManager.getInstance().saveDnsServer(dnsServer);
        if (isSuccess) {
            HttpDnsRequestManager.getInstance().onServerRequestSuccess();
        }

    }

    public void unregister() throws Exception {
        if (null != this.serverResultNotifyService) {
            ResultNotifyService.getInstance().getServiceKeeperController().unregister(this.serverResultNotifyService);
        }

    }

    public void notifyServerAddress(DNSServer dnsServer) {
        if (this.serverResultNotifyService != null) {
            this.serverResultNotifyService.sendOrDispatch(dnsServer);
        }

    }

    public IPCDefaultObservableService<DNSServer> getServerResultNotifyService() {
        return this.serverResultNotifyService;
    }
}