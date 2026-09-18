package com.netease.android.extension.servicekeeper.service.ipc.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.util.ELog;

public class IPCCommunicationAndroidService extends Service {
    private static final String TAG = IPCCommunicationAndroidService.class.getSimpleName();
    private IPCServer.Stub ipcServerBinder = new IPCServerBinder();

    public static void bindServerService(Context applicationContext, ServiceConnection serviceConnection, int flag) {
        Intent intent = new Intent(applicationContext, IPCCommunicationAndroidService.class);
        applicationContext.bindService(intent, serviceConnection, flag);
    }

    public static void unbindServerService(Context applicationContext, ServiceConnection serviceConnection) {
        applicationContext.unbindService(serviceConnection);
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]onBind, intent: " + intent);
        }

        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]onBind, pid: " + Process.myPid());
        }

        return this.ipcServerBinder;
    }

    public boolean onUnbind(Intent intent) {
        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]onUnbind, intent: " + intent);
        }

        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]onUnbind, pid: " + Process.myPid());
        }

        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]onRebind, intent: " + intent);
        }

    }
}