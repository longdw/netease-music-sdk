package com.netease.httpdns.downgrade;

import com.netease.httpdns.request.HttpDnsRequestManager;

public abstract class AbstractDowngradeHandler implements IDowngradeHandler {
    public final void onDowngrade() {
        HttpDnsRequestManager.getInstance().setUseIpType(this.getUseIpType());
    }
}