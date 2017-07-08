package com.mobileeftpos.android.eftpos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.mobileeftpos.android.eftpos.database.DBStaticField;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.utils.AppUtil;



import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;



import java.util.ArrayList;
import java.util.List;


public class SettlementFlow extends AppCompatActivity {

    private TransactionDetails transDetails = new TransactionDetails();
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



    //int[] inHdtList = new int[100];
   // private PacketCreation isoPacket = new PacketCreation();
    //private RemoteHost remoteHost = new RemoteHost();
    //public byte[] FinalData = new byte[1512];
    public HostModel hostModel = new HostModel();



    private ListView settelmentListview;
    private PacketCreation isoPacket = new PacketCreation();
    private RemoteHost remoteHost = new RemoteHost();
    public byte[] FinalData = new byte[1512];
    private Context mcontext;
    private List<HostModel>settlementList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_flow);
        mcontext=SettlementFlow.this;
        databaseObj = new DBHelper(SettlementFlow.this);

        settelmentListview=(ListView)findViewById(R.id.settlement_menu);

        inSettlement();
    }

    private void inSettlement() {
        //findout number of host available
        HostModel hostModel;
        List<HostModel> hostModelList = databaseObj.getAllHostTableData();
        for (int i = 0; i < hostModelList.size(); i++) {
            hostModel = hostModelList.get(i);
            if (!(hostModel == null || hostModel.equals(""))) {
                if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {
                    //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                    settlementList.add(hostModel);
                }
            }
        }
        SettlementAdapter adapter=new SettlementAdapter(settlementList, mcontext);
        settelmentListview.setAdapter(adapter);


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
            if(result!=null && !result.equalsIgnoreCase("null")) {
                if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SettlementFlow.this, PaymentSuccess.class);
                            startActivity(intent);

                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                    //Delete the Batch File
                    databaseObj.deleteallvalues(DBStaticField.TABLE_BATCH);
                    //Increment Batch Number
                    payService.vdUpdateSystemBatch(databaseObj);
                    //finish();
                }
                else if (Integer.parseInt(result) == Constants.ReturnValues.NO_TRANSCATION) {
                    TransactionDetails.responseMessge = "NO TRANSCATIONS TO SETTLE";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SettlementFlow.this, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                }else
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SettlementFlow.this, PaymentFailure.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }, TIME_OUT);
                }
            }else
             {
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
            long SaleCount=0,SaleAmount=0,RefundCount=0,RefundAmount=0;
            int inRet=0;
            //Password Entry
            while(whLoop) {
                switch(inPhase++)
                {
                    case 0://Validation; in force settlement;


                    inRet = isoPacket.vdScanRecord(databaseObj);
                    if(inRet == Constants.ReturnValues.NO_TRANSCATION){
                        return Constants.ReturnValues.NO_TRANSCATION;
                    }
                    Log.i(TAG,"GetInvoice::Alipay::inSORTPAN ___OKOK");
                    break;


                       /* Log.i(TAG,"GetInvoice::processRequest_2");
                        if (trDetails.inSortPAN(databaseObj) == 1) {
                            Log.i(TAG,"GetInvoice::ERROR in SORTPAN");
                            inError = 1;
                        }
                        Log.i(TAG,"GetInvoice::Alipay::inSORTPAN ___OKOK");*/
                        //break;

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

                        String ReversalData = KeyValueDB.getReversal(SettlementFlow.this);
                        if(!ReversalData.isEmpty())
                        {
                            //FinalData = UploadData.getBytes();
                            byte[] FinalData = new BigInteger(ReversalData, 16).toByteArray();
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
                            KeyValueDB.removeReversal(SettlementFlow.this);
                        }
                        String UploadData = KeyValueDB.getUpload(SettlementFlow.this);
                        if(!UploadData.isEmpty())
                        {
                            FinalData = new BigInteger(UploadData, 16).toByteArray();
                           // FinalData = UploadData.getBytes();
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
                            KeyValueDB.removeUpload(SettlementFlow.this);

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

                        break;
                    case 4://

                        Log.i(TAG, "GetInvoice:inProcessPacket:");
                        inRet = isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength);
                        if(inRet == Constants.ReturnValues.RETURN_BATCH_TRANSFER) {
                            if(isoPacket.BatchTranfer(databaseObj,ServerIP,Port) ==Constants.ReturnValues.RETURN_OK){
                                //Final Settlement
                                TransactionDetails.trxType = Constants.TransType.FINAL_SETTLEMENT;
                                TransactionDetails.inFinalLength = isoPacket.inCreatePacket(databaseObj,FinalData, Constants.TransType.FINAL_SETTLEMENT);
                                if(TransactionDetails.inFinalLength == 0)
                                    return Constants.ReturnValues.RETURN_ERROR;

                                if (remoteHost.inConnection(ServerIP, Port) != 0)
                                    return Constants.ReturnValues.RETURN_ERROR;

                                if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null)
                                    return Constants.ReturnValues.RETURN_ERROR;

                                inRet = isoPacket.inProcessPacket(FinalData,TransactionDetails.inFinalLength);
                                if(inRet != Constants.ReturnValues.RETURN_OK) {
                                    return Constants.ReturnValues.RETURN_ERROR;
                                }
                                if (remoteHost.inDisconnection() != Constants.ReturnValues.RETURN_OK) {
                                    return Constants.ReturnValues.RETURN_ERROR;
                                }
                                if(inRet == Constants.ReturnValues.RETURN_OK) {
                                    inError=0;
                                }
                            }


                        }else if(inRet == Constants.ReturnValues.RETURN_OK) {
                            inError=0;
                        }
                            else if(inRet != 0) {
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

                        break;
                    case 6://
                        //Print receipt
                        Log.i(TAG, "\nPrinting Receipt");
                        printReceipt.inPrintReceipt(databaseObj,SettlementFlow.this);
                        break;
                    case 7://Show the receipt in the display and give option to print or email


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
                return Constants.ReturnValues.RETURN_OK;

            }
            return Constants.ReturnValues.RETURN_ERROR;
        }


    class ViewHolder {
        TextView txtitemView;

    }

    class SettlementAdapter extends BaseAdapter {

        private Context context;
        List<HostModel> Datastring;

        public SettlementAdapter(List<HostModel> Datastring, Context mContext) {

            this.Datastring = Datastring;
            context = mContext;

        }


        @Override
        public int getCount() {
            return Datastring.size();
        }

        @Override
        public Object getItem(int position) {
            return Datastring.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.settelment_item, null);
                holder = new ViewHolder();
                holder.txtitemView = (TextView) convertView.findViewById(R.id.settelment_tv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final HostModel rowHostItemModelItem = (HostModel) getItem(position);

            holder.txtitemView.setText(rowHostItemModelItem.getHDT_DESCRIPTION());

            holder.txtitemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    TransactionDetails.inGHDT = Integer.parseInt(rowHostItemModelItem.getHDT_HOST_ID());
                    TransactionDetails.inGCOM = Integer.parseInt(rowHostItemModelItem.getHDT_COM_INDEX());
                    TransactionDetails.inGCURR = Integer.parseInt(rowHostItemModelItem.getHDT_CURR_INDEX());

                      if (AppUtil.isNetworkAvailable(SettlementFlow.this)) {
                       mAsyncTask = new AsyncTaskRunner();
                       mAsyncTask.execute(new String[]{"null", "null"});
                        }else {
                         Toast.makeText(SettlementFlow.this, "GetInvoice:BAR:NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
                       }

                }
            });

            return convertView;
        }

    }
}
