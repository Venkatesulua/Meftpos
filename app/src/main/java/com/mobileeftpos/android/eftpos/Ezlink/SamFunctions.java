package com.mobileeftpos.android.eftpos.Ezlink;

import android.os.RemoteException;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.SupportClasses.TripleDes;
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

    public int initPSAMCard(ReadCardOpt mReadCardOpt) {
        byte[] outdata = new byte[512];
        try {
            int result = mReadCardOpt.initPSAMCard(outdata);
            byte[] newByte = new byte[result];
            System.arraycopy(outdata, 0, newByte, 0, newByte.length);
            if (result > 0) {
                return Constants.ReturnValues.RETURN_OK;
            } else {
                return Constants.ReturnValues.RETURN_ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
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
        return Constants.ReturnValues.RETURN_ERROR;
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


    static int crc16(final byte[] buffer) {
        int crc = 0xFFFF;

        for (int j = 0; j < buffer.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (buffer[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;

    }


    int calculate_crc(byte[] bytes) {
        int i;
        int crc_value = 0;
        for (int len = 0; len < bytes.length; len++) {
            for (i = 0x80; i != 0; i >>= 1) {
                if ((crc_value & 0x8000) != 0) {
                    crc_value = (crc_value << 1) ^ 0x8005;
                } else {
                    crc_value = crc_value << 1;
                }
                if ((bytes[len] & i) != 0) {
                    crc_value ^= 0x8005;
                }
            }
        }
        return crc_value;
    }

    int vdRecordDebitCRC()
    {
        byte[] data=new byte[24];
        int i=0;
        byte[] temp;

        System.arraycopy(TransactionDetails.PaymentTRP,0,data,0,4);
        data[4] = 0x00; //Debit Options
        data[5] = 0x03;
        data[6] = 0x14;
        data[7] = 0x03;
        data[8] = (byte)0xA0;//txn type
        System.arraycopy(TransactionDetails.EzlinkPaymentAmt,0,data,9,3);
        System.arraycopy(TransactionDetails.JulianDate,0,data,12,4);
        byte[] userdata = new byte[8];
        userdata = Constants.Ezlink.User_Data.getBytes();
        //userdata[7] = 0x20;
        System.arraycopy(userdata,0,data,16,8);

        //int crcRes = calculate_crc(data);
        //System.out.println(Integer.toHexString(crcRes));

        //TransactionDetails.GtxnCRC[0] = (byte) ((crcRes & 0x000000ff));
        //TransactionDetails.GtxnCRC[1] = (byte) ((crcRes & 0x0000ff00) >>> 8);

        //System.out.printf("%02X\n%02X", TransactionDetails.GtxnCRC[0],TransactionDetails.GtxnCRC[1]);

        //int CRC = crc16(data);
        //System.out.println("hhhhh");
        TransactionDetails.GtxnCRC = ComputeCrc(1, data, 24);
        //String hxStr = Integer.toHexString(inCRC);
       //byte[] GtxnCRC = hxStr.getBytes();
        //ComputeCrc(CRC_B,data,24,GtxnCRC,GtxnCRC+1);
        return Constants.ReturnValues.RETURN_OK;
    }


    int UpdateCrc(byte ch, int lpwCrc) {
        int hi=0;

        ch = (byte)(ch ^ ((byte) (lpwCrc & 0x00FF)));

        ch = (byte)(ch ^ (byte)((ch << 4)));
        if(ch<0) {
            hi = 256 + ch;
        }else
            hi =ch;
	lpwCrc = (lpwCrc >> 8) ^ ((int) hi << 8) ^ ((int) hi << 3) ^ ((int) hi >> 4);
        return (lpwCrc);
    }
    byte[] ComputeCrc(int CRCType, byte[] Data, int Length) {
        byte[] byRet = new byte[2];
        byte chBlock;
        int wCrc;
        int i=0;
        switch (CRCType) {
            case 0:
                wCrc = 0x6363; // ITU-V.41
                break;
            case 1:
                wCrc = 0xFFFF; // ISO 3309
                break;
            default:
                return null;
        }
        do {
            chBlock = Data[i++];
            wCrc = UpdateCrc(chBlock, wCrc);
            Length = Length-1;
        } while (Length !=0);
        if (CRCType == 1)
            wCrc = ~wCrc; // ISO 3309
        if(wCrc<0)
            wCrc=65536 + wCrc;
        byRet[0] = (byte) (wCrc & 0xFF);
        byRet[1] = (byte) ((wCrc >> 8) & 0xFF);
        return byRet;
    }

    ///YYMMDD format for ascii date
    long ulGetJulianDate(byte[] AsciiDate, byte[] JulianDate)
    {
        int Year;
        int Month;
        int Day;
        long lnJulDt;
        Year = (AsciiDate[0] -'0')*10;
        Year = Year + (AsciiDate[1] -'0');
        Year = Year -1; //add uptio last year only, this year is not finished yet

        Month = (AsciiDate[2] -'0')*10;
        Month = Month + (AsciiDate[3] -'0');

        Day = (AsciiDate[4] -'0')*10;
        Day = Day + (AsciiDate[5] -'0');

        lnJulDt = 0;
        lnJulDt = lnJulDt + (365*Year);
        lnJulDt = lnJulDt + (Year/4);

        switch(Month)
        {
            case 1:
                //nothing to add here
                break;
            case 2:
                lnJulDt = lnJulDt +31;
                break;
            case 3:
                lnJulDt = lnJulDt +31+28;
                break;
            case 4:
                lnJulDt = lnJulDt +31+28+31;
                break;
            case 5:
                lnJulDt = lnJulDt +31+28+31+30;
                break;
            case 6:
                lnJulDt = lnJulDt +31+28+31+30+31;
                break;
            case 7:
                lnJulDt = lnJulDt +31+28+31+30+31+30;
                break;
            case 8:
                lnJulDt = lnJulDt +31+28+31+30+31+30+31;
                break;
            case 9:
                lnJulDt = lnJulDt +31+28+31+30+31+30+31+31;
                break;
            case 10:
                lnJulDt = lnJulDt +31+28+31+30+31+30+31+31+30;
                break;
            case 11:
                lnJulDt = lnJulDt +31+28+31+30+31+30+31+31+30+31;
                break;
            case 12:
                lnJulDt = lnJulDt +31+28+31+30+31+30+31+31+30+31+30;
                break;
        }
        if( ( ((Year+1) %4) ==0) && (Month >2)) //current year is leap year
            lnJulDt = lnJulDt+1 ; //added extra day for feb
        lnJulDt = lnJulDt + Day;
        lnJulDt = lnJulDt + 2192-1;//JAN01 - 0x890
        vdLongToHex(lnJulDt,JulianDate,4);
        return lnJulDt;
    }

    ///YYMMDD format for ascii date, HHMMSS for Time
    long ulGetJulianSeconds(byte[] AsciiDate,byte[] AsciiTime, byte[] JulianSeconds)
    {
        int Hour;
        int Minute;
        int Second;
        long lnJulDt;
        lnJulDt = ulGetJulianDate(AsciiDate,JulianSeconds);
        lnJulDt = lnJulDt*24*60*60; //convert julian date to julian seconds


        Hour = (AsciiTime[0] -'0')*10;
        Hour = Hour + (AsciiTime[1] -'0');

        Minute = (AsciiTime[2] -'0')*10;
        Minute = Minute + (AsciiTime[3] -'0');

        Second = (AsciiTime[4] -'0')*10;
        Second = Second + (AsciiTime[5] -'0');

        lnJulDt = lnJulDt + (Hour*60*60) + (Minute*60) + Second;
        vdLongToHex(lnJulDt,JulianSeconds,4);
        return lnJulDt;
    }

    void vdLongToHexAmt(long src,byte[] dst)
    {
        byte[] tempdst = new byte[10];
        int i;
        i =0;
        int inValidDigits;
        if(src<0)
            src = src + 0x1000000;
        while(src !=0)
        {
            tempdst[i] =(byte)(src%256);
            src = src/256;
            i++;
        }
        inValidDigits = i;
        for(i=inValidDigits; i<3; i++)
            tempdst[i] = 0x00;
        ///reverse the digits to get correct order
        for(i=0; i<3; i++)
            dst[i] = tempdst[3-(i+1)];
    }

    void vdLongToHex(long src,byte[] dst, int len)
    {
        byte[] tempdst = new byte[10];
        int i;
        i =0;
        int inValidDigits;
        while(src !=0)
        {
            tempdst[i] =(byte)(src%256);
            src = src/256;
            i++;
        }
        inValidDigits = i;
        for(i=inValidDigits; i<len; i++)
            tempdst[i] = 0x00;
        ///reverse the digits to get correct order
        for(i=0; i<len; i++)
            dst[i] = tempdst[len-(i+1)];
    }

    int Ezlink_inGetSessionKey(ReadCardOpt mReadCardOpt,int KeyType)
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

        if(KeyType ==0x00 && TransactionDetails.IssuerSpecificdata[8] == 0x55)
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
                    //String aa = "5899BD26F6CAF32897FFBEBF75F0B558";
                    //TransactionDetails.SessionKey = StringByteUtils.HexString2Bytes(aa);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }

    public byte[] inCreateField63(DBHelper databaseObj){
        //Ezlink header end here
        EzlinkModel EzlinkData = databaseObj.getEzlinkData(0);
        byte[] byDevType = StringByteUtils.HexString2Bytes(EzlinkData.getEZLINK_PAYMENT_DEVICE_TYPE());
        byte[] byField63 = new byte[32+112];
        byte[] byField63Header=new byte[32];
        byte[] byField63Content=new byte[112];

        System.arraycopy(StringByteUtils.HexString2Bytes("001E"), 0, byField63Header, 0, 2);//Header length
        System.arraycopy(StringByteUtils.HexString2Bytes("0000"), 0, byField63Header, 2, 2);//Header_Ver
        System.arraycopy(byDevType, 0, byField63Header, 4, 2);//Device type
        System.arraycopy(TransactionDetails.SAMSerialNumber, 0, byField63Header, 6, 8);//SAM Serial Number
        System.arraycopy(StringByteUtils.HexString2Bytes("7000"), 0, byField63Header, 14, 2);//Packet Type
        System.arraycopy(StringByteUtils.HexString2Bytes("00000002"), 0, byField63Header, 16, 4);//Packet code
        if(TransactionDetails.CAN[0] != (byte)0x80)
            System.arraycopy(StringByteUtils.HexString2Bytes("02"), 0, byField63Header, 20, 1);//Card type
        else
            System.arraycopy(StringByteUtils.HexString2Bytes("80"), 0, byField63Header, 20, 1);//Card type
        System.arraycopy(StringByteUtils.HexString2Bytes("00"), 0, byField63Header, 21, 1);//retry count
        System.arraycopy(StringByteUtils.HexString2Bytes("00"), 0, byField63Header, 22, 1);//Transaction type 00-normal , 01-uncnfirmed
        System.arraycopy(StringByteUtils.HexString2Bytes("00000095"), 0, byField63Header, 23, 4);//Payload len
        System.arraycopy(StringByteUtils.HexString2Bytes("00"), 0, byField63Header, 27, 1);//Payload len
        System.arraycopy(StringByteUtils.HexString2Bytes("0000"), 0, byField63Header, 28, 2);//
        byte[] byCRC = ComputeCrc(1,byField63Header,30);
        System.arraycopy(byCRC, 0, byField63Header, 30, 2);

        System.arraycopy(StringByteUtils.HexString2Bytes("0000006C"), 0, byField63Content, 0, 4);//Data len
        System.arraycopy(TransactionDetails.JulianDate, 0, byField63Content, 4, 4);//Date
        HostModel hostdata = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        String stTerminalID = hostdata.getHDT_TERMINAL_ID();
        byte[] byTerminalID = StringByteUtils.HexString2Bytes(stTerminalID);
        System.arraycopy(byTerminalID, 0, byField63Content,8 , 4);
        System.arraycopy(TransactionDetails.JulianDate, 0, byField63Content, 12, 4);
        System.arraycopy(TransactionDetails.CAN, 0, byField63Content, 16, 8);
        System.arraycopy(TransactionDetails.bBDC, 0, byField63Content, 24, 1);
        System.arraycopy(StringByteUtils.HexString2Bytes("A0"), 0, byField63Content, 25, 1);
        System.arraycopy(TransactionDetails.EzlinkPaymentAmt, 0, byField63Content, 26, 3);
        System.arraycopy(TransactionDetails.JulianDate, 0, byField63Content, 29, 4);
        System.arraycopy(TransactionDetails.PurseBalance, 0, byField63Content, 33, 3);
        System.arraycopy(TransactionDetails.CounterData, 0, byField63Content, 36, 8);
        System.arraycopy(TransactionDetails.SignCert, 0, byField63Content, 44, 8);
        System.arraycopy(TransactionDetails.LastCreditHeader, 0, byField63Content, 52, 8);
        System.arraycopy(TransactionDetails.LastCreditTRP, 0, byField63Content, 60, 4);
        System.arraycopy(TransactionDetails.LastPruseStatus, 0, byField63Content, 64, 1);
        System.arraycopy(TransactionDetails.LastHeader, 0, byField63Content, 65, 8);
        System.arraycopy(TransactionDetails.LastTRP, 0, byField63Content, 73, 4);
        System.arraycopy(TransactionDetails.bOrigBal, 0, byField63Content, 77, 3);
        System.arraycopy(TransactionDetails.LastCounterData, 0, byField63Content, 80, 8);
        System.arraycopy(TransactionDetails.LastSignCert, 0, byField63Content, 88, 8);
        System.arraycopy(TransactionDetails.LastOptions, 0, byField63Content, 96, 1);
        System.arraycopy(TransactionDetails.AutoLoadAmt, 0, byField63Content, 97, 3);
        System.arraycopy(TransactionDetails.PaymentTRP, 0, byField63Content, 100, 4);
        byte[] byMAC=inCalculateEZMAC(byField63Content,104,StringByteUtils.HexString2Bytes("0000000000000000"),databaseObj);
        System.arraycopy(byMAC, 0, byField63Content, 104, 8);

        System.arraycopy(byField63Header, 0, byField63, 0, 32);
        System.arraycopy(byField63Content, 0, byField63, 32, 112);


        return byField63;
    }

    byte[] inCalculateEZMAC(byte[] chData2MAC, int inLength, byte[] chInVector,DBHelper databaseObj) {

        byte[] chLeftKey=new byte[8];
        byte[] chRightKey=new byte[8];
        byte[] chMacKey=new byte[16];
        byte[] chBlock=new byte[8];
        byte[] chDestination=new byte[8];
        int inTotalMACLen = 0, iCnt = 0;
        //Load HostStruct
        // pad the data to MAC to multiples of 8
        while ((inLength % 8) != 0) {
            chData2MAC[inLength++] = 0;
        }
        TripleDes tripleDes=new TripleDes(TransactionDetails.SessionKey);
        inTotalMACLen = inLength;

        EzlinkModel EzlinkData = databaseObj.getEzlinkData(0);
        //if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE)
        //{
           // chMacKey= EzlinkData.getEZLINK_PAYMENT_MAC_KEY().getBytes();
        //}
        //get MAC Key
        /*memset(chLeftKey, 0, sizeof(chLeftKey));
        memset(chRightKey, 0, sizeof(chRightKey));
        //vdStrToHex((char*)chMacKey,(char*)stGHostStruct.HDT_EZ_MAC_KEY,32);
        //vdStrToHex((char*)chMacKey,(char*)stGHDTStruct.HDT_PEK,32);
        if(stGlobalex.inGTransaction == EZLINK_SALE)//VENKAT ADDED
            memcpy((char*)chMacKey,(char*)stGEZLINKConfig.EZLINK_PAYMENT_MAC_KEY,16);
		else
        memcpy((char*)chMacKey,(char*)stGEZLINKConfig.EZLINK_TOPUP_MAC_KEY, 16);
        memcpy(chLeftKey, chMacKey, 8);
        memcpy(chRightKey, chMacKey + 8, 8);
        //INIT DES
        //OS_DesKey(chLeftKey);
        deskey((unsigned char*) chLeftKey, EN0);
        memset(chDestination, 0, sizeof(chDestination));
        while (inLength > 0) {
            memcpy(chBlock, chData2MAC, 8);

            for (iCnt = 0; iCnt < 8; iCnt++)
                chDestination[iCnt] = chDestination[iCnt] ^ chBlock[iCnt];
            //encrypt block

            //OS_DesEncrypt(chDestination);
            des(chDestination, chDestination);
            chData2MAC += 8;
            inLength -= 8;

        }
        //INIT DES
        deskey((unsigned char*) chRightKey, DE1);
        //Decrypt Last 8 byte block to get MAC
        des(chDestination, chDestination);

        //INIT DES
        deskey((unsigned char*) chLeftKey, EN0); //Ezlink: ABL Changes
        //Encrypt LAst 8 byte block to complete 3des MAC
        des(chDestination, chDestination);

        memcpy(chResult, chDestination, 8);*/

        return StringByteUtils.HexString2Bytes("0000000000000000");
    }

}
