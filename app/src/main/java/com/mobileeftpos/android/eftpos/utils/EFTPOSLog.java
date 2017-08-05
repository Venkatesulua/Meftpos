package com.mobileeftpos.android.eftpos.utils;

import android.util.Log;

/**
 * @since 1.0
 * @author Prathap.
 * 
 */
public class EFTPOSLog {

	public static void d(String tag, String msg) {
		if (EFTPOSBuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}
}
