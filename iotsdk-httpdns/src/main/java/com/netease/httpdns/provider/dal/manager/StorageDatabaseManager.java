package com.netease.httpdns.provider.dal.manager;

import android.database.sqlite.SQLiteDatabase;

public interface StorageDatabaseManager {
    void setDatabase(SQLiteDatabase var1);

    void createTable(SQLiteDatabase var1);

    void dropTable(SQLiteDatabase var1);
}