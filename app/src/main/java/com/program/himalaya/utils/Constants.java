package com.program.himalaya.utils;

public class Constants {
    //获取推荐列表的专辑数量
    public static int COUNT_RENCOMMEND= 50;

    //默认请求数量
    public static int COINT_DETAULT =50;

    //热词的数量
    public static int COUNT_HOT_WORD =10;

    //数据库相关常量
    public static final String DB_NAME="ximala.db";
    //数据库版本
    public static final int DB_VERSION_CODE=1;

    //订阅的表明
    public static final String SUB_TB_NAME ="yb_subscription";
    public static final String SUB_ID="_id";
    public static final String SUB_COVER_URL="coverurl";
    public static final String SUB_TITLE="title";
    public static final String SUB_DESCRIPTION="description";
    public static final String SUB_PLAY_COUNT ="playCount";
    public static final String SUB_TRACK_COUNT ="trackCount";
    public static final String SUB_AUTHORNAME="authorName";
    public static final String SUB_ALBUM_ID ="albumId";
    //订阅最多个数
    public static final int MAX_SUB_COUNT=100;
    //历史记录表名
    public static final String HISTORY_TB_NAME ="tb_history";
    public static final String HISTORY_ID ="_id";
    public static final String HISTORY_TRACK_ID ="historyTrackId";
    public static final String HISTORY_TITLE ="historyTitle";
    public static final String HISTORY_PLAY_COUNT ="historyPlayCount";
    public static final String HISTORY_PLAY_DURATION ="historyDuration";
    public static final String HISTORY_UPDATA_TIME ="historyUpdataTime";
    public static final String HISTORY_COVER ="historyCover";
    public static final String HISTORY_AUTHOR = "history_author";
    //最大的历史记录数
    public static final int MAX_HISTORY_COUNT = 100;
}
