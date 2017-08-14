package com.mobileeftpos.android.eftpos.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.mobileeftpos.android.eftpos.SupportClasses.Review_Transaction;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class SettlementFlow extends AppCompatActivity {

    private TransactionDetails transDetails = new TransactionDetails();
   // private DBHelper databaseObj;
    private Constants constants = new Constants();
    private Review_Transaction reviewTrans = new Review_Transaction();
    private BatchModel batchModeldata = new BatchModel();
    private TransactionDetails trDetails = new TransactionDetails();
    private final String TAG = "my_custom_msg";
    AsyncTaskRunner mAsyncTask;
    private static final int TIME_OUT = 5000;

    public HAfterTransaction afterTranscation = new HAfterTransaction();


    //int[] inHdtList = new int[100];
   // private PacketCreation isoPacket = new PacketCreation();
    //private RemoteHost remoteHost = new RemoteHost();
    //public byte[] FinalData = new byte[1512];
    public HostModel hostModel = new HostModel();



    private ListView settelmentListview;
    //private PacketCreation isoPacket = new PacketCreation();
   // private RemoteHost remoteHost = new RemoteHost();
    //public byte[] FinalData = new byte[1512];
    private Context mcontext;
    //private DaoSession daoSession;
    private List<HostModel>settlementList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_flow);
        mcontext=SettlementFlow.this;
        //daoSession = GreenDaoSupport.getInstance(SettlementFlow.this);
        settelmentListview=(ListView)findViewById(R.id.settlement_menu);

        inSettlement();
    }

    private void inSettlement() {
        //findout number of host available
        HostModel hostModel;
        List<HostModel> hostModelList = GreenDaoSupport.getHostModelOBJList(SettlementFlow.this);//databaseObj
        // .getAllHostTableData();
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
            afterTranscation.inAfterTrans();
            progressDialog.dismiss();
            afterTranscation.FinalStatusDisplay(SettlementFlow.this,result);

            //Delete the Batch File
            if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                GreenDaoSupport.DeleteBatchTransactions(SettlementFlow.this, String.format("%02d",TransactionDetails.inGHDT));//databaseObj
                //Increment Batch Number
                afterTranscation.inUpdateBatchNumber();
            }



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


                    inRet = afterTranscation.vdScanRecord(SettlementFlow.this);
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

                    case 1:
                        inError = afterTranscation.inCheckReversal();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "REVERSAL FAILED";
                            return Constants.ReturnValues.RETURN_REVERSAL_FAILED;
                        }else
                            KeyValueDB.removeReversal(mcontext);//Clear Reversal

                        inError = afterTranscation.inCheckUpload();
                        if (inError != Constants.ReturnValues.RETURN_OK) {
                            TransactionDetails.responseMessge = "UPLOAD FAILED";
                            return Constants.ReturnValues.RETURN_UPLOAD_FAILED;
                        }else
                            KeyValueDB.removeUpload(mcontext);//Clear Reversal

                        break;
                    case 2://

                        inError = afterTranscation.inHostConnect();
                        if(inError == Constants.ReturnValues.RETURN_BATCH_TRANSFER) {
                            inError = afterTranscation.BatchTranfer(SettlementFlow.this);
                            if( inError != Constants.ReturnValues.RETURN_OK)
                                return inError;
                            TransactionDetails.trxType = Constants.TransType.FINAL_SETTLEMENT;
                            inError = afterTranscation.inHostConnect();
                        }
                        if( inError != Constants.ReturnValues.RETURN_OK)
                            return inError;
                        break;
                    case 3://


                        break;
                    case 4://
                        break;
                    case 5:

                        break;
                    case 6://
                        //Print receipt
                        Log.i(TAG, "\nPrinting Receipt");
                        afterTranscation.inPrintReceipt();
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
            return inError;
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
                    afterTranscation.inGetAllRelaventParams();

                      if (AppUtil.isNetworkAvailable(SettlementFlow.this)) {
                       mAsyncTask = new AsyncTaskRunner();
                       mAsyncTask.execute(new String[]{"null", "null"});
                        }else {
                         Toast.makeText(SettlementFlow.this, "NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
                       }

                }
            });

            return convertView;
        }

    }
}
