package com.autulin.bigbrother.base;

import android.app.Application;
import android.util.Log;

import com.autulin.bigbrother.BuildConfig;
import com.autulin.library.base.AppInfo;
import com.autulin.library.http.Http;
import com.autulin.library.http.HttpCommonParams;

import timber.log.Timber;

public class AppInit {

    private static final String LOCAL_HOST = "http://10.103.101.233:8820";

    public static void init(Application app) {
        // init log
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // init app info
        AppInfo.init(app);

        // set http
        Http.setTimeOut(30);
//        Http.setHost(LOCAL_HOST, Http.DEFAULT_API_VERSION);

        // init http common params
        new HttpCommonParams.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addParam("appVersion", AppInfo.appVersion)
                .addParam("appName", AppInfo.appName);

        // init green db
//        DbGreen.getInstance().init(app);
    }
}
