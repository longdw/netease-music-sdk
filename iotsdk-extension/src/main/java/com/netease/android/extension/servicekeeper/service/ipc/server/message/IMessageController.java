package com.netease.android.extension.servicekeeper.service.ipc.server.message;

import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface IMessageController {
    void handleMessage(ConcurrentHashMap<String, Set<ClientBinderWrapper>> var1, String var2, String var3, IPCPack var4);
}