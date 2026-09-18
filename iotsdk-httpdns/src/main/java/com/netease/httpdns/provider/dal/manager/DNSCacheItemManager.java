package com.netease.httpdns.provider.dal.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.netease.android.extension.func.NFunc1R;
import com.netease.httpdns.provider.dal.model.DNSCacheItem;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.List;

/** @deprecated */
@Deprecated
public class DNSCacheItemManager implements StorageDatabaseManager {
    private static DNSCacheItemManager instance = new DNSCacheItemManager();
    private SQLiteDatabase db;

    private DNSCacheItemManager() {
    }

    public static DNSCacheItemManager getInstance() {
        return instance;
    }

    private boolean isDBAvailable() {
        return null != this.db && this.db.isOpen();
    }

    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    public void createTable(SQLiteDatabase db) {
        if (null != db) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSCacheItemManager]createTable: dns_cache_item");
            }

            try {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS `dns_cache_item` (`config_id` text,`host` varchar(20),`network_type` varchar(20),`content_json` text,`created_at` long, UNIQUE(config_id, host, network_type) );";
                db.execSQL(createTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemManager]createTable, error: ", throwable);
            }

        }
    }

    public void dropTable(SQLiteDatabase db) {
        if (null != db) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSCacheItemManager]dropTable: dns_cache_item");
            }

            try {
                String dropTableSQL = "DROP TABLE IF EXISTS dns_cache_item";
                db.execSQL(dropTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemManager]dropTable, error: ", throwable);
            }

        }
    }

    public void clearTable() {
        if (this.isDBAvailable()) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSCacheItemManager]clearTable: dns_cache_item");
            }

            try {
                String dropTableSQL = "DELETE FROM dns_cache_item";
                this.db.execSQL(dropTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemManager]clearTable, error: ", throwable);
            }

        }
    }

    public void createOrUpdate(DNSCacheItem dnsCacheItem) {
        if (this.isDBAvailable()) {
            if (dnsCacheItem != null) {
                if (this.isExist(dnsCacheItem.getConfigId(), dnsCacheItem.getHost(), dnsCacheItem.getNetworkType())) {
                    this.update(dnsCacheItem);
                } else {
                    this.create(dnsCacheItem);
                }

            }
        }
    }

    public boolean isExist(String configId, String host, String networkType) {
        if (!this.isDBAvailable()) {
            return false;
        } else {
            Cursor cursor = null;

            try {
                cursor = this.db.rawQuery("SELECT * FROM dns_cache_item where config_id= ? and host= ? and network_type= ? ", new String[]{configId, host, networkType});
                boolean var5 = cursor.moveToFirst();
                return var5;
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemManager]isExist, error: ", throwable);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }

            }

            return false;
        }
    }

    public void create(DNSCacheItem dnsCacheItem) {
        if (this.isDBAvailable()) {
            if (dnsCacheItem != null) {
                try {
                    this.db.execSQL("INSERT INTO dns_cache_item VALUES(?, ?, ?, ?, ?)", new Object[]{dnsCacheItem.getConfigId(), dnsCacheItem.getHost(), dnsCacheItem.getNetworkType(), dnsCacheItem.getContentJson(), dnsCacheItem.getCreatedAt()});
                } catch (Throwable throwable) {
                    S.LOG.e("[DNSCacheItemDatabaseManager]save error: " + throwable.getMessage());
                }

            }
        }
    }

    public void update(DNSCacheItem dnsCacheItem) {
        if (this.isDBAvailable()) {
            if (dnsCacheItem != null) {
                try {
                    this.db.execSQL("UPDATE dns_cache_item SET content_json = ?  WHERE config_id = ? and host = ? and network_type = ?", new Object[]{dnsCacheItem.getContentJson(), dnsCacheItem.getConfigId(), dnsCacheItem.getHost(), dnsCacheItem.getNetworkType()});
                } catch (Throwable throwable) {
                    S.LOG.e("[DNSCacheItemDatabaseManager]save error: ", throwable);
                }

            }
        }
    }

    @Nullable
    public DNSCacheItem queryByHost(String networkType, String host) {
        if (!this.isDBAvailable()) {
            return null;
        } else {
            Cursor cursor = null;

            try {
                cursor = this.db.rawQuery("SELECT * FROM dns_cache_item WHERE network_type = ? AND host = ?", new String[]{networkType, host});

                DNSCacheItem result;
                for(result = null; cursor.moveToNext(); result = this.parseFromCursor(cursor)) {
                }

                DNSCacheItem var5 = result;
                return var5;
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemDatabaseManager]queryAll, error: ", throwable);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }

            }

            return null;
        }
    }

    @Nullable
    public List<DNSCacheItem> queryByHosts(String networkType, List<String> hosts) {
        if (!this.isDBAvailable()) {
            return null;
        } else {
            Cursor cursor = null;

            try {
                String ids = CollectionUtil.join(hosts, ", ", new NFunc1R<String, String>() {
                    public String call(String item) {
                        return item;
                    }
                });
                cursor = this.db.rawQuery("SELECT * FROM dns_cache_item WHERE network_type = ?  AND host IN (?)", new String[]{networkType, ids});
                List<DNSCacheItem> result = new ArrayList();

                while(cursor.moveToNext()) {
                    DNSCacheItem dnsCacheItem = this.parseFromCursor(cursor);
                    if (null != dnsCacheItem) {
                        result.add(dnsCacheItem);
                    }
                }

                Object var12 = result;
                return (List<DNSCacheItem>)var12;
            } catch (Throwable throwable) {
                S.LOG.e("[DNSCacheItemDatabaseManager]queryAll, error: ", throwable);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }

            }

            return null;
        }
    }

    private DNSCacheItem parseFromCursor(Cursor cursor) {
        if (null == cursor) {
            return null;
        } else {
            DNSCacheItem dnsCacheItem = new DNSCacheItem();
            dnsCacheItem.setConfigId(cursor.getString(cursor.getColumnIndex("config_id")));
            dnsCacheItem.setHost(cursor.getString(cursor.getColumnIndex("host")));
            dnsCacheItem.setNetworkType(cursor.getString(cursor.getColumnIndex("network_type")));
            dnsCacheItem.setContentJson(cursor.getString(cursor.getColumnIndex("content_json")));
            dnsCacheItem.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
            return dnsCacheItem;
        }
    }
}