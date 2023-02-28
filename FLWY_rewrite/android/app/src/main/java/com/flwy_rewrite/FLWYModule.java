package com.flwy_rewrite;

import android.app.ActivityManager;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Rational;
import android.view.Gravity;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class FLWYModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;
    private BubbleView mBubbleView;
    private final WindowManager mWindowManager;
    private ActivityManager mActivityManager;
    @Override
    public String getName() {
        return "FLWYModule";
    }

    @ReactMethod
    public void show() {
        requestPermission();
        createBubble();
    }

    @ReactMethod
    public void hide() {
        // Code to hide the floating bubble
        if(mBubbleView != null){
            mWindowManager.removeView(mBubbleView);
        }
    }

    @ReactMethod
    public void enterPipMode() {
        int width = 600;
        int height = 400;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            int ratWidth = width > 0 ? width : 380;
            int ratHeight = height > 0 ? height : 214;

            Rational ratio
                    = new Rational(ratWidth, ratHeight);
            PictureInPictureParams.Builder
                    pip_Builder
                    = null;

            pip_Builder = new PictureInPictureParams
                    .Builder();
            pip_Builder.setAspectRatio(ratio).build();
            reactContext.getCurrentActivity().enterPictureInPictureMode(pip_Builder.build());
        }
    }

    @ReactMethod
    public boolean isInPictureInPictureMode(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return reactContext.getCurrentActivity().isInPictureInPictureMode();
        }
        else return false;
    }

    FLWYModule(ReactApplicationContext context){
        super(context);
        this.reactContext = context;
        mWindowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
    }

    public void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(reactContext)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + reactContext.getPackageName()));
                Bundle bundle = new Bundle();
                reactContext.startActivityForResult(intent, 1, bundle);
            }
        }
    }

    public void createBubble(){
        if(mBubbleView == null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.START;
            params.x = 0;
            params.y = 100;
            mBubbleView = new BubbleView(reactContext);
            mBubbleView.setListener(new BubbleClickListener() {
                @Override
                public void onBubbleSingleClick() {
                    sendEvent("BubbleSingleClick", "");
                }

                @Override
                public void onBubbleDoubleClick() {
                    sendEvent("BubbleDoubleClick", "");
                }
            });
            mWindowManager.addView(mBubbleView, params);
        }
    }

    private void sendEvent(String eventName, String value) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, value);
    }
}
