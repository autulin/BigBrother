package com.autulin.bigbrother.base;

import android.app.Application;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this);
    }
}
