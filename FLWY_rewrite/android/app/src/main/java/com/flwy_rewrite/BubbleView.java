package com.flwy_rewrite;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BubbleView extends FrameLayout {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private boolean isLongClick = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private boolean isClickPending = false;
    private CountDownTimer clickTimer;
    private BubbleClickListener clickListener;

    private ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.75f);
    private ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.75f);
    private AnimatorSet animatorSet = new AnimatorSet();

    public BubbleView(Context context){
        super(context);
        init();
        windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
    }

    public void setListener(BubbleClickListener clickListener){
        this.clickListener = clickListener;
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.chat_bubble, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isClickPending) {
                        // Double click detected
                        isClickPending = false;
                        if (clickTimer != null) {
                            clickTimer.cancel();
                        }
                        clickListener.onBubbleDoubleClick();
                    } else {
                        isClickPending = true;
                        clickTimer = new CountDownTimer(500, 100) {
                            public void onTick(long millisUntilFinished) {}
                            public void onFinish() {
                                isClickPending = false;
                            }
                        }.start();
                    }
                    layoutParams = (WindowManager.LayoutParams) getLayoutParams();
                    initialX = layoutParams.x;
                    initialY = layoutParams.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    animatorSet.start();
                    break;
                case MotionEvent.ACTION_UP:
                    if(!isLongClick) {
                        if (isClickPending) {
                            // Single click detected
                            isClickPending = false;
                            if (clickTimer != null) {
                                clickTimer.cancel();
                            }
                            clickListener.onBubbleSingleClick();
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        animatorSet.reverse();
                    }
                    if(isLongClick) {
                        goToWall();
                    }
                    isLongClick = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    isLongClick = true;
                    int x = initialX + (int)(event.getRawX() - initialTouchX);
                    int y = initialY + (int)(event.getRawY() - initialTouchY);
                    layoutParams.x = x;
                    layoutParams.y = y;
                    windowManager.updateViewLayout(this, layoutParams);
                    break;
            }
        } catch (Exception e) {
            Log.e("BubbleView", "Error: " + e.getMessage());
        }
        return true;
    }

    public void goToWall() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - this.getWidth();
        int middle = width / 2;
        layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        float nearestXWall = layoutParams.x >= middle ? width : 0;
        ValueAnimator animator = ValueAnimator.ofInt(layoutParams.x, (int) nearestXWall);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                layoutParams.x = x;
                windowManager.updateViewLayout(BubbleView.this, layoutParams);
            }
        });
        animator.start();
    }

}

