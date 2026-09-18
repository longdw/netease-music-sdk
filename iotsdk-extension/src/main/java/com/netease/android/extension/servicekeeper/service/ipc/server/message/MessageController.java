package com.netease.android.extension.servicekeeper.service.ipc.server.message;

import com.netease.android.extension.ext.CollectionExt;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.service.ipc.server.message.handler.IIPCServerMessageHandler;
import com.netease.android.extension.servicekeeper.service.ipc.server.message.handler.ObservableIPCServerMessageHandler;
import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.util.ELog;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageController implements IMessageController {
    private static final String TAG = MessageController.class.getSimpleName();
    private static final HashMap<ServiceUniqueIdType, IIPCServerMessageHandler> SERVICE_UNIQUE_ID_TYPE_MESSAGE_HANDLERS = new HashMap();

    public void handleMessage(ConcurrentHashMap<String, Set<ClientBinderWrapper>> subscribers, String uniqueIdType, String uniqueId, IPCPack ipcPack) {
        IPCRoute ipcRoute = ipcPack.getIpcRoute();
        SKCSerial skcSerial = ipcRoute.getFromSKCSerial();
        String skcName = skcSerial.getSkcName();
        if (null == skcName) {
            throw new IllegalArgumentException("[" + TAG + "]send, skcName is null !");
        } else {
            Set<ClientBinderWrapper> clientBinderWrappers = (Set)subscribers.get(skcName);
            if (CollectionExt.isEmpty(clientBinderWrappers)) {
                ELog.w("[" + TAG + "]send, empty subscribers with skcName[" + skcName + "]");
            } else {
                ServiceUniqueIdType serviceUniqueIdType;
                try {
                    serviceUniqueIdType = ServiceUniqueIdType.valueOf(uniqueIdType);
                } catch (Throwable var11) {
                    throw new UnsupportedOperationException("[" + TAG + "]Unique id type '" + uniqueIdType + "' is not found !");
                }

                IIPCServerMessageHandler ipcServerHandler = (IIPCServerMessageHandler)SERVICE_UNIQUE_ID_TYPE_MESSAGE_HANDLERS.get(serviceUniqueIdType);
                if (null == ipcServerHandler) {
                    throw new UnsupportedOperationException("[" + TAG + "]The ServerMessageHandler with Unique id type '" + uniqueIdType + "' is not found !");
                } else {
                    ipcServerHandler.handle(clientBinderWrappers, serviceUniqueIdType, uniqueId, ipcPack);
                }
            }
        }
    }

    static {
        SERVICE_UNIQUE_ID_TYPE_MESSAGE_HANDLERS.put(ServiceUniqueIdType.IPC_OBSERVABLE_SERVICE_UNIQUE_ID, new ObservableIPCServerMessageHandler());
    }
}