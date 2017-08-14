package com.mobileeftpos.android.eftpos.Ezlink;

import android.os.RemoteException;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.SupportClasses.TripleDes;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.EzlinkModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.AidlErrorCode;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

/**
 * Created by venkat on 7/12/2017.
 */

public class SamFunctions {

    private int cardType, time=30;
    private byte[] apduByte, apduOutByte;
    public HAfterTransaction afterTranscation = new HAfterTransaction();

    public int initPSAMCard(ReadCardOpt mReadCardOpt) {
        byte[] outdata = new byte[512];
        try {
            int result = mReadCardOpt.initPSAMCard(outdata);
            byte[] newByte = new byte[result];
            System.arraycopy(outdata, 0, newByte, 0, newByte.length);
            if (result > 0) {
                return Constants.ReturnValues.RETURN_OK;
            } else {
                return Constants.ReturnValues.SAM_INIT_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.SAM_INIT_ERROR;
    }

    int SelectEzlSAMApp(ReadCardOpt mReadCardOpt){
        apduByte = StringByteUtils.HexString2Bytes("00A404000788776655443322");
        try {
            apduOutByte = mReadCardOpt.PSAMAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[0] == -112 && apduOutByte[1] == 0x00) {
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.EZLINK_SAM_ERROR;
    }

    int SelectEzlSAMGetId(ReadCardOpt mReadCardOpt){
        apduByte = StringByteUtils.HexString2Bytes("901A00000201000A");
        try {
            apduOutByte = mReadCardOpt.PSAMAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[8] == -112 && apduOutByte[9] == 0x00) {
                    System.arraycopy(apduOutByte, 0, TransactionDetails.SAMSerialNumber, 0, 8);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }

    int SelectEzlSAMEnable(ReadCardOpt mReadCardOpt){
        apduByte = StringByteUtils.HexString2Bytes("9003000012FFFFF89234564ABD890BCDED09FFEE33220007");
        try {
            apduOutByte = mReadCardOpt.PSAMAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[5] == -112 && apduOutByte[6] == 0x00) {
                    //System.arraycopy(apduOutByte, 0, TransactionDetails.SAMSerialNumber, 0, 5);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }






    int Ezlink_inGetSessionKey(ReadCardOpt mReadCardOpt,int KeyType,int inDecryptLast)
    {
        String inputApdu = "9011000020";// + new String(TransactionDetails.TerminalRandomNumber);
        byte[] localByte = new byte[32+5];
                //new String(TransactionDetails.CardNumberRandomNumber) + new String(TransactionDetails.CAN)+new String(TransactionDetails.CSN);
        //localByte = StringByteUtils.HexString2Bytes(inputApdu);
        localByte[0]=(byte)0x90;
        localByte[1]=0x11;
        localByte[2]=0x00;
        localByte[3]=0x00;
        localByte[4]=0x20;

        if(inDecryptLast ==0 && KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x55)
        localByte[1]=0x15;
        else if(inDecryptLast == 0)
            localByte[1]=0x11;
        else if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x55)
            localByte[1]=0x11;
        else if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x56)
            localByte[1]=(byte)0xA2;
        else if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x57)
            localByte[1]=(byte)0xA3;
        else if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x58)
            localByte[1]=(byte)0xA4;
        else if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x5B)
            localByte[1]=(byte)0xA5;
        else if(KeyType ==0x01 && TransactionDetails.IssuerSpecificdata[8] == 0x55)
            localByte[1]=0x13;
        else if(KeyType ==0x01 && TransactionDetails.IssuerSpecificdata[8] == 0x56)
            localByte[1]=(byte)0xB2;
        else if(KeyType ==0x01 && TransactionDetails.IssuerSpecificdata[8] == 0x57)
            localByte[1]=(byte)0xB3;
        else if(KeyType ==0x01 && TransactionDetails.IssuerSpecificdata[8] == 0x58)
            localByte[1]=(byte)0xB4;
        else if(KeyType ==0x01 && TransactionDetails.IssuerSpecificdata[8] == 0x5B)
            localByte[1]=(byte)0xB5;
        System.arraycopy(TransactionDetails.TerminalRandomNumber,0,localByte,5,8);
        System.arraycopy(TransactionDetails.CardNumberRandomNumber,0,localByte,13,8);
        System.arraycopy(TransactionDetails.CAN,0,localByte,21,8);
        System.arraycopy(TransactionDetails.CSN,0,localByte,29,8);
        apduByte =localByte;

        try {
            apduOutByte = mReadCardOpt.PSAMAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[16] == -112 && apduOutByte[17] == 0x00) {
                    System.arraycopy(apduOutByte, 0, TransactionDetails.SessionKey, 0, 16);
                    //String aa = "5899BD26F6CAF3287305974C210B0321";
                    //TransactionDetails.SessionKey = StringByteUtils.HexString2Bytes(aa);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }





}
