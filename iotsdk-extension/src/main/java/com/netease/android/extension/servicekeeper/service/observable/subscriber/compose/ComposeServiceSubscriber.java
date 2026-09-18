package com.netease.android.extension.servicekeeper.service.observable.subscriber.compose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.observable.IObservableService;
import com.netease.android.extension.util.ELog;

public abstract class ComposeServiceSubscriber<Emit> implements IComposeServiceSubscriber<Emit> {
    private static final String TAG = ComposeServiceSubscriber.class.getSimpleName();
    @NonNull
    protected IServiceKeeperMaster serviceKeeperMaster;
    @Nullable
    protected IObservableService observableService;

    public ComposeServiceSubscriber(@NonNull IServiceKeeperMaster serviceKeeperMaster) {
        this.serviceKeeperMaster = serviceKeeperMaster;
    }

    public void onCancel(boolean cancelBySelf) {
        IObservableService observableService = this.observableService;
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
            if (newService instanceof IObservableService) {
                if (ELog.showLog()) {
                    ELog.i("[" + TAG + "]resubscribe with new Service");
                }

                ((IObservableService)newService).subscribe(this);
            }

        }
    }

    public void onBindObservableService(IObservableService observableService) {
        this.observableService = observableService;
    }

    public void onUnbindObservableService(IObservableService observableService) {
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