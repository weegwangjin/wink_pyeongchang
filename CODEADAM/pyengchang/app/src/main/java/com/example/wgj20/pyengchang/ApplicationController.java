package com.example.wgj20.pyengchang;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by wgj20 on 2017-08-12.
 */

public class ApplicationController extends Application {
    private static ApplicationController instance = null;
    private static volatile Activity currentActivity = null;

    public static ApplicationController getInstance() { return instance ;}

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static Activity getCurrentActivity() { return currentActivity; }

    public static void setCurrentActivity(Activity currentActivity){
        ApplicationController.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
