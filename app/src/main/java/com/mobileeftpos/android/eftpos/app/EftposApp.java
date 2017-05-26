package com.mobileeftpos.android.eftpos.app;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;

import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Prathap on 4/21/17.
 */

public class EftposApp extends MultiDexApplication {

    public static EftposApp mInstance;
    private static DBHelper db = null;
    public static final String TAG = EftposApp.class
            .getSimpleName();

    private static String testDeviceId = "";


    /**
     * Get Application instance.
     *
     * @return Application instance.
     */
    public static synchronized EftposApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        db = new DBHelper(this);
        db.open();
        SharedPreferenceStore.setEncryptedSharedPref("NotiFicationEnabled", true + "");


    }


    private static void generateTestDeviceIdAdmob(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure
                .ANDROID_ID);
        testDeviceId = md5(android_id).toUpperCase();
    }

    private static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public DBHelper getDatabase() {

        return db;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
