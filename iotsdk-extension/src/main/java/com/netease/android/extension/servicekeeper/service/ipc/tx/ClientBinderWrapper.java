package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.netease.android.extension.ext.ObjectExt;

public class ClientBinderWrapper implements Parcelable {
    private SKCSerial skcSerial;
    @NonNull
    private IBinder binder;
    public static final Creator<ClientBinderWrapper> CREATOR = new Creator<ClientBinderWrapper>() {
        public ClientBinderWrapper createFromParcel(Parcel in) {
            return new ClientBinderWrapper(in);
        }

        public ClientBinderWrapper[] newArray(int size) {
            return new ClientBinderWrapper[size];
        }
    };

    public ClientBinderWrapper(SKCSerial skcSerial, @NonNull IBinder binder) {
        this.skcSerial = skcSerial;
        this.binder = binder;
    }

    protected ClientBinderWrapper(Parcel in) {
        this.skcSerial = (SKCSerial)in.readParcelable(SKCSerial.class.getClassLoader());
        this.binder = in.readStrongBinder();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.skcSerial, flags);
        dest.writeStrongBinder(this.binder);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ClientBinderWrapper{skcSerial=" + this.skcSerial + ", binder=" + this.binder + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ClientBinderWrapper that = (ClientBinderWrapper)o;
            return ObjectExt.equals(this.skcSerial, that.skcSerial);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ObjectExt.hash(new Object[]{this.skcSerial});
    }

    public SKCSerial getSkcSerial() {
        return this.skcSerial;
    }

    @NonNull
    public IBinder getBinder() {
        return this.binder;
    }
}