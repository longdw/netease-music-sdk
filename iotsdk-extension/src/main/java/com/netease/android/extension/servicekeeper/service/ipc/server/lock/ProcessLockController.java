package com.netease.android.extension.servicekeeper.service.ipc.server.lock;

import android.os.Bundle;
import android.os.Parcelable;
import com.netease.android.extension.ext.CollectionExt;
import com.netease.android.extension.ext.MapExt;
import com.netease.android.extension.ext.PoolExt;
import com.netease.android.extension.ext.StringExt;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.func.NFunc1R;
import com.netease.android.extension.servicekeeper.service.ipc.parceltype.StringParcel;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessLockController implements IProcessLockController {
    private static final int DEFAULT_TTL = 15000;
    private static final String TAG = ProcessLockController.class.getSimpleName();
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, LockItem<String>>> pendingLocks = new ConcurrentHashMap();
    private final PoolExt.SynchronizedPool<LockItem<String>> lockItemPool = new PoolExt.SynchronizedPool(20, new NFunc0R<LockItem<String>>() {
        public LockItem<String> call() {
            return new LockItem();
        }
    });
    private static final String SYNCHRONIZE_STRING_PREFIX_PROCESS_LOCK_BY_TARGET = "d93165086cac17950237975e853d5116_";

    public boolean tryLock(IPCPack ipcPack) {
        IPCRoute ipcRoute = ipcPack.getIpcRoute();
        SKCSerial skcSerial = ipcRoute.getFromSKCSerial();
        String skcName = skcSerial.getSkcName();
        if (null == skcName) {
            throw new IllegalArgumentException("[" + TAG + "]tryLock, skcName is null !");
        } else {
            Parcelable content = ipcPack.getContent();
            if (!(content instanceof StringParcel)) {
                throw new IllegalArgumentException("[" + TAG + "]tryLock, ipcPack content must instance of StringParcel !");
            } else {
                String target = ((StringParcel)content).getData();
                if (StringExt.isEmpty(target)) {
                    throw new IllegalArgumentException("[" + TAG + "]tryLock, ipcPack content is null !");
                } else {
                    Bundle paramExtra = ipcRoute.getParamExtra();
                    if (null == paramExtra) {
                        throw new IllegalArgumentException("[" + TAG + "]tryLock, ipcPack paramExtra is null !");
                    } else {
                        int ttl = paramExtra.getInt("LOCK__TTL", 15000);
                        String serviceUniqueId = paramExtra.getString("LOCK__SERVICE_UNIQUE_ID");
                        if (StringExt.isBlank(serviceUniqueId)) {
                            throw new IllegalArgumentException("[" + TAG + "]tryLock, ipcPack paramExtra serviceUniqueId is null !");
                        } else {
                            String syncFlag = "d93165086cac17950237975e853d5116_" + target;
                            synchronized(syncFlag.intern()) {
                                ConcurrentHashMap<String, LockItem<String>> lockItems = (ConcurrentHashMap)MapExt.computeIfAbsent(this.pendingLocks, skcName, new NFunc1R<String, ConcurrentHashMap<String, LockItem<String>>>() {
                                    public ConcurrentHashMap<String, LockItem<String>> call(String param) {
                                        return new ConcurrentHashMap();
                                    }
                                });
                                LockItem<String> lockItem = (LockItem)lockItems.get(target);
                                if (null == lockItem) {
                                    lockItems.put(target, lockItem = ((LockItem)this.lockItemPool.acquire()).reset(target));
                                }

                                return lockItem.lock(skcSerial, serviceUniqueId, ttl);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean release(IPCPack ipcPack) {
        IPCRoute ipcRoute = ipcPack.getIpcRoute();
        SKCSerial skcSerial = ipcRoute.getFromSKCSerial();
        String skcName = skcSerial.getSkcName();
        if (null == skcName) {
            throw new IllegalArgumentException("[" + TAG + "]release, skcName is null !");
        } else {
            Parcelable content = ipcPack.getContent();
            if (!(content instanceof StringParcel)) {
                throw new IllegalArgumentException("[" + TAG + "]release, ipcPack content must instance of StringParcel !");
            } else {
                Bundle paramExtra = ipcRoute.getParamExtra();
                if (null == paramExtra) {
                    throw new IllegalArgumentException("[" + TAG + "]release, ipcPack paramExtra is null !");
                } else {
                    String serviceUniqueId = paramExtra.getString("LOCK__SERVICE_UNIQUE_ID");
                    if (StringExt.isBlank(serviceUniqueId)) {
                        throw new IllegalArgumentException("[" + TAG + "]release, ipcPack paramExtra serviceUniqueId is null !");
                    } else {
                        String target = ((StringParcel)content).getData();
                        if (StringExt.isEmpty(target)) {
                            throw new IllegalArgumentException("[" + TAG + "]release, ipcPack content is null !");
                        } else {
                            String syncFlag = "d93165086cac17950237975e853d5116_" + target;
                            synchronized(syncFlag.intern()) {
                                ConcurrentHashMap<String, LockItem<String>> lockItems = (ConcurrentHashMap)this.pendingLocks.get(skcName);
                                if (CollectionExt.isEmpty(lockItems)) {
                                    return false;
                                } else {
                                    LockItem<String> lockItem = (LockItem)lockItems.get(target);
                                    if (null == lockItem) {
                                        return false;
                                    } else {
                                        boolean unlock = lockItem.unlock(skcSerial, serviceUniqueId);
                                        if (unlock) {
                                            lockItems.remove(target);
                                            this.lockItemPool.release(lockItem);
                                        }

                                        return unlock;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}