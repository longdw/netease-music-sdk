package com.netease.android.extension.servicekeeper.service.proxy.compose;

import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyServiceUniqueId;
import com.netease.android.extension.servicekeeper.servicestarter.AbstractServiceStarter;

public abstract class ProxyServiceStarter<Proxy> extends AbstractServiceStarter<ProxyServiceUniqueId<Proxy>, ComposeProxyService<Proxy>> {
    public ProxyServiceStarter(IServiceKeeperMaster serviceKeeperMaster, ProxyServiceUniqueId<Proxy> serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }
}