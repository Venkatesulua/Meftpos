package com.mobileeftpos.android.eftpos.app;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

//import com.crashlytics.android.Crashlytics;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.db.DaoMaster;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;

//import io.fabric.sdk.android.Fabric;
import org.greenrobot.greendao.database.Database;

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

//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"users-db"); //The users-db here is the name of our database.
//        Database db = helper.getWritableDb();
//        daoSession = new DaoMaster(db).newSession();

        ///// Using the below lines of code we can toggle ENCRYPTED to true or false in other to use either an encrypted database or not.
      DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "ANDROID_EFTPOS-db");
      Database greenDaodb = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
      daoSession = new DaoMaster(greenDaodb).newSession();

        db = new DBHelper(this);
        db.open();
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
