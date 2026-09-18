package com.netease.android.extension.servicekeeper.master.scopepreferred;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.error.SDKRuntimeException;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.master.AbstractServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.util.ELog;

public class ScopePreferredServiceKeeperMaster extends AbstractServiceKeeperMaster {
    private static final String TAG = ScopePreferredServiceKeeperMaster.class.getSimpleName();

    @NonNull
    public IServiceKeeperController getTargetSKC(@NonNull IServiceUniqueId serviceUniqueId) {
        ServiceUniqueIdScope expectServiceUniqueIdScope = serviceUniqueId.getExpectServiceUniqueIdScope();
        switch (expectServiceUniqueIdScope) {
            case ROOT:
                IServiceKeeperController rootSKC = this.getRootSKC();
                if (rootSKC == this.localSKC && ELog.showLog()) {
                    ELog.w("[" + TAG + "]getTargetSKC, ExpectServiceUniqueIdScope[" + serviceUniqueId + "] is " + ServiceUniqueIdScope.ROOT + ", but rootSKC == localSKC.");
                }

                return rootSKC;
            case PARENT:
                if (null != this.parentSKM) {
                    IServiceKeeperController parentLocalSKC = this.parentSKM.getLocalSKC();
                    if (null != parentLocalSKC) {
                        return parentLocalSKC;
                    }

                    if (ELog.showLog()) {
                        ELog.w("[" + TAG + "]getTargetSKC, ExpectServiceUniqueIdScope[" + serviceUniqueId + "] is " + ServiceUniqueIdScope.PARENT + ", but parentSKM.localSkc is null, then downgrade to local.");
                    }
                }

                if (ELog.showLog()) {
                    ELog.w("[" + TAG + "]getTargetSKC, ExpectServiceUniqueIdScope[" + serviceUniqueId + "] is " + ServiceUniqueIdScope.PARENT + ", but parentSKM is null, then downgrade to local.");
                }
            case LOCAL:
                if (null != this.localSKC) {
                    return this.localSKC;
                }
            default:
                throw new SDKRuntimeException("[" + TAG + "]getTargetSKC, unknown unique id scope.");
        }
    }

    @Nullable
    public <ServiceTick extends IServiceTick> ServiceTick register(@NonNull ServiceTick serviceTick) throws SDKServiceAlreadyRegisteredException {
        try {
            return (ServiceTick)super.register(serviceTick);
        } catch (SDKServiceAlreadyRegisteredException e) {
            if (ServiceUniqueIdScope.isLocal(serviceTick.getUniqueId().getExpectServiceUniqueIdScope())) {
                throw e;
            } else {
                if (ELog.showLog()) {
                    ELog.w("[" + TAG + "]register fail, service[" + serviceTick.getUniqueId() + "]. If the service is already registered in another scope, ignore this error.");
                }

                return null;
            }
        }
    }
}