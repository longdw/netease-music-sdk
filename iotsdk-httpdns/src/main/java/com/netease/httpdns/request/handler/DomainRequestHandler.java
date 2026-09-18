package com.netease.httpdns.request.handler;

import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.module.NAHttpEntity;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.DomainRequestTask;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.request.ServerRequestTask;
import com.netease.httpdns.util.NetworkUtil;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DomainRequestHandler extends AbstractRequestHandler {
    private static final String TAG = DomainRequestTask.class.getSimpleName();
    private String host = "httpdns.n.netease.com";

    public DomainRequestHandler() {
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        if (options != null && !TextUtils.isEmpty(options.getCustomHttpDnsHost())) {
            this.host = options.getCustomHttpDnsHost();
        }

    }

    protected String getServerDiscoveryHost(ServerRequestTask serverRequestTask) {
        return null;
    }

    public DNSServer requestServerDiscovery(ServerRequestTask serverRequestTask) {
        return new DNSServer(NetworkUtil.getNetworkType(), "", serverRequestTask.getNetworkEnvironment(), serverRequestTask.getUseIpType());
    }

    public NAHttpEntity requestDomainResolve(DomainRequestTask domainRequestTask) throws Exception {
        return this.requestDomainResolveInternal(domainRequestTask);
    }

    protected void onDnsError() {
        HttpDnsRequestManager.getInstance().onDnsError();
    }

    protected void onHttpHijacking(boolean isHttp) {
        HttpDnsRequestManager.getInstance().onHttpHijacking(isHttp);
    }

    protected String getHostHeader() {
        return this.host;
    }

    protected String getDomainResolveHost(boolean isHttp) {
        IpEnvironment currentNetwork = HttpDnsRequestManager.getInstance().getCurrentIpStackType();
        if (currentNetwork != IpEnvironment.IP_DUAL_STACK) {
            return this.host;
        } else {
            String randomIpv6LocalDns = this.getRandomIpv6LocalDns(this.host, isHttp);
            return TextUtils.isEmpty(randomIpv6LocalDns) ? this.host : randomIpv6LocalDns;
        }
    }

    private String getRandomIpv6LocalDns(String host, boolean isHttp) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            List<String> ipv6List = new ArrayList();

            for(InetAddress address : addresses) {
                if (address instanceof Inet6Address) {
                    String ipv6 = address.getHostAddress();
                    if (!TextUtils.isEmpty(ipv6)) {
                        ipv6List.add(ipv6);
                    }
                }
            }

            if (ipv6List.isEmpty()) {
                return "";
            } else {
                int size = ipv6List.size();
                Random random = new Random();
                int index = random.nextInt(size);
                String ipv6 = "[" + (String)ipv6List.get(index) + "]";
                return isHttp ? ipv6 + ":80" : ipv6 + ":443";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}