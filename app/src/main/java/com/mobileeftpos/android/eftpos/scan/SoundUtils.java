package com.mobileeftpos.android.eftpos.scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;


@SuppressLint("UseSparseArrays")
//@SuppressWarnings("deprecation")
public class SoundUtils {

	private Context context;

	private SoundPool soundPool;

	private HashMap<Integer, Integer> soundPoolMap;

	private int soundVolType = 3;

	public static final int INFINITE_PLAY = -1;

	public static final int SINGLE_PLAY = 0;

	public static final int RING_SOUND = 2;

	public static final int MEDIA_SOUND = 3;

	public SoundUtils(Context context, int soundVolType) {
		this.context = context;
		this.soundVolType = soundVolType;
 		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();
	}


	public void putSound(int order, int soundRes) {
 		soundPoolMap.put(order, soundPool.load(context, soundRes, 1));
	}


	@SuppressWarnings("static-access")
	public void playSound(int order, int times) {
 		AudioManager am = (AudioManager) context
				.getSystemService(context.AUDIO_SERVICE);
 		float maxVolumn = am.getStreamMaxVolume(soundVolType);
 		float currentVolumn = am.getStreamVolume(soundVolType);
 		float volumnRatio = currentVolumn / maxVolumn;
		soundPool.play(soundPoolMap.get(order), volumnRatio, volumnRatio, 1,
				times, 1);
	}


	public void setSoundVolType(int soundVolType) {
		this.soundVolType = soundVolType;
	}
}