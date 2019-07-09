package com.example.sqlite2.utils;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.sqlite2.view.ViewPage;

public abstract class OnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    private int slideMinDistance = 20;
    private int slideMinVelocity = 0;

    public OnItemTouchListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new MyGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void slideToGestureRightActivity() {
        //Intent i = new Intent(getApplicationContext(), GestureRightActivity.class);
        //startActivity(i);

        // 设置切换动画，从右边进入，左边退出
        //overridePendingTransition(R.anim.right_activity_enter_from_right, R.anim.left_activity_out_to_left);

        //finish();
    }
    private void slideToGestureLeftActivity() {
        //Intent i = new Intent(getApplicationContext(), GestureRightActivity.class);
        //startActivity(i);

        // 设置切换动画，从右边进入，左边退出
        //overridePendingTransition(R.anim.right_activity_enter_from_right, R.anim.left_activity_out_to_left);

       // finish();
    }
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (((e1.getX() - e2.getX()) > slideMinDistance) && Math.abs(velocityX) > slideMinVelocity) {
            Toast.makeText(null, "向左手势", Toast.LENGTH_SHORT).show();
            Log.d("ssssssssssssssssssss:","left");
            slideToGestureRightActivity();
        } else if (e2.getX() - e1.getX() > slideMinDistance && Math.abs(velocityX) > slideMinVelocity) {

            // 切换Activity
            slideToGestureLeftActivity();
            Toast.makeText(null, "向右手势", Toast.LENGTH_SHORT).show();
            Log.d("ssssssssssssssssssss:","right");
        }

        return false;
    }



    public abstract void onItemClick(RecyclerView.ViewHolder vh);
    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemLongClick(VH);
            }
        }
    }
}