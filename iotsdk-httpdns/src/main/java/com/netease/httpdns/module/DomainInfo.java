package com.netease.httpdns.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.IpTypeUtil;
import com.netease.httpdns.util.LocalDNSUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DomainInfo implements Parcelable {
    private static final String DATA = "data";
    private static final String IP = "ip";
    private static final String IPV6 = "ipv6";
    private static final String USE_IP = "useIp";
    public static final String SCORE_PREFER_IPV6 = "ipv6";
    private static final String TTL = "ttl";
    private static final String DOMAIN = "domain";
    private static final String REFRESH_TIME = "refreshTime";
    private static final String NETWORK_TYPE = "networkType";
    private static final String SCORE_PREFER = "prefer";
    private static final String SCORE = "score";
    private static final String IPV4_SCORE_DELAY = "ipv4ScoreDelay";
    private String host;
    private List<String> ips;
    private List<String> ipv6s;
    private List<String> availableIps;
    private long ttl;
    private long refreshTime;
    private boolean isScore;
    private String scorePrefer;
    private int ipv4ScoreDelay;
    private int scoreCount;
    private boolean isPending;
    private String networkType;
    private String responseRawJson;
    public static final Creator<DomainInfo> CREATOR = new Creator<DomainInfo>() {
        public DomainInfo createFromParcel(Parcel source) {
            return new DomainInfo(source);
        }

        public DomainInfo[] newArray(int size) {
            return new DomainInfo[size];
        }
    };

    public DomainInfo() {
    }

    public DomainInfo(DomainInfo domainInfo) {
        if (domainInfo != null) {
            this.host = domainInfo.host;
            this.ttl = domainInfo.ttl;
            this.refreshTime = domainInfo.refreshTime;
            this.scorePrefer = domainInfo.scorePrefer;
            this.isScore = domainInfo.isScore;
            this.ipv4ScoreDelay = domainInfo.ipv4ScoreDelay;
            this.scoreCount = domainInfo.scoreCount;
            this.isPending = domainInfo.isPending;
            this.networkType = domainInfo.networkType;
            this.responseRawJson = domainInfo.responseRawJson;
            if (domainInfo.ips != null && !domainInfo.ips.isEmpty()) {
                this.ips = Collections.synchronizedList(domainInfo.ips);
            }

            if (domainInfo.ipv6s != null && !domainInfo.ipv6s.isEmpty()) {
                this.ipv6s = Collections.synchronizedList(domainInfo.ipv6s);
            }

            if (domainInfo.availableIps != null && !domainInfo.availableIps.isEmpty()) {
                this.availableIps = Collections.synchronizedList(domainInfo.availableIps);
            }
        }

    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<String> getIps() {
        return this.ips;
    }

    public List<String> getIpv6s() {
        return this.ipv6s;
    }

    public void setIpv6s(List<String> ipv6s) {
        if (ipv6s != null && !ipv6s.isEmpty()) {
            this.ipv6s = Collections.synchronizedList(ipv6s);
        }

    }

    public List<String> getAvailableIps() {
        return this.availableIps;
    }

    public void setAvailableIps(List<String> availableIps) {
        if (availableIps != null && !availableIps.isEmpty()) {
            this.availableIps = Collections.synchronizedList(availableIps);
        }

    }

    public void setIps(List<String> ips) {
        if (ips != null && !ips.isEmpty()) {
            this.ips = Collections.synchronizedList(ips);
        }

    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public long getRefreshTime() {
        return this.refreshTime;
    }

    public long getTtl() {
        return this.ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getScorePrefer() {
        return this.scorePrefer;
    }

    public DomainInfo setScorePrefer(String scorePrefer) {
        this.scorePrefer = scorePrefer;
        return this;
    }

    public boolean isScore() {
        return this.isScore;
    }

    public void setScore(boolean score) {
        this.isScore = score;
    }

    public int getIpv4ScoreDelay() {
        return this.ipv4ScoreDelay;
    }

    public void setIpv4ScoreDelay(int ipv4ScoreDelay) {
        this.ipv4ScoreDelay = ipv4ScoreDelay;
    }

    public int getScoreCount() {
        return this.scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean pending) {
        this.isPending = pending;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getResponseRawJson() {
        return this.responseRawJson;
    }

    public DomainInfo setResponseRawJson(String responseRawJson) {
        this.responseRawJson = responseRawJson;
        return this;
    }

    public void mergeLocalDNSResult() {
        if (!TextUtils.isEmpty(this.host)) {
            List<String> localDNSResult = LocalDNSUtil.getLocalDomainIp(this.host);
            if (localDNSResult != null && localDNSResult.size() != 0) {
                DNSLog.i("mergeLocalDNSResult ,get LocalDns ips: " + localDNSResult.toString());
                if (this.availableIps == null) {
                    this.availableIps = Collections.synchronizedList(localDNSResult);
                } else {
                    for(String localIp : localDNSResult) {
                        if (!this.contains(localIp)) {
                            this.availableIps.add(localIp);
                        }
                    }

                }
            }
        }
    }

    public void mergeIpv4Result() {
        if (!TextUtils.isEmpty(this.host)) {
            if (this.availableIps == null) {
                this.availableIps = Collections.synchronizedList(new ArrayList());
            }

            if (this.ips != null && !this.ips.isEmpty()) {
                for(String ip : this.ips) {
                    if (!this.contains(ip)) {
                        this.availableIps.add(ip);
                    }
                }
            }

        }
    }

    public void mergeIpv6Result() {
        if (!TextUtils.isEmpty(this.host)) {
            if (this.availableIps == null) {
                this.availableIps = Collections.synchronizedList(new ArrayList());
            }

            if (this.ipv6s != null && !this.ipv6s.isEmpty()) {
                for(String ip : this.ipv6s) {
                    if (!this.contains(ip)) {
                        this.availableIps.add(ip);
                    }
                }
            }

        }
    }

    public List<String> getLocalDNSResult() {
        if (TextUtils.isEmpty(this.host)) {
            return Collections.emptyList();
        } else {
            List<String> localDNSResult = LocalDNSUtil.getLocalDomainIp(this.host);
            if (localDNSResult != null && !localDNSResult.isEmpty()) {
                for(String localIp : localDNSResult) {
                    if (this.ips != null && !this.ips.isEmpty() && this.ips.contains(localIp)) {
                        localDNSResult.remove(localIp);
                    }

                    if (this.ipv6s != null && !this.ipv6s.isEmpty() && this.ipv6s.contains(localIp)) {
                        localDNSResult.remove(localIp);
                    }
                }

                return localDNSResult;
            } else {
                return Collections.emptyList();
            }
        }
    }

    public boolean isCacheExpires() {
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        long userDefinedTll = options.getUserDefinedTTL();
        if (userDefinedTll == -1L) {
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            long tempTll;
            if (userDefinedTll > 0L) {
                tempTll = userDefinedTll;
            } else {
                tempTll = this.ttl;
            }

            if (S.LOG.showLog()) {
                S.LOG.i("[DomainInfo]isCacheExpires domain: " + this.getHost() + " ,userDefinedTll : " + userDefinedTll + " ,ttl: " + this.ttl + ", refreshTime : " + this.refreshTime + " , currentTime : " + currentTime);
            }

            return this.refreshTime + tempTll * 1000L <= currentTime;
        }
    }

    public boolean isCacheAdvent() {
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        long userDefinedTll = options.getUserDefinedTTL();
        if (userDefinedTll == -1L) {
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            long tempTll;
            if (userDefinedTll > 0L) {
                tempTll = userDefinedTll;
            } else {
                tempTll = this.ttl;
            }

            return !((double)this.refreshTime + (double)(tempTll * 1000L) * 0.9 > (double)currentTime);
        }
    }

    public boolean contains(String ip) {
        return this.availableIps != null && !this.availableIps.isEmpty() ? this.availableIps.contains(ip) : false;
    }

    private static JSONObject getDataJsonObj(String response) {
        if (TextUtils.isEmpty(response)) {
            return null;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.optJSONObject("data");
            } catch (JSONException var2) {
                return null;
            }
        }
    }

    public static List<DomainInfo> parseDomainInfoList(String response, String networkType) {
        if (TextUtils.isEmpty(response)) {
            return Collections.emptyList();
        } else {
            List<DomainInfo> infoList = new ArrayList();
            JSONObject dataObj = getDataJsonObj(response);
            if (dataObj == null) {
                return Collections.emptyList();
            } else {
                Iterator<String> iterator = dataObj.keys();

                while(iterator.hasNext()) {
                    String domain = (String)iterator.next();
                    JSONObject itemJson = dataObj.optJSONObject(domain);
                    if (itemJson != null) {
                        DomainInfo domainInfo = parse(itemJson, domain);
                        if (domainInfo != null) {
                            domainInfo.setNetworkType(networkType);
                            infoList.add(domainInfo);
                        }
                    }
                }

                return infoList;
            }
        }
    }

    public static DomainInfo parse(JSONObject infoObj, String domain) {
        if (infoObj == null) {
            return null;
        } else {
            long ttl = infoObj.optLong("ttl", -2L);
            long refreshTime = infoObj.optLong("refreshTime");
            String prefer = infoObj.optString("prefer");
            boolean isScore = infoObj.optBoolean("score");
            int ipv4ScoreDelay = infoObj.optInt("ipv4ScoreDelay");
            JSONArray ipv4Array = infoObj.optJSONArray("ip");
            JSONArray ipv6Array = infoObj.optJSONArray("ipv6");
            JSONArray userIpArray = infoObj.optJSONArray("useIp");
            String networkType = infoObj.optString("networkType");
            DomainInfo info = new DomainInfo();
            info.setResponseRawJson(infoObj.toString());
            info.setRefreshTime(refreshTime == 0L ? System.currentTimeMillis() : refreshTime);
            info.setHost(domain);
            info.setScorePrefer(prefer);
            info.setScore(isScore);
            info.setIpv4ScoreDelay(ipv4ScoreDelay);
            info.setNetworkType(networkType);
            List<String> ipv4s = new ArrayList();
            List<String> ipv6s = new ArrayList();
            List<String> useIp = new ArrayList();
            info.setTtl(ttl);
            if (ipv6Array != null && ipv6Array.length() > 0) {
                int size = ipv6Array.length();

                for(int i = 0; i < size; ++i) {
                    String ip = ipv6Array.optString(i);
                    if (!TextUtils.isEmpty(ip) && IpTypeUtil.isIpv6(ip)) {
                        ipv6s.add(ip);
                    }
                }

                info.setIpv6s(ipv6s);
            }

            if (ipv4Array != null && ipv4Array.length() > 0) {
                int size = ipv4Array.length();

                for(int i = 0; i < size; ++i) {
                    String ip = ipv4Array.optString(i);
                    if (!TextUtils.isEmpty(ip) && IpTypeUtil.isIpv4(ip)) {
                        ipv4s.add(ip);
                    }
                }

                info.setIps(ipv4s);
            }

            if (userIpArray != null && userIpArray.length() > 0) {
                int size = userIpArray.length();

                for(int i = 0; i < size; ++i) {
                    String ip = userIpArray.optString(i);
                    useIp.add(ip);
                }

                info.setAvailableIps(useIp);
            }

            return ipv4s.isEmpty() && ipv6s.isEmpty() ? null : info;
        }
    }

    public static DomainInfo parseDomainInfo(String domain, String response) {
        JSONObject dataObj = getDataJsonObj(response);
        if (dataObj == null) {
            return null;
        } else {
            JSONObject infoObj = dataObj.optJSONObject(domain);
            return infoObj == null ? null : parse(infoObj, domain);
        }
    }

    public static DomainInfo getDomainFromCache(String cacheStr) {
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        } else {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(cacheStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject == null) {
                return null;
            } else {
                String domain = jsonObject.optString("domain");
                return parse(jsonObject, domain);
            }
        }
    }

    public boolean isAvailableIpEmpty() {
        return CollectionUtil.isEmpty(this.availableIps);
    }

    public boolean isUseless() {
        return this.isAvailableIpEmpty() && !this.isPending;
    }

    public boolean isWaiting() {
        return this.isAvailableIpEmpty() && this.isPending;
    }

    public void saveIps(IpEnvironment currentNetwork) {
        if (!TextUtils.isEmpty(this.getHost())) {
            switch (currentNetwork) {
                case IPV4:
                    this.setAvailableIps(this.getIps());
                    break;
                case IPV6:
                    this.setAvailableIps(this.getIpv6s());
                    break;
                case IP_DUAL_STACK:
                    if (TextUtils.equals(this.getScorePrefer(), "ipv6")) {
                        this.mergeIpv6Result();
                        this.mergeIpv4Result();
                    } else {
                        this.mergeIpv4Result();
                        this.mergeIpv6Result();
                    }
            }

            this.setPending(false);
        }
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("domain", this.host);
            jsonObject.put("refreshTime", this.refreshTime);
            jsonObject.put("score", this.isScore);
            jsonObject.put("prefer", this.scorePrefer);
            jsonObject.put("ipv4ScoreDelay", this.ipv4ScoreDelay);
            jsonObject.put("networkType", this.networkType);
            jsonObject.put("ttl", this.ttl);
            if (!CollectionUtil.isEmpty(this.ips)) {
                JSONArray jsonArray = new JSONArray(this.ips);
                jsonObject.put("ip", jsonArray);
            }

            if (!CollectionUtil.isEmpty(this.ipv6s)) {
                JSONArray jsonArrayIpv6 = new JSONArray(this.ipv6s);
                jsonObject.put("ipv6", jsonArrayIpv6);
            }

            if (!CollectionUtil.isEmpty(this.availableIps)) {
                JSONArray jsonArray = new JSONArray(this.availableIps);
                jsonObject.put("useIp", jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.host);
        dest.writeStringList(this.ips);
        dest.writeStringList(this.ipv6s);
        dest.writeStringList(this.availableIps);
        dest.writeLong(this.ttl);
        dest.writeLong(this.refreshTime);
        dest.writeByte((byte)(this.isScore ? 1 : 0));
        dest.writeString(this.scorePrefer);
        dest.writeInt(this.ipv4ScoreDelay);
        dest.writeInt(this.scoreCount);
        dest.writeByte((byte)(this.isPending ? 1 : 0));
        dest.writeString(this.networkType);
        dest.writeString(this.responseRawJson);
    }

    public void readFromParcel(Parcel source) {
        this.host = source.readString();
        this.ips = source.createStringArrayList();
        this.ipv6s = source.createStringArrayList();
        this.availableIps = source.createStringArrayList();
        this.ttl = source.readLong();
        this.refreshTime = source.readLong();
        this.isScore = source.readByte() != 0;
        this.scorePrefer = source.readString();
        this.ipv4ScoreDelay = source.readInt();
        this.scoreCount = source.readInt();
        this.isPending = source.readByte() != 0;
        this.networkType = source.readString();
        this.responseRawJson = source.readString();
    }

    protected DomainInfo(Parcel in) {
        this.host = in.readString();
        this.ips = in.createStringArrayList();
        this.ipv6s = in.createStringArrayList();
        this.availableIps = in.createStringArrayList();
        this.ttl = in.readLong();
        this.refreshTime = in.readLong();
        this.isScore = in.readByte() != 0;
        this.scorePrefer = in.readString();
        this.ipv4ScoreDelay = in.readInt();
        this.scoreCount = in.readInt();
        this.isPending = in.readByte() != 0;
        this.networkType = in.readString();
        this.responseRawJson = in.readString();
    }
}
