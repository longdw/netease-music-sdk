package com.netease.android.extension.servicekeeper.service.observable.subscriber.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.observable.IObservableService;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;

public interface IComposeServiceSubscriber<Emit> extends ServiceSubscriber<Emit> {
    void onCancel(boolean var1);

    void onBindObservableService(IObservableService var1);

    void onUnbindObservableService(IObservableService var1);

    @NonNull
    IServiceKeeperMaster getOwnerSKM();
}