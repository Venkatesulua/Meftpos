package com.mobileeftpos.android.eftpos.sharedpreference;


/**
 * Created by Prathap on 4/26/17.
 */

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptography.
 */
public final class Cryptography {
	/**
	 * Private Constructror.
	 */
	/**
	 * Seed key.
	 */
	public static final String ENCRYPTION_SEED = "95DA0F5669DB628DA492CFF6C26B2E0F";
	/**
	 * Cryptography Algorithm.
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * Hex String.
	 */
	private static final String HEX = "0123456789ABCDEF";

	private Cryptography() {

	}

	/**
	 * Encryption.
	 *
	 * @param seed      seed value.
	 * @param cleartext text
	 * @return result encrypted string.
	 * @throws InvalidKeyException       Exception.
	 * @throws NoSuchAlgorithmException  Exception.
	 * @throws NoSuchPaddingException    Exception.
	 * @throws IllegalBlockSizeException Exception.
	 * @throws BadPaddingException       Exception.
	 */
	public static String encrypt(String seed, String cleartext) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		if (!TextUtils.isEmpty(cleartext)) {
			byte[] rawKey = getRawKey(seed.getBytes());
			byte[] result = encrypt(rawKey, cleartext.getBytes());
			return toHex(result);
		} else {
			return null;
		}
	}

	/**
	 * Decryption.
	 *
	 * @param seed      value.
	 * @param encrypted encrypted text.
	 * @return result decrypted String.
	 * @throws InvalidKeyException       Exception.
	 * @throws NoSuchAlgorithmException  Exception.
	 * @throws NoSuchPaddingException    Exception.
	 * @throws IllegalBlockSizeException Exception.
	 * @throws BadPaddingException       Exception.
	 */
	public static String decrypt(String seed, String encrypted) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		if (!TextUtils.isEmpty(encrypted)) {
			byte[] rawKey = getRawKey(seed.getBytes());
			byte[] enc = toByte(encrypted);
			byte[] result = decrypt(rawKey, enc);
			return new String(result);
		} else {
			return null;
		}
	}

	/**
	 * Get Raw key.
	 *
	 * @param seed seed value.
	 * @return
	 */
	private static byte[] getRawKey(byte[] seed) {
		final int sixteen = 16;
		seed = Arrays.copyOf(seed, sixteen);
		SecretKey key = new SecretKeySpec(seed, ALGORITHM);
		byte[] raw = key.getEncoded();
		return raw;
	}

	/**
	 * Encrypt byte array.
	 * Since the credentials are already secured through shared prefs, we're
	 * using this as a lightweight solution for obfuscation. Fixing SecureRandom
	 * to provide cryptographically strong values is outside the scope of this
	 * application. See
	 * http://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html.
	 *
	 * @param raw   byte array.
	 * @param clear byte array.
	 * @return byte array.
	 * @throws NoSuchAlgorithmException  Exception.
	 * @throws NoSuchPaddingException    Exception.
	 * @throws InvalidKeyException       Exception.
	 * @throws IllegalBlockSizeException Exception.
	 * @throws BadPaddingException       Exception.
	 */
	@SuppressLint("TrulyRandom")
	private static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	/**
	 * Decrypt byte array.
	 *
	 * @param raw       byte array.
	 * @param encrypted byte array.
	 * @return byte array.
	 * @throws NoSuchAlgorithmException  Exception.
	 * @throws NoSuchPaddingException    Exception.
	 * @throws InvalidKeyException       Exception.
	 * @throws IllegalBlockSizeException Exception.
	 * @throws BadPaddingException       Exception.
	 */
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	/**
	 * Convert Heg String to bytes.
	 *
	 * @param hexString Hex String
	 * @return byte array.
	 */
	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		final int sixteen = 16;
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), sixteen).byteValue();
		}
		return result;
	}

	/**
	 * Convert byte array to Hex string.
	 *
	 * @param buf byte array
	 * @return Hex String.
	 */
	public static String toHex(byte[] buf) {
		if (buf == null) {
			return "";
		}
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	/**
	 * Append Hex String.
	 *
	 * @param sb String buffer.
	 * @param b  byte.
	 */
	private static void appendHex(StringBuffer sb, byte b) {
		final int four = 4;
		final int hex = 0x0f;
		sb.append(HEX.charAt((b >> four) & hex)).append(HEX.charAt(b & hex));
	}
}
