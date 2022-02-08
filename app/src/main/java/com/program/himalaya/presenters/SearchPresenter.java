package com.program.himalaya.presenters;

import androidx.annotation.Nullable;

import com.program.himalaya.data.XimalayaApi;
import com.program.himalaya.interfaces.ISeacherCallback;
import com.program.himalaya.interfaces.ISearchPresenter;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {

    private List<Album> mSearchResult = new ArrayList<>();

    private static final String TAG = "SearchPresenter";
    //当前的搜索关键字
    private String mCurrentKeyWord = null;
    private final XimalayaApi mXimalayaApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;

    private SearchPresenter() {
        mXimalayaApi = XimalayaApi.getXimalayaApi();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getSearchPresenter() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }

    private List<ISeacherCallback> mCallbacks = new ArrayList<>();

    @Override
    public void deSearch(String keyword) {
        mCurrentPage = DEFAULT_PAGE;
        mSearchResult.clear();
        //用于得新搜索
        //当网络不好时，用户会重新搜索
        this.mCurrentKeyWord = keyword;
        search(keyword);

    }

    private void search(String keyword) {
        mXimalayaApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                mSearchResult.addAll(albums);
                if (albums != null) {
                    LogUtil.d(TAG, "albums size --->" + albums.size());
                    if (mIsLoadMore) {
                        for (ISeacherCallback iSeacherCallback : mCallbacks) {
                                iSeacherCallback.onLoadMoreResult(mSearchResult,albums.size()!= 0);
                        }

                        mIsLoadMore = false;
                    }else {

                        mIsLoadMore = false;
                    }
                    for (ISeacherCallback callback : mCallbacks) {
                        callback.onSearchResultLoaded(mSearchResult);
                    }
                } else {
                    LogUtil.d(TAG, "albums is null");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode=" + errorCode);
                LogUtil.d(TAG, "errorMsg=" + errorMsg);
                    for (ISeacherCallback iSeacherCallback : mCallbacks) {
                        if (mIsLoadMore) {
                            iSeacherCallback.onLoadMoreResult(mSearchResult,false);
                            mCurrentPage--;
                            mIsLoadMore = false;
                        }else {
                            iSeacherCallback.onError(errorCode,errorMsg);

                        }
                    }

            }
        });
    }

    @Override
    public void reSearch() {
        search(mCurrentKeyWord);
    }

    private boolean mIsLoadMore = false;

    @Override
    public void loadMore() {
        //判断是否有必要加载更多
        if (mSearchResult.size()< Constants.COINT_DETAULT) {
            for (ISeacherCallback iSeacherCallback : mCallbacks) {
                iSeacherCallback.onLoadMoreResult(mSearchResult,false);
            }
        }else {
            mIsLoadMore = true;
            mCurrentPage++;
            search(mCurrentKeyWord);
        }


    }

    @Override
    public void getHotWord() {
        //todo:做一个热词缓存
        mXimalayaApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(@Nullable HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    LogUtil.d(TAG, "hotWords size  -->" + hotWords.size());
                    LogUtil.d(TAG, "hotWords   -->" + hotWords);
                    for (ISeacherCallback callback : mCallbacks) {
                        callback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWords errorCode=" + errorCode);
                LogUtil.d(TAG, "getHotWords errorMsg=" + errorMsg);

            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        mXimalayaApi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(@Nullable SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    LogUtil.d(TAG, "keyWordList size --->" + keyWordList.size());
                    for (ISeacherCallback callback : mCallbacks) {
                        callback.onRecommendWordLoaded(keyWordList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getRecommendWord errorCode=" + errorCode);
                LogUtil.d(TAG, "getRecommendWord errorMsg=" + errorMsg);
                for (ISeacherCallback iSeacherCallback : mCallbacks) {
                    iSeacherCallback.onError(errorCode,errorMsg);
                }
            }
        });
    }

    @Override
    public void registerViewCallback(ISeacherCallback iSeacherCallback) {
        if (!mCallbacks.contains(iSeacherCallback)) {
            mCallbacks.add(iSeacherCallback);
        }
    }

    @Override
    public void ungisterViewCallback(ISeacherCallback iSeacherCallback) {
        mCallbacks.remove(iSeacherCallback);
    }
}
