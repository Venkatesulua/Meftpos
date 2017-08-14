package com.mobileeftpos.android.eftpos.Ezlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.TransactionFlow.HAfterTransaction;
import com.mobileeftpos.android.eftpos.activity.HomePagerActivity;
import com.mobileeftpos.android.eftpos.activity.MyApplication;

import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public class Logon extends AppCompatActivity {

    private Context context;
    ISOMsg isoMsg = new ISOMsg();
    ISOPackager1 packager = new ISOPackager1();
    private final String TAG = "my_custom_msg";
    private ProgressDialog progressDialog;
    double progressValue=0;
    private SamFunctions samRelated =new SamFunctions();
    private ReadCardOpt mReadCardOpt;
    public HAfterTransaction afterTranscation = new HAfterTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        context = Logon.this;
        mReadCardOpt = MyApplication.mReadCardOpt;
        //databaseObj = new DBHelper(Logon.this);
        inLogon();
    }
    private void inLogon() {
        //Parameter if ..else

        if (AppUtil.isNetworkAvailable(Logon.this)) {
            TransactionDetails.trxType =  Constants.TransType.LOGON;
            new AsyncTaskRunner().execute();


        }else {
            Toast.makeText(context, "BLACKLIST :NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

    }



    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {
            String result = "";
            int inRet = -1;
            try {

                inRet = processRequest();
                return Integer.toString(inRet);
            } catch (Exception e) {
                e.printStackTrace();
                inRet= Constants.ReturnValues.RETURN_ERROR;
                resp =Integer.toString(inRet);

            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            super.onPostExecute(result);
            //cardRelated.cancelCheckCard();
            if(result != Integer.toString(Constants.ReturnValues.RETURN_OK))
                afterTranscation.FinalStatusDisplay(Logon.this,result);
            else
            {
                Toast.makeText(Logon.this, "LOGON SUCCESSFUL",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context,
                    "Logon Processing",
                    "Please wait...");
            progressDialog.show();
           /* progressDialog=new ProgressDialog(Logon.this);
            progressDialog.setTitle("BLACKLIST Downloading");
            progressDialog.setMessage("Downloading..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();*/
        }

    }

    /*public int inSearchHost(String lstHostType) {
        //findout number of host available
        HostModel hostModel;
        List<HostModel> hostModelList = databaseObj.getAllHostTableData();
        for (int i = 0; i < hostModelList.size(); i++) {
            hostModel = hostModelList.get(i);
            if (!(hostModel == null || hostModel.equals(""))) {
                if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {
                    //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                    if(hostModel.getHDT_HOST_TYPE().equals(lstHostType)){
                        TransactionDetails.inGHDT = Integer.parseInt(hostModel.getHDT_HOST_ID());
                        TransactionDetails.inGCOM = Integer.parseInt(hostModel.getHDT_COM_INDEX());
                        TransactionDetails.inGCURR = Integer.parseInt(hostModel.getHDT_CURR_INDEX());
                        return Constants.ReturnValues.RETURN_OK;
                    }
                }
            }
        }

        return Constants.ReturnValues.RETURN_ERROR;

    }*/

    private int processRequest() {

        int inError=Constants.ReturnValues.RETURN_OK;
        //CommsModel comModel = new CommsModel();
        if(TransactionDetails.trxType ==  Constants.TransType.LOGON )
        {

            if(samRelated.initPSAMCard(mReadCardOpt) != Constants.ReturnValues.RETURN_OK)
            {
                Toast.makeText(this,"SAM INITILIATION FAILED",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, HomePagerActivity.class));
                finish();
                return Constants.ReturnValues.RETURN_ERROR;
            }
            if(samRelated.SelectEzlSAMApp(mReadCardOpt) != Constants.ReturnValues.RETURN_OK)
            {
                Toast.makeText(this,"SAM INITILIATION FAILED",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, HomePagerActivity.class));
                finish();
                return Constants.ReturnValues.RETURN_ERROR;
            }
            if(samRelated.SelectEzlSAMGetId(mReadCardOpt) != Constants.ReturnValues.RETURN_OK)
            {
                Toast.makeText(this,"SAM INITILIATION FAILED",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, HomePagerActivity.class));
                finish();
                return Constants.ReturnValues.RETURN_ERROR;
            }
            inError = afterTranscation.inSearchHost(Constants.HostType.EZLINK_PAYMENT_HOST);
            if(inError != Constants.ReturnValues.RETURN_OK)
                return inError;
        }else
            return Constants.ReturnValues.RETURN_ERROR;


        TransactionDetails.responseMessge = StringByteUtils.bytesToHexString(afterTranscation.inCreateField63());
        inError = afterTranscation.inHostConnect();


        if(inError == Constants.ReturnValues.RETURN_OK){
            TransactionDetails.responseMessge="LOGON SUCCESSFUL";
        }else{
            TransactionDetails.responseMessge="LOGON FAILED. \n TRY AGAIN";
        }
        return inError;
    }



}

