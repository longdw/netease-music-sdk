package com.netease.android.extension.servicekeeper.master;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.error.SDKRuntimeException;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.starter.IStarterService;
import com.netease.android.extension.servicekeeper.service.starter.StarterServiceKeeper;
import com.netease.android.extension.servicekeeper.servicestarter.ServiceStarter;
import com.netease.android.extension.usage.NLazy;
import com.netease.android.extension.util.ELog;

public abstract class AbstractServiceKeeperMaster implements IServiceKeeperMaster {
    private static final String SYNCHRONIZE_STRING_PREFIX_STARTER_WORKER_BY_NAME = "703f53e0117edca5dda40d675c0e291e_";
    private NLazy<IServiceKeeperController> rootSKC = new NLazy(new NFunc0R<IServiceKeeperController>() {
        public IServiceKeeperController call() {
            IServiceKeeperMaster currentMaster;
            IServiceKeeperMaster parentMaster;
            for(currentMaster = AbstractServiceKeeperMaster.this; null != (parentMaster = currentMaster.getParentSKM()) && parentMaster != currentMaster; currentMaster = parentMaster) {
            }

            return currentMaster.getLocalSKC();
        }
    });
    @Nullable
    protected IServiceKeeperMaster parentSKM;
    @Nullable
    protected IServiceKeeperController localSKC;

    public void setLocalSKC(@Nullable IServiceKeeperController localSKC) {
        this.localSKC = localSKC;
    }

    public void setParentSKM(@Nullable IServiceKeeperMaster parentSKM) {
        this.parentSKM = parentSKM;
    }

    @Nullable
    public IServiceKeeperMaster getParentSKM() {
        return this.parentSKM;
    }

    @NonNull
    public IServiceKeeperController getRootSKC() {
        return (IServiceKeeperController)this.rootSKC.get();
    }

    @Nullable
    public IServiceKeeperController getLocalSKC() {
        return this.localSKC;
    }

    @NonNull
    public SKCSerial getSKCSerial() {
        if (null == this.localSKC) {
            throw new SDKRuntimeException("[" + this.getClass().getSimpleName() + "]getSKCSerial, localSKC is null ");
        } else {
            return this.localSKC.getSKCSerial();
        }
    }

    public void initialize() {
        if (null != this.localSKC) {
            this.localSKC.initialize();
        }

    }

    public void destroy() {
        if (null != this.localSKC) {
            this.localSKC.destroy();
        }

    }

    @Nullable
    private <Starter extends ServiceStarter> Starter getLocalStarter(@NonNull IServiceUniqueId serviceUniqueId) {
        if (null != this.localSKC) {
            try {
                StarterServiceKeeper serviceKeeper = (StarterServiceKeeper)this.localSKC.getServiceKeeper(ServiceUniqueIdType.STARTER_SERVICE_UNIQUE_ID);
                IStarterService<ServiceStarter> starterService = (IStarterService)serviceKeeper.obtainServiceOrNull(serviceUniqueId.getName());
                if (null != starterService) {
                    return (Starter)starterService.getStarter();
                }
            } catch (Exception e) {
                ELog.e("[getLocalStarterService]error: ", e);
            }
        }

        return null;
    }

    private boolean grabService(@NonNull IServiceKeeperController targetSKC, @NonNull IServiceUniqueId serviceUniqueId) {
        return this.grabService(targetSKC, serviceUniqueId, true);
    }

