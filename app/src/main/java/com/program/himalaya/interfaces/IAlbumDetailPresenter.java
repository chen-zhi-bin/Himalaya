package com.program.himalaya.interfaces;

import com.program.himalaya.base.IBasePresnter;

public interface IAlbumDetailPresenter extends IBasePresnter<IAlbumDetailViewCallback> {
    /**
     * 下拉刷新
     */
    void pull2RefreshMore();
    /*
    上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId,int page);

    //IBasePresnter
//    /**
//     * 注册UI通知接口
//     * @param detailViewCallback
//     */
//    void  registerViewCallback(IAlbumDetailViewCallback detailViewCallback);
//
//    /**
//     * 删除UI通知接口
//     * @param detailViewCallback
//     */
//    void  ungisterViewCallback(IAlbumDetailViewCallback detailViewCallback);

}
