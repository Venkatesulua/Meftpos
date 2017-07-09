package com.mobileeftpos.android.eftpos.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;

import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.async.AsyncTaskRequestResponse;
import com.mobileeftpos.android.eftpos.async.WebServiceCall;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.AlipayResponceModel;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;
import com.mobileeftpos.android.eftpos.scan.SunmiScanner;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.JSONUtil;

import org.jpos.iso.ISOMsg;
import org.jpos.security.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlipayActivity extends AppCompatActivity {


    private AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse();
    public static Context context;
    public static String barCodeValue=null;
    public static boolean isFromBarcodeScanner;
    private final String TAG = "my_custom_msg";
    public static int ALIPAY_CONSTANT=111;
    public static String ALIPAY_SCANRESULT="scanResult";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public TransactionDetails trDetails = new TransactionDetails();
    public PacketCreation isoPacket = new PacketCreation();
    public RemoteHost remoteHost = new RemoteHost();
    public PrintReceipt printReceipt = new PrintReceipt();
    public PayServices payServices = new PayServices();
    AsyncTaskRunner mAsyncTask;

    public byte[] FinalData = new byte[1512];
    //public int TransactionDetails.inFinalLength = 0;


    private static DBHelper databaseObj;

    private static final int TIME_OUT = 5000;
    private static String BASE_URL="https://devapi.prayapay.com/v2?";
    private String requestTypeValue,brandValue,storeIdValue,deviceIdValue,
            reqIdValue,reqDTValue,custCodeValue,amountValue,currValue,signTypeValue,
            signValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        //databaseObj = new DBHelper(AlipayActivity.this);
        context = AlipayActivity.this;

        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        barCodeValue=null;
        isFromBarcodeScanner=false;
        isFromBarcodeScanner=true;
        //String ReversalString = KeyValueDB.getReversal(context);

        Log.i(TransactionDetails.TAG,"AlipayActivity::Alipay_onCreate_1");
        Intent i = new Intent(AlipayActivity.this, SunmiScanner.class);
        i.putExtra("FromAlipayActivity", true);
        startActivityForResult(i, ALIPAY_CONSTANT);

        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();*/

        /*Intent intentScan = new Intent(AlipayActivity.this, CaptureActivity.class);
        intentScan.setAction(Constants.QRCODE.BARCODE_INTENT_ACTION);
        intentScan.putExtra(Constants.QRCODE.BARCODE_DISABLE_HISTORY, false);
         //  intentScan.putExtra(Intents.Scan.FORMATS, String.format("%s,%s"
        //  , BarcodeFormat.CODE_128.toString(), BarcodeFormat.ITF.toString()));
        startActivityForResult(intentScan, Constants.QRCODE.BARCODE_RESULT_CODE);*/



    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "QRCODE READ FAILED", Toast.LENGTH_LONG).show();
            } else {

                processBarcode(result.getContents());
                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/


    private  boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==ALIPAY_CONSTANT && intent!=null){
            String contents = intent.getStringExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY);
            processBarcode(contents);

        } else {
            Log.i(TAG,"AlipayActivity::onActivityResult_3");
            Toast.makeText(getApplicationContext(), "QRCODE READ FAILED", Toast.LENGTH_SHORT).show();
            finish();
        }


    }


   /* @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"AlipayActivity::Alipay:onResume");
        if(isFromBarcodeScanner){
            Log.i(TAG,"AlipayActivity::Alipay:barCodeValue::"+barCodeValue);
            Log.i(TAG,"AlipayActivity::Alipay:barCodeValue.length"+barCodeValue.length());
            if(barCodeValue!=null && barCodeValue.length()>0) {
                Log.i(TAG,"AlipayActivity::Alipay:CallprocessBarcode");
                processBarcode(barCodeValue);
            }

        }
    }*/

    private void processBarcode(String contents) {
        //Parameter if ..else
        Log.i(TAG,"AlipayActivity::Alipay:processBarcode");
        TransactionDetails.inGTrxMode=Constants.TransMode.BARCODE;
        if (AppUtil.isNetworkAvailable(AlipayActivity.this)) {
            TransactionDetails.PAN = contents;
            TransactionDetails.trxType = Constants.TransType.ALIPAY_SALE;
            TransactionDetails.inOritrxType = Constants.TransType.ALIPAY_SALE;
            ASTask.AsyncTaskCreation(context);


        }else {
            Toast.makeText(AlipayActivity.this, "BAR:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String result="";
            int inRet=-1;
            try {

                Log.i(TAG,"AlipayActivity::Alipay:AsyncTaskRunner");
                inRet = processRequest(params[0],params[1]);
                return Integer.toString(inRet);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,"AlipayActivity::Alipay:AsyncTaskRunner "+e.getMessage());
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);

            if(result!=null && Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK)
            {
                printReceipt.inPrintReceipt(databaseObj,context);
                //Redirect to Success Activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayActivity.this, PaymentSuccess.class);
                        startActivity(intent);

                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR)
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AlipayActivity.this, PaymentFailure.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }
            else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN)
            {
                startActivity(new Intent(AlipayActivity.this,AlipayCheckPrompt.class));
            }
            payServices.vdUpdateSystemTrace(databaseObj);
            //progressDialog.dismiss();


        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"AlipayActivity::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(AlipayActivity.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private int processRequest(String ServerIP,String Port) {

        int inError=0;
        int inPhase=0;
        boolean whLoop=true;
        String result="";
        CommsModel comModel = new CommsModel();
        BatchModel batchModel = new BatchModel();
        Gson gson = new Gson();
        // SharedPreference sharedpref= new SharedPreference();
        //Password Entry
        while(whLoop) {
            switch(inPhase++)
            {
                case 0://Validation; in force settlement;
                    if (trDetails.inSortPAN(databaseObj) == 1) {
                        Log.i(TAG,"AlipayActivity::ERROR in SORTPAN");
                        inError = 1;
                    }
                    break;
                case 1://

                    comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);
                    String IP_Port = comModel.getCOM_PRIMARY_IP_PORT();
                    int indexOffset = IP_Port.indexOf("|");
                    ServerIP = IP_Port.substring(0,indexOffset);
                    Port = IP_Port.substring(indexOffset+1);
                    Log.i(TAG, "Aipay:ServerIP ::: "+ServerIP);
                    Log.i(TAG, "Aipay:Server PORT ::: "+Port);

                    String stTrace = payServices.pGetSystemTrace(databaseObj);
                    TransactionDetails.InvoiceNumber = stTrace;

                    Log.i(TAG, "Aipay:inCreatePacket:stTrace::"+stTrace);

                    if (remoteHost.inConnection(ServerIP, Port) != 0) {
                        Log.i(TAG,"AlipayActivity::Connection Failed");
                        inError = 1;
                        break;
                    }

                    //KeyValueDB.removeUpload(context);
                    //KeyValueDB.removeReversal(context);
                    String ReversalData = KeyValueDB.getReversal(context);
                    if(!ReversalData.isEmpty())
                    {
                        //FinalData = UploadData.getBytes();
                        byte[] FinalData = new BigInteger(ReversalData, 16).toByteArray();
                        TransactionDetails.inFinalLength = FinalData[0] *256;
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (FinalData[1]);
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
                        if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                            Log.i(TAG,"AlipayActivity::REversal Send Failed");
                            inError = 1;
                            break;
                        }

                        if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                            Log.i(TAG,"AlipayActivity::REversal Receive Failed");
                            inError = 1;
                            //redirect to error
                            break;
                        }
                        KeyValueDB.removeReversal(context);

                        if (remoteHost.inDisconnection() != 0) {
                            inError = 1;
                            break;
                        }

                        if (remoteHost.inConnection(ServerIP, Port) != 0) {
                            Log.i(TAG,"AlipayActivity::Connection Failed");
                            inError = 1;
                            break;
                        }
                    }
                    String UploadData = KeyValueDB.getUpload(context);
                    if(!UploadData.isEmpty())
                    {
                        //FinalData = UploadData.getBytes();
                        byte[] FinalData = new BigInteger(UploadData, 16).toByteArray();
                        TransactionDetails.inFinalLength = FinalData[0] *256;
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (FinalData[1]);
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
                        if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                            Log.i(TAG,"AlipayActivity::Upload Send Failed");
                            inError = 1;
                            break;
                        }
                        if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                            inError = 1;
                            Log.i(TAG,"AlipayActivity::Upload Receive Failed");
                            //redirect to error
                            break;
                        }
                        KeyValueDB.removeUpload(context);

                        if (remoteHost.inDisconnection() != 0) {
                            inError = 1;
                            break;
                        }

                        if (remoteHost.inConnection(ServerIP, Port) != 0) {
                            Log.i(TAG,"AlipayActivity::Connection Failed");
                            inError = 1;
                            break;
                        }
                    }

                    /*String BatchReversalData = KeyValueDB.getReversal(context);
                    if(!BatchReversalData.isEmpty() )
                    {
                        batchModel = gson.fromJson(BatchReversalData,BatchModel.class);
                        //Take backup of the parameters and then restore it later
                        String StHDT = batchModel.getHDT_INDEX();
                        if(TransactionDetails.inGHDT==Integer.parseInt(StHDT)) {
                            //TransactionDetails trBackupTrans = new TransactionDetails();

                            int inOritrxType =TransactionDetails.inOritrxType;
                            int inGTrxMode=TransactionDetails.inGTrxMode;
                            String processingcode=TransactionDetails.processingcode;
                            String trxAmount=TransactionDetails.trxAmount;
                            String tipAmount=TransactionDetails.tipAmount;
                            String trxDateTime=TransactionDetails.trxDateTime;
                            String messagetype=TransactionDetails.messagetype;

                            String ExpDate=TransactionDetails.ExpDate;
                            String RetrievalRefNumber=TransactionDetails.RetrievalRefNumber;
                            String chApprovalCode=TransactionDetails.chApprovalCode;
                            String ResponseCode=TransactionDetails.ResponseCode;
                            String PAN=TransactionDetails.PAN;
                            String PersonName=TransactionDetails.PersonName;
                            String temptrxAmount=TransactionDetails.trxAmount;
                            String responseMessge=TransactionDetails.responseMessge;
                            String POSEntryMode=TransactionDetails.POSEntryMode;
                            String NII=TransactionDetails.NII;
                            String POS_COND_CODE = TransactionDetails.POS_COND_CODE;

                            TransactionDetails.inOritrxType = Integer.parseInt(batchModel.getTRANS_TYPE());
                            TransactionDetails.inGTrxMode = Integer.parseInt(batchModel.getTRANS_MODE());
                            TransactionDetails.processingcode = batchModel.getPROC_CODE();
                            TransactionDetails.trxAmount = batchModel.getAMOUNT();
                            TransactionDetails.tipAmount = batchModel.getTIP_AMOUNT();
                            TransactionDetails.trxDateTime = batchModel.getYEAR();
                            TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModel.getDATE();
                            TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModel.getTIME();
                            TransactionDetails.messagetype = batchModel.getORG_MESS_ID();
//        batchModel.getSYS_TRACE_NUM(payServices.pGetSystemTrace(databaseObj));
                            TransactionDetails.ExpDate = batchModel.getDATE_EXP();
                            TransactionDetails.RetrievalRefNumber = batchModel.getRETR_REF_NUM();
                            TransactionDetails.chApprovalCode = batchModel.getAUTH_ID_RESP();
                            TransactionDetails.ResponseCode = batchModel.getRESP_CODE();
                            TransactionDetails.PAN = batchModel.getACCT_NUMBER();
                            TransactionDetails.PersonName = batchModel.getPERSON_NAME();
                            TransactionDetails.trxAmount = batchModel.getORIGINAL_AMOUNT();
                            TransactionDetails.responseMessge = batchModel.getADDITIONAL_DATA();
                            //batchModel.getPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
                            TransactionDetails.PAN = batchModel.getPRIMARY_ACC_NUM();
                            TransactionDetails.POSEntryMode = batchModel.getPOS_ENT_MODE();
                            TransactionDetails.NII = batchModel.getNII();
                            TransactionDetails.POS_COND_CODE = batchModel.getPOS_COND_CODE();

                            TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.REVERSAL);
                            if(TransactionDetails.inFinalLength == 0) {
                                inError = 1;
                                break;
                            }
                            Log.i(TAG, "Aipay:inSendRecvPacket:");
                            if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                                inError = 1;
                                break;
                            }

                            result = "";
                            for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                                result = result + String.format("%02x", FinalData[k]);
                            }
                            Log.i(TAG,"AlipayActivity::\nAlipay_inSendRecvPacket_Received:");
                            Log.i(TAG,result);
                            Log.i(TAG, "Aipay:inProcessPacket:");
                            if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                                inError = 1;
                                //redirect to error
                                break;
                            }

                            TransactionDetails.inOritrxType=inOritrxType;
                            TransactionDetails.inGTrxMode=inGTrxMode;
                            TransactionDetails.processingcode=processingcode;
                            TransactionDetails.trxAmount=trxAmount;
                            TransactionDetails.tipAmount=tipAmount;
                            TransactionDetails.trxDateTime=trxDateTime;
                            TransactionDetails.messagetype=messagetype;

                            TransactionDetails.ExpDate=ExpDate;
                            TransactionDetails.RetrievalRefNumber=RetrievalRefNumber;
                            TransactionDetails.chApprovalCode=chApprovalCode;
                            TransactionDetails.ResponseCode=ResponseCode;
                            TransactionDetails.PAN=PAN;
                            TransactionDetails.PersonName=PersonName;
                            TransactionDetails.trxAmount=temptrxAmount;
                            TransactionDetails.responseMessge=responseMessge;
                            TransactionDetails.POSEntryMode=POSEntryMode;
                            TransactionDetails.NII=NII;
                            TransactionDetails.POS_COND_CODE=POS_COND_CODE;

                        }
                        
                    }*/

                    break;
                case 2://

                    //int inTempTrxType = TransactionDetails.trxType;
                    //TransactionDetails.trxType=Constants.TransType.REVERSAL;
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.REVERSAL);
                    if(TransactionDetails.inFinalLength == 0) {
                        inError = 1;
                        break;
                    }
                    result = "";
                    for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"\nSet Reversal:");
                    Log.i(TAG,result);
                    KeyValueDB.setReversal(AlipayActivity.this,new String(result));

                    //TransactionDetails.trxType = inTempTrxType;
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.ALIPAY_SALE);
                    if(TransactionDetails.inFinalLength == 0) {
                        inError = 1;
                        break;
                    }
                    break;
                case 3://
                    Log.i(TAG, "Aipay:inSendRecvPacket:");
                    if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                        inError = 1;
                        break;
                    }

                    break;
                case 4:
                    KeyValueDB.removeReversal(AlipayActivity.this);//Clear Reversal
                    int inRet = isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength);

                    if (remoteHost.inDisconnection() != 0) {
                        inError = 1;
                        break;
                    }

                    if (inRet == Constants.ReturnValues.RETURN_OK) {
                        //redirect to error
                        break;
                    }else if(inRet == Constants.ReturnValues.RETURN_UNKNOWN)
                    {//PROMPT CHECK REVERSAL and ENQUIRY
                        return Constants.ReturnValues.RETURN_UNKNOWN;
                    }else{
                        inError = 1;
                        //redirect to error
                        break;
                    }
                case 5:
                    Log.i(TAG, "\nSave Record:");
                    //save Record

                    isoPacket.vdSaveRecord(databaseObj);
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
                return Constants.ReturnValues.RETURN_ERROR;
                //break;
            }
        }

        if(inError == 0)
        {
            Log.i(TAG, "Aipay:SUCCESS");
            Log.i(TAG, "Aipay:SUCCESS");
            Log.i(TAG, "Aipay:SUCCESS");
            //Redirect to Success Activity
            return Constants.ReturnValues.RETURN_OK;

        }
        return Constants.ReturnValues.RETURN_ERROR;
    }



    private void processWebServiceCall() {

        WebServiceCall serv = new WebServiceCall(AlipayActivity.this) {
            @Override
            public void onResp(JSONObject response) {
                Log.d("Response:::", response.toString());
                try {
                    String status = response.getString("respcode");
                    Log.i("status : ", status);
                    if (status.equalsIgnoreCase("0")) {

                        try {
                            AlipayResponceModel modelObject = JSONUtil.parseTransactionResponce(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

                Toast.makeText(AlipayActivity.this, error.toString(),Toast.LENGTH_SHORT).show();
            }
        };

        if (AppUtil.isNetworkAvailable(AlipayActivity.this)) {
            serv.makeJsonGetRequestObject(buildJsonreqObject());
        }   else {
            serv.hideProgressDialog();
            Toast.makeText(AlipayActivity.this, "NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

    }


    private String buildJsonreqObject(){

        // Need to Assign Values here

        String urlString = BASE_URL+
                "reqtype="+requestTypeValue+
                "&brand="+brandValue+
                "&storeid="+storeIdValue+
                "&deviceid="+deviceIdValue+
                "&reqid="+reqIdValue+
                "&reqdt="+reqDTValue+
                "&custcode="+custCodeValue+
                "&amt="+amountValue+
                "&curr="+currValue+
                "&signtype="+signTypeValue+
                "&sign="+signValue;

        return urlString;
    }



}
