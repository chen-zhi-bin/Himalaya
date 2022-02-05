package com.program.himalaya.interfaces;

import com.program.himalaya.base.IBasePresnter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresnter<IPlayerCallback> {

    /**
     * 播放
     */
    void play();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 播放上一首
     */
    void playpre();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式
     * @param mode
     */
    void swichPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放内容
     */
    void getPlayList();

    /**
     * 根据节目的位置进行播放
     * @param index 节目在列表的位置
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 判断播放器是否在播放
     * @return
     */
    boolean isPlay();

    /**
     * 反转播放器列表内容
     */
    void reversePlayList();

    /**
     * 播放专辑的第一个节目
     * @param id
     */
    void playByAlbumId(long id);
}
