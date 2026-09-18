package com.netease.httpdns.request.filter.chainhandler;

import java.util.List;

public interface ICompositeHandler<T, R> {
    R handler(T var1) throws Exception;

    void addChainHandler(IChainHandler<T, R> var1);

    List<IChainHandler<T, R>> getChainHandlers();
}
