package com.netease.android.extension.servicekeeper.service.observable.subscriber;

public abstract class NotifyServiceSubscriber implements ServiceSubscriber<Void> {
    public final void onReceive(Void aVoid) {
        this.onReceive();
    }

    public abstract void onReceive();
}