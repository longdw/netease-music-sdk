package com.netease.httpdns.provider.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.netease.httpdns.provider.dal.manager.DNSCacheItemManager;
import com.netease.httpdns.provider.dal.manager.DNSServerManager;
import com.netease.httpdns.util.S;

public class DNSCacheOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DNS_CACHE";
    private static final int CURRENT_VERSION = 1002;

    public DNSCacheOpenHelper(Context context) {
        super(context, "DNS_CACHE", (SQLiteDatabase.CursorFactory)null, 1002);
    }

    public void init() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            DNSCacheItemManager.getInstance().setDatabase(db);
            DNSServerManager.getInstance().setDatabase(db);
        } catch (Throwable throwable) {
            S.LOG.e("[DNSCacheOpenHelper]init, error: ", throwable);
        }

    }

    public void onCreate(SQLiteDatabase db) {
        DNSCacheItemManager.getInstance().createTable(db);
        DNSServerManager.getInstance().createTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            DNSCacheItemManager.getInstance().dropTable(db);
            DNSServerManager.getInstance().dropTable(db);
            DNSCacheItemManager.getInstance().createTable(db);
            DNSServerManager.getInstance().createTable(db);
        }

    }
}