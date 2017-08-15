package com.mobileeftpos.android.eftpos.Ezlink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;

import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.activity.AlipayCheckPrompt;
import com.mobileeftpos.android.eftpos.activity.HomePagerActivity;
import com.mobileeftpos.android.eftpos.activity.MyApplication;
import com.mobileeftpos.android.eftpos.activity.PaymentFailure;
import com.mobileeftpos.android.eftpos.activity.PaymentSuccess;

import com.mobileeftpos.android.eftpos.db.DaoSession;

import com.mobileeftpos.android.eftpos.scan.SoundUtils;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

import sunmi.sunmiui.utils.LogUtil;

public class CepasPayment extends AppCompatActivity {

    public HAfterTransaction afterTranscation = new HAfterTransaction();
    private CardFunctions cardRelated =new CardFunctions();
    private SamFunctions samRelated =new SamFunctions();
    public TextView TvStatus,TvTransAmt,TvCurBal,TvNewBal;
    private int cardType, time=45;
    private ReadCardOpt mReadCardOpt;
    private byte[] apduByte, apduOutByte;
    public Context context;

    private Activity activity;
    private DaoSession daoSession;
	SoundUtils soundUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cepas_payment);
        mReadCardOpt = MyApplication.mReadCardOpt;
        TvTransAmt =(TextView)findViewById(R.id.trxAmount);
        TvCurBal =(TextView)findViewById(R.id.currentBalance);
        TvNewBal =(TextView)findViewById(R.id.newBalance);
        TvStatus = (TextView)findViewById(R.id.payStatus);
        initBeepSound();
        int inAmount = Integer.parseInt(TransactionDetails.trxAmount);
        String inAmtDisplay = "AMOUNT SGD: "+String.format("%01d",(inAmount/100))+"."+String.format("%02d",(inAmount%100));
        TvTransAmt.setText(inAmtDisplay);
        //daoSession = GreenDaoSupport.getInstance(CepasPayment.this);

        //Salect SAM CARd
        activity = CepasPayment.this;

        int inError = afterTranscation.inSearchHost(Constants.HostType.EZLINK_PAYMENT_HOST);
        if(inError != Constants.ReturnValues.RETURN_OK)
        {
            afterTranscation.FinalStatusDisplay(activity,Integer.toString(inError));
            return;
        }
        inError = samRelated.initPSAMCard(mReadCardOpt);
        if(inError != Constants.ReturnValues.RETURN_OK)
        {
            afterTranscation.FinalStatusDisplay(activity,Integer.toString(inError));
            return;
        }
        inError = samRelated.SelectEzlSAMApp(mReadCardOpt);
        if(inError != Constants.ReturnValues.RETURN_OK)
        {
            afterTranscation.FinalStatusDisplay(activity,Integer.toString(inError));
            return;
        }
        inError = samRelated.SelectEzlSAMGetId(mReadCardOpt);
        if(inError != Constants.ReturnValues.RETURN_OK)
        {
            afterTranscation.FinalStatusDisplay(activity,Integer.toString(inError));
            return;
        }
        inError = samRelated.SelectEzlSAMEnable(mReadCardOpt);
        if(inError != Constants.ReturnValues.RETURN_OK)
        {
            afterTranscation.FinalStatusDisplay(activity,Integer.toString(inError));
            return;
        }

        //hardcoded value
        int in32LocalAmount = Integer.parseInt(TransactionDetails.trxAmount);
        in32LocalAmount = 0x1000000 - in32LocalAmount;
        for(int i=2;i>=0;i--){
            TransactionDetails.EzlinkPaymentAmt[i] = (byte)(in32LocalAmount%256);
            in32LocalAmount = in32LocalAmount /256;
        }

        String Date = TransactionDetails.trxDateTime.substring(2,8);
        String Time = TransactionDetails.trxDateTime.substring(8,14);
        afterTranscation.ulGetJulianSeconds(Date.getBytes(),Time.getBytes(),TransactionDetails.JulianDate);


        //Host Enabled or not
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
                    if (AppUtil.isNetworkAvailable(CepasPayment.this)) {
                        new AsyncTaskRunner().execute();


                    }else {
                        Toast.makeText(CepasPayment.this, "CEPAS:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();

                    }




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

    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
                String result = "";
                int inRet = -1;
                try {

                    inRet = processRequest();
                    return Integer.toString(inRet);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);
            //cardRelated.cancelCheckCard();
            cancelCheckCard();

            //payServices.vdUpdateSystemTrace(databaseObj);
            if(result != null) {
                if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                    //printReceipt.inPrintReceipt(databaseObj);
                    //Redirect to Success Activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CepasPayment.this, PaymentSuccess.class);
                            startActivity(intent);

                            progressDialog.dismiss();
                        }
                    }, 3000);
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR) {
                    int i=0;
                    while(i<5) {
                        playBeepSoundAndVibrate();
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                        }
                        i++;
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CepasPayment.this, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, 3000);
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                    startActivity(new Intent(CepasPayment.this, AlipayCheckPrompt.class));
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_SEND_RECV_FAILED) {

                    TransactionDetails.responseMessge ="SEND RECV FAILED";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CepasPayment.this, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, 3000);
                }else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_NOTIFICATION) {
                    //startActivity(new Intent(AlipayActivity.this,AlipayCheckPrompt.class));
                }else
                {
                    startActivity(new Intent(CepasPayment.this, HomePagerActivity.class));
                }
            }else
                progressDialog.dismiss();
        }

        private int processRequest() {
            //save record
            //print receipt
            //message display
            int keytype=0;
            int inDecryptLast=0;

            String stB,stN;
            playBeepSoundAndVibrate();
            if(cardRelated.inGetChallenge(mReadCardOpt) != Constants.ReturnValues.RETURN_OK) {
                //TvStatus.setText("GET Challenge Failed !!!");
                //Toast.makeText(CepasPayment.this,"GET Challenge Failed !!!",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="GET Challenge Failed !!!";

                return Constants.ReturnValues.RETURN_ERROR;
            }
            int inBal = cardRelated.lnReadPurse(mReadCardOpt);
            if(inBal != Constants.ReturnValues.RETURN_FAILED) {
                stB = "BALANCE SGD: " + String.format("%01d", inBal / 100) + "." + String.format("%02d", inBal % 100);
                //TvCurBal.setText(stB);
            }else {
                //TvStatus.setText("Read Purse Failed !!!");
                //Toast.makeText(CepasPayment.this,"Read Purse Failed !!!",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="Read Purse Failed !!!";
                return Constants.ReturnValues.RETURN_ERROR;
            }

            if(samRelated.Ezlink_inGetSessionKey(mReadCardOpt,keytype,inDecryptLast) != Constants.ReturnValues.RETURN_OK)
            {
                Toast.makeText(CepasPayment.this,"GET SESSION KEY FAILED!!!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(CepasPayment.this, HomePagerActivity.class));
            }
            cardRelated.Ezlink_vdDecryptCounterData();

            //Sort PAN
            TransactionDetails.PAN = StringByteUtils.bytesToHexString(TransactionDetails.CAN);
            int inError = afterTranscation.inSortPAN();
            if(inError != Constants.ReturnValues.RETURN_OK) {
                //Toast.makeText(CepasPayment.this,"CARD NOT SUPPORTED",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="CARD NOT SUPPORTED";
                return Constants.ReturnValues.RETURN_ERROR;
            }





            afterTranscation.vdRecordDebitCRC();

            if (TransactionDetails.LastPruseStatus[0] == 0x03) //autoload transaction
            {
                int lnActualAmt = Integer.parseInt(TransactionDetails.trxAmount);
                if (lnActualAmt
                        > ((TransactionDetails.bOrigBal[0] * 256 * 256) + (TransactionDetails.bOrigBal[1] * 256) + TransactionDetails.bOrigBal[2] +
                        TransactionDetails.AutoLoadAmt[0] * 256 * 256 + TransactionDetails.AutoLoadAmt[1] * 256 + TransactionDetails.AutoLoadAmt[2])) {
                    //Toast.makeText(CepasPayment.this,"PAYMENT LIMIT EXCEEDED",Toast.LENGTH_LONG).show();
                    TransactionDetails.responseMessge="PAYMENT LIMIT EXCEEDED";
                    return Constants.ReturnValues.RETURN_ERROR;
                } else if (lnActualAmt > ((TransactionDetails.bOrigBal[0] * 256 * 256) + (TransactionDetails.bOrigBal[1] * 256) + TransactionDetails.bOrigBal[2])) {
                    keytype = 1;
                }
            }

            if(samRelated.SelectEzlSAMEnable(mReadCardOpt) != Constants.ReturnValues.RETURN_OK)
            {
                //Toast.makeText(CepasPayment.this,"SAM INITILIATION FAILED",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="SAM INITILIATION FAILED";
                return Constants.ReturnValues.RETURN_ERROR;
            }
            inDecryptLast=1;

            if(samRelated.Ezlink_inGetSessionKey(mReadCardOpt,keytype,inDecryptLast) != Constants.ReturnValues.RETURN_OK)
            {
                //Toast.makeText(CepasPayment.this,"GET SESSION KEY FAILED!!!",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="GET SESSION KEY FAILED!!!";
                return Constants.ReturnValues.RETURN_ERROR;
            }



            int inRet = cardRelated.DeductAmount(mReadCardOpt,keytype);
            if(inRet == Constants.ReturnValues.RETURN_OK)
            {
               // hostData  = databaseObj.getHostTableData(TransactionDetails.inGHDT);
                //hostData=GreenDaoSupport.getHostTableModelOBJ(activity);
                int inN = inBal - Integer.parseInt(TransactionDetails.trxAmount);
                stN = "NEW BALANCE SGD: " + String.format("%01d", inN / 100) + "." + String.format("%02d", inN % 100);
                //TvNewBal.setText(stN);
            }else
            {
                //Toast.makeText(CepasPayment.this,"PAYMENT FAILED. TRY AGAIN!!!",Toast.LENGTH_LONG).show();
                TransactionDetails.responseMessge="PAYMENT FAILED. TRY AGAIN!!!";
                return Constants.ReturnValues.RETURN_ERROR;
            }
            TransactionDetails.RetrievalRefNumber = TransactionDetails.trxDateTime.substring(4,8) + TransactionDetails.trxDateTime.substring(8,14);

            TransactionDetails.responseMessge = StringByteUtils.bytesToHexString(afterTranscation.inCreateField63());
            //String stTrace = payServices.pGetSystemTrace(activity);
            //TransactionDetails.InvoiceNumber = stTrace;
            afterTranscation.vdSaveRecord();
            final int percent=100;
            CepasPayment.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    progressDialog.setProgress(percent);
                    progressDialog.incrementProgressBy(percent);
                    progressDialog.setTitle("Payment Done");
                    progressDialog.setMessage("REMOVE CARD");

                }
            });
            playBeepSoundAndVibrate();
            afterTranscation.inPrintReceipt();
            afterTranscation.inEzlinkUpload();
            //TaskReqRes.inStoreReversal();

                while(cardRelated.inGetChallenge(mReadCardOpt) == Constants.ReturnValues.RETURN_OK)
                {
                    try {
                        Thread.sleep(1000);
                    }catch(Exception e){}
                }
            return Constants.ReturnValues.RETURN_OK;
        }


        @Override
        protected void onPreExecute() {
            /*progressDialog = ProgressDialog.show(CepasPayment.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();*/
            progressDialog = new ProgressDialog(CepasPayment.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Payment Processing");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }

    }

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
            if (AppUtil.isNetworkAvailable(CepasPayment.this)) {
                new AsyncTaskRunner().execute();


            }else {
                Toast.makeText(CepasPayment.this, "CEPAS:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();

            }
            //mHandler.sendMessage(message);
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
