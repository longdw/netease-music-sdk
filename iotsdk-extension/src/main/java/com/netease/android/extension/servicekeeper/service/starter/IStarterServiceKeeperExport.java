package com.netease.android.extension.servicekeeper.service.starter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;

public interface IStarterServiceKeeperExport {
    @NonNull
    <T> T obtainStarter(String var1) throws SDKServiceNotFoundException;

    @Nullable
    <T> T obtainStarterOrNull(String var1);
}