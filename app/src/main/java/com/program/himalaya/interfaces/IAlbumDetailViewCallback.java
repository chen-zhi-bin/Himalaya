package com.program.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallback {

    /**
     * 专辑详情内容
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 网络错误
     */
    void onNetworkError(int errorCode, String errorMsg);

    /**
     * 把album传给UI使用
     * @param album
     */
    void onAblumLoaded(Album album);

    /**
     * 加载更多
     * @param size  size>0表示加载更多,否则表示加载失败
     */
    void onLoaderMoreFinished(int size);

    /**
     * 下拉加载更多结果,
     * @param size  size>0表示加载更多,否则表示加载失败
     */
    void onRefreshFinished(int size);
}
