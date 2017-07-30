package com.mobileeftpos.android.eftpos.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.mobileeftpos.android.eftpos.db.DaoMaster;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;

import org.greenrobot.greendao.database.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;

/**
 * Created by Prathap on 4/21/17.
 */

public class EftposApp extends MultiDexApplication {

    public static EftposApp mInstance;
    //private static DBHelper db = null;
    public static final String TAG = EftposApp.class
            .getSimpleName();
    private static Context mContext;
    private static String testDeviceId = "";

    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
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
        mContext =getApplicationContext();
        ///// Using the below lines of code we can toggle ENCRYPTED to true or false in other to use either an encrypted database or not.
//      DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "ANDROID_EFTPOS-db");
//      Database greenDaodb = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
//      daoSession = new DaoMaster(greenDaodb).newSession();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "android_eftpos-db");
        Database greenDaodb = helper.getWritableDb();
        daoSession = new DaoMaster(greenDaodb).newSession();
//        db = new DBHelper(this);
//        db.open();
        SharedPreferenceStore.setEncryptedSharedPref("NotiFicationEnabled", true + "");
       /* try{
            Fabric.with(this, new Crashlytics());

        }catch (Exception e){
            Log.e("No Network",e.toString());
        }*/

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return mContext;
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
