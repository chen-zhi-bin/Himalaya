package com.program.himalaya.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.program.himalaya.base.BaseApplication;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class HistoryDao implements IHistoryDao {

    private String TAG = "HistoryDao";
    private final XimalayaDBHelper mDbHelper;
    private IHistoryDaoCallback mCallback = null;
    private Object mLoack = new Object();

    public HistoryDao() {
        mDbHelper = new XimalayaDBHelper(BaseApplication.getAppContext());
    }

    @Override
    public void setCallback(IHistoryDaoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void addHistory(Track track) {
        synchronized (mLoack) {
            SQLiteDatabase db = null;
            boolean isSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();

//                int delete = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
//                LogUtil.d(TAG,"DeleteRuslt = =="+delete);

                ContentValues values = new ContentValues();
                //封装数据
                values.put(Constants.HISTORY_TRACK_ID,track.getDataId());
                values.put(Constants.HISTORY_TITLE,track.getTrackTitle());
                values.put(Constants.HISTORY_PLAY_COUNT,track.getPlayCount());
                values.put(Constants.HISTORY_PLAY_DURATION,track.getDuration());
                values.put(Constants.HISTORY_UPDATA_TIME,track.getUpdatedAt());
                values.put(Constants.HISTORY_COVER,track.getCoverUrlLarge());
                values.put(Constants.HISTORY_AUTHOR,track.getAnnouncer().getNickname());
                //插入数据
                db.insert(Constants.HISTORY_TB_NAME, null, values);

                db.setTransactionSuccessful();
                isSuccess = true;

//                int delete = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_ID + "=?", new String[]{track.getDataId() + ""});
//                int delete1 = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TITLE + "=?", new String[]{track.getTrackTitle()});
//                LogUtil.d(TAG,"id==========="+delete);
//                LogUtil.d(TAG,"id==========="+delete1);
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryAdd(isSuccess);
                }
            }
        }
    }

    @Override
    public void delHistory(Track track) {
        synchronized (mLoack) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                ContentValues contentValues = new ContentValues();
                //插入数据
                int delete = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
                LogUtil.d(TAG, "delete=" + delete);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                    if (mCallback != null) {
                        mCallback.onHistoryDel(isDeleteSuccess);
                    }
                }
            }
        }

    }

    @Override
    public void clearHistory() {
        synchronized (mLoack) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDbHelper.getWritableDatabase();
                db.beginTransaction();
                ContentValues contentValues = new ContentValues();
                //插入数据
                int delete = db.delete(Constants.HISTORY_TB_NAME, null, null);
                LogUtil.d(TAG, "delete=" + delete);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                    if (mCallback != null) {
                        mCallback.onHistoriesClear(isDeleteSuccess);
                    }
                }
            }
        }
    }

    @Override
    public void listHistories() {
        synchronized (mLoack) {
            //从数据表中查到历史记录
            SQLiteDatabase db = null;
            List<Track> histories = new ArrayList<>();
            try {
                db = mDbHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor currsor = db.query(Constants.HISTORY_TB_NAME, null, null, null, null, null, "_id desc");//以id逆序排列
                LogUtil.d(TAG,"currsor"+currsor.moveToNext());
                while (currsor.moveToNext()) {
                    Track track = new Track();
                    int trackId = currsor.getInt(currsor.getColumnIndex(Constants.HISTORY_TRACK_ID));
                    track.setDataId(trackId);
                    String title = currsor.getString(currsor.getColumnIndex(Constants.HISTORY_TITLE));
                    track.setTrackTitle(title);
                    int playCount = currsor.getInt(currsor.getColumnIndex(Constants.HISTORY_PLAY_COUNT));
                    track.setPlayCount(playCount);
                    int duration = currsor.getInt(currsor.getColumnIndex(Constants.HISTORY_PLAY_DURATION));
                    track.setDuration(duration);
                    long updataTime = currsor.getLong(currsor.getColumnIndex(Constants.HISTORY_UPDATA_TIME));
                    track.setUpdatedAt(updataTime);
                    String cover = currsor.getString(currsor.getColumnIndex(Constants.HISTORY_COVER));
                    track.setCoverUrlLarge(cover);
                    track.setCoverUrlSmall(cover);
                    track.setCoverUrlMiddle(cover);
                    String author = currsor.getString(currsor.getColumnIndex(Constants.HISTORY_AUTHOR));
                    Announcer announcer=new Announcer();
                    announcer.setNickname(author);
                    track.setAnnouncer(announcer);
                    histories.add(track);
                    LogUtil.d(TAG,"track"+track);
                }
                LogUtil.d(TAG,"histories"+histories);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                //通知出去
                if (mCallback != null) {
                    mCallback.onHistoriesLoaded(histories);
                }
            }
        }
    }
}
