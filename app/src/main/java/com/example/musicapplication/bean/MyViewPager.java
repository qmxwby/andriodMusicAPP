package com.example.musicapplication.bean;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    private onViewPagerTouchListener mTouchListener = null;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context,AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mTouchListener != null){
                    mTouchListener.onPagerTouch(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(mTouchListener != null){
                    mTouchListener.onPagerTouch(false);
                }
                break;

        }

        return super.onTouchEvent(ev);
    }
    public void setonViewPagerTouchListener(onViewPagerTouchListener Listener){
        this.mTouchListener = Listener;
    }
    public interface onViewPagerTouchListener{
        void onPagerTouch(boolean isTouch);
    }
}

