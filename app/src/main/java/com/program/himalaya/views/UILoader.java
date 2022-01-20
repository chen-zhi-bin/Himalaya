package com.program.himalaya.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.program.himalaya.R;
import com.program.himalaya.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View mloadingView;
    private View mSuccessview;
    private View mNetworkErrorView;
    private View mEmptyView;
    private OnRetryClickListener mOnRetryClickListener=null;

    public enum UIStatus{
        LOADING,SUCCESS,NETWORK_ERROR,EMPTY,NONE
    }

    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        init();
    }

    /**
     * 初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    public void updateStatus(UIStatus status){
        mCurrentStatus = status;
        //更新UI一定要到主线程上
        BaseApplication.getsHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }
    private void switchUIByCurrentStatus() {
        //加载中
        if (mloadingView==null) {
            mloadingView = getLoadingView();
            addView(mloadingView);
        }
        //根据状态设置是否可见
        mloadingView.setVisibility(mCurrentStatus==UIStatus.LOADING ? VISIBLE:GONE);


        //加载成功
        if (mSuccessview==null) {
            mSuccessview = getSuccessView(this);
            addView(mSuccessview);
        }
        //根据状态设置是否可见
        mSuccessview.setVisibility(mCurrentStatus==UIStatus.SUCCESS ? VISIBLE:GONE);


        //网络错误页面
        if (mNetworkErrorView==null) {
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        //根据状态设置是否可见
        mNetworkErrorView.setVisibility(mCurrentStatus==UIStatus.NETWORK_ERROR ? VISIBLE:GONE);


        //数据为空
        if (mEmptyView==null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        //根据状态设置是否可见
        mEmptyView.setVisibility(mCurrentStatus==UIStatus.EMPTY ? VISIBLE:GONE);
    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    private View getNetworkErrorView() {
        View networkErrorView=LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        networkErrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新获取数据（刷新）
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.onRetryClick();
                }
            }
        });

        return networkErrorView;
    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener){
        this.mOnRetryClickListener = listener;
    }
    public interface OnRetryClickListener{
        void onRetryClick();
    };
}
