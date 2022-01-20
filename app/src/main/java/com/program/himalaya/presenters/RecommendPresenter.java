package com.program.himalaya.presenters;

import androidx.annotation.Nullable;

import com.program.himalaya.interfaces.IRecommendPresenter;
import com.program.himalaya.interfaces.IRecommendViewCallback;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实现类
public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();

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
     * 获取推荐内容，其实就是猜你喜欢
     */
    @Override
    public void getRecommendList() {
        //推荐喜欢内容
        //封装参数
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RENCOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
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
            }
        });
    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI
        if (mCallbacks!=null) {
            for(IRecommendViewCallback callback:mCallbacks){
                callback.onRecommendListLoaded(albumList);
            }
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
            mCallbacks.remove(mCallbacks);
        }
    }
}
