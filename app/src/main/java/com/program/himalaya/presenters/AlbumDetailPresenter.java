package com.program.himalaya.presenters;

import android.util.Log;

import androidx.annotation.Nullable;

import com.program.himalaya.api.XimalayaApi;
import com.program.himalaya.interfaces.IAlbumDetailPresenter;
import com.program.himalaya.interfaces.IAlbumDetailViewCallback;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter";

    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private List<Track> mTracks = new ArrayList<>();

    private Album mTargsetAlbum = null;
    //当前的专辑id
    private int mCurrentAlbumId=-1;
    //当前页
    private int mCurrentPageIndex;

    private AlbumDetailPresenter() {

    }

    private static AlbumDetailPresenter sInsstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInsstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sInsstance == null) {
                    sInsstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInsstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {
        //去加载更多内容
        mCurrentPageIndex++;
        //传入true表示结果追加到列表的后方
        doLoaded(true);
    }

    private void doLoaded(final boolean isLoadMore){
        XimalayaApi ximalayaApi=XimalayaApi.getXimalayaApi();
        ximalayaApi.getAlbumDetail(new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG, "tracks size -->" + tracks.size());
                    if (isLoadMore) {
                        //这个是上拉加载，结果放到后面
                        mTracks.addAll(mTracks.size()-1,tracks);
                        int size = tracks.size();
                        handlerLoaderMoreResult(size);
                    }else {
                        //这个是下加载，结果放到前面
                        mTracks.addAll(tracks);
                    }
                    handlerAlbumDetailResult(mTracks);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (isLoadMore) {
                    mCurrentPageIndex--;
                }
                LogUtil.d(TAG, "errorCode--->" + errorCode);
                LogUtil.d(TAG, "errorMsg--->" + errorMsg);
                handlerError(errorCode,errorMsg);
            }
        },mCurrentAlbumId,mCurrentPageIndex);
    }

    /**
     * 处理加载更多的接口
     * @param size
     */
    private void handlerLoaderMoreResult(int size) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onLoaderMoreFinished(size);
        }
    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        mTracks.clear();
        this.mCurrentAlbumId =albumId;
        this.mCurrentPageIndex=page;
        doLoaded(false);
    }
    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }
    /**
     * 如果发生错误，通知UI
     * @param errorCode
     * @param errorMsg
     */
    private void handlerError(int errorCode, String errorMsg) {
        for (IAlbumDetailViewCallback callback:mCallbacks){
            callback.onNetworkError(errorCode,errorMsg);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargsetAlbum != null) {
                detailViewCallback.onAblumLoaded(mTargsetAlbum);
            }
        }
    }

    @Override
    public void ungisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        mCallbacks.remove(detailViewCallback);
    }

    public void setTargetAlbum(Album targetAlbum) {
        this.mTargsetAlbum = targetAlbum;
    }
}
