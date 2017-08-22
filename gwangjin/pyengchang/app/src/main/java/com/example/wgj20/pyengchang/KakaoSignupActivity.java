package com.example.wgj20.pyengchang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

/**
 * Created by wgj20 on 2017-08-12.
 */

public class KakaoSignupActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {


            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Log.v("fail", "fail");

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());

                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();

            }

            @Override
            public void onNotSignedUp() {
                showSignup();
            }

            @Override
            public void onSuccess(UserProfile result) {
                Logger.d("UserProfile : " + result.getId());

                Log.v("user", String.valueOf(result.getId()));
                String ret = String.valueOf(result.getId());
                redirectMainActivity(ret);
            }
        }); }

        protected void showSignup(){
        redirectLoginActivity();
        }

        private void redirectLoginActivity() {
            startActivity(new Intent(this, map_activity.class));
        }

        protected void redirectMainActivity(String str) {
            final Intent intent = new Intent(this, map_activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("ID", str);
            startActivity(intent);
            finish();
        }
    }

