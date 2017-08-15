package com.mobileeftpos.android.eftpos.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

import butterknife.BindView;

public class AlipayCheckPrompt extends AppCompatActivity {

    AsyncTaskRunner mAsyncTask;

    @BindView(R.id.btnCancel)
    Button btnCancel1;
    @BindView(R.id.btnCheck)
    Button btnCheck1;
    //private byte[] FinalData = new byte[1512];
    //private static DBHelper databaseObj;
    //private PacketCreation isoPacket = new PacketCreation();
    //private RemoteHost remoteHost = new RemoteHost();
    private final String TAG = "my_custom_msg";
    private int inInitiation=0;
    private static final int TIME_OUT = 5000;
    public static Context context;
    //private DaoSession daoSession;
    public HAfterTransaction afterTranscation = new HAfterTransaction();

    private CommsModel comModel = new CommsModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_check_prompt);
        //daoSession = GreenDaoSupport.getInstance(AlipayCheckPrompt.this);
        //loadDataBaseContent();
        //databaseObj = new DBHelper(AlipayCheckPrompt.this);
        //comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);


        context = AlipayCheckPrompt.this;

        btnCancel1 = (Button) findViewById(R.id.btnCancel);
        btnCheck1 = (Button) findViewById(R.id.btnCheck);

        btnCancel1.setOnClickListener(new ClickLIstener1());
        btnCheck1.setOnClickListener(new ClickLIstener1());


    }
    private class ClickLIstener1 implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (view == btnCancel1) {
                inInitiation=0;
                ProcessAsynCall();


            }
            else if (view == btnCheck1) {
                inInitiation=1;
                ProcessAsynCall();
                //enquiry
                //TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj, FinalData, Constants.TransType.ALIPAY_SALE_REPEAT);

            }
        }
    }

    int ProcessAsynCall(){
        if (AppUtil.isNetworkAvailable(AlipayCheckPrompt.this)) {
            mAsyncTask = new AsyncTaskRunner();
            mAsyncTask.execute(new String[]{"52.88.135.124", "10002"});

        }else {
            Toast.makeText(AlipayCheckPrompt.this, "BAR:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            int inError=0;
             try {

                Log.i(TAG, "AlipayCheckPrompt::Alipay:AsyncTaskRunner");
                if (inInitiation == 0) {
                    TransactionDetails.trxType = Constants.TransType.REVERSAL;
                } else{
                    TransactionDetails.trxType = Constants.TransType.ALIPAY_SALE_REPEAT;
                }
                inError = afterTranscation.inHostConnect();
                 if(inError == Constants.ReturnValues.RETURN_OK)
                 {
                     afterTranscation.vdSaveRecord();
                     afterTranscation.inPrintReceipt();
                 }
                return Integer.toString(inError);
                //return Integer.toString(Constants.ReturnValues.RETURN_OK);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "AlipayCheckPrompt::Alipay:AsyncTaskRunner " + e.getMessage());
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);


            afterTranscation.inAfterTrans();
            progressDialog.dismiss();
            afterTranscation.FinalStatusDisplay(AlipayCheckPrompt.this,result);
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "AlipayCheckPrompt::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(AlipayCheckPrompt.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }
    }

    }

