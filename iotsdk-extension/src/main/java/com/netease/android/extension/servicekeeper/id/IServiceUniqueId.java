package com.netease.android.extension.servicekeeper.id;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;

public interface IServiceUniqueId<ServiceKeeper extends IServiceKeeper> {
    @NonNull
    String getName();

    ServiceUniqueIdType getUniqueIdType();

    ServiceUniqueIdScope getExpectServiceUniqueIdScope();
}