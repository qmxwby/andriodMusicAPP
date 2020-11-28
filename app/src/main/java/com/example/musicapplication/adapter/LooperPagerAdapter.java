package com.example.musicapplication.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<Integer> mPics = null;

    @Override
    public int getCount() {
        if(mPics != null){
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    //初始化界面
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mPics.size();
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(mPics.get(realPosition));
        //图片全屏
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //设置完数据以后就添加到容器里面
        container.addView(imageView);
        return imageView;
    }

    //销毁界面
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<Integer> colos) {
        this.mPics = colos;
    }
    public int getDataRealSize(){
        if(mPics != null){
            return mPics.size();
        }
        return 0;
    }
}

