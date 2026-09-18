package com.netease.android.extension.servicekeeper.service;

import androidx.annotation.NonNull;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;

public abstract class AbstractServiceTick<ServiceUniqueId extends IServiceUniqueId> implements IServiceTick<ServiceUniqueId> {
    protected ServiceUniqueId serviceUniqueId;
    private boolean registered;

    public AbstractServiceTick(@NonNull ServiceUniqueId serviceUniqueId) {
        ObjectExt.requireNonNull(serviceUniqueId);
        this.serviceUniqueId = serviceUniqueId;
    }

    public void onRegister() {
        this.registered = true;
    }

    public void onUnregister() {
        this.registered = false;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    @NonNull
    public ServiceUniqueId getUniqueId() {
        return this.serviceUniqueId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbstractServiceTick that = (AbstractServiceTick)o;
            return this.serviceUniqueId.equals(that.serviceUniqueId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ObjectExt.hash(new Object[]{this.serviceUniqueId});
    }
}
