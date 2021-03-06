package com.mobileeftpos.android.eftpos.utils;


/**
 * Created by Prathap on 4/26/17.
 */

import android.util.Log;



/**
 * Display Application Log.
 */
public final class AppLog {

	/**
	 * Private Constructor.
	 */
	private AppLog() {

	}

	/**
	 * Create debug level Log.
	 *
	 * @param tag TAG Name.
	 * @param msg Log Message.
	 */
	public static void d(final String tag, final String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	/**
	 * Create warn level Log.
	 *
	 * @param tag TAG Name.
	 * @param msg Log Message.
	 */
	public static void w(final String tag, final String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}

	/**
	 * Create warn level Log.
	 *
	 * @param tag TAG Name.
	 * @param t   Throwable Error.
	 */
	public static void w(final String tag, final Throwable t) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, t);
		}
	}
}
