package com.netease.android.extension.servicekeeper.servicestarter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.error.SDKRuntimeException;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.starter.StarterService;
import com.netease.android.extension.servicekeeper.service.starter.StarterServiceUniqueId;
import com.netease.android.extension.util.ELog;

public abstract class AbstractServiceStarter<ServiceUniqueId extends IServiceUniqueId, ServiceTick extends IServiceTick<ServiceUniqueId>> implements ServiceStarter<ServiceUniqueId, ServiceTick> {
    protected IServiceKeeperMaster serviceKeeperMaster;
    protected final ServiceUniqueId serviceUniqueId;
    private StarterService starterService;
    @Nullable
    protected ServiceTick serviceTick;

    public AbstractServiceStarter(IServiceKeeperMaster serviceKeeperMaster, ServiceUniqueId serviceUniqueId) {
        this.serviceKeeperMaster = serviceKeeperMaster;
        this.serviceUniqueId = serviceUniqueId;
    }

    public final void initialize() throws SDKServiceAlreadyRegisteredException {
        IServiceKeeperController localSKC = this.serviceKeeperMaster.getLocalSKC();
        if (null == localSKC) {
            throw new SDKRuntimeException("[" + this.getClass().getSimpleName() + "]local SKC is null.");
        } else {
            localSKC.register(this.starterService = new StarterService(new StarterServiceUniqueId(this.serviceUniqueId.getName()), this));
            this.tryGrabService();
        }
    }

    public final ServiceTick createService(ServiceUniqueId serviceUniqueId) {
        this.serviceTick = this.create(serviceUniqueId);
        return this.serviceTick;
    }

    @NonNull
    protected abstract ServiceTick create(ServiceUniqueId var1);

    private void tryGrabService() {
        boolean grabSuccess = this.serviceKeeperMaster.grabRegisterService(this.serviceUniqueId);
        if (ELog.showLog()) {
            if (grabSuccess) {
                ELog.i("[" + this.getClass().getSimpleName() + "]tryGrabService, grab success.");
            } else {
                ELog.i("[" + this.getClass().getSimpleName() + "]tryGrabService, grab miss.");
            }
        }

    }

    public final void destroy() {
        IServiceKeeperController localSKC = this.serviceKeeperMaster.getLocalSKC();
        if (null != localSKC && null != this.starterService) {
            localSKC.unregister(this.starterService);
        }

        ServiceTick serviceTick = this.serviceTick;
        if (null != serviceTick) {
            this.serviceKeeperMaster.unregister(serviceTick);
            this.serviceTick = null;
        }

        this.onDestroy();
    }

    public final void start() {
        if (null != this.serviceTick) {
            this.onStart();
        }

    }

    protected void onStart() {
    }

    protected void onDestroy() {
    }

    public void onBeforeCreateService() {
    }

    public void onAfterCreateService(ServiceTick serviceTick) {
    }

    public void onBeforeRegisterService(ServiceTick serviceTick) {
    }

    public void onAfterRegisterService(ServiceTick serviceTick) {
    }
}