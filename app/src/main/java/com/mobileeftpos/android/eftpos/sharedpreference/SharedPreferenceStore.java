package com.mobileeftpos.android.eftpos.sharedpreference;


/**
 * Created by Prathap on 4/26/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.mobileeftpos.android.eftpos.app.EftposApp;
import com.mobileeftpos.android.eftpos.utils.AppLog;

import java.util.Map;

/**
 * SharedPrefrences Instance.
 */
public final class SharedPreferenceStore {

	/**
	 * Default Shared preference return value.
	 */
	public static final String KEY_LOGIN_STATUS = "LOGIN_STATUS";
	public static final String KEY_UNASSIGNED = "";
	private static final String LOG_TAG = SharedPreferenceStore.class.getSimpleName();
	private static final String SHARED_PREFERENCE_NAME = SharedPreferenceStore.class
			.getSimpleName();
	public static final String KEY_REVERSAL = "reversal";
	public static final String KEY_UPLOAD = "upload";



	/**
	 * Set value Shared Preference.
	 *
	 * @param key  key.
	 * @param data data.
	 */
	public static void setEncryptedSharedPref(String key, String data) {
		SharedPreferences prefs = EftposApp.getInstance()
				.getSharedPreferences(SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		if (data == null) {
			AppLog.d(LOG_TAG, "Data is null, cannot save");
			return;
		}
		try {
			editor.putString(key, Cryptography.encrypt(Cryptography.ENCRYPTION_SEED, data));
			editor.apply();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Get data from shared preference.
	 *
	 * @param key key
	 * @return value associated with key.
	 */
	public static String getEncryptedSharedPref(String key) {
		SharedPreferences prefs = EftposApp.getInstance()
				.getSharedPreferences(SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
		String encryptedData = prefs.getString(key, null);
		try {
			if (encryptedData != null) {
				return Cryptography.decrypt(Cryptography.ENCRYPTION_SEED, encryptedData);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return KEY_UNASSIGNED;
	}
	public static boolean containsKey(String key){
		SharedPreferences prefs = EftposApp.getInstance()
				.getSharedPreferences(SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
		if(prefs.contains(key)){
			return true;
		}else {
			return false;
		}
	}
	public static Map<String,?> getAllKeys(){
		SharedPreferences prefs = EftposApp.getInstance()
				.getSharedPreferences(SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
		Map<String,?> keys=prefs.getAll();

		return keys;
	}
	public static void deleteSharedPreference(String key){
		SharedPreferences prefs = EftposApp.getInstance()
				.getSharedPreferences(SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		try {
			editor.remove(key);
			editor.apply();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
