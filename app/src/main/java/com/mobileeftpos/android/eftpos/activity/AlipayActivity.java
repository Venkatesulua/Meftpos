package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

import org.jpos.iso.ISOMsg;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlipayActivity extends AppCompatActivity {

    //public GlobalVar globalVar = new GlobalVar();
    //public PaymentProcessing paymentProcess = new PaymentProcessing();
    public TransactionDetails trDetails = new TransactionDetails();
    public PacketCreation isoPacket = new PacketCreation();
    public RemoteHost remoteHost = new RemoteHost();
    public PrintReceipt printReceipt = new PrintReceipt();
    AsyncTaskRunner mAsyncTask;
    TextView backBtn;
    public static String barCodeValue=null;
    public byte[] FinalData = new byte[1512];
    public int inFinalLength = 0;
    //public Socket smtpSocket = null;
    //ISOPackager1 packager = new ISOPackager1();
    //ISOMsg isoMsg = new ISOMsg();
    public static boolean isFromBarcodeScanner;
    private final String TAG = "my_custom_msg";
    private static DBHelper databaseObj;
    public static Context context;
    private static final int TIME_OUT = 5000;

   /* private BarcodeModel barcode = new BarcodeModel();
    private CurrencyModel currModel = new CurrencyModel();
    private HostModel hostData = new HostModel();
    private CommsModel commData = new CommsModel();
    private MerchantModel merchantData = new MerchantModel();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);

        databaseObj = new DBHelper(AlipayActivity.this);
        context = AlipayActivity.this;



        barCodeValue=null;
        isFromBarcodeScanner=false;
        isFromBarcodeScanner=true;

        Log.i(TAG,"Alipay_onCreate_1");
        //startActivity(new Intent(AlipayActivity.this,FullScannerActivity.class));
        Intent i = new Intent(AlipayActivity.this, FullScannerActivity.class);
        i.putExtra("FromAlipayActivity", true);
        startActivityForResult(i, 111);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==111 && intent!=null){
            String contents = intent.getStringExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY);
            processBarcode(contents);

        } else {
            Log.i(TAG,"onActivityResult_3");
            Toast.makeText(getApplicationContext(), "QRCODE READ FAILED", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

   /* @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"Alipay:onResume");
        if(isFromBarcodeScanner){
            Log.i(TAG,"Alipay:barCodeValue::"+barCodeValue);
            Log.i(TAG,"Alipay:barCodeValue.length"+barCodeValue.length());
            if(barCodeValue!=null && barCodeValue.length()>0) {
                Log.i(TAG,"Alipay:CallprocessBarcode");
                processBarcode(barCodeValue);
            }

        }
    }*/

    private void processBarcode(String contents) {
        Log.i(TAG,"Alipay:processBarcode");
        mAsyncTask = new AsyncTaskRunner();
        mAsyncTask.execute(new String[] { "52.88.135.124", "10002" });
        //mAsyncTask.execute(new String[] { "192.168.43.117", "9999" });
        trDetails.setPAN(contents);

    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String result="";
            int inRet=-1;
            try {

                Log.i(TAG,"Alipay:AsyncTaskRunner");
                inRet = processRequest(params[0],params[1]);
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
            Log.i(TAG,"Alipay:onPostExecute");
            if(result!=null && result.equals("0"))
            {
                Log.i(TAG, "Aipay:onPostExecute:SUCCESS");
                Log.i(TAG, "Aipay:onPostExecute:SUCCESS");
                Log.i(TAG, "Aipay:onPostExecute:SUCCESS");
                printReceipt.inPrintReceipt(databaseObj);
                //Redirect to Success Activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayActivity.this, PaymentSuccess.class);
                        startActivity(intent);

                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }else
            {
                Log.i(TAG, "Aipay:onPostExecute:ERROR");
                Log.i(TAG, "Aipay:onPostExecute:ERROR");
                Log.i(TAG, "Aipay:onPostExecute:ERROR");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayActivity.this, PaymentFailure.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }
            //progressDialog.dismiss();


        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(AlipayActivity.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private int processRequest(String ServerIP,String Port) {

        Log.i(TAG,"processRequest_1");

        int inError=0;
        int inPhase=0;
        boolean whLoop=true;
        String result="";
        //Password Entry
        while(whLoop) {
            switch(inPhase++)
            {
                case 0://Validation; in force settlement;
                    Log.i(TAG,"processRequest_2");
                    if (trDetails.inSortPAN(databaseObj) == 1) {
                        Log.i(TAG,"ERROR in SORTPAN");
                        inError = 1;
                    }
                    break;
                case 1://

                    Log.i(TAG, "Aipay:inCreatePacket:");
                    inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.ALIPAY_SALE);
                    if(inFinalLength == 0)
                        inError = 1;
                    break;
                case 2://
                    Log.i(TAG, "Aipay:inConnection:");
                    if (remoteHost.inConnection(ServerIP, Port) != 0) {
                        inError = 1;
                        break;
                    }
                    break;
                case 3://
                    Log.i(TAG, "Aipay:inSendRecvPacket:");
                    if ((FinalData = remoteHost.inSendRecvPacket(FinalData,inFinalLength)) ==null) {
                        inError = 1;
                        break;
                    }

                    result = "";
                    for (int k = 0; k < inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"\nAlipay_inSendRecvPacket_Received:");
                    Log.i(TAG,result);

                    break;
                case 4://

                    result = "";
                    for (int k = 0; k < inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"\nAlipay_inProcessPacket_Received:");
                    Log.i(TAG,result);

                    Log.i(TAG, "Aipay:inProcessPacket:");
                    if (isoPacket.inProcessPacket(FinalData,inFinalLength) != 0) {
                        inError = 1;
                        //redirect to error
                        break;
                    }

                    Log.i(TAG, "Aipay:inDisconnection:");
                    if (remoteHost.inDisconnection() != 0) {
                    inError = 1;
                    break;
                    }
                    break;
                case 5:
                    Log.i(TAG, "\nSave Record:");
                    //save Record
                    //vdSaveRecord();
                    break;
                case 6://
                    //Print receipt
                    Log.i(TAG, "\nPrinting Receipt");

                    break;
                case 7://Show the receipt in the display and give option to print or email
                    //startActivity(new Intent(AlipayActivity.this, HomeActivity.class));
                    whLoop=false;
                    break;
                //break;
            }
            if(inError == 1)
            {
                Log.i(TAG, "Aipay:inError:1:");
                Log.i(TAG, "Aipay:inError:1:");
                Log.i(TAG, "Aipay:inError:1:");

                //Redirect to error Activity

                break;
            }
        }

        if(inError == 0)
        {
            Log.i(TAG, "Aipay:SUCCESS");
            Log.i(TAG, "Aipay:SUCCESS");
            Log.i(TAG, "Aipay:SUCCESS");
            //Redirect to Success Activity

        }
        return inError;
    }


}
