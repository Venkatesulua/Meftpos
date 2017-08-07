package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.app.EftposApp;
import com.mobileeftpos.android.eftpos.async.AsyncTaskRequestResponse;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class ConfirmRefund extends AppCompatActivity {

    private Button BTConfirmBtn;
    private Button BTCancelBtn;
    private TextView TVRefAmount;
    private TextView TVOriAmount;
    private TextView TVPartnetTransactionID;
    private DaoSession daoSession;
    private Activity activity;

    private AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse();
    public Context loContext;
    public HAfterTransaction afterTranscation = new HAfterTransaction();
    private final String TAG = "my_custom_msg";
    private static final int TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_refund);


        loContext=ConfirmRefund.this;
        activity=ConfirmRefund.this;
        daoSession = ((EftposApp) getApplication()).getDaoSession();
        BTConfirmBtn=(Button)findViewById(R.id.ConfirmBtn);
        BTCancelBtn=(Button)findViewById(R.id.CancelBtn);
        TVRefAmount=(TextView) findViewById(R.id.ConfirmRefAmount);
        TVOriAmount=(TextView)findViewById(R.id.ConfirmOriAmount);
        TVPartnetTransactionID=(TextView)findViewById(R.id.ConfirmPartnerTransID);

        String Temp = TransactionDetails.trxAmount;
        long dubValue = Long.parseLong(Temp);
        Temp = "$"+ String.format("%01d",(dubValue/100)) + "." + String.format("%02d",(dubValue%100));
        TVRefAmount.setText(Temp);

        Temp = TransactionDetails.stOriAmount;
        dubValue = Long.parseLong(Temp);
        Temp = "$"+ String.format("%01d",(dubValue/100)) + "." + String.format("%02d",(dubValue%100));
        TVOriAmount.setText(Temp);

        TVPartnetTransactionID.setText(TransactionDetails.RetrievalRefNumber);

        TextView backBtn=(TextView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BTConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.isNetworkAvailable(ConfirmRefund.this)) {
                    TransactionDetails.trxType = Constants.TransType.ALIPAY_REFUND;
                    TransactionDetails.inOritrxType = Constants.TransType.ALIPAY_REFUND;
                    //ASTask.AsyncTaskCreation(context,activity,daoSession);
                    new AsyncTaskRunner().execute();
                }else {
                    Toast.makeText(ConfirmRefund.this, "REFUND CONNECTION ISSUE",Toast.LENGTH_SHORT).show();
                }
            }
        });

        BTCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmRefund.this, HomePagerActivity.class));
            }
        });


    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String result="";
            int inRet=-1;
            try {

                Log.i(TAG,"ConfirmRefund::Alipay:AsyncTaskRunner");
                inRet = processRequest();
                return Integer.toString(inRet);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,"ConfirmRefund::Alipay:AsyncTaskRunner "+e.getMessage());
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);

            afterTranscation.inAfterTrans();
            //payServices.vdUpdateSystemTrace(daoSession);
            if(result != null) {
                if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                    loContext.startActivity(new Intent(loContext, AlipayCheckPrompt.class));
                }
                if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                    //printReceipt.inPrintReceipt(databaseObj);
                    //Redirect to Success Activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(loContext, PaymentSuccess.class);
                            loContext.startActivity(intent);

                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR ||
                        Integer.parseInt(result) == Constants.ReturnValues.RETURN_REVERSAL_FAILED ||
                        Integer.parseInt(result) == Constants.ReturnValues.RETURN_REVERSAL_FAILED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(loContext, PaymentFailure.class);
                            loContext.startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                    loContext.startActivity(new Intent(loContext, AlipayCheckPrompt.class));
                    progressDialog.dismiss();
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_SEND_RECV_FAILED) {

                    TransactionDetails.responseMessge ="SEND RECV FAILED";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(loContext, PaymentFailure.class);
                            loContext.startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                }else
                {
                    //loContext.startActivity(new Intent(loContext, HomePagerActivity.class));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(loContext, PaymentFailure.class);
                            loContext.startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                }
            }

            //finish();
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"ConfirmRefund::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(ConfirmRefund.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private int processRequest() {

        int inError=0;
        int inPhase=0;
        boolean whLoop=true;
        String result="";

        TransactionDetails.responseMessge="";
        try {
            // SharedPreference sharedpref= new SharedPreference();
            //Password Entry
            while (whLoop) {
                switch (inPhase++) {
                    case 0://Validation; in force settlement;
                        if(TransactionDetails.trxType == Constants.TransType.REFUND) {
                            inError = afterTranscation.inSearchHost(Constants.HostType.ALIPAY_HOST);
                        }

                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;

                        inError = afterTranscation.inCheckSettlementFlag();
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;

                        break;
                    case 1://


                        break;
                    case 2://
                        inError = afterTranscation.inCheckReversal();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "REVERSAL FAILED";
                            return Constants.ReturnValues.RETURN_REVERSAL_FAILED;
                        }else
                            KeyValueDB.removeReversal(loContext);//Clear Reversal

                        inError = afterTranscation.inCheckUpload();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "UPLOAD FAILED";
                            return Constants.ReturnValues.RETURN_UPLOAD_FAILED;
                        }else
                            KeyValueDB.removeUpload(loContext);//Clear Reversal


                        break;
                    case 3://

                        break;


                    case 4:
                        inError = afterTranscation.inHostConnect();
                        if( inError != Constants.ReturnValues.RETURN_OK)
                            return inError;
                        else
                            KeyValueDB.removeReversal(ConfirmRefund.this);
                        break;
                    case 5:
                        afterTranscation.vdSaveRecord();
                        KeyValueDB.removeReversal(loContext);//Clear Reversal
                        if(afterTranscation.inSaveUpload() != Constants.ReturnValues.RETURN_OK)
                            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
                        break;
                    case 6://
                        afterTranscation.inPrintReceipt();
                        break;
                    case 7:
                        //Increment STAN NUMBER
                        break;
                    case 8:
                        //Keep Upload Transcation On
                        inError = afterTranscation.inCheckUpload();
                        if (inError != Constants.ReturnValues.RETURN_OK)
                            break;
                        break;
                    default://Show the receipt in the display and give option to print or email
                        whLoop = false;
                        break;
                }
                if (inError != Constants.ReturnValues.RETURN_OK) {
                    Log.i(TAG, "Aipay:inError:1:");
                    Log.i(TAG, "Aipay:inError:1:");
                    Log.i(TAG, "Aipay:inError:1:");
                    return inError;
                }
            }
        }catch(Exception e){
            return Constants.ReturnValues.RETURN_ERROR;
        }

        return inError;
    }
}
