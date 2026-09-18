package com.netease.httpdns.request.filter;

import com.netease.httpdns.request.filter.chainhandler.ICompositeHandler;
import java.util.List;

public interface IDomainCompositeFilter extends ICompositeHandler<List<String>, List<String>> {
    void release(List<String> var1) throws Exception;
}