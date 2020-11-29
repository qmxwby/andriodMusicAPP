package com.example.musicapplication.avtivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LooperPagerAdapter;
import com.example.musicapplication.bean.MyViewPager;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements MyViewPager.onViewPagerTouchListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "HomeActivity";
    private MyViewPager mLoopPager;
    private ImageView musicIv, userIv;
    private LooperPagerAdapter mLooperPagerAdapter;
    private static List<Integer> sPics = new ArrayList<>();
    private long exitTime;//第一次单机退出键的时间
    static{
        sPics.add(R.mipmap.first);
        sPics.add(R.mipmap.second);
        sPics.add(R.mipmap.third);
        sPics.add(R.mipmap.fourth);
        sPics.add(R.mipmap.five);
        sPics.add(R.mipmap.six);
        sPics.add(R.mipmap.seven);

    }
    private Handler mHandler;
    private boolean mIsTouch = false;
    private  LinearLayout mPointContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        mHandler = new Handler();
    }


    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Log.d(TAG,"onAttachedToWindow");
        //当此界面绑定到窗口的时候
        mHandler.post(mLooperTask);
    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mLooperTask);
    }

    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if(!mIsTouch){
                //切换viewPager里的图片到下一个
                int currentItem = mLoopPager.getCurrentItem();
                mLoopPager.setCurrentItem(++currentItem,true);
            }
            mHandler.postDelayed(this,3000);
        }
    };
    private void initView() {
        //找到viewPager控件
        mLoopPager = (MyViewPager) this.findViewById(R.id.looper_pager);
        musicIv = findViewById(R.id.home_user_musiclist);
        userIv = findViewById(R.id.home_user_center);
        //设置适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        mLoopPager.setAdapter(mLooperPagerAdapter);
        mLoopPager.addOnPageChangeListener(this);
        mLoopPager.setonViewPagerTouchListener(this);
        mPointContainer = (LinearLayout)this.findViewById(R.id.points_container);
        //根据图片的个数去添加点的个数
        insertPoint();
        mLoopPager.setCurrentItem(mLooperPagerAdapter.getDataRealSize() * 100 ,false);

        //设置点击事件
        musicIv.setOnClickListener(this);
        userIv.setOnClickListener(this);

    }

    private void insertPoint() {
        for (int i = 0;i < sPics.size();i++) {
            View point = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
            point.setLayoutParams(layoutParams);
            point.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_point_normal));
            layoutParams.leftMargin = 20;
            point.setLayoutParams(layoutParams);
            mPointContainer.addView(point);

        }
    }

    @Override
    public void onPagerTouch(boolean isTouch) {
        mIsTouch = isTouch;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 退出按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // System.currentTimeMillis()无论何时调用，肯定大于2000
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onPageSelected(int position) {
        int realPosition;
        if(mLooperPagerAdapter.getDataRealSize()!= 0){
            realPosition = position % mLooperPagerAdapter.getDataRealSize();
        }
        else{
            realPosition = 0;
        }
        setSelectPoint(realPosition);
    }


    private void setSelectPoint(int realPosition) {
        for(int i = 0; i < mPointContainer.getChildCount();i++){
            View point = mPointContainer.getChildAt(i);
            if(i!= realPosition){
                point.setBackgroundResource(R.drawable.shape_point_normal);
            }else{
                point.setBackgroundResource(R.drawable.shape_point_selected);
            }
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.home_user_musiclist:
                 intent.setClass(this, MusicMainActivity.class);
                startActivity(intent);
                break;
            case R.id.home_user_center:
                intent.setClass(this, MySpaceActivity.class);
                startActivity(intent);
                break;
            case R.id.home_user_diantai:
                Toast.makeText(this,"该功能尚未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_user_paihangbang:
                Toast.makeText(this,"该功能尚未开放",Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
