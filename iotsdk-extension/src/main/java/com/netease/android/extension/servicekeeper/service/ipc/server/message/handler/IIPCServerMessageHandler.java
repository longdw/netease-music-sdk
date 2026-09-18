package com.netease.android.extension.servicekeeper.service.ipc.server.message.handler;

import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import java.util.Set;

public interface IIPCServerMessageHandler {
    void handle(Set<ClientBinderWrapper> var1, ServiceUniqueIdType var2, String var3, IPCPack var4);
}