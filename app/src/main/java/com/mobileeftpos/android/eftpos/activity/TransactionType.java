package com.mobileeftpos.android.eftpos.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

import org.jpos.iso.ISOMsg;

import java.net.Socket;

public class TransactionType extends AppCompatActivity {

    public GlobalVar globalVar = new GlobalVar();
    //public PaymentProcessing paymentProcess = new PaymentProcessing();
    public TransactionDetails trDetails = new TransactionDetails();
    AsyncTaskRunner mAsyncTask;
    TextView backBtn;
    public static String barCodeValue=null;
    public byte[] FinalData = new byte[1512];
    public int inFinalLength = 0;
    public Socket smtpSocket = null;
    ISOPackager1 packager = new ISOPackager1();
    ISOMsg isoMsg = new ISOMsg();
    public static boolean isFromBarcodeScanner;
    private final String TAG = "my_custom_msg";
     public static Context context;

    private BarcodeModel barcode = new BarcodeModel();
    private CurrencyModel currModel = new CurrencyModel();
    private HostModel hostData = new HostModel();
    private CommsModel commData = new CommsModel();
    private MerchantModel merchantData = new MerchantModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_type);
         context = TransactionType.this;
        barCodeValue=null;
        isFromBarcodeScanner=false;
        isFromBarcodeScanner=true;
        Log.i(TAG,"TransType::Trans_Type_onCreate_1");
        startActivity(new Intent(TransactionType.this,FullScannerActivity.class));
        //addListenerOnButton();

    }

    public void addListenerOnButton() {

        Button btnlCardPayment = (Button) findViewById(R.id.btnCardPayment);
        Button btnAlipayPayment = (Button) findViewById(R.id.btnAlipay);
        Button btnCepasPayment = (Button) findViewById(R.id.btnCepas);
        backBtn=(TextView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnlCardPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert or Swipe or Contactless transaction prompt and waiting for the card
            }
        });
        btnAlipayPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Log.i(TAG,"ALIPAY CLICK");
                globalVar.setGTransactionType(MenuConstants.TransType.ALIPAY_SALE);
                Log.i(TAG,"ALIPAY CLICK_1");
                //startActivity(new Intent(TransactionType.this,ScannerActivity.class));
                Intent intentScan = new Intent(TransactionType.this, CaptureActivity.class);
                Log.i(TAG,"ALIPAY CLICK_2");
                intentScan.setAction(MenuConstants.QRCODE.BARCODE_INTENT_ACTION);
                Log.i(TAG,"ALIPAY CLICK_3");
                intentScan.putExtra(MenuConstants.QRCODE.BARCODE_DISABLE_HISTORY, false);
                Log.i(TAG,"ALIPAY CLICK_4");
                startActivityForResult(intentScan, MenuConstants.QRCODE.BARCODE_RESULT_CODE);
                Log.i(TAG,"ALIPAY CLICK_5");*/

                isFromBarcodeScanner=true;
                startActivity(new Intent(TransactionType.this,FullScannerActivity.class));
            }
        });

        btnCepasPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Read Contactless or NFC
            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

<<<<<<< HEAD
        Log.i(TAG,"TransType::onActivityResult");
        if (requestCode == Constants.QRCODE.BARCODE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG,"TransType::onActivityResult_2");
            String contents = intent.getStringExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY);
=======
        Log.i(TAG,"onActivityResult");
        if (requestCode == MenuConstants.QRCODE.BARCODE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG,"onActivityResult_2");
            String contents = intent.getStringExtra(MenuConstants.QRCODE.BARCODE_INTENT_RESULT_KEY);
>>>>>>> a7f5da5d14fb4f212b68ca15386c43018395b51c
            processBarcode(contents);
        } else {
            Log.i(TAG,"TransType::onActivityResult_3");
            Toast.makeText(getApplicationContext(), "QRCODE READ FAILED", Toast.LENGTH_SHORT).show();
            finish();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"TransType::Trans_Type:onResume");
        if(isFromBarcodeScanner){
            if(barCodeValue!=null && barCodeValue.length()>0) {
                processBarcode(barCodeValue);
            }

        }
    }

    private void processBarcode(String contents) {
        Log.i(TAG,"TransType::Trans_Type:processBarcode");
        mAsyncTask = new AsyncTaskRunner();
        //mAsyncTask.execute(new String[] { "52.88.135.124", "10002" });
        mAsyncTask.execute(new String[] { "192.168.43.117", "9999" });
        //trDetails.setPAN(contents);
        TransactionDetails.PAN = contents;

    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                Log.i(TAG,"TransType::Trans_Type:AsyncTaskRunner");
                //processRequest(params[0],params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            Log.i(TAG,"TransType::Trans_Type:onPostExecute");
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"TransType::Trans_Type:onPreExecute");
            progressDialog = ProgressDialog.show(TransactionType.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }





}
