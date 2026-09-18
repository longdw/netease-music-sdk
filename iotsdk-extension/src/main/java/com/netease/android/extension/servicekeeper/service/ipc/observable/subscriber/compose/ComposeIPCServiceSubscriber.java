package com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.compose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IIPCObservableService;
import com.netease.android.extension.util.ELog;

public abstract class ComposeIPCServiceSubscriber<Emit> implements IComposeIPCServiceSubscriber<Emit> {
    private static final String TAG = ComposeIPCServiceSubscriber.class.getSimpleName();
    protected IServiceKeeperMaster serviceKeeperMaster;
    @Nullable
    protected IIPCObservableService observableService;

    public ComposeIPCServiceSubscriber(IServiceKeeperMaster serviceKeeperMaster) {
        this.serviceKeeperMaster = serviceKeeperMaster;
    }

    public void onCancel(boolean cancelBySelf) {
        IIPCObservableService observableService = this.observableService;
        if (null == observableService) {
            ELog.e("[" + TAG + "]onCancel, observableService is null.");
        } else {
            IServiceUniqueId serviceUniqueId = observableService.getUniqueId();
            observableService.unsubscribe(this);
            this.onUnbindObservableService(observableService);
            if (!cancelBySelf) {
                this.tryResubscribe(serviceUniqueId);
            }

        }
    }

    private void tryResubscribe(IServiceUniqueId serviceUniqueId) {
        IServiceTick newService = this.serviceKeeperMaster.obtainServiceOrNull(serviceUniqueId);
        if (null == newService) {
            ELog.w("[" + TAG + "]onCancel, newService not found");
        } else {
            if (newService instanceof IIPCObservableService) {
                if (ELog.showLog()) {
                    ELog.i("[" + TAG + "]resubscribe with new Service");
                }

                ((IIPCObservableService)newService).subscribe(this);
            }

        }
    }

    public void onBindObservableService(IIPCObservableService observableService) {
        this.observableService = observableService;
    }

    public void onUnbindObservableService(IIPCObservableService observableService) {
        if (this.observableService == observableService) {
            this.observableService = null;
        } else {
            ELog.e("[" + TAG + "]onUnbindObservableService, No permission to unbind.");
        }

    }

    @NonNull
    public IServiceKeeperMaster getOwnerSKM() {
        return this.serviceKeeperMaster;
    }
}