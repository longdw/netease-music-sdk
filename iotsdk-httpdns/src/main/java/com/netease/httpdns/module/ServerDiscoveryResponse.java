package com.netease.httpdns.module;

import android.text.TextUtils;
import com.netease.httpdns.util.IpTypeUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerDiscoveryResponse {
    private static final String TAG = ServerDiscoveryResponse.class.getSimpleName();
    private static final String SERVERS = "servers";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String SERVER_PREFER = "prefer";
    private static final String UPDATED_AT = "updated_at";
    private String prefer;
    private List<ServerAddress> servers;
    private List<ServerAddress> serversIpv4;
    private List<ServerAddress> serversIpv6;
    private long updatedAt;

    public String getPrefer() {
        return this.prefer;
    }

    public List<ServerAddress> getServers() {
        return this.servers;
    }

    public List<ServerAddress> getServersIpv4() {
        return this.serversIpv4;
    }

    public List<ServerAddress> getServersIpv6() {
        return this.serversIpv6;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public ServerDiscoveryResponse setPrefer(String prefer) {
        this.prefer = prefer;
        return this;
    }

    public ServerDiscoveryResponse setServers(List<ServerAddress> servers) {
        this.servers = servers;
        return this;
    }

    public ServerDiscoveryResponse setServersIpv4(List<ServerAddress> serversIpv4) {
        this.serversIpv4 = serversIpv4;
        return this;
    }

    public ServerDiscoveryResponse setServersIpv6(List<ServerAddress> serversIpv6) {
        this.serversIpv6 = serversIpv6;
        return this;
    }

    public ServerDiscoveryResponse setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public static ServerDiscoveryResponse parse(String response) throws Exception {
        if (TextUtils.isEmpty(response)) {
            if (S.LOG.showLog()) {
                S.LOG.i("[ServerDiscoveryResponse]parse, response is null.");
            }

            return null;
        } else {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray servers = jsonObj.optJSONArray("servers");
            if (servers == null) {
                return null;
            } else {
                String prefer = jsonObj.optString("prefer");
                long updatedAt = jsonObj.optLong("updated_at");
                int length = servers.length();
                List<ServerAddress> addressesIpv4 = new ArrayList();
                List<ServerAddress> addressesIpv6 = new ArrayList();
                List<ServerAddress> addresses = new ArrayList();

                for(int i = 0; i < length; ++i) {
                    JSONObject json = servers.optJSONObject(i);
                    String host = json.optString("host");
                    String port = json.optString("port");
                    ServerAddress address = new ServerAddress(host, port, prefer);
                    if (IpTypeUtil.isIpv6(host)) {
                        addressesIpv6.add(address);
                    }

                    if (IpTypeUtil.isIpv4(host)) {
                        addressesIpv4.add(address);
                    }

                    addresses.add(address);
                }

                ServerDiscoveryResponse serverDiscoveryResponse = new ServerDiscoveryResponse();
                serverDiscoveryResponse.setServers(addresses).setServersIpv4(addressesIpv4).setServersIpv6(addressesIpv6).setPrefer(prefer).setUpdatedAt(updatedAt);
                return serverDiscoveryResponse;
            }
        }
    }
}
