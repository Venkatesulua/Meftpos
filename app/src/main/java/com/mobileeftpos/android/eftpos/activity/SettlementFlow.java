package com.mobileeftpos.android.eftpos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class SettlementFlow extends AppCompatActivity {

    private TransactionDetails transDetails = new TransactionDetails();
    private EditText editInvoice;
    private Button submitButton;
    private DBHelper databaseObj;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_flow);
        databaseObj = new DBHelper(SettlementFlow.this);
        inSettlement();
    }

    private int inSettlement(){
        //findout number of host available
        HostModel hostModel = new HostModel();
        for(int i=0;i<100;i++) {
            hostModel = databaseObj.getHostTableData(i);
            if( !(hostModel ==null || hostModel.equals(""))){
                if(hostModel.getHDT_HOST_ENABLED()=="1"){
                    //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();

                }
            }else
                break;
        }
        if (AppUtil.isNetworkAvailable(SettlementFlow.this)) {
            mAsyncTask = new AsyncTaskRunner();
            mAsyncTask.execute(new String[]{"null", "null"});
        }else {
            Toast.makeText(SettlementFlow.this, "GetInvoice:BAR:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
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

                Log.i(TAG, "GetInvoice::Alipay:AsyncTaskRunner");
                inRet = processRequest(params[0], params[1]);
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
            Log.i(TAG, "GetInvoice::Alipay:onPostExecute");
            payService.vdUpdateSystemTrace(databaseObj);
            if (result != null && result.equals("0")) {
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
                Log.i(TAG, "GetInvoice:onPostExecute:SUCCESS");
                // printReceipt.inPrintReceipt(databaseObj);
                //Redirect to Success Activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SettlementFlow.this, PaymentSuccess.class);
                        startActivity(intent);

                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
                //finish();
            } else {
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");
                Log.i(TAG, "GetInvoice:onPostExecute:ERROR");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SettlementFlow.this, PaymentFailure.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, TIME_OUT);
            }
            //progressDialog.dismiss();


        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "GetInvoice::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(SettlementFlow.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }
    }
        private int processRequest(String ServerIP,String Port){

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
                        if (trDetails.inSortPAN(databaseObj) == 1) {
                            Log.i(TAG,"GetInvoice::ERROR in SORTPAN");
                            inError = 1;
                        }
                        Log.i(TAG,"GetInvoice::Alipay::inSORTPAN ___OKOK");
                        break;
                    case 1://

                        Log.i(TAG,"GetInvoice::Alipay::COMMUNICATION PARAMS...");
                        comModel = databaseObj.getCommsData(TransactionDetails.inGCOM);
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

                        String stTrace = payService.pGetSystemTrace(databaseObj);

                        Log.i(TAG, "GetInvoice:inCreatePacket:stTrace::"+stTrace);
                        Log.i(TAG, "GetInvoice:inCreatePacket:stTrace::"+stTrace);

                        Log.i(TAG, "GetInvoice:inConnection:");
                        if (remoteHost.inConnection(ServerIP, Port) != 0) {
                            inError = 1;
                            break;
                        }

                        String UploadData = KeyValueDB.getUpload(SettlementFlow.this);
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
                        TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.INIT_SETTLEMENT);
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
                        batchModeldata.setVOIDED(Integer.toString(Constants.TRUE));
                        databaseObj.UpdateBatchData(batchModeldata);
                        break;
                    case 6://
                        //Print receipt
                        Log.i(TAG, "\nPrinting Receipt");
                        printReceipt.inPrintReceipt(databaseObj);
                        break;
                    case 7://Show the receipt in the display and give option to print or email
                        //startActivity(new Intent(GetInvoice.this, HomeActivity.class));
                        //Upload void transaction
                        //whLoop=false;
                        stTrace = payService.pGetSystemTrace(databaseObj);
                        FinalData=null;
                        FinalData = new byte[1512];

                        TransactionDetails.trxType=Constants.TransType.ALIPAY_UPLOAD;
                        TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.ALIPAY_UPLOAD);
                        //String result;
                        result = "";
                        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                            result = result + String.format("%02x", FinalData[k]);
                        }
                        Log.i(TAG,"\nSendings:");
                        Log.i(TAG,result);
                        KeyValueDB.setUpload(SettlementFlow.this,new String(result));

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
                        batchModeldata.setUPLOADED(Integer.toString(Constants.TRUE));
                        databaseObj.UpdateBatchData(batchModeldata);

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
