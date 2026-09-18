package com.netease.android.extension.servicekeeper.master.local;

import androidx.annotation.NonNull;
import com.netease.android.extension.error.SDKRuntimeException;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.master.AbstractServiceKeeperMaster;

public class LocalServiceKeeperMaster extends AbstractServiceKeeperMaster {
    private static final String TAG = LocalServiceKeeperMaster.class.getSimpleName();

    @NonNull
    public IServiceKeeperController getTargetSKC(@NonNull IServiceUniqueId serviceUniqueId) {
        if (null != this.localSKC) {
            return this.localSKC;
        } else {
            throw new SDKRuntimeException("[" + TAG + "]getTargetSKC, localSKC is null.");
        }
    }
}