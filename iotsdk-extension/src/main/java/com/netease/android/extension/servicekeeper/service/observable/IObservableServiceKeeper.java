package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceKeeperException;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;

public interface IObservableServiceKeeper extends IObservableServiceKeeperExport, IServiceKeeper<ObservableServiceUniqueId, IObservableService> {
    <Emit> boolean send(@NonNull ObservableServiceUniqueId<Emit> var1, @Nullable Emit var2) throws SDKServiceKeeperException;

    <Emit> boolean sendOrIgnore(@NonNull ObservableServiceUniqueId<Emit> var1, @Nullable Emit var2);
}