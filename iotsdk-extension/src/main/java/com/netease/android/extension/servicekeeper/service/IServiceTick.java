package com.netease.android.extension.servicekeeper.service;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;

public interface IServiceTick<ServiceUniqueId extends IServiceUniqueId> {
    @NonNull
    ServiceUniqueId getUniqueId();

    void onRegister();

    void onUnregister();

    boolean isRegistered();
}