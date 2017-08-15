package com.mobileeftpos.android.eftpos.SupportClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;

/**
 * Created by venkat on 6/23/2017.
 */

public class KeyValueDB {

    //private SharedPreferences sharedPreferences;
    //private static String PREF_NAME = "prefs";
    //private String Reversal = "reversal";

    public KeyValueDB() {
        // Blank
    }

    //private static SharedPreferences getPrefs(Context context) {
        //return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    //}

    public static String getReversal(Context context) {
        return SharedPreferenceStore.getEncryptedSharedPref(SharedPreferenceStore.KEY_REVERSAL+Integer.toString(TransactionDetails.inGHDT));
        //return getPrefs(context).getString("Reversal", "");
    }

    public static void setReversal(Context context, String input) {
        SharedPreferenceStore.setEncryptedSharedPref(SharedPreferenceStore.KEY_REVERSAL+Integer.toString(TransactionDetails.inGHDT),input);
        //SharedPreferences.Editor editor = getPrefs(context).edit();
       // editor.putString("Reversal", input);
        //editor.commit();
    }
    public static void removeReversal(Context context) {
        SharedPreferenceStore.deleteSharedPreference(SharedPreferenceStore.KEY_REVERSAL+Integer.toString(TransactionDetails.inGHDT));
        //SharedPreferences.Editor editor = getPrefs(context).edit();
        //editor.remove("Reversal");
        //editor.apply();
    }

    public static String getUpload(Context context) {
        return SharedPreferenceStore.getEncryptedSharedPref(SharedPreferenceStore.KEY_UPLOAD+Integer.toString(TransactionDetails.inGHDT));
        //return getPrefs(context).getString("Upload", "");
    }

    public static void setUpload(Context context, String input) {
        SharedPreferenceStore.setEncryptedSharedPref(SharedPreferenceStore.KEY_UPLOAD+Integer.toString(TransactionDetails.inGHDT),input);
        //SharedPreferences.Editor editor = getPrefs(context).edit();
        //editor.putString("Upload", input);
        //editor.commit();
    }
    public static void removeUpload(Context context) {
        //SharedPreferences.Editor editor = getPrefs(context).edit();
        //editor.remove("Upload");
        //editor.apply();
        SharedPreferenceStore.deleteSharedPreference(SharedPreferenceStore.KEY_UPLOAD+Integer.toString(TransactionDetails.inGHDT));
    }

    public static String getLastReceipt(Context context) {
        return SharedPreferenceStore.getEncryptedSharedPref(SharedPreferenceStore.KEY_LAST_RECEIPT);
        //return getPrefs(context).getString("Reversal", "");
    }

    public static void setLastReceipt(Context context, String input) {
        SharedPreferenceStore.setEncryptedSharedPref(SharedPreferenceStore.KEY_LAST_RECEIPT,input);
        //SharedPreferences.Editor editor = getPrefs(context).edit();
        // editor.putString("Reversal", input);
        //editor.commit();
    }
    public static void removeLastReceipt(Context context) {
        //SharedPreferences.Editor editor = getPrefs(context).edit();
        //editor.remove("Upload");
        //editor.apply();
        SharedPreferenceStore.deleteSharedPreference(SharedPreferenceStore.KEY_LAST_RECEIPT);
    }


}
