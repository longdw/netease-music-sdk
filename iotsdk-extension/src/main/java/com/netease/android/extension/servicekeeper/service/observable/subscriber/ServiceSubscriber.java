package com.netease.android.extension.servicekeeper.service.observable.subscriber;

public interface ServiceSubscriber<Emit> {
    void onReceive(Emit var1);
}