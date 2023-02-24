package com.flwy_rewrite;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class FLWYModule extends ReactContextBaseJavaModule {
    @Override
    public String getName() {
        return "FLWYModule";
    }

    FLWYModule(ReactApplicationContext context){
        super(context);
    }
}
