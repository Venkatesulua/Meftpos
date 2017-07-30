package com.mobileeftpos.android.eftpos.activity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.Review_Transaction;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class VoidFlow extends AppCompatActivity {

    private TransactionDetails transDetails = new TransactionDetails();
    private EditText editInvoice;
    private Button submitButton;
    //private DBHelper databaseObj;
    private Constants constants = new Constants();
    private Review_Transaction reviewTrans = new Review_Transaction();
    private BatchModel batchModeldata = new BatchModel();
    private TransactionDetails trDetails = new TransactionDetails();
    private PayServices payService = new PayServices();
    private final String TAG = "my_custom_msg";
    AsyncTaskRunner mAsyncTask;
    private PrintReceipt printReceipt = new PrintReceipt();
    private static final int TIME_OUT = 5000;
    
    private PacketCreation isoPacket = new PacketCreation();
    private RemoteHost remoteHost = new RemoteHost();
    public byte[] FinalData = new byte[1512];
    //private int TransactionDetails.inFinalLength = 0;
   // private AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse();
    public static Context context;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_invoice);
        //transDetails.vdCleanFields();
        editInvoice = (EditText) findViewById(R.id.invoice_et);
        submitButton = (Button) findViewById(R.id.invoice_btn);
        submitButton.setOnClickListener(new ClickLIstener());
        daoSession = GreenDaoSupport.getInstance(VoidFlow.this);
        context = VoidFlow.this;

    }
    private class ClickLIstener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == submitButton) {

                if (editInvoice.getText().toString().length() > 0 && editInvoice.getText().toString().length() < 7)
                    inVoid(editInvoice.getText().toString());
                else
                    Toast.makeText(VoidFlow.this, "Invoice Field is missing or \n Excess Code entered", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int inVoid(String stInvoice){


        TransactionDetails.EntryMode=Integer.toString(Constants.TransMode.SWIPE);
        if(stInvoice.length() < 6)
        {
            stInvoice = String.format("%06d",Integer.parseInt(stInvoice));
        }
        TransactionDetails.InvoiceNumber = stInvoice;
        batchModeldata = reviewTrans.lgReviewAllTrans(VoidFlow.this,stInvoice,batchModeldata);

        if(batchModeldata == null) {
            TransactionDetails.responseMessge = "ALREADY VOIDED OR TRANS NOT FOUND";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(VoidFlow.this, PaymentFailure.class);
                    startActivity(intent);
                }
            }, TIME_OUT);
            return Constants.FALSE;
        }
        processVoid();
       
        /*if(stRet == null) {
            if (trDetails.inSortPAN(databaseObj) == 1) {
                Log.i(TAG, "GetInvoice::ERROR in SORTPAN");
                
            }
        ;
        }*/
        /* PREPARING TO TRANSMIT MESSAGE TO HOST */
        payService.vdUpdateSystemTrace(daoSession);
        return Constants.TRUE;
    }
    
    int processVoid(){

        if (AppUtil.isNetworkAvailable(VoidFlow.this)) {
            //String ppp = TransactionDetails.PAN ;
            //TransactionDetails.trxType = Constants.TransType.VOID;
            //ASTask.AsyncTaskCreation(context);
            mAsyncTask = new AsyncTaskRunner();
            mAsyncTask.execute(new String[]{"null", "null"});
        }else {
            Toast.makeText(VoidFlow.this, "GetInvoice:BAR:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }
        
        return 0;
    }
    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            String result="";
            int inRet=-1;
            try {

                Log.i(TAG,"GetInvoice::Alipay:AsyncTaskRunner");
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
            Log.i(TAG,"GetInvoice::Alipay:onPostExecute");
            payService.vdUpdateSystemTrace(daoSession);
            if(result!=null && result.equals("0"))
            {
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
               // printReceipt.inPrintReceipt(databaseObj);
                //Redirect to Success Activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(VoidFlow.this, PaymentSuccess.class);
                        startActivity(intent);

                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
                //finish();
            }else
            {
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(VoidFlow.this, PaymentFailure.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }
            //progressDialog.dismiss();


        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"GetInvoice::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(VoidFlow.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private int processRequest(String ServerIP,String Port) {

        Log.i(TAG,"GetInvoice::processRequest_1");

        int inError=0;
        int inPhase=0;
        boolean whLoop=true;
        String result="";
        CommsModel comModel = new CommsModel();
        //Password Entry
        while(whLoop) {
            switch(inPhase++)
            {
                case 0://Validation; in force settlement;
                    Log.i(TAG,"GetInvoice::processRequest_2");
                    if (trDetails.inSortPAN(daoSession) == 1) {
                        Log.i(TAG,"GetInvoice::ERROR in SORTPAN");
                        inError = 1;
                    }
                    Log.i(TAG,"GetInvoice::Alipay::inSORTPAN ___OKOK");
                    break;
                case 1://

                    Log.i(TAG,"GetInvoice::Alipay::COMMUNICATION PARAMS...");
                   // comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);
                    comModel=GreenDaoSupport.getCommsModelOBJ(VoidFlow.this);
                    Log.i(TAG,"GetInvoice::Alipay::COMMUNICATION PARAMS2...");
                    String IP_Port = comModel.getCOM_PRIMARY_IP_PORT();
                    Log.i(TAG,"GetInvoice::Alipay::IP_Port::"+IP_Port);
                    int indexOffset = IP_Port.indexOf("|");
                    Log.i(TAG,"GetInvoice::Alipay::indexOffset::"+indexOffset);
                    ServerIP = IP_Port.substring(0,indexOffset);

                    Log.i(TAG,"GetInvoice::Alipay::ServerIP::"+ServerIP);

                    Port = IP_Port.substring(indexOffset+1);
                    Log.i(TAG, "GetInvoice:inCreatePacket:");
                    Log.i(TAG, "GetInvoice:ServerIP ::: "+ServerIP);
                    Log.i(TAG, "GetInvoice:Server PORT ::: "+Port);

                    String stTrace = payService.pGetSystemTrace(VoidFlow.this);

                    Log.i(TAG, "GetInvoice:inCreatePacket:stTrace::"+stTrace);
                    Log.i(TAG, "GetInvoice:inCreatePacket:stTrace::"+stTrace);

                    Log.i(TAG, "GetInvoice:inConnection:");
                    if (remoteHost.inConnection(ServerIP, Port) != 0) {
                        inError = 1;
                        break;
                    }

                    String UploadData = KeyValueDB.getUpload(VoidFlow.this);
                    if(!UploadData.isEmpty())
                    {
                        TransactionDetails.inFinalLength = UploadData.length();
                        FinalData = UploadData.getBytes();
                        TransactionDetails.inFinalLength = FinalData[0] *256;
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (FinalData[1]);
                        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
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
                    }

                    break;
                case 2://

                    FinalData=null;
                    FinalData = new byte[1512];
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(FinalData, Constants.TransType.VOID,VoidFlow.this);
                    if(TransactionDetails.inFinalLength == 0)
                        inError = 1;
                    break;
                case 3://
                    Log.i(TAG, "GetInvoice:inSendRecvPacket:");
                    if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                        inError = 1;
                        break;
                    }

                    result = "";
                    for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"GetInvoice::\nAlipay_inSendRecvPacket_Received:");
                    Log.i(TAG,result);

                    break;
                case 4://

                    result = "";
                    for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"GetInvoice::\nAlipay_inProcessPacket_Received:");
                    Log.i(TAG,result);

                    Log.i(TAG, "GetInvoice:inProcessPacket:");
                    if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                        inError = 1;
                        //redirect to error
                        break;
                    }

                    Log.i(TAG, "GetInvoice:inDisconnection:");
                    if (remoteHost.inDisconnection() != 0) {
                        inError = 1;
                        break;
                    }
                    break;
                case 5:
                    Log.i(TAG, "\nSave Record:");
                    //save Record
                   // isoPacket.vdSaveRecord(databaseObj);
                    batchModeldata.setVoided(Integer.toString(Constants.TRUE));
                    GreenDaoSupport.insertBatchModelOBJ(VoidFlow.this,batchModeldata);
                   // databaseObj.UpdateBatchData(batchModeldata);
                    break;
                case 6://
                    //Print receipt
                    Log.i(TAG, "\nPrinting Receipt");
                    TransactionDetails.trxType = Constants.TransType.VOID;
                    printReceipt.inPrintReceipt(daoSession,VoidFlow.this);
                    break;
                case 7://Show the receipt in the display and give option to print or email
                    //startActivity(new Intent(GetInvoice.this, HomeActivity.class));
                    //Upload void transaction
                    //whLoop=false;
                    stTrace = payService.pGetSystemTrace(VoidFlow.this);
                    FinalData=null;
                    FinalData = new byte[1512];

                    TransactionDetails.trxType=Constants.TransType.ALIPAY_UPLOAD;
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(FinalData, Constants.TransType
                            .ALIPAY_UPLOAD,VoidFlow.this);
                    //String result;
                    result = "";
                    for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                        result = result + String.format("%02x", FinalData[k]);
                    }
                    Log.i(TAG,"\nSendings:");
                    Log.i(TAG,result);
                    KeyValueDB.setUpload(VoidFlow.this,new String(result));

                    if(TransactionDetails.inFinalLength == 0)
                       break;
                    if (remoteHost.inConnection(ServerIP, Port) != 0) {
                    break;
                }
                    if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                        break;
                    }
                    if (isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength) != 0) {
                        //redirect to error
                        break;
                    }

                    Log.i(TAG, "GetInvoice:inDisconnection:");
                    if (remoteHost.inDisconnection() != 0) {
                        break;
                    }
                    batchModeldata.setUploaded(Integer.toString(Constants.TRUE));
                //databaseObj.UpdateBatchData(batchModeldata);
                    GreenDaoSupport.insertBatchModelOBJ(VoidFlow.this,batchModeldata);
                KeyValueDB.removeUpload(VoidFlow.this);

                break;
                default:
                    whLoop=false;
                    break;
                //break;
            }
            if(inError == 1)
            {
                Log.i(TAG, "GetInvoice:inError:1:");
                Log.i(TAG, "GetInvoice:inError:1:");
                Log.i(TAG, "GetInvoice:inError:1:");

                //Redirect to error Activity

                break;
            }
        }

        if(inError == 0)
        {
            Log.i(TAG, "GetInvoice:SUCCESS");
            Log.i(TAG, "GetInvoice:SUCCESS");
            Log.i(TAG, "GetInvoice:SUCCESS");
            //Redirect to Success Activity

        }
        return inError;
    }

}
