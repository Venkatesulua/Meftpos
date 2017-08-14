package com.mobileeftpos.android.eftpos.Ezlink;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.SupportClasses.TripleDes;
import com.mobileeftpos.android.eftpos.activity.MyApplication;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

import java.util.Random;

import sunmi.sunmiui.utils.LogUtil;

/**
 * Created by venkat on 7/12/2017.
 */

public class CardFunctions {

    private int cardType, time=30;
    private ReadCardOpt mReadCardOpt1;
    private byte[] apduByte, apduOutByte;

    CardFunctions() {
        mReadCardOpt1 = MyApplication.mReadCardOpt;
    }

    public void checkCard() {
        cardType = 0;
        cardType += AidlConstants.CardType.NFC;
        try {
            mReadCardOpt1.readCard(cardType, readCardCallback, time);
        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }
    public void cancelCheckCard()
    {
        try {
            //终止检卡
            mReadCardOpt1.cancelCheckCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int inGetChallenge(ReadCardOpt mReadCardOpt){
        apduByte = StringByteUtils.HexString2Bytes("0084000008");
        try {
            apduOutByte = mReadCardOpt.NFCAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[8] == -112 && apduOutByte[9] == 0x00) {
                    System.arraycopy(apduOutByte, 0, TransactionDetails.CardNumberRandomNumber, 0, 8);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }

    void getTerminalRandomNumber(){
        int min = 10000000;
        int max = 99999999;
        Random rand = new Random();
        int nResult1 = rand.nextInt((99999999 - 10000000) + 1) + 10000000;
        int nResult2 = rand.nextInt((99999999 - 10000000) + 1) + 10000000;
        String stRandom = String.format("%08d",nResult1);// + String.format("%08d",nResult2);
        TransactionDetails.TerminalRandomNumber = stRandom.getBytes();

    }

    int lnReadPurse(ReadCardOpt mReadCardOpt) {

        byte[] localBytes = new byte[16];
        getTerminalRandomNumber();
        String inputApdu = "903203000A1403";//+ new String(TransactionDetails.TerminalRandomNumber) + "00";
       // localBytes = StringByteUtils.HexString2Bytes(inputApdu);
        localBytes[0]=(byte)0x90;
        localBytes[1]=0x32;
        localBytes[2]=0x03;
        localBytes[3]=0x00;
        localBytes[4]=0x0A;
        localBytes[5]=0x14;
        localBytes[6]=0x03;
        System.arraycopy(TransactionDetails.TerminalRandomNumber, 0, localBytes, 7, 8);
        localBytes[15]=0x00;
        apduByte = localBytes;
        try {
            apduOutByte = mReadCardOpt.NFCAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_FAILED;
            } else   {
                int returnst= apduOutByte[apduOutByte.length-2]+apduOutByte[apduOutByte.length-1];
                //Get Card information
                int bal = (apduOutByte[2]*256*256) + (apduOutByte[3] *256) + apduOutByte[4];
                String Displaystr= "CEPAS BALANCE SGD : "+ Integer.toString(bal/100)+"."+ String.format("%02d",(bal%100));

                System.arraycopy(apduOutByte,0,TransactionDetails.CEPASver,0,1);
                System.arraycopy(apduOutByte,1,TransactionDetails.LastPruseStatus,0,1);
                System.arraycopy(apduOutByte,2,TransactionDetails.bOrigBal,0,3);
                System.arraycopy(apduOutByte,5,TransactionDetails.AutoLoadAmt,0,3);
                System.arraycopy(apduOutByte,8,TransactionDetails.CAN,0,8);
                System.arraycopy(apduOutByte,16,TransactionDetails.CSN,0,8);
                System.arraycopy(apduOutByte,24,TransactionDetails.PurseExpiryDate,0,2);
                System.arraycopy(apduOutByte,26,TransactionDetails.PurseCreationDate,0,2);
                System.arraycopy(apduOutByte,28,TransactionDetails.LastCreditTRP,0,4);
                System.arraycopy(apduOutByte,32,TransactionDetails.LastCreditHeader,0,8);
                System.arraycopy(apduOutByte,40,TransactionDetails.TransLogCount,0,1);
                System.arraycopy(apduOutByte,41,TransactionDetails.IssuerSpecificdataLength,0,1);
                System.arraycopy(apduOutByte,42,TransactionDetails.LastTRP,0,4);
                System.arraycopy(apduOutByte,46,TransactionDetails.LastHeader,0,16);
                System.arraycopy(apduOutByte,62,TransactionDetails.IssuerSpecificdata,0,32);
                System.arraycopy(apduOutByte,94,TransactionDetails.LastOptions,0,1);
                System.arraycopy(apduOutByte,95,TransactionDetails.LastSignCert,0,8);
                System.arraycopy(apduOutByte,103,TransactionDetails.LastCounterData,0,8);
                System.arraycopy(apduOutByte,111,TransactionDetails.IsoCrc_b,0,2);

                TransactionDetails.bBDC[0] = (byte)(apduOutByte[63] & 0x03);
                 TransactionDetails.bRefund[0] = (byte)(apduOutByte[66] & 0xC0); // Co-branded cards
                return bal;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_FAILED;
    }

    byte[] vdXorBlock(byte[] Data1, byte[] Data2) {
        byte[]output = new byte[8];
        int i = 0;
        for (i = 0; i < 8; i++)
            output[i] = (byte)(Data1[i] ^ Data2[i]);
        return output;
    }

    int DeductAmount(ReadCardOpt mReadCardOpt,int KeyType){
        //Calculate Crytogram
        byte[] localBytes = new byte[43];
        byte[] Cryptogram1=new byte[8];
        byte[] Cryptogram2=new byte[8];
        byte[] InputData1=new byte[8];
        byte[] InputData2=new byte[8];
        byte[] stInputData= new byte[16];

        System.arraycopy(TransactionDetails.PaymentTRP,0,InputData1,0,4);
        System.arraycopy(TransactionDetails.GtxnCRC,0,InputData1,4,2);
        InputData1[6] = 0x14;
        InputData1[7] = 0x03;

        InputData2[0] =(byte)0xA0;
        System.arraycopy(TransactionDetails.EzlinkPaymentAmt,0,InputData2,1,3);
        System.arraycopy(TransactionDetails.JulianDate,0,InputData2,4,4);

        TripleDes tripleDes=new TripleDes(TransactionDetails.SessionKey);
        try {
            stInputData = tripleDes.encrypt(InputData1);
            System.arraycopy(stInputData,0,Cryptogram1,0,8);
           // Cryptogram1 = stInputData;
            InputData1=null;
            InputData1=new byte[8];
            InputData1 = vdXorBlock(Cryptogram1, InputData2);
            stInputData = tripleDes.encrypt(InputData1);
            System.arraycopy(stInputData,0,Cryptogram2,0,8);



        }catch(Exception e){

        }

       // apduByte = StringByteUtils.HexString2Bytes("90340000250315021403");
        localBytes[0]=(byte)0x90;
        localBytes[1]=0x34;
        localBytes[2]=0x00;
        localBytes[3]=0x00;
        localBytes[4]=0x25;
        localBytes[5]=0x03;
        if(KeyType == 0x01)
        {
            localBytes[6] = 0x15;
            localBytes[7] = 0x01;
        }else {
            localBytes[6] = 0x15;
            localBytes[7] = 0x02;
        }
        localBytes[8]=0x14;
        localBytes[9]=0x03;
        System.arraycopy(TransactionDetails.TerminalRandomNumber,0,localBytes,10,8);
        System.arraycopy(Cryptogram1,0,localBytes,18,8);
        System.arraycopy(Cryptogram2,0,localBytes,26,8);
        System.arraycopy(Constants.Ezlink.User_Data.getBytes(),0,localBytes,34,8);
        localBytes[42]=0x18;
        apduByte=localBytes;

        try {
            apduOutByte = mReadCardOpt.NFCAPDUComm(apduByte);
            if (apduOutByte == null) {
                return Constants.ReturnValues.RETURN_ERROR;
            } else {
                //Get Card information
                if(apduOutByte[(apduOutByte.length)-2] == -112 && apduOutByte[(apduOutByte.length)-1] == 0x00) {
                    //System.arraycopy(apduOutByte, 0, TransactionDetails.CardNumberRandomNumber, 0, 8);
                    vdDecryptFinalResponse(apduOutByte);
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }

    void vdDecryptFinalResponse(byte[] baRxBuf) {
        byte[] sndBuff= new byte[50];
        byte[]  Cryptogram1= new byte[8];
        byte[]  Cryptogram2= new byte[8];
        byte[]  Cryptogram3= new byte[8];
        byte[]  InputData= new byte[8];

        System.arraycopy(baRxBuf, 0, Cryptogram1, 0, 8);
        System.arraycopy(baRxBuf, 8, Cryptogram2, 0, 8);
        System.arraycopy(baRxBuf, 16, Cryptogram3, 0, 8);

        TripleDes tripleDes=new TripleDes(TransactionDetails.SessionKey);
        try {
            Cryptogram1 = tripleDes.decrypt(Cryptogram1);
            Cryptogram2 = tripleDes.decrypt(Cryptogram2);
            Cryptogram3 = tripleDes.decrypt(Cryptogram3);

            System.arraycopy(baRxBuf, 8, InputData, 0, 8);
            Cryptogram3 = vdXorBlock(Cryptogram3, InputData);
            Cryptogram1 = vdXorBlock(Cryptogram3, Cryptogram1);
            Cryptogram2 = vdXorBlock(baRxBuf, Cryptogram2);

            //System.arraycopy(Cryptogram1, 0, baRxBuf, 0, 8);
            //System.arraycopy(Cryptogram2, 0, baRxBuf, 8, 8);
            //System.arraycopy(Cryptogram3, 0, baRxBuf, 16, 8);

            System.arraycopy(Cryptogram1, 0, TransactionDetails.PurseBalance, 0, 3);
            System.arraycopy(Cryptogram2, 0, TransactionDetails.SignCert, 0, 8);
            System.arraycopy(Cryptogram3, 0, TransactionDetails.CounterData, 0, 8);


        }catch(Exception e){

        }

        //vdTDESDecrypt( SessionKey,InputData ,Cryptogram1);
        /*des2key((unsigned char*) SessionKey, DE1);
        Ddes(InputData, InputData);
        memcpy(Cryptogram1, InputData, 8);

        memset(InputData, 0x00, sizeof(InputData));
        memcpy(InputData, baRxBuf + 8, 8);
        //vdTDESDecrypt( SessionKey,InputData ,Cryptogram2);
        des2key((unsigned char*) SessionKey, DE1);
        Ddes(InputData, InputData);
        memcpy(Cryptogram2, InputData, 8);

        memset(InputData, 0x00, sizeof(InputData));
        memcpy(InputData, baRxBuf + 16, 8);
        //vdTDESDecrypt( SessionKey,InputData ,Cryptogram3);
        des2key((unsigned char*) SessionKey, DE1);
        Ddes(InputData, InputData);
        memcpy(Cryptogram3, InputData, 8);

        memset(InputData, 0x00, sizeof(InputData));
        memcpy(InputData, baRxBuf + 8, 8);
        memset(sndBuff, 0x00, sizeof(sndBuff));
        vdXorBlock(Cryptogram3, InputData, sndBuff); //this is the counter data
        memset(Cryptogram3, 0x00, sizeof(Cryptogram3));
        memcpy(Cryptogram3, sndBuff, 8); //copy the clear data to crypto3

        memset(sndBuff, 0x00, sizeof(sndBuff));
        vdXorBlock(Cryptogram1, Cryptogram3, sndBuff);
        memset(Cryptogram1, 0x00, sizeof(Cryptogram1));
        memcpy(Cryptogram1, sndBuff, 8); //clear packet balance

        memset(sndBuff, 0x00, sizeof(sndBuff));
        memset(InputData, 0x00, sizeof(InputData));
        //c_memcpy(InputData,baRxBuf+8,8);
        memcpy(InputData, baRxBuf, 8);
        vdXorBlock(InputData, Cryptogram2, sndBuff);
        memcpy(Cryptogram2, sndBuff, 8); //clear signed certificate

        memset(baRxBuf, 0x00, sizeof(baRxBuf));
        memcpy(baRxBuf, Cryptogram1, 8);
        memcpy(baRxBuf + 8, Cryptogram2, 8);
        memcpy(baRxBuf + 16, Cryptogram3, 8);
        //memset(stCepasConfig.FinalDecryptedResponse, 0x00, sizeof(stCepasConfig.FinalDecryptedResponse));
        //memcpy(stCepasConfig.FinalDecryptedResponse, baRxBuf, 24);

        memcpy(stCepasConfig.PurseBalance, baRxBuf, 3);
        memcpy(stCepasConfig.CounterData, baRxBuf + 16, 8);
        memcpy(stCepasConfig.SignCert, baRxBuf + 8, 8);*/
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0x01://Magstripe
                    CardInfo cardInfo = (CardInfo) msg.obj;
                    if(cardInfo.cardNo == null)
                        break;
                case 0x02://NFC
                    //Read NFC CARD
                    inGetChallenge(mReadCardOpt1);
                    lnReadPurse(mReadCardOpt1);
                    break;
                case 0x03:
                    break;
                case 0x0e://timeout
                    break;
                case 0x0f://error
                    break;
            }
        }
    };

    private ReadCardCallback readCardCallback = new ReadCardCallback.Stub() {

        @Override
        public void onStartReadCard() throws RemoteException {
        }

        @Override
        public void onFindMAGCard(CardInfo cardInfo) throws RemoteException {
            LogUtil.d("lj", cardInfo.toString());
            Message message = new Message();
            message.what = 0x01;
            message.obj = cardInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFindNFCCard(CardInfo cardInfo) throws RemoteException {
            Message message = new Message();
            message.what = 0x02;
            message.obj = cardInfo;
            mHandler.sendMessage(message);

        }

        @Override
        public void onFindICCard(CardInfo cardInfo) throws RemoteException {
            Message message = new Message();
            message.what = 0x03;
            message.obj = cardInfo;
            mHandler.sendMessage(message);

        }

        @Override
        public void onError(int i) throws RemoteException {
            Message message = new Message();
            message.what = 0x0f;
            message.obj = i;
            mHandler.sendMessage(message);

        }

        @Override
        public void onTimeOut() throws RemoteException {
            mHandler.sendEmptyMessage(0x0e);

        }
    };

    public void Ezlink_vdDecryptCounterData() {
        byte[] InputData =new byte[8];
        byte[] CounterData =new byte[8];
        byte[] Output =new byte[8];
        byte[] tempbuf=new byte[8];

        System.arraycopy(TransactionDetails.LastSignCert, 0, InputData, 0, 8);
        System.arraycopy(TransactionDetails.LastSignCert, 0, tempbuf, 0, 8);
        System.arraycopy(TransactionDetails.LastCounterData, 0, CounterData, 0, 8);

        TripleDes tripleDes=new TripleDes(TransactionDetails.SessionKey);
        try {
            TransactionDetails.LastSignCert = tripleDes.decrypt(InputData);
            Output = tripleDes.decrypt(CounterData);
            TransactionDetails.LastCounterData = vdXorBlock(Output, tempbuf);
        }catch(Exception e){

            String sss=e.getMessage();
            String sss1="";
        }
        /*memcpy(InputData, TransactionDetails.LastSignCert, 8);
        des2key((unsigned char*) SessionKey, DE1);
        Ddes(InputData, InputData);
        memcpy(OutputData, InputData, 8);
        memcpy(outLastTransSignedCert, OutputData, 8);

        memset(InputData, 0x00, sizeof(InputData));
        memcpy(InputData, EncryptedData + 9, 8);
        memset(OutputData, 0x00, sizeof(OutputData));

        des2key((unsigned char*) SessionKey, DE1);
        Ddes(InputData, InputData);
        memcpy(OutputData, InputData, 8);

        memset(tempbuf, 0x00, sizeof(tempbuf));
        memset(InputData, 0x00, sizeof(InputData));
        memcpy(InputData, EncryptedData + 1, 8);
        vdXorBlock(OutputData, InputData, tempbuf);
        memcpy(outLastCounterData , tempbuf, 8);*/

    }
}
