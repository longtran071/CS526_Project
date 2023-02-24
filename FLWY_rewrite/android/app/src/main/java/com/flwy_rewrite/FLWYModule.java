package com.flwy_rewrite;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
    @Override
    public String getName() {
        return "FLWYModule";
    }

    @ReactMethod
    public void show() {
        requestPermission();
        createBubble();
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

    private void sendEvent(String eventName, String value) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, value);
    }
}
