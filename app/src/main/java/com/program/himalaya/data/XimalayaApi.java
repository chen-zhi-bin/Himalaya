package com.program.himalaya.data;

import com.program.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.Map;

public class XimalayaApi {

    private XimalayaApi() {
    }

    private static XimalayaApi sXimalayaApi;

    public static XimalayaApi getXimalayaApi() {
        if (sXimalayaApi == null) {
            synchronized (XimalayaApi.class) {
                if (sXimalayaApi == null) {
                    sXimalayaApi = new XimalayaApi();
                }
            }
        }
        return sXimalayaApi;
    }

    /**
     * 获取推荐的内容
     *
     * @param callBack 请求结果的回调
     */
    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RENCOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, callBack);
    }

    /**
     * 根据专辑的id获取到专辑内容
     *
     * @param callBack  获取专辑详情回调接口
     * @param albumId   专辑的ID
     * @param pageIndex 页码
     */
    public void getAlbumDetail(IDataCallBack<TrackList> callBack, long albumId, int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");     //设置正逆序
        map.put(DTransferConstants.PAGE, pageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COINT_DETAULT + "");
        CommonRequest.getTracks(map, callBack);
    }

    /**
     * 根据关键字进行搜索
     * @param keyword
     */
    public void searchByKeyword(String keyword, int page, IDataCallBack<SearchAlbumList> callBack) {
        Map<String,String> map=new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY,keyword);
        map.put(DTransferConstants.PAGE,page+"");
        map.put(DTransferConstants.PAGE_SIZE,Constants.COINT_DETAULT+"");
        CommonRequest.getSearchedAlbums(map,callBack);
    }

    /**
     * 获取推荐的热词
     * @param callBack
     */
    public void getHotWords(IDataCallBack<HotWordList> callBack){
        Map<String,String> map =new HashMap<>();
        map.put(DTransferConstants.TOP,String.valueOf(Constants.COUNT_HOT_WORD));
        CommonRequest.getHotWords(map,callBack);
    }

    /**
     * 根据关键字获取联想词
     * @param keyword   关键字
     * @param callBack  回调
     */
    public void getSuggestWord(String keyword, IDataCallBack<SuggestWords> callBack){
        Map<String,String> map =new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY,keyword);
        CommonRequest.getSuggestWord(map,callBack);
    }
}
