package com.netease.httpdns.provider.dal.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.util.S;

public class DNSServerManager implements StorageDatabaseManager {
    private static DNSServerManager instance = new DNSServerManager();
    private SQLiteDatabase db;

    private DNSServerManager() {
    }

    public static DNSServerManager getInstance() {
        return instance;
    }

    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    private boolean isDBAvailable() {
        return null != this.db && this.db.isOpen();
    }

    public void createTable(SQLiteDatabase db) {
        if (null != db) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSServerManager]createTable: dns_server");
            }

            try {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS `dns_server` (`network_type` varchar(20),`network_environment` integer,`use_environment` integer,`content_json` text,`created_at` long, UNIQUE(network_type) );";
                db.execSQL(createTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSServerManager]createTable, error", throwable);
            }

        }
    }

    public void dropTable(SQLiteDatabase db) {
        if (null != db) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSServerManager]dropTable: dns_server");
            }

            try {
                String dropTableSQL = "DROP TABLE IF EXISTS dns_server";
                db.execSQL(dropTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSServerManager]dropTable, error: ", throwable);
            }

        }
    }

    public void clearTable() {
        if (this.isDBAvailable()) {
            if (S.LOG.showLog()) {
                S.LOG.i("[DNSServerManager]clearTable: dns_server");
            }

            try {
                String dropTableSQL = "DELETE FROM dns_server";
                this.db.execSQL(dropTableSQL);
            } catch (Throwable throwable) {
                S.LOG.e("[DNSServerManager]clearTable, error: ", throwable);
            }

        }
    }

    public void createOrUpdate(DNSServer dnsServer) {
        if (this.isDBAvailable()) {
            if (dnsServer != null) {
                if (S.LOG.showLog()) {
                    S.LOG.i("[DNSServerManager]createOrUpdate");
                }

                if (this.isExist(dnsServer.getNetworkType())) {
                    this.update(dnsServer);
                } else {
                    this.create(dnsServer);
                }

            }
        }
    }

    public boolean isExist(String networkType) {
        if (!this.isDBAvailable()) {
            return false;
        } else {
            Cursor cursor = null;

            try {
                cursor = this.db.rawQuery("SELECT * FROM dns_server where network_type= ? ", new String[]{networkType});
                boolean var3 = cursor.moveToFirst();
                return var3;
            } catch (Throwable throwable) {
                S.LOG.e("[DNSServerManager]isExist, error: ", throwable);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }

            }

            return false;
        }
    }

    public void create(DNSServer dnsServer) {
        if (this.isDBAvailable()) {
            if (dnsServer != null) {
                try {
                    this.db.execSQL("INSERT INTO dns_server VALUES(?, ?, ?, ?, ?)", new Object[]{dnsServer.getNetworkType(), dnsServer.getIpEnvironment().getCode(), dnsServer.getUseIpType().getCode(), dnsServer.getContentJson(), dnsServer.getCreatedAt()});
                    if (S.LOG.showLog()) {
                        S.LOG.i("[DNSServerManager]create");
                    }
                } catch (Throwable throwable) {
                    S.LOG.e("[DNSServerDatabaseManager]save error: " + throwable.getMessage());
                }

            }
        }
    }

    public void update(DNSServer dnsServer) {
        if (this.isDBAvailable()) {
            if (dnsServer != null) {
                try {
                    this.db.execSQL("UPDATE dns_server SET content_json = ? ,network_environment = ? ,use_environment = ?  WHERE network_type = ?", new Object[]{dnsServer.getContentJson(), dnsServer.getIpEnvironment().getCode(), dnsServer.getUseIpType().getCode(), dnsServer.getNetworkType()});
                } catch (Throwable throwable) {
                    S.LOG.e("[DNSServerDatabaseManager]save error: ", throwable);
                }

            }
        }
    }

    @Nullable
    public DNSServer query(String networkType) {
        if (!this.isDBAvailable()) {
            return null;
        } else {
            Cursor cursor = null;

            try {
                cursor = this.db.rawQuery("SELECT * FROM dns_server WHERE network_type = ?", new String[]{networkType});

                DNSServer result;
                for(result = null; cursor.moveToNext(); result = this.parseFromCursor(cursor)) {
                }

                DNSServer var4 = result;
                return var4;
            } catch (Throwable throwable) {
                S.LOG.e("[DNSServerDatabaseManager]queryAll, error: ", throwable);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }

            }

            return null;
        }
    }

    private DNSServer parseFromCursor(Cursor cursor) {
        if (null == cursor) {
            return null;
        } else {
            DNSServer dnsServer = new DNSServer();
            dnsServer.setNetworkType(cursor.getString(cursor.getColumnIndex("network_type")));
            dnsServer.setContentJson(cursor.getString(cursor.getColumnIndex("content_json")));
            dnsServer.setIpEnvironment(IpEnvironment.parse(cursor.getInt(cursor.getColumnIndex("network_environment"))));
            dnsServer.setUseIpType(IpEnvironment.parse(cursor.getInt(cursor.getColumnIndex("use_environment"))));
            dnsServer.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
            return dnsServer;
        }
    }
}