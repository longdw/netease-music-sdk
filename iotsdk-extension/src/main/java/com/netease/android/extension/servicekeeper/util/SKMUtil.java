package com.netease.android.extension.servicekeeper.util;

import androidx.annotation.Nullable;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;

public final class SKMUtil {
    public static boolean isSameLocalSKC(@Nullable IServiceKeeperMaster skm1, @Nullable IServiceKeeperMaster skm2) {
        if (null != skm1 && null != skm2) {
            IServiceKeeperController skc1 = skm1.getLocalSKC();
            IServiceKeeperController skc2 = skm2.getLocalSKC();
            return isSameSKC(skc1, skc2);
        } else {
            return false;
        }
    }

    public static boolean isSameSKC(@Nullable IServiceKeeperController skc1, @Nullable IServiceKeeperController skc2) {
        return null != skc1 && null != skc2 ? ObjectExt.isEquals(skc1.getSKCSerial(), skc2.getSKCSerial()) : false;
    }
}