package com.netease.httpdns.downgrade;

import com.netease.httpdns.module.IpEnvironment;

public interface IDowngradeHandler {
    void onDowngrade();

    IpEnvironment getUseIpType();
}