package com.mobileeftpos.android.eftpos.async;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.activity.AlipayActivity;
import com.mobileeftpos.android.eftpos.activity.AlipayCheckPrompt;
import com.mobileeftpos.android.eftpos.activity.HomeActivity;
import com.mobileeftpos.android.eftpos.activity.PaymentFailure;
import com.mobileeftpos.android.eftpos.activity.PaymentSuccess;
import com.mobileeftpos.android.eftpos.activity.SettlementFlow;
import com.mobileeftpos.android.eftpos.activity.VoidFlow;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.HostModel;

import java.math.BigInteger;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by venkat on 7/4/2017.
 */

public class AsyncTaskRequestResponse {

    AsyncTaskRunner mAsyncTask;
    private final String TAG = "my_custom_msg";

    private static DBHelper databaseObj;
    public static Context loContext;

    //Refer other clases
    public TransactionDetails trDetails = new TransactionDetails();
    public PacketCreation isoPacket = new PacketCreation();
    public RemoteHost remoteHost = new RemoteHost();
    public PrintReceipt printReceipt = new PrintReceipt();
    public PayServices payServices = new PayServices();
        public static String barCodeValue=null;
    public byte[] FinalData = new byte[1512];
    //public int TransactionDetails.inFinalLength = 0;
    public static boolean isFromBarcodeScanner;
    private static final int TIME_OUT = 5000;
    private static String BASE_URL="https://devapi.prayapay.com/v2?";
    private String ServerIP="";
    private String Port="";

    public int AsyncTaskCreation(Context context) {
        loContext = context;
        databaseObj = new DBHelper(loContext);

        new AsyncTaskRunner().execute();
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

                inRet = processRequest();
                return Integer.toString(inRet);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "AlipayActivity::Alipay:AsyncTaskRunner " + e.getMessage());
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);

            payServices.vdUpdateSystemTrace(databaseObj);
            if(result != null) {
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
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR) {
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
                } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_NOTIFICATION) {
                    //startActivity(new Intent(AlipayActivity.this,AlipayCheckPrompt.class));
                }else
                {
                    loContext.startActivity(new Intent(loContext, HomeActivity.class));
                }
            }
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "AlipayActivity::Alipay:onPreExecute");
            progressDialog = ProgressDialog.show(loContext,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }
    }

    private int inSearchAlipayHost() {
        //findout number of host available
        HostModel hostModel;
        List<HostModel> hostModelList = databaseObj.getAllHostTableData();
        for (int i = 0; i < hostModelList.size(); i++) {
            hostModel = hostModelList.get(i);
            if (!(hostModel == null || hostModel.equals(""))) {
                if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {
                    //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                    if(hostModel.getHDT_HOST_LABEL().contains("ALI")){
                        TransactionDetails.inGHDT = Integer.parseInt(hostModel.getHDT_HOST_ID());
                        TransactionDetails.inGCOM = Integer.parseInt(hostModel.getHDT_COM_INDEX());
                        TransactionDetails.inGCURR = Integer.parseInt(hostModel.getHDT_CURR_INDEX());
                        return Constants.ReturnValues.RETURN_OK;
                    }
                }
            }
        }

        return Constants.ReturnValues.RETURN_ERROR;

    }
    int inCheckSettlementFlag()
    {
        HostModel hostModel = new HostModel();
        hostModel = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        if(hostModel.getHDT_SETTLEMENT_FLAG().equals("1")){
            return Constants.ReturnValues.RETURN_SETTLEMENT_NEEDED;
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    int inCheckReversal()
    {
        String ReversalData = KeyValueDB.getReversal(loContext);
        if(!ReversalData.isEmpty())
        {
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

            if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
        }
        //Store Reversal
        TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.REVERSAL);
        if(TransactionDetails.inFinalLength == 0) {
            return Constants.ReturnValues.RETURN_ERROR;
        }
        String result = "";
        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        Log.i(TAG,"\nSet Reversal:");
        Log.i(TAG,result);
        KeyValueDB.setReversal(loContext,new String(result));
        return Constants.ReturnValues.RETURN_OK;
    }

    int inCheckUpload(int inConnectionRequired) {
        String UploadData = KeyValueDB.getUpload(loContext);
        if (!UploadData.isEmpty()) {
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

            if(inConnectionRequired == Constants.TRUE) {
                if (remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
                    return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
            }
        }
        return Constants.ReturnValues.RETURN_OK;
    }
        private int processRequest()
        {

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
                        if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE || TransactionDetails.trxType == Constants.TransType.VOID) {
                            inError = trDetails.inSortPAN(databaseObj);
                            if(inError != Constants.ReturnValues.RETURN_OK)
                                break;
                        }else
                        {
                            inError = inSearchAlipayHost();
                            if(inError != Constants.ReturnValues.RETURN_OK)
                                break;
                        }
                        inError = inCheckSettlementFlag();
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;

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

                        inError = remoteHost.inConnection(ServerIP, Port);
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;

                        inError = inCheckReversal();
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;

                        inError = inCheckUpload(Constants.TRUE);
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;
                        break;
                    case 2://
                        //TransactionDetails.trxType = inTempTrxType;
                    TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, TransactionDetails.trxType);
                    if(TransactionDetails.inFinalLength == 0) {
                        inError = Constants.ReturnValues.RETURN_ERROR;
                        break;
                    }
                    break;
                    case 3://
                        Log.i(TAG, "Aipay:inSendRecvPacket:");
                        if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null) {
                            inError = Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
                            break;
                        }

                        break;
                    case 4:

                        inError = isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength);

                        if (remoteHost.inDisconnection() != 0) {
                            inError = Constants.ReturnValues.RETURN_ERROR;
                            break;
                        }
                        break;
                    case 5:
                        Log.i(TAG, "\nSave Record:");
                        //save Record
                        isoPacket.vdSaveRecord(databaseObj);
                        KeyValueDB.removeReversal(loContext);//Clear Reversal
                        break;
                    case 6://
                        //Print receipt
                        Log.i(TAG, "\nPrinting Receipt");
                        printReceipt.inPrintReceipt(databaseObj,loContext);
                        break;
                    case 7:
                        //Increment STAN NUMBER
                        payServices.vdUpdateSystemTrace(databaseObj);
                        break;
                    case 8:
                        //Keep Upload Transcation On
                        if(TransactionDetails.trxType == Constants.TransType.VOID || TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND){
                            stTrace = payServices.pGetSystemTrace(databaseObj);
                            FinalData=null;
                            FinalData = new byte[1512];
                            TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.ALIPAY_UPLOAD);
                            result = "";
                            for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                                result = result + String.format("%02x", FinalData[k]);
                            }
                            Log.i(TAG,"\nUpload data store::");
                            Log.i(TAG,result);
                            KeyValueDB.setUpload(loContext,new String(result));
                            inError = remoteHost.inConnection(ServerIP, Port);
                            if(inError != Constants.ReturnValues.RETURN_OK)
                                break;
                            inError = inCheckUpload(Constants.FALSE);
                            if(inError != Constants.ReturnValues.RETURN_OK)
                                break;
                        }
                        inError = inCheckUpload(Constants.FALSE);
                        if(inError != Constants.ReturnValues.RETURN_OK)
                            break;
                        break;
                    default://Show the receipt in the display and give option to print or email
                        whLoop=false;
                        break;
                }
                if(inError != Constants.ReturnValues.RETURN_OK)
                {
                    Log.i(TAG, "Aipay:inError:1:");
                    Log.i(TAG, "Aipay:inError:1:");
                    Log.i(TAG, "Aipay:inError:1:");
                    return inError;
                }
            }

            return inError;
        }


}

