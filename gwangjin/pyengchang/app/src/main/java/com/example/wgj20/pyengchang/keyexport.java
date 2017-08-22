package com.example.wgj20.pyengchang;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wgj20 on 2017-08-20.
 */

public class keyexport extends AppCompatActivity {
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

    public void main(){
        Log.d("",getKeyHash(this));
    }
}
