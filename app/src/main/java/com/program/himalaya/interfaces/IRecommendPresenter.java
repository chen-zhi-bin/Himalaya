package com.program.himalaya.interfaces;

import com.program.himalaya.base.IBasePresnter;

public interface IRecommendPresenter extends IBasePresnter<IRecommendViewCallback> {
    /**
     * 获取推荐内容
     */
    void getRecommendList();
    /**
     * 下拉刷新
     */
    void pull2RefreshMore();
    /*
    上拉加载更多
     */
    void loadMore();

    //IBasePresnter
//    /**
//     * 这个方法用于注册UI回调
//     * @param callback
//     */
//    void registerViewCallback(IRecommendViewCallback callback);
//
//    /**
//     * 取消UI的回调注册
//     * @param callback
//     */
//    void ungisterViewCallback(IRecommendViewCallback callback);
}
