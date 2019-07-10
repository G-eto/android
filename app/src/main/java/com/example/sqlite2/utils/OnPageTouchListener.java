package com.example.sqlite2.utils;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.sqlite2.R;

public abstract class OnPageTouchListener implements GestureDetector.OnGestureListener {
    /**定义手势检测器实例*/
    private GestureDetector mDetector;
    //LinearLayout.OnTouchListener


    final int FLIP_DISTANCE = 50;


    //@Override
    public boolean onTouchEvent(MotionEvent event) {
        //将activity上面的触碰事件交给GestureDetector处理
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    public abstract void onPageFling(char lr);


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        //如果第一个触点事件的X坐标减去第二个触点事件的X坐标大于FLIP_DISTANCE
        //也就是手势从右往左滑
        float dis = e1.getX() - e2.getX();
        Log.d(FLIP_DISTANCE+" "," "+dis);
        if(e1.getX() - e2.getX() > FLIP_DISTANCE){
            Log.d("触发左滑动","dis:"+dis);
            onPageFling('L');

            return true;
        }
        //如果第二个触点事件的X坐标减去第一个触点事件的X坐标大于FLIP_DISTANCE
        //也就是手势从左往右滑
        else if(e2.getX() - e1.getX() > FLIP_DISTANCE){
            Log.d("触发右滑动","dis:"+dis);
            onPageFling('R');
            return true;
        }
        Log.d("未触发："," "+dis);
        return false;
    }
}
