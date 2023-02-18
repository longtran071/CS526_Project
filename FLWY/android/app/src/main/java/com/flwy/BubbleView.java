package com.flwy;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
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


    public BubbleView(Context context) {
        super(context);
        // Initialize the chat bubble view
        init();
        windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.chat_bubble, this);
    }

    public void setListener(BubbleClickListener clickListener){
        this.clickListener = clickListener;
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
}