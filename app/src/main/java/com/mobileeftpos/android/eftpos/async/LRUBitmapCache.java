package com.mobileeftpos.android.eftpos.async;


/**
 * Created by Prathap on 4/26/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LRUBitmapCache extends LruCache<String, Bitmap> implements ImageCache
{
	private LRUBitmapCache(final int maxSize)
	{
		super(maxSize);
	}

	public LRUBitmapCache(@NonNull final Context ctx)
	{
		this(getCacheSize(ctx));
	}

	@Override
	protected int sizeOf(String key, Bitmap value)
	{
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url)
	{
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap)
	{
		put(url, bitmap);
	}

	private static int getCacheSize(@NonNull final Context context)
	{
		final DisplayMetrics displayMetrics = context.getResources().
				getDisplayMetrics();
		final int screenWidth = displayMetrics.widthPixels;
		final int screenHeight = displayMetrics.heightPixels;
		// 4 bytes per pixel
		final int screenBytes = screenWidth * screenHeight * 4;

		return screenBytes * 3;
	}
}