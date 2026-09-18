package com.netease.httpdns.request.filter.chainhandler;

import java.util.List;

public class RealHandlerChain<T, R> implements IChainHandler.Chain<T, R> {
    private List<IChainHandler<T, R>> filterHandlers;
    private int index;
    private T filterObjList;

    public RealHandlerChain(List<IChainHandler<T, R>> filterHandlers, int index, T handlerObj) {
        this.filterHandlers = filterHandlers;
        this.index = index;
        this.filterObjList = handlerObj;
    }

    public T handlerObj() {
        return this.filterObjList;
    }

    public R proceed(T handlerObj) throws Exception {
        if (this.index >= this.filterHandlers.size()) {
            throw new Exception();
        } else {
            RealHandlerChain<T, R> next = new RealHandlerChain<T, R>(this.filterHandlers, this.index + 1, handlerObj);
            IChainHandler<T, R> filterHandler = (IChainHandler)this.filterHandlers.get(this.index);
            return (R)filterHandler.handler(next);
        }
    }
}
