package com.netease.httpdns.downgrade;

import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.usage.NLazy;
import com.netease.httpdns.module.IpEnvironment;

public class DomainDowngradeHandler extends AbstractDowngradeHandler {
    public static final NLazy<IDowngradeHandler> handlerLazy = new NLazy(new NFunc0R<IDowngradeHandler>() {
        public IDowngradeHandler call() {
            return new DomainDowngradeHandler();
        }
    });

    public IpEnvironment getUseIpType() {
        return IpEnvironment.DOMAIN;
    }
}
