package com.netease.android.extension.servicekeeper.service.starter;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.AbstractServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.servicestarter.ServiceStarter;

public class StarterServiceUniqueId<Starter extends ServiceStarter> extends AbstractServiceUniqueId<IStarterServiceKeeper> {
    public StarterServiceUniqueId() {
    }

    public StarterServiceUniqueId(@NonNull String name) {
        super(name);
    }

    public StarterServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        super(name, expectServiceUniqueIdScope);
    }

    public ServiceUniqueIdType getUniqueIdType() {
        return ServiceUniqueIdType.STARTER_SERVICE_UNIQUE_ID;
    }
}