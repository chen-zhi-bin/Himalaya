package com.program.himalaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.program.himalaya.adapters.IndicatorAdapter;
import com.program.himalaya.adapters.MainContenAdapter;
import com.program.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {
    private static final String TAG="MainActivity";
    private MagicIndicator magicIndicator;
    private ViewPager mcontentPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        //最初的测试代码
       /* Map<String,String> map = new HashMap<>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(@Nullable CategoryList categoryList) {
                List<Category> categories=categoryList.getCategories();
                if (categories!=null){
                    int size = categories.size();
                    Log.d(TAG,"categories size =="+size);
                    for (Category category:categories){
                        //Log.d(TAG,"category -->"+category.getCategoryName());
                        LogUtil.d(TAG,"category -->"+category.getCategoryName());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                //Log.e(TAG,"error cade-->"+i+"error msg ==>"+s);
                LogUtil.d(TAG,"error code --"+i+"error msg==>"+s);
            }
        });*/
    }

    private void initEvent() {
        mIndicatorAdapter.setOnIdicatorTapClickListener(new IndicatorAdapter.OnIdicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"click index -->"+index);
                if (mcontentPager!=null){
                    mcontentPager.setCurrentItem(index);
                }
            }
        });

    }

    private void initView() {
         magicIndicator =this.findViewById(R.id.magic_indicator);
         magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
         //创建indicator的适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);    //自我调节屏幕上的大类导航
        commonNavigator.setAdapter(mIndicatorAdapter);
        //设置要显示的内容
        //ViewPager
        mcontentPager =this.findViewById(R.id.content_pager);

         //创建适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContenAdapter mainContenAdapter=new MainContenAdapter(supportFragmentManager);
        mcontentPager.setAdapter(mainContenAdapter);
         //把ViewPagerHelper和IndicatorAdapter绑定
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,mcontentPager);
    }
}