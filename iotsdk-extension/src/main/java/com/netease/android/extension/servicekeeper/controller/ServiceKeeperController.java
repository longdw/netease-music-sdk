package com.netease.android.extension.servicekeeper.controller;

import android.content.Context;
import android.os.Parcelable;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.ext.CollectionExt;
import com.netease.android.extension.ext.StringExt;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.error.SDKServiceKeeperNotFoundException;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;
import com.netease.android.extension.servicekeeper.service.ipc.client.IPCClientBinder;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.listener.OnIPCConnectListener;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IIPCObservableServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.servicekeeper.service.observable.IObservableServiceKeeper;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceKeeper;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.proxy.IProxyServiceKeeper;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyServiceKeeper;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.starter.StarterServiceKeeper;
import com.netease.android.extension.usage.NLazy;
import com.netease.android.extension.util.ELog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceKeeperController implements IServiceKeeperController, OnIPCConnectListener {
    @NonNull
    private Context applicationContext;
    @NonNull
    private SKCSerial skcSerial;
    private final NLazy<IIPCClientBinder> ipcClientBinder;
    private ConcurrentHashMap<ServiceUniqueIdType, NLazy<IServiceKeeper>> serviceKeepers;

    public ServiceKeeperController(@NonNull Context applicationContext) {
        this(applicationContext, StringExt.randomUUIDString());
    }

    public ServiceKeeperController(@NonNull Context applicationContext, @NonNull String skcName) {
        this(applicationContext, new SKCSerial(applicationContext.getPackageName(), skcName, Process.myPid()));
    }

    private ServiceKeeperController(@NonNull Context applicationContext, @NonNull SKCSerial skcSerial) {
        this.ipcClientBinder = new NLazy(new NFunc0R<IIPCClientBinder>() {
            public IIPCClientBinder call() {
                IPCClientBinder ipcClientBinder = new IPCClientBinder(ServiceKeeperController.this.applicationContext, ServiceKeeperController.this.skcSerial);
                ipcClientBinder.addOnIPCConnectedListener(ServiceKeeperController.this);
                ipcClientBinder.initialize();
                return ipcClientBinder;
            }
        });
        this.serviceKeepers = new ConcurrentHashMap();
        this.applicationContext = applicationContext.getApplicationContext();
        this.skcSerial = skcSerial;
    }

    public void initialize() {
        this.serviceKeepers.clear();
        this.initializeServiceTypes(ServiceUniqueIdType.STARTER_SERVICE_UNIQUE_ID, new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                return new StarterServiceKeeper();
            }
        });
        this.initializeServiceTypes(ServiceUniqueIdType.PROXY_SERVICE_UNIQUE_ID, new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                return new ProxyServiceKeeper();
            }
        });
        this.initializeServiceTypes(ServiceUniqueIdType.OBSERVABLE_SERVICE_UNIQUE_ID, new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                return new ObservableServiceKeeper();
            }
        });
        this.initializeServiceTypes(ServiceUniqueIdType.IPC_OBSERVABLE_SERVICE_UNIQUE_ID, new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                return new IPCObservableServiceKeeper((IIPCClientBinder)ServiceKeeperController.this.ipcClientBinder.get());
            }
        });
        this.initializeServiceTypes(ServiceUniqueIdType.IPC_LOCK_UNIQUE_ID, new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                return new IPCLockServiceKeeper((IIPCClientBinder)ServiceKeeperController.this.ipcClientBinder.get());
            }
        });
    }

    @NonNull
    public SKCSerial getSKCSerial() {
        return this.skcSerial;
    }

    @Nullable
    public IServiceTick obtainServiceOrNull(@NonNull IServiceUniqueId serviceUniqueId) {
        IServiceKeeper serviceKeeper = this.getServiceKeeperOrNull(serviceUniqueId);
        return null == serviceKeeper ? null : serviceKeeper.obtainServiceOrNull(serviceUniqueId);
    }

    public boolean isServiceExist(@NonNull IServiceUniqueId serviceUniqueId) {
        return null != this.obtainServiceOrNull(serviceUniqueId);
    }

    protected void initializeServiceTypes(@NonNull ServiceUniqueIdType serviceUniqueIdType, @NonNull final NFunc0R<IServiceKeeper> createServiceKeeperFunc) {
        this.serviceKeepers.put(serviceUniqueIdType, new NLazy(new NFunc0R<IServiceKeeper>() {
            public IServiceKeeper call() {
                IServiceKeeper serviceKeeper = (IServiceKeeper)createServiceKeeperFunc.call();
                serviceKeeper.injectServiceKeeperController(ServiceKeeperController.this);
                serviceKeeper.initialize();
                return serviceKeeper;
            }
        }));
    }

    public void onIPCConnected() {
    }

    public void onIPCAttached() {
    }

    public void onIPCDetached() {
    }

    public void onIPCDisconnected() {
    }

    public <ServiceTick extends IServiceTick> ServiceTick register(@NonNull ServiceTick serviceTick) throws SDKServiceAlreadyRegisteredException {
        return (ServiceTick)this.getServiceKeeper(serviceTick.getUniqueId()).register(serviceTick);
    }

    public <ServiceTick extends IServiceTick> ServiceTick unregister(@NonNull ServiceTick serviceTick) {
        return (ServiceTick)this.getServiceKeeper(serviceTick.getUniqueId()).unregister(serviceTick);
    }

    public IServiceTick unregister(@NonNull IServiceUniqueId serviceUniqueId) {
        return this.getServiceKeeper(serviceUniqueId).unregister(serviceUniqueId);
    }

    public void destroy() {
        if (!CollectionExt.isEmpty(this.serviceKeepers)) {
            for(Map.Entry<ServiceUniqueIdType, NLazy<IServiceKeeper>> lazyEntry : this.serviceKeepers.entrySet()) {
                NLazy<IServiceKeeper> value = (NLazy)lazyEntry.getValue();

                try {
                    if (!value.isTargetNull()) {
                        IServiceKeeper serviceKeeper = (IServiceKeeper)value.get();
                        serviceKeeper.destroy();
                    }
                } catch (Throwable throwable) {
                    ELog.e("[ServiceKeeperController]cleanAll, error service unique id type[" + lazyEntry.getKey() + "]: ", throwable);
                }
            }

            this.serviceKeepers.clear();
        }

        if (!this.ipcClientBinder.isTargetNull()) {
            ((IIPCClientBinder)this.ipcClientBinder.get()).destroy();
            this.ipcClientBinder.clear();
        }

    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull IServiceUniqueId<ServiceKeeper> serviceUniqueId) {
        return (ServiceKeeper)this.getServiceKeeper(serviceUniqueId.getUniqueIdType());
    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperInternal(@NonNull IServiceUniqueId<ServiceKeeper> serviceUniqueId) {
        return (ServiceKeeper)this.getServiceKeeper(serviceUniqueId.getUniqueIdType());
    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull ServiceUniqueIdType serviceUniqueIdType) {
        NLazy<IServiceKeeper> serviceKeeperNLazy = (NLazy)this.serviceKeepers.get(serviceUniqueIdType);
        if (null == serviceKeeperNLazy) {
            throw new SDKServiceKeeperNotFoundException("[ServiceKeeperController]getServiceKeeper, ServiceKeeper not found with UniqueId type " + serviceUniqueIdType);
        } else {
            return (ServiceKeeper)(serviceKeeperNLazy.get());
        }
    }

    @NonNull
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperInternal(@NonNull ServiceUniqueIdType serviceUniqueIdType) {
        return (ServiceKeeper)this.getServiceKeeper(serviceUniqueIdType);
    }

    @Nullable
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull IServiceUniqueId<ServiceKeeper> serviceUniqueId) {
        return (ServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId.getUniqueIdType());
    }

    @Nullable
    public <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull ServiceUniqueIdType serviceUniqueIdType) {
        NLazy<IServiceKeeper> serviceKeeperNLazy = (NLazy)this.serviceKeepers.get(serviceUniqueIdType);
        return (ServiceKeeper)(null == serviceKeeperNLazy ? null : (IServiceKeeper)serviceKeeperNLazy.get());
    }

    @NonNull
    public <T> T obtainProxy(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) throws SDKServiceNotFoundException {
        return (T)((IProxyServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId)).obtainProxy(serviceUniqueId);
    }

    @Nullable
    public <T> T obtainProxyOrNull(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) {
        IProxyServiceKeeper serviceKeeper = (IProxyServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        return (T)(null == serviceKeeper ? null : serviceKeeper.obtainProxyOrNull(serviceUniqueId));
    }

    public <Emit> boolean subscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        return ((IObservableServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId)).subscribe(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean subscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IObservableServiceKeeper serviceKeeper = (IObservableServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        return null != serviceKeeper && serviceKeeper.subscribeOrFalse(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean unsubscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        return ((IObservableServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId)).unsubscribe(serviceUniqueId, serviceSubscriber);
    }

    public <Emit> boolean unsubscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IObservableServiceKeeper serviceKeeper = (IObservableServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        return null != serviceKeeper && serviceKeeper.unsubscribeOrFalse(serviceUniqueId, serviceSubscriber);
    }

    public <Emit extends Parcelable> boolean subscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        return ((IIPCObservableServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId)).subscribe(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean subscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IIPCObservableServiceKeeper serviceKeeper = (IIPCObservableServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        return null != serviceKeeper && serviceKeeper.subscribeOrFalse(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        return ((IIPCObservableServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId)).unsubscribe(serviceUniqueId, subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IIPCObservableServiceKeeper serviceKeeper = (IIPCObservableServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        return null != serviceKeeper && serviceKeeper.unsubscribeOrFalse(serviceUniqueId, subscriber);
    }

    public void tryLock(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException {
        IPCLockServiceKeeper serviceKeeper = (IPCLockServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId);
        serviceKeeper.tryLock(serviceUniqueId, target, autoReleaseMillis, callBack);
    }

    public void tryLockOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) {
        IPCLockServiceKeeper serviceKeeper = (IPCLockServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        if (null != serviceKeeper) {
            serviceKeeper.tryLockOrIgnore(serviceUniqueId, target, autoReleaseMillis, callBack);
        }

    }

    public void release(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) throws SDKServiceNotFoundException {
        IPCLockServiceKeeper serviceKeeper = (IPCLockServiceKeeper)this.getServiceKeeperInternal(serviceUniqueId);
        serviceKeeper.release(serviceUniqueId, target, callBack);
    }

    public void releaseOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) {
        IPCLockServiceKeeper serviceKeeper = (IPCLockServiceKeeper)this.getServiceKeeperOrNull(serviceUniqueId);
        if (null != serviceKeeper) {
            serviceKeeper.releaseOrIgnore(serviceUniqueId, target, callBack);
        }

    }
}