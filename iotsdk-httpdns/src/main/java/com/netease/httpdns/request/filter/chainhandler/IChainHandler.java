package com.netease.httpdns.request.filter.chainhandler;

public interface IChainHandler<T, R> {
    R handler(Chain<T, R> var1) throws Exception;

    public interface Chain<T, R> {
        T handlerObj();

        R proceed(T var1) throws Exception;
    }
}