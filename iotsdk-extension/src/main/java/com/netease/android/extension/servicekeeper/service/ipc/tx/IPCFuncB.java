package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPCFuncB extends IInterface {
    void onCall(boolean var1) throws RemoteException;

    public static class Default implements IPCFuncB {
        public void onCall(boolean value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public abstract static class Stub extends Binder implements IPCFuncB {
        private static final String DESCRIPTOR = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB";
        static final int TRANSACTION_onCall = 1;

        public Stub() {
            this.attachInterface(this, "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB");
        }

        public static IPCFuncB asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB");
                return (IPCFuncB)(iin != null && iin instanceof IPCFuncB ? (IPCFuncB)iin : new Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB";
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    boolean _arg0 = 0 != data.readInt();
                    this.onCall(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IPCFuncB impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IPCFuncB getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy implements IPCFuncB {
            private IBinder mRemote;
            public static IPCFuncB sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB";
            }

            public void onCall(boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB");
                    _data.writeInt(value ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    Stub.getDefaultImpl().onCall(value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}