package com.example.wgj20.pyengchang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wgj20 on 2017-08-25.
 */

public class SplashActivity extends Activity { // CODEADAM 로딩화면

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000); // 4초간 로딩화면 보여준다.
        }
        catch (InterruptedException e){
            e.printStackTrace();

        }

        startActivity(new Intent(this, LoginActivity.class));
    }
}
