package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPCClient extends IInterface {
    void onReceive(String var1, IPCPack var2) throws RemoteException;

    public static class Default implements IPCClient {
        public void onReceive(String uniqueId, IPCPack ipcPack) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public abstract static class Stub extends Binder implements IPCClient {
        private static final String DESCRIPTOR = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient";
        static final int TRANSACTION_onReceive = 1;

        public Stub() {
            this.attachInterface(this, "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient");
        }

        public static IPCClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient");
                return (IPCClient)(iin != null && iin instanceof IPCClient ? (IPCClient)iin : new Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient";
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    IPCPack _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = (IPCPack)IPCPack.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    this.onReceive(_arg0, _arg1);
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IPCClient impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IPCClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy implements IPCClient {
            private IBinder mRemote;
            public static IPCClient sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient";
            }

            public void onReceive(String uniqueId, IPCPack ipcPack) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient");
                    _data.writeString(uniqueId);
                    if (ipcPack != null) {
                        _data.writeInt(1);
                        ipcPack.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(1, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().onReceive(uniqueId, ipcPack);
                } finally {
                    _data.recycle();
                }

            }
        }
    }
}
