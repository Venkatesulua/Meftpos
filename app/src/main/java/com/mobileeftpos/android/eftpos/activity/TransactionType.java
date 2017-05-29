package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.zxing.client.android.CaptureActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.model.TransactionDetails;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_type);
        barCodeValue=null;
        isFromBarcodeScanner=false;
        addListenerOnButton();

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
                globalVar.setGTransactionType(Constants.TransType.ALIPAY_SALE);
                Log.i(TAG,"ALIPAY CLICK_1");
                //startActivity(new Intent(TransactionType.this,ScannerActivity.class));
                Intent intentScan = new Intent(TransactionType.this, CaptureActivity.class);
                Log.i(TAG,"ALIPAY CLICK_2");
                intentScan.setAction(Constants.QRCODE.BARCODE_INTENT_ACTION);
                Log.i(TAG,"ALIPAY CLICK_3");
                intentScan.putExtra(Constants.QRCODE.BARCODE_DISABLE_HISTORY, false);
                Log.i(TAG,"ALIPAY CLICK_4");
                startActivityForResult(intentScan, Constants.QRCODE.BARCODE_RESULT_CODE);
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

        Log.i(TAG,"onActivityResult");
        if (requestCode == Constants.QRCODE.BARCODE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG,"onActivityResult_2");
            String contents = intent.getStringExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY);
            processBarcode(contents);
        } else {
            Log.i(TAG,"onActivityResult_3");
            Toast.makeText(getApplicationContext(), "QRCODE READ FAILED", Toast.LENGTH_SHORT).show();
            finish();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if(isFromBarcodeScanner){
            if(barCodeValue!=null && barCodeValue.length()>0) {
                processBarcode(barCodeValue);
            }

        }
    }

    private void processBarcode(String contents) {
        mAsyncTask = new AsyncTaskRunner();
        //mAsyncTask.execute(new String[] { "52.88.135.124", "10002" });
        mAsyncTask.execute(new String[] { "192.168.43.117", "9999" });
        trDetails.setPAN(contents);

    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                processRequest(params[0],params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TransactionType.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private void processRequest(String ServerIP,String Port) {

        isoMsg.setPackager(packager);
        inCreatePacket();
        if (inConnection(ServerIP, Port) != 0) {
            return;
        }
        if (inSendRecvPacket() != 0) {
            return;
        }
        if (inProcessPacket() != 0) {
            return;
        }
        if (inDisconnection() != 0) {
            return;
        }
    }

    private int inCreatePacket() {
        String stde27 = "";
        String stde29 = "";
        inFinalLength=0;
        isoMsg.setPackager(packager);
        try {

            Log.i(TAG,"inCreatePacket : ");
            Log.i(TAG,Integer.toString(globalVar.getGTransactionType()));

            switch (globalVar.getGTransactionType()) {
                case Constants.TransType.TMS_INITIAL_PACKET:
                    isoMsg.setHeader(globalVar.tmsParam.getTMS_TPDU().getBytes());
                    isoMsg.setMTI("0100");
                    isoMsg.set(3, Constants.PROCESSINGCODE.pcTmsInitial);
                    isoMsg.set(11, "000001");
                    isoMsg.set(24, "700");
                    isoMsg.set(26, globalVar.tmsParam.getTMS_FILENAME());
                    stde27 = Constants.TMS_DEFAULT_MMS_FAMILY + "\\" + globalVar.tmsParam.getTMS_APPLICATION();
                    isoMsg.set(27, stde27);

                    if (globalVar.getActivationDate() == null)
                        isoMsg.set(30, "0000000000");
                    else
                        isoMsg.set(30, globalVar.getActivationDate());
                    isoMsg.set(41, globalVar.tmsParam.getTMS_TERMINAL_ID());
                    break;
                case Constants.TransType.TMS_SUBSEQUENT_PACKET:
                    isoMsg.setHeader(globalVar.tmsParam.getTMS_TPDU().getBytes());
                    isoMsg.setMTI("0200");
                    isoMsg.set(3, Constants.PROCESSINGCODE.pcTmsSubsequent);
                    isoMsg.set(11, "000001");
                    isoMsg.set(24, "700");
                    isoMsg.set(26, globalVar.tmsParam.getTMS_FILENAME());
                    stde27 = Constants.TMS_DEFAULT_MMS_FAMILY + "\\" + globalVar.tmsParam.getTMS_APPLICATION();
                    isoMsg.set(27, stde27);
                    stde29 = String.format("%02d", globalVar.getGPartToDownload());
                    isoMsg.set(29, stde29);
                    isoMsg.set(41, globalVar.tmsParam.getTMS_TERMINAL_ID());
                    break;
                case Constants.TransType.ALIPAY_SALE:
                    Log.i(TAG,"ALIPAY_SALE : ");
                    CreateTLVFields(1, Constants.MTI.Financial);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest);
                    CreateTLVFields(4, "2088021411132342");
                    CreateTLVFields(5,"2088021411132342");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
                    Date date = new Date();
                    String stDate = dateFormat.format(date);
                    stDate = stDate+"00";
                    CreateTLVFields(8,stDate);
                    CreateTLVFields(9,"SGD");
                    CreateTLVFields(10,"100");
                    CreateTLVFields(11,trDetails.getPAN());
                    CreateTLVFields(41,"10000008");

                    String staddinfo ="merchant_name:";
                    staddinfo = staddinfo + "REGRESSION";
                    staddinfo = staddinfo + ",merchant_no:";
                    staddinfo = staddinfo + "254000000000001";
                    staddinfo = staddinfo + ",business_no:";
                    staddinfo = staddinfo + "0";
                    staddinfo = staddinfo + ",terminal_id:";
                    staddinfo = staddinfo + "10000008";
                    staddinfo = staddinfo + ",mcc:";
                    staddinfo = staddinfo + "0";
                    staddinfo = staddinfo + ",region_code:";
                    staddinfo = staddinfo + "RETAIL";
                    staddinfo = staddinfo + ",";
                    CreateTLVFields(47,staddinfo);
                    break;
                case Constants.TransType.ALIPAY_REFUND:
                    break;
                case Constants.TransType.PAYMENT_SALE:
                    break;
                case Constants.TransType.VOID:
                    break;
                case Constants.TransType.REVERSAL:
                    break;
                case Constants.TransType.REFUND:
                    break;
                case Constants.TransType.SETTLEMENT:
                    break;
            }

            String result;
            result = "";
            for (int k = 0; k < inFinalLength; k++) {
                result = result + String.format("%02x", FinalData[k]);
            }
            Log.i(TAG,"\nCreate Message:");
            Log.i(TAG,result);

            //  Log.i(TAG,"PACK:");
            // Get and print the output result
            try {
                if(globalVar.getGTransactionType() != Constants.TransType.ALIPAY_SALE) {
                    Log.i(TAG,"NOT ALIPAY SALES ");
                    Log.i(TAG,"NOT ALIPAY SALES ");
                    logISOMsg(isoMsg);
                    byte[] data = isoMsg.pack();
                    inFinalLength = AddLength_Tpdu(data, FinalData);
                }else
                {
                    Log.i(TAG," ALIPAY SALES ");
                    Log.i(TAG," ALIPAY SALES ");
                    byte[] data = new byte[inFinalLength];
                    for(int ij=0;ij<inFinalLength;ij++){
                        data[ij] = FinalData[ij];
                    }
                    inFinalLength = AddLength_Tpdu(data, FinalData);
                }


                //  Log.i(TAG,"data" + new String(data));
                //  Log.i(TAG,"FINAL DATA" + new String(FinalData));
            } catch (Exception ex) {
                Log.i(TAG, "IOException EXCEPTION");
                Log.i(TAG, ex.getMessage());
            }

        } catch (ISOException ex) {
            Log.i(TAG, "ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public int AddLength_Tpdu(byte[] data, byte[] FinalData) {
        int inOffset = 0;
        // byte[] FinalData = new byte[data.length + 7];
        int inPacLen = (data.length);

        Log.i(TAG,"AddLength_Tpdu_1");
        inOffset = inOffset + 2;
        byte[] tpdu = new byte[10];
        // Copy the respective TPDU Value
        Log.i(TAG,"AddLength_Tpdu_2");
        if(globalVar.getGTransactionType() != Constants.TransType.ALIPAY_SALE)
            tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();
        else
            tpdu = new BigInteger("6002540000", 16).toByteArray();

        Log.i(TAG,"AddLength_Tpdu_3");
        for (int i = 0; i < tpdu.length; i++) {
            FinalData[inOffset++] = tpdu[i];
        }
        Log.i(TAG,"AddLength_Tpdu_4");
		/*
		 * byte[] byLen = new BigInteger(Integer.toString(inPacLen +
		 * tpdu.length), 16).toByteArray(); if (byLen.length == 1) {
		 * FinalData[0] = 0x00; FinalData[1] = byLen[0]; } else { FinalData[0] =
		 * byLen[0]; FinalData[1] = byLen[1]; }
		 */
        // Check the length in BCD or HEX
        FinalData[0] = (byte) ((inPacLen + tpdu.length) / 256);
        FinalData[1] = (byte) ((inPacLen + tpdu.length) % 256);

        Log.i(TAG,"AddLength_Tpdu_5");
        for (int i = 0; i < inPacLen; i++) {
            FinalData[inOffset++] = data[i];
        }

        Log.i(TAG,"AddLength_Tpdu_6");

        String result;
        result = "";
        for (int k = 0; k < inOffset; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        Log.i(TAG,"\nResults:");
        Log.i(TAG,result);
        return inOffset;
    }
    private int inProcessPacket() {
        try {
            isoMsg.unpack(FinalData);
            // print the DE list
            logISOMsg(isoMsg);
        } catch (ISOException ex) {
            Log.i(TAG, "ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }

        return 0;
    }

    private int inConnection(String ServerIp,String Port) {
        Log.i(TAG, "Connection");
        Log.i(TAG, ServerIp);
        Log.i(TAG, Port);
        try {

            smtpSocket = new Socket(ServerIp, Integer.parseInt(Port));
        } catch (UnknownHostException e) {
            Log.i(TAG, "UnknownHostException");
            Log.i(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }

    private int inDisconnection() {
        try {
            if (smtpSocket != null)
                smtpSocket.close();
        } catch (UnknownHostException e) {
            Log.i(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }

    private void logISOMsg(ISOMsg msg) {
        Log.i(TAG, "----ISO MESSAGE-----");
        try {
            Log.i(TAG, "  MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    Log.i(TAG, "    Field-" + i + " : " + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "--------------------");
        }

    }

    private int inSendRecvPacket() {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = smtpSocket.getOutputStream();
            is = smtpSocket.getInputStream();

            String result;
            result = "";
            for (int k = 0; k < inFinalLength; k++) {
                result = result + String.format("%02x", FinalData[k]);
            }
            Log.i(TAG,"\nSendings:");
            Log.i(TAG,result);

            if (smtpSocket != null && os != null && is != null) {


                os.write(FinalData, 0, inFinalLength);
                FinalData = new byte[1512];
                inFinalLength = 0;
                long timeNow = System.currentTimeMillis();
                do {
                    inFinalLength = is.read(FinalData, 0, 2);
                    inFinalLength = is.read(FinalData, 0, 5);
                    inFinalLength = is.read(FinalData);
                } while (inFinalLength <= 0 && (System.currentTimeMillis() - timeNow <= 60000));

            }
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        } catch (UnknownHostException e) {
            Log.i(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }

    public int CreateTLVFields(int inTag, String stValue) {
        byte[] byValue = new byte[2000];
        int inposition = 0;

        Log.i(TAG,"CreateTLVFields_1 : ");
        byValue[0] = (byte) (inTag / 256);// 0x00;
        Log.i(TAG,"CreateTLVFields_2 : ");
        byValue[1] = (byte) (inTag % 256);
        Log.i(TAG,"CreateTLVFields_3: ");
        inposition = inposition + 2;
        Log.i(TAG,"CreateTLVFields_4 : ");
        byValue[2] = (byte) (stValue.length() / 256);
        Log.i(TAG,"CreateTLVFields_5 : ");
        byValue[3] = (byte) (stValue.length() % 256);
        Log.i(TAG,"CreateTLVFields_6 : ");
        inposition = inposition + 2;
        Log.i(TAG,"CreateTLVFields_7 : ");

        byte[] bytemp1 = stValue.getBytes();
        Log.i(TAG,"CreateTLVFields_8 : ");
        for (int i = 0; i < stValue.length(); i++) {
            byValue[i + 4] = bytemp1[i];
        }
        Log.i(TAG,"CreateTLVFields_9 : ");
        inposition = inposition + stValue.length();
        Log.i(TAG,"CreateTLVFields_10 : ");

        for (int i = 0; i < inposition; i++) {
            FinalData[inFinalLength + i] = byValue[i];
        }
        Log.i(TAG,"CreateTLVFields_11 : ");
        inFinalLength = inFinalLength + inposition;
        Log.i(TAG,"CreateTLVFields_12 : ");
        return inposition;
    }
}
