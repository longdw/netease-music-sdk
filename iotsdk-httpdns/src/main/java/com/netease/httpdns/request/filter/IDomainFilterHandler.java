package com.netease.httpdns.request.filter;

import com.netease.httpdns.request.filter.chainhandler.IChainHandler;
import java.util.List;

public interface IDomainFilterHandler extends IChainHandler<List<String>, List<String>> {
    void release(List<String> var1) throws Exception;
}
