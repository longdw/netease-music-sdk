package com.netease.android.extension.servicekeeper.master;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.IServiceTick;

public interface IServiceKeeperMaster extends IServiceKeeperController {
    void setParentSKM(@Nullable IServiceKeeperMaster var1);

    void setLocalSKC(@Nullable IServiceKeeperController var1);

    @NonNull
    IServiceKeeperController getTargetSKC(@NonNull IServiceUniqueId var1);

    @Nullable
    IServiceKeeperMaster getParentSKM();

    @NonNull
    IServiceKeeperController getRootSKC();

    @Nullable
    IServiceKeeperController getLocalSKC();

    @Nullable
    <ServiceTick extends IServiceTick> ServiceTick register(@NonNull ServiceTick var1) throws SDKServiceAlreadyRegisteredException;

    boolean grabRegisterService(@NonNull IServiceUniqueId var1);
}
