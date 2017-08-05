package com.mobileeftpos.android.eftpos.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.AGetCard;
import com.mobileeftpos.android.eftpos.TransactionFlow.BValidateCard;
import com.mobileeftpos.android.eftpos.TransactionFlow.CCheckReversal;
import com.mobileeftpos.android.eftpos.TransactionFlow.DCheckUpload;
import com.mobileeftpos.android.eftpos.TransactionFlow.EHostConnectivity;
import com.mobileeftpos.android.eftpos.TransactionFlow.FSaveRecord;
import com.mobileeftpos.android.eftpos.TransactionFlow.GPrintReceipt;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.async.WebServiceCall;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.CommsModelDao;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HTTModel;
import com.mobileeftpos.android.eftpos.db.HTTModelDao;
import com.mobileeftpos.android.eftpos.model.AlipayResponceModel;
import com.mobileeftpos.android.eftpos.scan.SunmiScanner;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class AlipayActivity extends AppCompatActivity {

    public AGetCard getCard;
    public BValidateCard validateCard;
    public CCheckReversal checkReversal;
    public DCheckUpload checkUpload;
    public EHostConnectivity hostConnectivity;
    public FSaveRecord saveRecord;
    public GPrintReceipt gprintReceipt;
    public HAfterTransaction afterTranscation;

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
    public static  Context loContext;
    private String ServerIP="";
    private String Port="";
    private DaoSession daoSession;
    private static final int TIME_OUT = 1000;
    private static String BASE_URL="https://devapi.prayapay.com/v2?";
    private String requestTypeValue,brandValue,storeIdValue,deviceIdValue,
            reqIdValue,reqDTValue,custCodeValue,amountValue,currValue,signTypeValue,
            signValue;
    private CommsModelDao commsModelDao;
    private HTTModelDao httModelDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        daoSession = GreenDaoSupport.getInstance(AlipayActivity.this);
        loadDatabaseContent();
        context = AlipayActivity.this;
        loContext=AlipayActivity.this;
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
            //AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse(context);
            //ASTask.AsyncTaskCreation(context);
            new AsyncTaskRunner().execute("0.0.0.0","0");
            //observable
            //1.

           // processRequest(null,null);
            /*Observable.just(Constants.SortPan, Constants.CheckReversal, Constants.CheckUpload,
                            Constants.HostConnectivity,Constants.SaveRecord,Constants.PrintReceipt,Constants.ClearGlobals)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<Integer>() {

                        @Override
                        public void onCompleted() {
                            //logger.info("connection coplete");
                            Log.i("tcp","COMPLETED");
                        }

                        @Override
                        public void onError(Throwable e) {
                            //logger.error(e.getMessage(), e);
                            //close();
                            Log.i("tcp","ON ERROR"+ e.getMessage());
                        }

                        @Override
                        public void onNext(Integer str) {
                        try{
                            if(str == Constants.inGetCardInfo){
                                getCard = new AGetCard(AlipayActivity.this);
                                getCard.readCard();
                            }
                            else if(str == Constants.SortPan){
                                validateCard.inSortPAN();
                            }else if(str == Constants.CheckReversal){
                                checkReversal.inCheckReversal();
                            }else if(str == Constants.CheckUpload){
                                checkUpload.inCheckUpload();
                            }else if(str == Constants.HostConnectivity){
                                hostConnectivity.inHostConnect();
                            }else if(str == Constants.SaveRecord){
                                saveRecord.vdSaveRecord();
                            }else if(str == Constants.PrintReceipt){
                                gprintReceipt.inPrintReceipt();
                            }else if(str == Constants.ClearGlobals){

                            }

                            }catch(Throwable  e)
                            {

                            }

                        }
                    });*/

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
            payServices.vdUpdateSystemTrace(daoSession);
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
            Log.i(TAG,"AlipayActivity::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(AlipayActivity.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private int processRequest(String ServerIP1,String Port1) {

        int inError=0;
        int inPhase=0;
        boolean whLoop=true;
        String result="";
        CommsModel comModel = new CommsModel();
        BatchModel batchModel = new BatchModel();
        Gson gson = new Gson();

        TransactionDetails.responseMessge="";
        try {
            // SharedPreference sharedpref= new SharedPreference();
            //Password Entry
            while (whLoop) {
                switch (inPhase++) {
                    case 0://Validation; in force settlement;
                        //getCard = new AGetCard(AlipayActivity.this);
                        if (TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE || TransactionDetails.trxType == Constants.TransType.VOID) {
                            getCard = new AGetCard(AlipayActivity.this);
                            getCard.readCard();
                            validateCard.inSortPAN();
                            //inError = trDetails.inSortPAN(daoSession);
                            //inError = validateCard.inSortPAN();
                            if (inError != Constants.ReturnValues.RETURN_OK) {
                                TransactionDetails.responseMessge = "TRANSCATION NOT SUPPORTED or \n CARD NOT READ PROPERLY";
                                return Constants.ReturnValues.RETURN_ERROR;
                                //break;
                            }
                        }/*else
                    {
                        if(TransactionDetails.trxType == Constants.TransType.REFUND) {
                            inError = inSearchHost(Constants.HostType.ALIPAY_HOST);
                        }else if(TransactionDetails.trxType ==  Constants.TransType.BLACKLIST_FIRST_DOWNLOAD ||
                                TransactionDetails.trxType ==  Constants.TransType.BLACKLIST_SUBSEQUENT_DOWNLOAD)
                        {
                            inError = inSearchHost(Constants.HostType.EZLINK_PAYMENT_HOST);
                        }
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;
                    }
                    inError = inCheckSettlementFlag();
                    if(inError != Constants.ReturnValues.RETURN_OK)
                        break;*/

                        break;
                    case 1://


                        /*TransactionDetails.responseMessge = "Case 1";
                        CommsModelDao commsModelDao =daoSession.getCommsModelDao();

                        comModel =commsModelDao.loadAll().get(0);// databaseObj.getCommsData(TransactionDetails.inGCOM);
                        String IP_Port = comModel.getCOM_PRIMARY_IP_PORT();
                        int indexOffset = IP_Port.indexOf("|");
                        ServerIP = IP_Port.substring(0, indexOffset);
                        Port = IP_Port.substring(indexOffset + 1);
                        Log.i(TAG, "Aipay:ServerIP ::: " + ServerIP);
                        Log.i(TAG, "Aipay:Server PORT ::: " + Port);
                        String stTrace = payServices.pGetSystemTrace(AlipayActivity.this);
                        TransactionDetails.InvoiceNumber = stTrace;
                        Log.i(TAG, "Aipay:inCreatePacket:stTrace::" + stTrace);*/

                        break;
                    case 2://
                        //TransactionDetails.trxType = inTempTrxType;
                        //inError = inCheckReversal();
                        inError = checkReversal.inCheckReversal();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "REVERSAL FAILED";
                            return Constants.ReturnValues.RETURN_REVERSAL_FAILED;
                        }

                        //inError = inCheckUpload();
                        inError = checkUpload.inCheckUpload();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "UPLOAD FAILED";
                            return Constants.ReturnValues.RETURN_UPLOAD_FAILED;
                        }


                        break;
                    case 3://


                        /*TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.REVERSAL);
                        if(TransactionDetails.inFinalLength == 0) {
                            inError = 1;
                            break;
                        }

                        if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;

                        result = "";
                        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                            result = result + String.format("%02x", FinalData[k]);
                        }
                        Log.i(TAG,"\nSet Reversal:");
                        Log.i(TAG,result);
                        KeyValueDB.setReversal(loContext,new String(result));*/

                       /* TransactionDetails.inFinalLength = isoPacket.inCreatePacket(FinalData, Constants.TransType
                                .ALIPAY_SALE,AlipayActivity.this);
                        if (TransactionDetails.inFinalLength == 0) {
                            inError = 1;
                            break;
                        }*/
                        break;


                    case 4:
                        if(hostConnectivity.inHostConnect() != Constants.ReturnValues.RETURN_OK)
                        //if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;

                        /*Log.i(TAG, "Aipay:inSendRecvPacket:");
                        HTTModelDao httModelDao=daoSession.getHTTModelDao();


                        HTTModel connectionTimeout =httModelDao.loadAll().get(0);
                        //databaseObj.getHostTransmissionModelData(0);
                        TransactionDetails.ConnectionTimeout = connectionTimeout.getCONNECTION_TIMEOUT();
                        if ((FinalData = remoteHost.inSendRecvPacket(FinalData, TransactionDetails.inFinalLength)) == null) {
                            //inError = 1;
                            //break;
                            return Constants.ReturnValues.RETURN_UNKNOWN;
                        }

                        int inRet = isoPacket.inProcessPacket(FinalData, TransactionDetails.inFinalLength);


                        remoteHost.inDisconnection();

                        if (inRet == Constants.ReturnValues.RETURN_UNKNOWN) {//PROMPT CHECK REVERSAL and ENQUIRY
                            return Constants.ReturnValues.RETURN_UNKNOWN;
                        } else if (inRet == Constants.ReturnValues.RETURN_OK) {
                            //redirect to error
                            break;
                        } else {
                            KeyValueDB.removeReversal(loContext);//Clear Reversal
                            //inError = 1;
                            //redirect to error
                            //break;
                            return Constants.ReturnValues.RETURN_ERROR;
                        }*/


                    case 5:

                        Log.i(TAG, "\nSave Record:");
                        //save Record
                        //isoPacket.vdSaveRecord(AlipayActivity.this);
                        saveRecord.vdSaveRecord();
                        KeyValueDB.removeReversal(loContext);//Clear Reversal
                        break;
                    case 6://

                        //Print receipt
                        Log.i(TAG, "\nPrinting Receipt");
                        //printReceipt.inPrintReceipt(daoSession, loContext);
                        gprintReceipt.inPrintReceipt();
                        break;
                    case 7:
                        //Increment STAN NUMBER
                        payServices.vdUpdateSystemTrace(daoSession);
                        break;
                    case 8:
                        //Keep Upload Transcation On
                        if (TransactionDetails.trxType == Constants.TransType.VOID || TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND) {
                            ///stTrace = payServices.pGetSystemTrace(daoSession);
                            FinalData = null;
                            FinalData = new byte[1512];
                            TransactionDetails.inFinalLength = isoPacket.inCreatePacket(FinalData, Constants
                                    .TransType.ALIPAY_UPLOAD,AlipayActivity.this);
                            result = "";
                            for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                                result = result + String.format("%02x", FinalData[k]);
                            }
                            Log.i(TAG, "\nUpload data store::");
                            Log.i(TAG, result);
                            KeyValueDB.setUpload(loContext, new String(result));

                            inError = inCheckUpload();
                            if (inError != Constants.ReturnValues.RETURN_OK)
                                break;
                        }
                        inError = inCheckUpload();
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

    int inCheckReversal()
    {
        String ReversalData = KeyValueDB.getReversal(loContext);
        if(!ReversalData.isEmpty())
        {
            if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
            //FinalData = UploadData.getBytes();
            byte[] FinalData = new BigInteger(ReversalData, 16).toByteArray();
            TransactionDetails.inFinalLength = FinalData[0] *256;
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (FinalData[1]);
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
            if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                Log.i(TAG,"AlipayActivity::REversal Send Failed");
                return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
            }

            if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                Log.i(TAG,"AlipayActivity::REversal Receive Failed");
                return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
            }
            KeyValueDB.removeReversal(loContext);

            if (remoteHost.inDisconnection() != 0) {
                return Constants.ReturnValues.RETURN_ERROR;
            }

        }
        //Store Reversal
        /*TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.REVERSAL);
        if(TransactionDetails.inFinalLength == 0) {
            return Constants.ReturnValues.RETURN_ERROR;
        }
        String result = "";
        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        Log.i(TAG,"\nSet Reversal:");
        Log.i(TAG,result);
        KeyValueDB.setReversal(loContext,new String(result));*/
        return Constants.ReturnValues.RETURN_OK;
    }

    int inCheckUpload() {

        String UploadData = KeyValueDB.getUpload(loContext);
        if (!UploadData.isEmpty()) {
            if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
            //FinalData = UploadData.getBytes();
            byte[] FinalData = new BigInteger(UploadData, 16).toByteArray();
            TransactionDetails.inFinalLength = FinalData[0] * 256;
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (FinalData[1]);
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + 2;
            if ((FinalData = remoteHost.inSendRecvPacket(FinalData, TransactionDetails.inFinalLength)) == null) {
                Log.i(TAG, "AlipayActivity::Upload Send Failed");
                return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
            }
            if (isoPacket.inProcessPacket(FinalData, TransactionDetails.inFinalLength) != 0) {
                Log.i(TAG, "AlipayActivity::Upload Receive Failed");
                return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
            }
            KeyValueDB.removeUpload(loContext);

            if (remoteHost.inDisconnection() != 0) {
                return Constants.ReturnValues.RETURN_ERROR;
            }


        }
        return Constants.ReturnValues.RETURN_OK;
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


    private void loadDatabaseContent(){

        commsModelDao =daoSession.getCommsModelDao();
        httModelDao=daoSession.getHTTModelDao();


    }


}
