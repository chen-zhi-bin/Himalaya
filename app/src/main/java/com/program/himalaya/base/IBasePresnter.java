package com.program.himalaya.base;

public interface IBasePresnter<T>{

    /**
     * 注册UI的回调接口
     * @param t
     */
    void registerViewCallback(T t);

    /**、
     * 取消注册
     * @param m
     */
    void ungisterViewCallback(T t);
}
