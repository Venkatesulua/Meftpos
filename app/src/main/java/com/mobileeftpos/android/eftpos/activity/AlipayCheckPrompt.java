package com.mobileeftpos.android.eftpos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import butterknife.BindView;

import static com.mobileeftpos.android.eftpos.R.id.btn_submit;

public class AlipayCheckPrompt extends AppCompatActivity {

    AsyncTaskRunner mAsyncTask;

    @BindView(R.id.btnCancel)
    Button btnCancel1;
    @BindView(R.id.btnCheck)
    Button btnCheck1;
    private byte[] FinalData = new byte[1512];
    private static DBHelper databaseObj;
    private PacketCreation isoPacket = new PacketCreation();
    private RemoteHost remoteHost = new RemoteHost();
    private final String TAG = "my_custom_msg";
    private int inInitiation=0;
    public PrintReceipt printReceipt = new PrintReceipt();
    private static final int TIME_OUT = 5000;
    public PayServices payServices = new PayServices();


    private CommsModel comModel = new CommsModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_check_prompt);
        databaseObj = new DBHelper(AlipayCheckPrompt.this);
        comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);


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
            String result = "";
            int inRet = -1;
            try {

                Log.i(TAG, "AlipayCheckPrompt::Alipay:AsyncTaskRunner");
                TransactionDetails.inFinalLength=0;
                FinalData=null;
                FinalData = new byte[1512];
                if (inInitiation == 0) {

                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj, FinalData, Constants.TransType.REVERSAL);
                    result = "";
                    for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"\nSendings:");
                    Log.i(TAG,result);
                    KeyValueDB.setReversal(AlipayCheckPrompt.this,new String(result));

                } else
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj, FinalData, Constants.TransType.ALIPAY_SALE_REPEAT);
                if (TransactionDetails.inFinalLength == 0) {
                    return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                }
                comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);
                String IP_Port = comModel.getCOM_PRIMARY_IP_PORT();
                int indexOffset = IP_Port.indexOf("|");
                String ServerIP = IP_Port.substring(0, indexOffset);
                String Port = IP_Port.substring(indexOffset + 1);
                if (remoteHost.inConnection(ServerIP, Port) != 0) {
                    return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                }
                if ((FinalData = remoteHost.inSendRecvPacket(FinalData, TransactionDetails.inFinalLength)) == null) {
                    return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                }
                inRet = isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength);

                if(inInitiation ==1)
                    KeyValueDB.removeReversal(AlipayCheckPrompt.this);
                if (remoteHost.inDisconnection() != 0) {

                    return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                }
                if (inRet == Constants.ReturnValues.RETURN_OK) {
                    if(inInitiation ==0) {
                        TransactionDetails.responseMessge="REVERSAL SUCCESSFUL";
                        KeyValueDB.removeReversal(AlipayCheckPrompt.this);
                        return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                    }
                    //Save transaction records
                    isoPacket.vdSaveRecord(databaseObj);
                    return Integer.toString(Constants.ReturnValues.RETURN_OK);
                }else if(inRet == Constants.ReturnValues.RETURN_UNKNOWN)
                {//PROMPT CHECK REVERSAL and ENQUIRY
                    return Integer.toString(Constants.ReturnValues.RETURN_UNKNOWN);
                }else{
                    return Integer.toString(Constants.ReturnValues.RETURN_ERROR);
                }
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

            if (result != null && Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                printReceipt.inPrintReceipt(databaseObj);
                //Redirect to Success Activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayCheckPrompt.this, PaymentSuccess.class);
                        startActivity(intent);

                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayCheckPrompt.this, PaymentFailure.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                startActivity(new Intent(AlipayCheckPrompt.this, AlipayCheckPrompt.class));
            }
            payServices.vdUpdateSystemTrace(databaseObj);
            //progressDialog.dismiss();


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

