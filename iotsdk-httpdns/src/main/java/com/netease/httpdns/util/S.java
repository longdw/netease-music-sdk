package com.netease.httpdns.util;

import com.netease.android.extension.log.NLogger;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceUniqueId;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.module.IPCDomainRequest;
import com.netease.httpdns.provider.dal.model.DNSServer;

public class S {
    public static final NLogger LOG = new NLogger();

    public interface Service {
        IPCObservableServiceUniqueId<DNSServer> SUID_IPC_OBSERVABLE_SERVER_ADDRESS = new IPCObservableServiceUniqueId("SUID_IPC_OBSERVABLE_SERVER_ADDRESS");
        IPCObservableServiceUniqueId<DomainInfo> SUID_IPC_OBSERVABLE_DOMAINN_INFO = new IPCObservableServiceUniqueId("SUID_IPC_OBSERVABLE_DOMAINN_INFO");
        IPCObservableServiceUniqueId<IPCDomainRequest> SUID_IPC_OBSERVABLE_DOMAIN_REQUEST = new IPCObservableServiceUniqueId("SUID_IPC_OBSERVABLE_DOMAIN_REQUEST");
        IPCLockServiceUniqueId SUID_IPC_LOCK_HTTP_DNS = new IPCLockServiceUniqueId("HTTP_DNS_IPC_LOCK");
    }
}