package com.program.himalaya.presenters;

import androidx.annotation.Nullable;

import com.program.himalaya.data.XimalayaApi;
import com.program.himalaya.interfaces.IRecommendPresenter;
import com.program.himalaya.interfaces.IRecommendViewCallback;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.List;

//实现类
public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Album> mCurrentRecommend=null;

    private RecommendPresenter() {

    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取当前的推荐专辑列表
     * @return  推荐专辑，使用前判空
     */
    public List<Album> getCurrentRecommend(){
        return mCurrentRecommend;
    }

    /**
     * 获取推荐内容，其实就是猜你喜欢
     */
    @Override
    public void getRecommendList() {
        //推荐喜欢内容
        //封装参数
        updateLoadnig();
        XimalayaApi ximalayaApi = XimalayaApi.getXimalayaApi();
        ximalayaApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "thread.name-->" + Thread.currentThread().getName());
                //返回成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //数据回来以后，要更新UI
//                    upRecommandUI(albumList);
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //返回出错
                LogUtil.d(TAG, "error==>" + i);
                LogUtil.d(TAG, "errorMSG==>" + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if (mCallbacks!=null) {
            for (IRecommendViewCallback callback:mCallbacks){
                callback.onNetworkError();
            }
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if (albumList!=null){
//            //测试，清空一下，让界面显示空
//            albumList.clear();
            if (albumList.size()==0){
                for (IRecommendViewCallback callback:mCallbacks){
                    callback.onEmpty();
                }
            }else {
                for(IRecommendViewCallback callback:mCallbacks){
                    callback.onRecommendListLoaded(albumList);
                }
                this.mCurrentRecommend = albumList;
            }
        }
    }

    private void updateLoadnig(){
        for(IRecommendViewCallback callback:mCallbacks){
            callback.onLoading();
        }
    }
    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void ungisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
