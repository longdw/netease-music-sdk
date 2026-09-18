package com.netease.httpdns.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.ThreadPool;
import com.netease.httpdns.log.DNSLog;

public class NetworkMonitor {
    private static final String TAG = NetworkMonitor.class.getSimpleName();
    private ConnectionChangeReceiver connectionReceiver;
    private Context context;
    private boolean isRunning = false;

    public void start(Context context) {
        if (this.isRunning) {
            DNSLog.i("[" + TAG + "] start, isRunning");
        } else {
            this.context = context;
            this.connectionReceiver = new ConnectionChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(this.connectionReceiver, filter);
            this.isRunning = true;
        }
    }

    public void stop() {
        if (this.context != null && this.connectionReceiver != null) {
            this.context.unregisterReceiver(this.connectionReceiver);
            this.connectionReceiver = null;
        }

        this.context = null;
        this.isRunning = false;
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {
        public void onReceive(final Context context, final Intent intent) {
            ThreadPool.submit(new Runnable() {
                public void run() {
                    if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                        NetworkUtil.syncNetworkType(context);
                        HttpDnsService.getInstance().updateCached();
                    }

                }
            });
        }
    }
}