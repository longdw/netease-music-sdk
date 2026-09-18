package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPCFunc extends IInterface {
    void onCall() throws RemoteException;

    public static class Default implements IPCFunc {
        public void onCall() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public abstract static class Stub extends Binder implements IPCFunc {
        private static final String DESCRIPTOR = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc";
        static final int TRANSACTION_onCall = 1;

        public Stub() {
            this.attachInterface(this, "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc");
        }

        public static IPCFunc asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc");
                return (IPCFunc)(iin != null && iin instanceof IPCFunc ? (IPCFunc)iin : new Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc";
            switch (code) {
                case 1:
                    data.enforceInterface(descriptor);
                    this.onCall();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IPCFunc impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IPCFunc getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy implements IPCFunc {
            private IBinder mRemote;
            public static IPCFunc sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc";
            }

            public void onCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    Stub.getDefaultImpl().onCall();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}