package com.mobileeftpos.android.eftpos.SupportClasses;

/**
 * Created by venkat on 7/13/2017.
 */


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TripleDes {
    //    public static String ALGO = "DESede/CBC/PKCS7Padding";
    //public static String ALGO = "DESede/ECB/PKCS7Padding";

    public static int MAX_KEY_LENGTH = DESedeKeySpec.DES_EDE_KEY_LEN;
    private static String ENCRYPTION_KEY_TYPE = "DESede";
    private static String ENCRYPTION_ALGORITHM = "DESede/CBC/Nopadding";
    private final SecretKeySpec keySpec;

    public TripleDes(byte[] passphrase) {
        byte[] key = new byte[24];
        //key =null;
        try {
            if(passphrase.length==8) {
                System.arraycopy(passphrase, 0, key, 0, 8);
            }
            else if(passphrase.length==16)
            {
                System.arraycopy(passphrase, 0, key, 0, 16);
                System.arraycopy(passphrase, 0, key, 16, 8);
            }else if(passphrase.length==24)
                System.arraycopy(passphrase, 0, key, 0, 24);


            // get bytes representation of the password
            //key = passphrase;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

       // key = padKeyToLength(key, MAX_KEY_LENGTH);
        keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
    }

    // !!! - see post below
   /* private byte[] padKeyToLength(byte[] key, int len) {
        byte[] newKey = new byte[len];
        System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
        return newKey;
    }*/

    // standard stuff
    public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
        return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
        return doCipher(encrypted, Cipher.DECRYPT_MODE);
    }

    private byte[] doCipher(byte[] original, int mode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        // IV = 0 is yet another issue, we'll ignore it here
        IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
        cipher.init(mode, keySpec, iv);
        return cipher.doFinal(original);
    }
}