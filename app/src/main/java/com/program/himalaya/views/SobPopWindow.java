package com.program.himalaya.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.program.himalaya.R;
import com.program.himalaya.adapters.PlayerListAdapter;
import com.program.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class SobPopWindow extends PopupWindow {

    private final View mPopView;
    private View mCloseBtn;
    private RecyclerView mTrackList;
    private PlayerListAdapter mPlayerListAdapter;
    private TextView mPlayModeTv;
    private ImageView mPlayModeIv;
    private View mPlayModeContainer;
    private PlayListActionClickListener mPlayModeClickListener = null;
    private View mOrderContainer;
    private ImageView mOrderIcon;
    private TextView mOrderText;

    public SobPopWindow() {
        //设置宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //点击内容框外使内容框消失
        setOutsideTouchable(true);

        //载进来View
        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        //设置内容
        setContentView(mPopView);

        //设置窗口进入和推出的动画
        setAnimationStyle(R.style.pop_animation);
        initView();
        intEvent();
    }


    private void initView() {
        mCloseBtn = mPopView.findViewById(R.id.play_list_closs_btn);
        //找到控件
        mTrackList = mPopView.findViewById(R.id.play_list_rv);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mTrackList.setLayoutManager(layoutManager);
        //设置适配器
        mPlayerListAdapter = new PlayerListAdapter();
        mTrackList.setAdapter(mPlayerListAdapter);
        //播放模式
        mPlayModeTv = mPopView.findViewById(R.id.play_list_play_mode_tv);
        mPlayModeIv = mPopView.findViewById(R.id.play_list_play_mode_iv);
        mPlayModeContainer = mPopView.findViewById(R.id.play_list_play_mode_contaniner);
        //
        mOrderContainer = mPopView.findViewById(R.id.play_list_order_container);
        mOrderIcon = mPopView.findViewById(R.id.play_list_order_iv);
        mOrderText = mPopView.findViewById(R.id.play_list_order_tv);
    }

    private void intEvent() {
        //点击关闭之后,窗口消失
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SobPopWindow.this.dismiss();
            }
        });
        mPlayModeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换播放模式
                if (mPlayModeClickListener != null) {
                    mPlayModeClickListener.onPlayModeClick();
                }
            }
        });
        mOrderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换播放列表的顺序或逆序播放
                mPlayModeClickListener.onOderClick();
            }
        });
    }

    /**
     * 给适配器设置
     *
     * @param data
     */
    public void setListData(List<Track> data) {
        if (mPlayerListAdapter != null) {
            mPlayerListAdapter.setData(data);
        }
    }

    public void setCurrentPlayPosition(int postion) {
        if (mPlayerListAdapter != null) {
            mPlayerListAdapter.setCurrentPLayPosition(postion);
            //使播放后点击列表时，播放的出现再眼前
            mTrackList.scrollToPosition(postion);
        }
    }

    public void setPlayListItemClickListener(PlayListItemClickListener listener) {
        mPlayerListAdapter.setOnItemClickListener(listener);
    }

    /**
     * 更新切换列表顺逆序的UI
     * @param isReverse
     */
    public void updateOrderIcon(boolean isReverse) {
        mOrderIcon.setImageResource(
                isReverse ? R.drawable.selector_play_mode_list_sort_des : R.drawable.selector_play_mode_list_sort_asc
        );
        mOrderText.setText(BaseApplication.getAppContext().getString(isReverse ?R.string.order_des_text:R.string.revers_asc_text));
    }

    /**
     * 更新播放列表的播放模式
     *
     * @param currentMode
     */
    public void updatePlayMode(XmPlayListControl.PlayMode currentMode) {
        updatePLayModeBtnImg(currentMode);

    }

    //根据当前的状态，更新播放模式图标
    //1.默认的是：PLAY_MODEL_LIST列表播放
    //2.PLAY_MODEL_LIST_LOOP列表循环
    //3.PLAY_MODEL_RANDOM 随机播放
    //4.PLAY_MODEL_SINGLE_LOOP 单曲循环播放

    private void updatePLayModeBtnImg(XmPlayListControl.PlayMode playMode) {
        int resId = R.drawable.selector_play_mode_list_order;
        int textId = R.string.play_mode_order_text;
        switch (playMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_play_mode_list_order;
                textId = R.string.play_mode_order_text;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_palyer_mode_random;
                textId = R.string.play_mode_random_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_palyer_mode_list_order_looper;
                textId = R.string.play_mode_list_play_text;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_palyer_mode_single_loop;
                textId = R.string.play_mode_single_play_text;
                break;
        }
        mPlayModeIv.setImageResource(resId);
        mPlayModeTv.setText(textId);
    }


    public interface PlayListItemClickListener {
        void onItemClick(int postion);
    }

    public void setPlayListActionClickListener(PlayListActionClickListener playModeListener) {
        mPlayModeClickListener = playModeListener;
    }

    public interface PlayListActionClickListener {
        //播放模式点击
        void onPlayModeClick();

        //顺逆序播放模式
        void onOderClick();
    }

}
