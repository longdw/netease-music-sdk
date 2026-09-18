package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.AbstractServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;

public class ObservableServiceUniqueId<Emit> extends AbstractServiceUniqueId<IObservableServiceKeeper> {
    public ObservableServiceUniqueId() {
    }

    public ObservableServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        super(name, expectServiceUniqueIdScope);
    }

    public ObservableServiceUniqueId(@NonNull String name) {
        super(name);
    }

    public ServiceUniqueIdType getUniqueIdType() {
        return ServiceUniqueIdType.OBSERVABLE_SERVICE_UNIQUE_ID;
    }
}