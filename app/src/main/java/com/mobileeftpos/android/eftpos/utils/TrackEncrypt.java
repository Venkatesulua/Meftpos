package com.mobileeftpos.android.eftpos.utils;

import android.os.RemoteException;

import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.mobileeftpos.android.eftpos.activity.MyApplication;

/**
 * @author xinle on 1/19/17.
 */
public class TrackEncrypt {

    public static String trackEncrypt(String track) {
        String _track = track.replaceAll("=", "D");
        byte[] returnData = new byte[8];
        if (16 == getTrackSubString(_track).length()) {
            byte[] track2Bytes = Utils.hexStr2Bytes(getTrackSubString(_track));
            int result = 0;
            try {
                result = MyApplication.mPinPadOpt.encryptData(MyApplication.TDKIndex, AidlConstants.PinPad.TDES, track2Bytes, returnData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (8 == result) {
                _track = insert(_track, Utils.byte2HexStr(returnData));
            }
        }
        return _track;
    }

    private static String getTrackSubString(String s) {
        return s.length() >= 17 ? s.substring(s.length() - 17, s.length() - 17 + 16) : "";
    }

    private static String insert(String s, String s2) {
        return s.substring(0, s.length() - 17) + s2 + s.substring(s.length() - 17 + 16, s.length());
    }

}

