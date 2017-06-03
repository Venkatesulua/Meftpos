package com.mobileeftpos.android.eftpos.SplashScreen;

/**
 * Created by Prathap on 4/26/17.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.j8583Params;
import com.mobileeftpos.android.eftpos.activity.HomeActivity;
import com.mobileeftpos.android.eftpos.activity.LoginActivity;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;


import org.jpos.iso.ISOMsg;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 7000;
    private KenBurnsView mKenBurns;
    private ImageView environment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        environment = (ImageView) findViewById(R.id.environment);
        setAnimation();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mKenBurns.setImageResource(R.drawable.splash_bg);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                try {



                    if (SharedPreferenceStore.getEncryptedSharedPref(SharedPreferenceStore.KEY_LOGIN_STATUS).equalsIgnoreCase
                            ("true")) {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

   /* private void deviceRegistration() {
        if (SharedPreferenceStore.getEncryptedSharedPref("LoginStatus").equalsIgnoreCase("true") &&
                (SharedPreferenceStore.getEncryptedSharedPref("DeviceRegistrationStatus").equalsIgnoreCase("false") ||
                        SharedPreferenceStore.getEncryptedSharedPref("DeviceRegistrationStatus").equalsIgnoreCase(""))) {
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String registration_id = preferences.getString("registration_id", null);

                String deviceRegistration = ServerURL.getDeviceRegistrationUrl();
                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("userId", AppUtil.getUserID());
                parmas.put("deviceType", "Android");
                parmas.put("deviceToken", registration_id);
                BackendServiceCall serviceCall = new BackendServiceCall(SplashActivity.this, false);
                serviceCall.setOnServiceCallCompleteListener
                        (new OnServiceCallCompleteListener() {
                            @Override
                            public void onJSONObjectResponse(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                                        SharedPreferenceStore.setEncryptedSharedPref("DeviceRegistrationStatus", "true");
                                    } else {
                                        SharedPreferenceStore.setEncryptedSharedPref("DeviceRegistrationStatus", "false");
                                    }
                                } catch (JSONException e) {

                                }
                            }

                            @Override
                            public void onJSONArrayResponse(JSONArray jsonArray) {

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                serviceCall.makeJSONObjectPostRequest(deviceRegistration, parmas, Request.Priority.IMMEDIATE);
            } catch (RuntimeException e) {
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void setAnimation() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();

        findViewById(R.id.imagelogo).setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        findViewById(R.id.imagelogo).startAnimation(anim);
    }



}
