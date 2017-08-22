package com.example.wgj20.pyengchang;

import
android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private SessionCallback callback;

    public
    static
    final
    String getKeyHash(Context context) {



        try {


            PackageInfo info

                    = context.getPackageManager().getPackageInfo(context.getPackageName(),


                    PackageManager.GET_SIGNATURES);



            for (Signature signature : info.signatures) {


                MessageDigest md

                        = MessageDigest.getInstance(
                        "SHA");


                md.update(signature.toByteArray());



                String keyHash

                        = Base64.encodeToString(md.digest(), Base64.DEFAULT);


                Log.d(""

                        +
                        "KeyHash:%s", keyHash);



                return keyHash;


            }





        }
        catch (PackageManager.NameNotFoundException e) {


            Log.d(""

                    +
                    "getKeyHash Error:%s", e.getMessage());

        }

        catch (NoSuchAlgorithmException e) {


            Log.d(""

                    +
                    "getKeyHash Error:%s", e.getMessage());


        }



        return
                "";


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("",getKeyHash(this));
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);
        // Set up the login form.

        mProgressView = findViewById(R.id.login_progress);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        Button volunteer_people = (Button) findViewById(R.id.volunteer_people_sign_in_button);
        volunteer_people.setOnClickListener(mClickListener);

    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, map_activity.class);
            startActivity(intent);
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception == null){
                Logger.e(exception);
            }

            setContentView(R.layout.activity_login);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }








}

