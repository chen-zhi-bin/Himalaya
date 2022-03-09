package com.program.himalaya.presenters;

import com.program.himalaya.base.BaseApplication;
import com.program.himalaya.data.HistoryDao;
import com.program.himalaya.data.IHistoryDao;
import com.program.himalaya.data.IHistoryDaoCallback;
import com.program.himalaya.interfaces.IHistoryCallback;
import com.program.himalaya.interfaces.IHistoryPresenter;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 历史数量最大值100条，超过100条则先删除前面的
 */
public class HistoryPresenter implements IHistoryPresenter, IHistoryDaoCallback {

    private static final String TAG = "HistoryPresenter";
    private List<IHistoryCallback> mCallback = new ArrayList<>();

    private final IHistoryDao mHistoryDao;
    private List<Track> mCurrentHistories=new ArrayList<>();
    private Track mCurrentAddTrack = null;

    private HistoryPresenter(){
        mHistoryDao = new HistoryDao();
        mHistoryDao.setCallback(this);
        listHistories();
    }

    private static HistoryPresenter sHistoryPresenter = null;
    public static HistoryPresenter getHistoryPresenter(){
        if (sHistoryPresenter==null) {
            synchronized (HistoryPresenter.class){
                if (sHistoryPresenter ==null) {
                    sHistoryPresenter =new HistoryPresenter();
                }
            }
        }
        return sHistoryPresenter;
    }
    @Override
    public void listHistories() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if(mHistoryDao != null) {
                    mHistoryDao.listHistories();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private boolean isDoDelAsOutOfSize = false;
    @Override
    public void addHistory(Track track) {
        //需要判断是否有100条了
        if (mCurrentHistories != null&&mCurrentHistories.size()>= Constants.MAX_HISTORY_COUNT) {

            isDoDelAsOutOfSize =true;
            this.mCurrentAddTrack =track;
            //先删除最前的1条
            delHistory(mCurrentHistories.get(mCurrentHistories.size()-1));
        }else {
            LogUtil.d(TAG,"mCurrentHistories"+mCurrentHistories);
            LogUtil.d(TAG,"mCurrentHistories size =="+mCurrentHistories.size());
            doAddHistory(track);
        }
    }

    private void doAddHistory(Track track) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.addHistory(track);
                    LogUtil.d(TAG,"track"+track);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void delHistory(Track track) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.delHistory(track);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void clearHistory() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.clearHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void registerViewCallback(IHistoryCallback iHistoryCallback) {
        //ui注册过来
        if (!mCallback.contains(iHistoryCallback)) {
            mCallback.add(iHistoryCallback);
        }
    }

    @Override
    public void ungisterViewCallback(IHistoryCallback iHistoryCallback) {
        //删除ui回调
        mCallback.remove(iHistoryCallback);
    }

    @Override
    public void onHistoryAdd(boolean isSuccess) {
        //nothing to do
        listHistories();
    }

    @Override
    public void onHistoryDel(boolean isSuccess) {
        //nothing to do
        if (isDoDelAsOutOfSize && mCurrentAddTrack!=null) {
            //添加当前的数据到数据库中
            isDoDelAsOutOfSize=false;
            addHistory(mCurrentAddTrack);
        }else {
            listHistories();
        }
    }

    @Override
    public void onHistoriesLoaded(List<Track> tracks) {
        this.mCurrentHistories = tracks;
        LogUtil.d(TAG,"history size-->"+tracks.size());
        LogUtil.d(TAG,"mCurrentHistories size-+++"+mCurrentHistories);
        //通知UI更新数据
        BaseApplication.getsHandler().post(new Runnable() {
            @Override
            public void run() {
                for (IHistoryCallback iHistoryCallback : mCallback) {
                    iHistoryCallback.onHistoriesLoaded(tracks);
                }
            }
        });
    }

    @Override
    public void onHistoriesClear(boolean isSuccess) {
        //nothing to do
        listHistories();
    }
}
