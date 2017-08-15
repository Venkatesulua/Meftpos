package com.mobileeftpos.android.eftpos.Ezlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.activity.AlipayCheckPrompt;
import com.mobileeftpos.android.eftpos.activity.HomePagerActivity;
import com.mobileeftpos.android.eftpos.activity.PaymentFailure;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class EzlBlacklist extends AppCompatActivity {

    private Context context;
    private final String TAG = "my_custom_msg";
    private ProgressDialog progressDialog;
    double progressValue=0;
    public HAfterTransaction afterTranscation = new HAfterTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezl_blacklist);
        context = EzlBlacklist.this;
        DownloadBlacklist();
    }
    private void DownloadBlacklist() {
        //Parameter if ..else
        TransactionDetails.vdCleanFields();
        if (AppUtil.isNetworkAvailable(EzlBlacklist.this)) {
            TransactionDetails.trxType =  Constants.TransType.BLACKLIST_FIRST_DOWNLOAD;
            new AsyncTaskRunner().execute();


        }else {
            Toast.makeText(context, "BLACKLIST :NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;


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


            //payServices.vdUpdateSystemTrace(databaseObj);
            if (result != null) {
                if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                    //printReceipt.inPrintReceipt(databaseObj);
                    //Redirect to Success Activity
                    progressDialog.dismiss();
                    Toast.makeText(EzlBlacklist.this,"BLACKLIST DOWNLOAD SUCCESSFUL",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, HomePagerActivity.class));
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, PaymentSuccess.class);
                            startActivity(intent);

                            progressDialog.dismiss();
                        }
                    }, 3000);*/
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR) {
                    progressDialog.dismiss();
                    Toast.makeText(EzlBlacklist.this,"BLACKLIST DOWNLOAD FAILED",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, HomePagerActivity.class));

                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, 3000);*/
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                    progressDialog.dismiss();
                    startActivity(new Intent(context, AlipayCheckPrompt.class));
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_SEND_RECV_FAILED) {

                    TransactionDetails.responseMessge = "SEND RECV FAILED";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, 3000);
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_NOTIFICATION) {
                    //startActivity(new Intent(AlipayActivity.this,AlipayCheckPrompt.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EzlBlacklist.this,"BLACKLIST DOWNLOAD FAILED",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, HomePagerActivity.class));
                }
            }
        }

        @Override
        protected void onPreExecute() {
            /*progressDialog = ProgressDialog.show(context,
                    "Blacklist Processing",
                    "Please wait...");
            progressDialog.show();*/
            progressDialog=new ProgressDialog(EzlBlacklist.this);
            progressDialog.setTitle("BLACKLIST Downloading");
            progressDialog.setMessage("Downloading..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

    }

    private int processRequest() {

        int inError=Constants.ReturnValues.RETURN_OK;
        int inRet=0;

        if(TransactionDetails.trxType ==  Constants.TransType.BLACKLIST_FIRST_DOWNLOAD ||
                TransactionDetails.trxType ==  Constants.TransType.BLACKLIST_SUBSEQUENT_DOWNLOAD){
            inError = afterTranscation.inSearchHost(Constants.HostType.EZLINK_PAYMENT_HOST);
            if(inError != Constants.ReturnValues.RETURN_OK)
                return inError;
        }
        TransactionDetails.trxType = Constants.TransType.BLACKLIST_FIRST_DOWNLOAD;
        inError = afterTranscation.inHostConnect();
        if(inError != Constants.ReturnValues.RETURN_OK && inError != Constants.ReturnValues.BLACKLIST_CONTINUE )
        {
            return inError;
        }
        String temp = afterTranscation.getTotalBlacklistCount();
        double b=Integer.parseInt(temp);
        while( inError == Constants.ReturnValues.BLACKLIST_CONTINUE)
        {

            double a=Integer.parseInt(afterTranscation.getField47().substring(4,10));

            double c=(a/b)*100;
            progressValue= progressValue + c/2;
            final int percent= (int)((c/2)%100);//(int)Math.round(c%100);
            EzlBlacklist.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    progressDialog.setProgress(percent);
                    progressDialog.incrementProgressBy(percent);
                }
            });
            TransactionDetails.trxType = Constants.TransType.BLACKLIST_SUBSEQUENT_DOWNLOAD;
            inError = afterTranscation.inHostConnect();
        }


        if(inError == Constants.ReturnValues.RETURN_OK){
            TransactionDetails.responseMessge="BLACKLIST DOWNLOAD SUCCESSFUL";
        }else{
            TransactionDetails.responseMessge="BLACKLIST DOWNLOAD FAILED. \n TRY AGAIN";
        }


        return inError;
    }

}
