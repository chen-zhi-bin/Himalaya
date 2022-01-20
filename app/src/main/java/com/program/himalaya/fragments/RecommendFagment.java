package com.program.himalaya.fragments;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.program.himalaya.R;
import com.program.himalaya.adapters.RecommendListAdapter;
import com.program.himalaya.base.BaseFragment;
import com.program.himalaya.interfaces.IRecommendViewCallback;
import com.program.himalaya.presenters.RecommendPresenter;
import com.program.himalaya.utils.Constants;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  RecommendFagment extends BaseFragment implements IRecommendViewCallback {
    private static final String TAG="RecommendFagment";

    private View mrootView;
    private RecyclerView mrecommendRV;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //view加载完成
         mrootView = layoutInflater.inflate(R.layout.fragment_recommend,container, false);

         //RecyclerView的使用
        //1.找到控件
        mrecommendRV = mrootView.findViewById(R.id.recommand_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecommendRV.setLayoutManager(linearLayoutManager);
        //设置间距
        mrecommendRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),5);    //px-->dp
                outRect.bottom=UIUtil.dip2px(view.getContext(),5);
                outRect.left=UIUtil.dip2px(view.getContext(),5);
                outRect.right=UIUtil.dip2px(view.getContext(),5);
            }
        });
        //3.设置适配器
        mRecommendListAdapter =new RecommendListAdapter();
        mrecommendRV.setAdapter(mRecommendListAdapter);
        //获取到逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先要设置通知接口的注册
        mRecommendPresenter.registerViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        //返回view，给界面显示
        return mrootView;
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //当我们获取到推荐内容时候，这个方法就会被调用（成功了）
        //数据回来之后就更新UI
        //把数据设置给适配器，并且更新
        mRecommendListAdapter.setData(result);
    }

    @Override
    public void onLoaderMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册，避免内存泄露
        if (mRecommendPresenter!=null) {
            mRecommendPresenter.ungisterViewCallback(this);
        }
    }
}
