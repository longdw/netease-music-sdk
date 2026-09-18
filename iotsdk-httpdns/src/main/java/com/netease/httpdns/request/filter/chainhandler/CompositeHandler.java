package com.netease.httpdns.request.filter.chainhandler;

import java.util.ArrayList;
import java.util.List;

public class CompositeHandler<T, R> implements ICompositeHandler<T, R> {
    private final List<IChainHandler<T, R>> filterHandlerList = new ArrayList();

    public R handler(T handlerObj) throws Exception {
        RealHandlerChain<T, R> realFilterChain = new RealHandlerChain(this.filterHandlerList, 0, handlerObj);
        return (R)realFilterChain.proceed(handlerObj);
    }

    public void addChainHandler(IChainHandler<T, R> chainHandler) {
        this.filterHandlerList.add(chainHandler);
    }

    public List<IChainHandler<T, R>> getChainHandlers() {
        return this.filterHandlerList;
    }
}