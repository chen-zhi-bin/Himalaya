package com.program.himalaya.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;
import java.util.TreeMap;

public interface IHistoryDaoCallback {

    /**
     * 添加历史结果
     * @param isSuccess
     */
    void onHistoryAdd(boolean isSuccess);

    /**
     * 删除历史结果
     * @param isSuccess
     */
    void onHistoryDel(boolean isSuccess);

    /**
     * 历史数据加载的结果
     * @param tracks
     */
    void onHistoriesLoaded(List<Track> tracks);

    /**
     * 历史内容清除结果
     * @param isSuccess
     */
    void onHistoriesClear(boolean isSuccess);
}
