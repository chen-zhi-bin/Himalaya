package com.program.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface ISubscriptionCallback {

    /**
     * 添加时调用通知UI
     * @param isSuccess
     */
    void onAddResult(boolean isSuccess);

    /**
     * 删除订阅的回调方法
     * @param isSuccess
     */
    void onDeleteResult(boolean isSuccess);

    /**
     * 订阅专辑加载的结果回调方法
     * @param albums
     */
    void onSubscritptonsLoaded(List<Album> albums);

    /**
     * 订阅数量满了
     */
    void onSubFull();
}
