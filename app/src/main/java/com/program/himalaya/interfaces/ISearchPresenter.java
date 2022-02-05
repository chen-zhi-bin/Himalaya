package com.program.himalaya.interfaces;

import com.program.himalaya.base.IBasePresnter;

public interface ISearchPresenter extends IBasePresnter<ISeacherCallback> {

    /**
     * 进行搜索
     * @param keyword
     */
    void deSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 加载更多的搜索结果
     */
    void loadMore();

    /**
     * 获取热词
     */
    void getHotWord();

    /**
     * 获取推荐的关键字（相关的关键字）
     * @param keyword
     */
    void getRecommendWord(String keyword);
}