    private boolean grabService(@NonNull IServiceKeeperController targetSKC, @NonNull IServiceUniqueId serviceUniqueId, boolean autoRunWorkerStart) {
        IServiceTick service = targetSKC.obtainServiceOrNull(serviceUniqueId);
        if (null != service) {
            return false;
        } else {
            ServiceStarter localStarter = this.getLocalStarter(serviceUniqueId);
            if (null == localStarter) {
                return false;
            } else {
                if (ELog.showLog()) {
                    ELog.w("[" + this.getClass().getSimpleName() + "]getEnsureTargetSKC, service not found, then create with localStarter..." + serviceUniqueId);
                }

                String syncFlag = "703f53e0117edca5dda40d675c0e291e_" + serviceUniqueId.getName();
                synchronized(syncFlag.intern()) {
                    service = targetSKC.obtainServiceOrNull(serviceUniqueId);
                    if (null == service) {
                        localStarter.onBeforeCreateService();
                        IServiceTick newService = localStarter.createService(serviceUniqueId);
                        localStarter.onAfterCreateService(newService);

                        boolean var10000;
                        try {
                            localStarter.onBeforeRegisterService(newService);
                            targetSKC.register(newService);
                            if (autoRunWorkerStart) {
                                localStarter.start();
                            }

                            var10000 = true;
                        } catch (SDKServiceAlreadyRegisteredException e) {
                            ELog.e("[" + this.getClass().getSimpleName() + "]getEnsureTargetSKC, register via localStarter error", e);
                            return false;
                        }

                        return var10000;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull IServiceUniqueId<ServiceKeeper> serviceUniqueId) {
        return (ServiceKeeper)this.getTargetSKC(serviceUniqueId).getServiceKeeper(serviceUniqueId);
    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull ServiceUniqueIdType serviceUniqueIdType) {
        throw new SDKRuntimeException("Not supported");
    }

    @Nullable
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull IServiceUniqueId<ServiceKeeper> serviceUniqueId) {
        return (ServiceKeeper)this.getTargetSKC(serviceUniqueId).getServiceKeeperOrNull(serviceUniqueId);
    }

    @Nullable
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull ServiceUniqueIdType serviceUniqueIdType) {
        throw new SDKRuntimeException("Not supported");
    }

    @Nullable
    public IServiceTick obtainServiceOrNull(@NonNull IServiceUniqueId serviceUniqueId) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.obtainServiceOrNull(serviceUniqueId);
    }

    public boolean isServiceExist(@NonNull IServiceUniqueId serviceUniqueId) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        return targetSKC.isServiceExist(serviceUniqueId);
    }

    public <ServiceTick extends IServiceTick> ServiceTick register(@NonNull ServiceTick serviceTick) throws SDKServiceAlreadyRegisteredException {
        return (ServiceTick)this.getTargetSKC(serviceTick.getUniqueId()).register(serviceTick);
    }

    public <ServiceTick extends IServiceTick> ServiceTick unregister(@NonNull ServiceTick serviceTick) {
        return (ServiceTick)this.unregister(serviceTick.getUniqueId());
    }

    public IServiceTick unregister(@NonNull IServiceUniqueId serviceUniqueId) {
        return this.getTargetSKC(serviceUniqueId).unregister(serviceUniqueId);
    }

    public void tryLock(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        targetSKC.tryLock(serviceUniqueId, target, autoReleaseMillis, callBack);
    }

    public void tryLockOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        targetSKC.tryLockOrIgnore(serviceUniqueId, target, autoReleaseMillis, callBack);
    }

    public void release(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        targetSKC.release(serviceUniqueId, target, callBack);
    }

    public void releaseOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        targetSKC.releaseOrIgnore(serviceUniqueId, target, callBack);
    }

    public <Emit extends Parcelable> boolean subscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.subscribe(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean subscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.subscribeOrFalse(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.unsubscribe(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.unsubscribeOrFalse(serviceUniqueId, subscriber);
    }

    public <Emit> boolean subscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.subscribe(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean subscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.subscribeOrFalse(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean unsubscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.unsubscribe(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean unsubscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return targetSKC.unsubscribeOrFalse(serviceUniqueId, serviceSubscriber);
    }

    @NonNull
    public <T> T obtainProxy(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) throws SDKServiceNotFoundException {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return (T)targetSKC.obtainProxy(serviceUniqueId);
    }

    @Nullable
    public <T> T obtainProxyOrNull(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        this.grabService(targetSKC, serviceUniqueId);
        return (T)targetSKC.obtainProxyOrNull(serviceUniqueId);
    }

    public boolean grabRegisterService(@NonNull IServiceUniqueId serviceUniqueId) {
        IServiceKeeperController targetSKC = this.getTargetSKC(serviceUniqueId);
        return this.grabService(targetSKC, serviceUniqueId, false);
    }
}
