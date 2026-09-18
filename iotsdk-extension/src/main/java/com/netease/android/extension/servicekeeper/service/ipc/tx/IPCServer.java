package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPCServer extends IInterface {
    void attach(SKCSerial var1, ClientBinderWrapper var2, IPCFunc var3) throws RemoteException;

    void detach(SKCSerial var1, ClientBinderWrapper var2, IPCFunc var3) throws RemoteException;

    void send(String var1, String var2, IPCPack var3) throws RemoteException;

    void tryLock(IPCPack var1, IPCFuncB var2) throws RemoteException;

    void release(IPCPack var1, IPCFuncB var2) throws RemoteException;

    public static class Default implements IPCServer {
        public void attach(SKCSerial skcSerial, ClientBinderWrapper clientBinderWrapper, IPCFunc ipcFunc) throws RemoteException {
        }

        public void detach(SKCSerial skcSerial, ClientBinderWrapper clientBinderWrapper, IPCFunc ipcFunc) throws RemoteException {
        }

        public void send(String uniqueIdType, String uniqueId, IPCPack ipcPack) throws RemoteException {
        }

        public void tryLock(IPCPack ipcPack, IPCFuncB ipcFuncB) throws RemoteException {
        }

        public void release(IPCPack ipcPack, IPCFuncB ipcFuncB) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public abstract static class Stub extends Binder implements IPCServer {
        private static final String DESCRIPTOR = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer";
        static final int TRANSACTION_attach = 1;
        static final int TRANSACTION_detach = 2;
        static final int TRANSACTION_send = 3;
        static final int TRANSACTION_tryLock = 4;
        static final int TRANSACTION_release = 5;

        public Stub() {
            this.attachInterface(this, "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
        }

        public static IPCServer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                return (IPCServer)(iin != null && iin instanceof IPCServer ? (IPCServer)iin : new Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer";
            switch (code) {
                case 1: {
                    data.enforceInterface(descriptor);
                    SKCSerial _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (SKCSerial) SKCSerial.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    ClientBinderWrapper _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = (ClientBinderWrapper) ClientBinderWrapper.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    IPCFunc _arg2 = IPCFunc.Stub.asInterface(data.readStrongBinder());
                    this.attach(_arg0, _arg1, _arg2);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    SKCSerial _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (SKCSerial) SKCSerial.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    ClientBinderWrapper _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = (ClientBinderWrapper) ClientBinderWrapper.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    IPCFunc _arg2 = IPCFunc.Stub.asInterface(data.readStrongBinder());
                    this.detach(_arg0, _arg1, _arg2);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    IPCPack _arg2;
                    if (0 != data.readInt()) {
                        _arg2 = (IPCPack) IPCPack.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }

                    this.send(_arg0, _arg1, _arg2);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    IPCPack _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (IPCPack) IPCPack.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    IPCFuncB _arg1 = IPCFuncB.Stub.asInterface(data.readStrongBinder());
                    this.tryLock(_arg0, _arg1);
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    IPCPack _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (IPCPack) IPCPack.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    IPCFuncB _arg1 = IPCFuncB.Stub.asInterface(data.readStrongBinder());
                    this.release(_arg0, _arg1);
                    return true;
                }
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IPCServer impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IPCServer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        private static class Proxy implements IPCServer {
            private IBinder mRemote;
            public static IPCServer sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer";
            }

            public void attach(SKCSerial skcSerial, ClientBinderWrapper clientBinderWrapper, IPCFunc ipcFunc) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                    if (skcSerial != null) {
                        _data.writeInt(1);
                        skcSerial.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if (clientBinderWrapper != null) {
                        _data.writeInt(1);
                        clientBinderWrapper.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(ipcFunc != null ? ipcFunc.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().attach(skcSerial, clientBinderWrapper, ipcFunc);
                } finally {
                    _data.recycle();
                }

            }

            public void detach(SKCSerial skcSerial, ClientBinderWrapper clientBinderWrapper, IPCFunc ipcFunc) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                    if (skcSerial != null) {
                        _data.writeInt(1);
                        skcSerial.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if (clientBinderWrapper != null) {
                        _data.writeInt(1);
                        clientBinderWrapper.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(ipcFunc != null ? ipcFunc.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().detach(skcSerial, clientBinderWrapper, ipcFunc);
                } finally {
                    _data.recycle();
                }

            }

            public void send(String uniqueIdType, String uniqueId, IPCPack ipcPack) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                    _data.writeString(uniqueIdType);
                    _data.writeString(uniqueId);
                    if (ipcPack != null) {
                        _data.writeInt(1);
                        ipcPack.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(3, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().send(uniqueIdType, uniqueId, ipcPack);
                } finally {
                    _data.recycle();
                }

            }

            public void tryLock(IPCPack ipcPack, IPCFuncB ipcFuncB) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                    if (ipcPack != null) {
                        _data.writeInt(1);
                        ipcPack.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(ipcFuncB != null ? ipcFuncB.asBinder() : null);
                    boolean _status = this.mRemote.transact(4, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().tryLock(ipcPack, ipcFuncB);
                } finally {
                    _data.recycle();
                }

            }

            public void release(IPCPack ipcPack, IPCFuncB ipcFuncB) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer");
                    if (ipcPack != null) {
                        _data.writeInt(1);
                        ipcPack.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(ipcFuncB != null ? ipcFuncB.asBinder() : null);
                    boolean _status = this.mRemote.transact(5, _data, (Parcel)null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        return;
                    }

                    Stub.getDefaultImpl().release(ipcPack, ipcFuncB);
                } finally {
                    _data.recycle();
                }

            }
        }
    }
}