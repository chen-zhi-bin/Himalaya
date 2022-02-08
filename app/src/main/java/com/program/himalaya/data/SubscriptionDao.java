package com.program.himalaya.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.program.himalaya.base.BaseApplication;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionDao implements ISubDao{
    private static final SubscriptionDao outInstance = new SubscriptionDao();
    private static final String TAG = "SubscriptionDao";
    private final XimalayaDBHelper mXimalayaDBHelper;
    private ISubDaoCallback mCallback =null;

    public static SubscriptionDao getInstance(){
        return outInstance;
    }

    private SubscriptionDao(){
        mXimalayaDBHelper = new XimalayaDBHelper(BaseApplication.getAppContext());
    }

    @Override
    public void setCallback(ISubDaoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void addAlbum(Album album) {
        SQLiteDatabase db =null;
        boolean isAddSuccess = false;
        try {
             db = mXimalayaDBHelper.getWritableDatabase();
             db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            //封装数据
            contentValues.put(Constants.SUB_COVER_URL,album.getCoverUrlLarge());
            contentValues.put(Constants.SUB_TITLE,album.getAlbumTitle());
            contentValues.put(Constants.SUB_DESCRIPTION,album.getAlbumIntro());
            contentValues.put(Constants.SUB_PLAY_COUNT,album.getPlayCount());
            contentValues.put(Constants.SUB_TRACK_COUNT,album.getIncludeTrackCount());
            contentValues.put(Constants.SUB_AUTHORNAME,album.getAnnouncer().getNickname());
            contentValues.put(Constants.SUB_ALBUM_ID,album.getId());
            //插入数据
             db.insert(Constants.SUB_TB_NAME,null,contentValues);
             db.setTransactionSuccessful();
             isAddSuccess =true;
        }catch (Exception e){
            e.printStackTrace();
            isAddSuccess=false;
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if (mCallback != null) {
                mCallback.onAddResult(isAddSuccess);
            }
        }
    }

    @Override
    public void delAlbum(Album album) {

        SQLiteDatabase db =null;
        boolean isDeleteSuccess = false;
        try {
            db = mXimalayaDBHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            //插入数据
            int delete = db.delete(Constants.SUB_TB_NAME, Constants.SUB_ALBUM_ID + "=?", new String[]{album.getId() + ""});
            LogUtil.d(TAG,"delete="+delete);
            db.setTransactionSuccessful();
            isDeleteSuccess =true;
        }catch (Exception e){
            e.printStackTrace();
            isDeleteSuccess = false;
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
                if (mCallback != null) {
                    mCallback.onDelResult(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void listAlbum() {
        SQLiteDatabase db =null;
        List<Album> result =new ArrayList<>();
        try {
            db = mXimalayaDBHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor query = db.query(Constants.SUB_TB_NAME, null, null, null, null, null, "_id desc");
            //封装数据
            while (query.moveToNext()) {
                Album album=new Album();
                //图片
                String coverUrl = query.getString(query.getColumnIndex(Constants.SUB_COVER_URL));
                album.setCoverUrlLarge(coverUrl);
                //
                String title = query.getString(query.getColumnIndex(Constants.SUB_TITLE));
                album.setAlbumTitle(title);

                String deceriprtion = query.getString(query.getColumnIndex(Constants.SUB_DESCRIPTION));
                album.setAlbumIntro(deceriprtion);

                int trackCount = query.getInt(query.getColumnIndex(Constants.SUB_TRACK_COUNT));
                album.setIncludeTrackCount(trackCount);

                int playCount = query.getInt(query.getColumnIndex(Constants.SUB_PLAY_COUNT));
                album.setPlayCount(playCount);

                int albumId = query.getInt(query.getColumnIndex(Constants.SUB_ALBUM_ID));
                album.setId(albumId);

                String albumAuthorName = query.getString(query.getColumnIndex(Constants.SUB_AUTHORNAME));
                Announcer announcer =new Announcer();
                announcer.setNickname(albumAuthorName);
                album.setAnnouncer(announcer);
                result.add(album);
            }

            //把数据通知出去
            if (mCallback != null) {
                mCallback.onSubListLoaded(result);
            }
            query.close();
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
