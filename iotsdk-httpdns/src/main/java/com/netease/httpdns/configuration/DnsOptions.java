package com.netease.httpdns.configuration;

import android.text.TextUtils;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.usage.NLazy;
import com.netease.httpdns.cache.ICustomerSp;
import com.netease.httpdns.http.IHttpRequest;
import com.netease.httpdns.listener.ISort;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.util.IWeakNetwork;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DnsOptions {
    private static final int INIT_LIST_SIZE = 8;
    public static final long NOT_USE_TTL = -1L;
    public static final long NOT_DEPLOY_TLL = -2L;
    public static final int DEFAULT_TIME_OUT = 12000;
    public static final int DEFAULT_REQUEST_WAIT_TIME = 3000;
    public static final int DEFAULT_REQUEST_RETRY_COUNT = 5;
    private boolean isUseLazyLoad;
    private boolean isRefreshHotDomainCache;
    private boolean isOpenScope;
    private long userDefinedTTL;
    private List<String> domainBlackList;
    private List<String> domainHotList;
    private int httpTimeOut;
    private ICustomerSp sp;
    private IHttpRequest httpRequest;
    private int requestWaitTime;
    private int requestRetryCount;
    private boolean isOpenMutiRequest;
    private boolean openScore;
    private ISort customSort;
    private boolean isMergeLocalDNS;
    private String mergeLocalRegexValue;
    private boolean isOpenIpv6Request;
    private boolean isFilterBlackListWithRegular;
    private Set<String> blackListRegexValueSet;
    private Set<NLazy<Pattern>> blackListPatternSet;
    private boolean isRefreshExpiringCache;
    private boolean isUseHttp;
    private String productKey;
    private String customHttpDnsHost;
    private IWeakNetwork weakNetwork;

    private DnsOptions(Builder builder) {
        this.isUseLazyLoad = builder.isUseLazyLoad;
        this.isRefreshHotDomainCache = builder.isRefreshHotDomainCache;
        this.isOpenScope = builder.isOpenScope;
        this.userDefinedTTL = builder.userDefinedTTL;
        this.domainBlackList = builder.domainBlackList;
        this.domainHotList = builder.domainHotList;
        this.httpTimeOut = builder.httpTimeOut;
        this.sp = builder.sp;
        this.httpRequest = builder.httpRequest;
        this.requestWaitTime = builder.requestWaitTime;
        this.requestRetryCount = builder.requestRetryCount;
        this.isOpenMutiRequest = builder.isOpenMutiRequest;
        this.openScore = builder.openScore;
        this.customSort = builder.sort;
        this.isMergeLocalDNS = builder.isMergeLocalDNS;
        this.mergeLocalRegexValue = builder.mergeLocalRegexValue;
        this.isOpenIpv6Request = builder.isOpenIpv6Request;
        this.isFilterBlackListWithRegular = builder.isFilterBlackListWithRegular;
        this.blackListRegexValueSet = builder.blackListRegexValueSet;
        this.createBlackListPattern();
        this.isRefreshExpiringCache = builder.isRefreshExpiringCache;
        this.isUseHttp = builder.isUseHttp;
        this.productKey = builder.productKey;
        this.customHttpDnsHost = builder.customHttpDnsHost;
        this.weakNetwork = builder.weakNetwork;
    }

    private void createBlackListPattern() {
        Set<NLazy<Pattern>> patternSet = new HashSet();
        if (this.blackListRegexValueSet != null) {
            Set<String> blackList = new HashSet(this.blackListRegexValueSet);
            if (blackList != null && !blackList.isEmpty()) {
                for(final String blackDomain : blackList) {
                    NLazy<Pattern> patternNLazy = new NLazy(new NFunc0R<Pattern>() {
                        public Pattern call() {
                            try {
                                return Pattern.compile(blackDomain);
                            } catch (Exception e) {
                                DNSLog.e("[DnsOptions]createBlackListPattern error : " + e.getMessage());
                                return null;
                            }
                        }
                    });
                    patternSet.add(patternNLazy);
                }

                this.blackListPatternSet = new HashSet(patternSet);
            }
        }
    }

    public boolean isUseLazyLoad() {
        return this.isUseLazyLoad;
    }

    public boolean isRefreshHotDomainCache() {
        return this.isRefreshHotDomainCache;
    }

    public boolean isOpenScope() {
        return this.isOpenScope;
    }

    public boolean isOpenScore() {
        return this.openScore;
    }

    public long getUserDefinedTTL() {
        return this.userDefinedTTL;
    }

    public List<String> getHotNameList() {
        return this.domainHotList;
    }

    public List<String> getBlackNameList() {
        return this.domainBlackList == null ? null : new ArrayList(this.domainBlackList);
    }

    public Set<NLazy<Pattern>> getBlackListPatternSet() {
        return this.blackListPatternSet == null ? null : new HashSet(this.blackListPatternSet);
    }

    public int getHttpTimeOut() {
        return this.httpTimeOut;
    }

    public void setHotNameList(List<String> hotNameList) {
        if (hotNameList != null) {
            this.domainHotList.clear();
            this.domainHotList.addAll(hotNameList);
        }

    }

    public void addHotNameList(String hotName) {
        if (!TextUtils.isEmpty(hotName) && !this.domainHotList.contains(hotName)) {
            this.domainHotList.add(hotName);
        }

    }

    public void setDomainBlackList(List<String> domainBlackList) {
        if (domainBlackList != null) {
            this.domainBlackList.clear();
            this.domainBlackList.addAll(domainBlackList);
        }

    }

    public void addDomainBlackList(String blackDomain) {
        if (!TextUtils.isEmpty(blackDomain) && !this.domainBlackList.contains(blackDomain)) {
            this.domainBlackList.add(blackDomain);
        }

    }

    public IHttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    public ISort getCustomSort() {
        return this.customSort;
    }

    public ICustomerSp getSp() {
        return this.sp;
    }

    public int getRequestWaitTime() {
        return this.requestWaitTime;
    }

    public int getRequestRetryCount() {
        return this.requestRetryCount;
    }

    public boolean isOpenMutiRequest() {
        return this.isOpenMutiRequest;
    }

    public boolean isRefreshExpiringCache() {
        return this.isRefreshExpiringCache;
    }

    public boolean isDomainNeedMergeLocalDNS(String domain) {
        if (!this.isMergeLocalDNS) {
            return false;
        } else if (TextUtils.isEmpty(this.mergeLocalRegexValue)) {
            return true;
        } else {
            try {
                return Pattern.matches(this.mergeLocalRegexValue, domain);
            } catch (PatternSyntaxException e) {
                DNSLog.e("PatternSyntaxException : " + e.toString());
                return false;
            }
        }
    }

    public boolean isDomainNeedFilter(String domain) {
        if (!this.isFilterBlackListWithRegular) {
            return false;
        } else {
            Set<NLazy<Pattern>> patternSet = this.getBlackListPatternSet();
            if (patternSet != null && !patternSet.isEmpty()) {
                for(NLazy<Pattern> patternNLazy : patternSet) {
                    try {
                        if (patternNLazy == null) {
                            return false;
                        }

                        Pattern pattern = (Pattern)patternNLazy.get();
                        if (pattern == null) {
                            return false;
                        }

                        if (pattern.matcher(domain).matches()) {
                            return true;
                        }
                    } catch (Exception e) {
                        DNSLog.e("[DnsOptions]isDomainNeedFilter error : " + e.toString());
                        return false;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }

    public boolean isOpenIpv6Request() {
        return this.isOpenIpv6Request;
    }

    public boolean isUseHttp() {
        return this.isUseHttp;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getCustomHttpDnsHost() {
        return this.customHttpDnsHost;
    }

    public IWeakNetwork getWeakNetwork() {
        return this.weakNetwork;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public String toString() {
        return "DnsOptions{isUseLazyLoad=" + this.isUseLazyLoad + ", isRefreshHotDomainCache=" + this.isRefreshHotDomainCache + ", isOpenScope=" + this.isOpenScope + ", userDefinedTTL=" + this.userDefinedTTL + ", domainBlackList=" + this.domainBlackList + ", domainHotList=" + this.domainHotList + ", httpTimeOut=" + this.httpTimeOut + ", sp=" + this.sp + ", httpRequest=" + this.httpRequest + ", requestWaitTime=" + this.requestWaitTime + ", requestRetryCount=" + this.requestRetryCount + ", isOpenMutiRequest=" + this.isOpenMutiRequest + ", openScore=" + this.openScore + ", customSort=" + this.customSort + ", isMergeLocalDNS=" + this.isMergeLocalDNS + ", mergeLocalRegexValue='" + this.mergeLocalRegexValue + '\'' + ", isOpenIpv6Request=" + this.isOpenIpv6Request + ", isFilterBlackListWithRegular=" + this.isFilterBlackListWithRegular + ", blackListRegexValueSet=" + this.blackListRegexValueSet + ", blackListPatternSet=" + this.blackListPatternSet + ", isRefreshExpiringCache=" + this.isRefreshExpiringCache + ", isUseHttp=" + this.isUseHttp + ", productKey='" + this.productKey + '\'' + ", customHttpDnsHost='" + this.customHttpDnsHost + '\'' + ", weakNetwork=" + this.weakNetwork + '}';
    }

    public static class Builder {
        private int httpTimeOut = 12000;
        private boolean isUseLazyLoad = false;
        private boolean isOpenScope = false;
        private boolean isRefreshHotDomainCache = false;
        private long userDefinedTTL = -2L;
        private List<String> domainBlackList = new ArrayList(8);
        private List<String> domainHotList = new ArrayList(8);
        private ICustomerSp sp;
        private IHttpRequest httpRequest;
        private int requestWaitTime = 3000;
        private int requestRetryCount = 5;
        private boolean isOpenMutiRequest = false;
        private boolean openScore = false;
        private ISort sort;
        private boolean isMergeLocalDNS = false;
        private String mergeLocalRegexValue;
        private boolean isOpenIpv6Request = false;
        private boolean isFilterBlackListWithRegular = false;
        private Set<String> blackListRegexValueSet = new HashSet(8);
        private boolean isRefreshExpiringCache = true;
        private boolean isUseHttp = false;
        private String productKey;
        private String customHttpDnsHost;
        private IWeakNetwork weakNetwork;

        public Builder() {
        }

        public Builder(DnsOptions options) {
            this.isUseLazyLoad = options.isUseLazyLoad;
            this.isRefreshHotDomainCache = options.isRefreshHotDomainCache;
            this.isOpenScope = options.isOpenScope;
            this.userDefinedTTL = options.userDefinedTTL;
            this.domainBlackList = options.domainBlackList;
            this.domainHotList = options.domainHotList;
            this.httpTimeOut = options.httpTimeOut;
            this.sp = options.sp;
            this.httpRequest = options.httpRequest;
            this.requestWaitTime = options.requestWaitTime;
            this.requestRetryCount = options.requestRetryCount;
            this.isOpenMutiRequest = options.isOpenMutiRequest;
            this.openScore = options.openScore;
            this.sort = options.customSort;
            this.isMergeLocalDNS = options.isMergeLocalDNS;
            this.mergeLocalRegexValue = options.mergeLocalRegexValue;
            this.isOpenIpv6Request = options.isOpenIpv6Request;
            this.isFilterBlackListWithRegular = options.isFilterBlackListWithRegular;
            this.blackListRegexValueSet = options.blackListRegexValueSet;
            this.isRefreshExpiringCache = options.isRefreshExpiringCache;
            this.isUseHttp = options.isUseHttp;
            this.productKey = options.productKey;
            this.customHttpDnsHost = options.customHttpDnsHost;
            this.weakNetwork = options.weakNetwork;
        }

        /** @deprecated */
        @Deprecated
        public Builder setUseLazyLoad(boolean useLazyLoad) {
            this.isUseLazyLoad = useLazyLoad;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder setRefreshHotDomainCache(boolean refreshHotDomainCache) {
            this.isRefreshHotDomainCache = refreshHotDomainCache;
            return this;
        }

        public Builder setHttpTimeOut(int httpTimeOut) {
            this.httpTimeOut = httpTimeOut;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder isOpenScope(boolean openScope) {
            this.isOpenScope = openScope;
            return this;
        }

        public Builder setUserDefinedTTL(long userDefinedTTL) {
            this.userDefinedTTL = userDefinedTTL;
            return this;
        }

        public Builder setHotNameList(List<String> hotNameList) {
            if (hotNameList != null) {
                this.domainHotList.clear();
                this.domainHotList.addAll(hotNameList);
            }

            return this;
        }

        public Builder addHotNameList(String hotName) {
            if (!TextUtils.isEmpty(hotName) && !this.domainHotList.contains(hotName)) {
                this.domainHotList.add(hotName);
            }

            return this;
        }

        public Builder setDomainBlackList(List<String> blackNameList) {
            if (blackNameList != null) {
                this.domainBlackList.clear();
                this.domainBlackList.addAll(blackNameList);
            }

            return this;
        }

        public Builder addBlackNameList(String blackName) {
            if (!TextUtils.isEmpty(blackName) && !this.domainBlackList.contains(blackName)) {
                this.domainBlackList.add(blackName);
            }

            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder setCustomerSharedPreference(ICustomerSp iCustomerSp) {
            this.sp = iCustomerSp;
            return this;
        }

        public Builder setHttpRequest(IHttpRequest httpRequest) {
            this.httpRequest = httpRequest;
            return this;
        }

        public Builder setRequestWaitTime(int requestWaitTime) {
            this.requestWaitTime = requestWaitTime;
            return this;
        }

        public Builder setRequestRetryCount(int requestRetryCount) {
            this.requestRetryCount = requestRetryCount;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public Builder setOpenMutiRequest(boolean openMutiRequest) {
            this.isOpenMutiRequest = openMutiRequest;
            return this;
        }

        public Builder setOpenScore(boolean openScore) {
            this.openScore = openScore;
            return this;
        }

        public Builder setSort(ISort sort) {
            this.sort = sort;
            return this;
        }

        public Builder isOpenMergeLocalDNS(boolean isMergeLocalDNS) {
            this.isMergeLocalDNS = isMergeLocalDNS;
            return this;
        }

        public Builder setMergeLocalDNSRegex(String regexValue) {
            this.mergeLocalRegexValue = regexValue;
            return this;
        }

        public Builder setOpenIpv6Request(boolean openIpv6Request) {
            this.isOpenIpv6Request = openIpv6Request;
            return this;
        }

        public Builder setFilterBlackListWithRegular(boolean filterBlackListWithRegular) {
            this.isFilterBlackListWithRegular = filterBlackListWithRegular;
            return this;
        }

        public Builder addFilterBlackListRegexValue(String filterBlackListRegexValue) {
            if (!TextUtils.isEmpty(filterBlackListRegexValue)) {
                this.blackListRegexValueSet.add(filterBlackListRegexValue);
            }

            return this;
        }

        public Builder addBlackListRegexValueSet(List<String> blackListRegexValueList) {
            if (blackListRegexValueList != null && !blackListRegexValueList.isEmpty()) {
                this.blackListRegexValueSet.addAll(blackListRegexValueList);
            }

            return this;
        }

        public Builder clearBlackListRegexValueSet() {
            this.blackListRegexValueSet.clear();
            return this;
        }

        public Builder setRefreshExpiringCache(boolean refreshExpiringCache) {
            this.isRefreshExpiringCache = refreshExpiringCache;
            return this;
        }

        public Builder setUseHttp(boolean useHttp) {
            this.isUseHttp = useHttp;
            return this;
        }

        public Builder setProductKey(String productKey) {
            this.productKey = productKey;
            return this;
        }

        public Builder setCustomHttpDnsHost(String customHttpDnsHost) {
            this.customHttpDnsHost = customHttpDnsHost;
            return this;
        }

        public void setWeakNetwork(IWeakNetwork weakNetwork) {
            this.weakNetwork = weakNetwork;
        }

        public DnsOptions build() {
            return new DnsOptions(this);
        }

        public static DnsOptions createSimpler() {
            return (new Builder()).build();
        }
    }
}