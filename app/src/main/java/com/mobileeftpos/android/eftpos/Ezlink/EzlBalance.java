package com.mobileeftpos.android.eftpos.Ezlink;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.activity.MyApplication;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

import sunmi.sunmiui.utils.LogUtil;

public class EzlBalance extends AppCompatActivity {


    private CardFunctions cardRelated =new CardFunctions();
    public TextView TVStatus;
    private int cardType, time=30;
    private ReadCardOpt mReadCardOpt;
    private byte[] apduByte, apduOutByte;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezl_balance);
        TVStatus = (TextView)findViewById(R.id.viewBalance) ;

        context = EzlBalance.this;
        //Get Card
        //Read Card content
        //Display Card

        //cardRelated.checkCard(context);
        mReadCardOpt = MyApplication.mReadCardOpt;
        checkCard();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //cardRelated.cancelCheckCard();
        cancelCheckCard();
        return super.onKeyDown(keyCode, event);
    }

    public void checkCard() {
        cardType = 0;
        cardType += AidlConstants.CardType.NFC;
        try {
            mReadCardOpt.readCard(cardType, readCardCallback, time);
        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }
    public void cancelCheckCard()
    {
        try {
            //终止检卡
            mReadCardOpt.cancelCheckCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if(cardRelated.inGetChallenge(mReadCardOpt) != Constants.ReturnValues.RETURN_OK) {
                        TVStatus.setText("GET Challenge Failed !!!");
                        return;
                    }
                    int inBal = cardRelated.lnReadPurse(mReadCardOpt);
                    if(inBal != Constants.ReturnValues.RETURN_FAILED) {
                        String inB = "CEPAS BALANCE SGD: " + String.format("%01d", inBal / 100) + "." + String.format("%02d", inBal % 100);
                        TVStatus.setText(inB);
                    }else
                        TVStatus.setText("Read Purse Failed !!!");
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

}
