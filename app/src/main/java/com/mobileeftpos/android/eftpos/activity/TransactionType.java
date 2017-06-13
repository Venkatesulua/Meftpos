package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.mobileeftpos.android.eftpos.SupportClasses.BluetoothUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.ESCUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
    private static DBHelper databaseObj;
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
        databaseObj = new DBHelper(TransactionType.this);
        context = TransactionType.this;
        barCodeValue=null;
        isFromBarcodeScanner=false;
        isFromBarcodeScanner=true;
        Log.i(TAG,"Trans_Type_onCreate_1");
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

        Log.i(TAG,"onActivityResult");
        if (requestCode == MenuConstants.QRCODE.BARCODE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG,"onActivityResult_2");
            String contents = intent.getStringExtra(MenuConstants.QRCODE.BARCODE_INTENT_RESULT_KEY);
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
        Log.i(TAG,"Trans_Type:onResume");
        if(isFromBarcodeScanner){
            if(barCodeValue!=null && barCodeValue.length()>0) {
                processBarcode(barCodeValue);
            }

        }
    }

    private void processBarcode(String contents) {
        Log.i(TAG,"Trans_Type:processBarcode");
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

                Log.i(TAG,"Trans_Type:AsyncTaskRunner");
                processRequest(params[0],params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            Log.i(TAG,"Trans_Type:onPostExecute");
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            Log.i(TAG,"Trans_Type:onPreExecute");
            progressDialog = ProgressDialog.show(TransactionType.this,
                    "Payment Processing",
                    "Please wait...");
            progressDialog.show();
        }

    }

    private void processRequest(String ServerIP,String Port) {

        Log.i(TAG,"processRequest_1");
        isoMsg.setPackager(packager);
        int inPhase=0;

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String stDate = dateFormat.format(date);
        TransactionDetails.trxDateTime=stDate;
        //Password Entry
        while(true) {
            switch(inPhase++)
            {
                case 0://Validation; in force settlement;
                    Log.i(TAG,"processRequest_2");
                    if (trDetails.inSortPAN(databaseObj) == 1) {
                        Log.i(TAG,"ERROR in SORTPAN");
                        return ;
                    }
                    break;
                case 1://
                    inCreatePacket();
                    break;
                case 2://
                    if (inConnection(ServerIP, Port) != 0) {
                        return;
                    }
                    break;
                case 3://
                    if (inSendRecvPacket() != 0) {
                        return;
                    }
                    break;
                case 4://
                    if (inProcessPacket() != 0) {
                        return;
                    }

                    if (inDisconnection() != 0) {
                        return;
                    }
                    break;
                case 5://
                    //save Record
                    vdSaveRecord();
                    break;
                case 6://
                    //Print receipt
                    Log.i(TAG, "\nPrinting:");
                    inPrintReceipt();
                    break;
                case 7://Show the receipt in the display and give option to print or email
                    startActivity(new Intent(TransactionType.this, HomeActivity.class));
                    return;
                    //break;
            }
        }
    }

    private int inCreatePacket() {
        String stde27 = "";
        String stde29 = "";
        inFinalLength=0;


        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);

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
                    Log.i(TAG,"PARTNER_ID::"+barcode.getPARTNER_ID());
                    Log.i(TAG,"SELLER_ID::"+barcode.getSELLER_ID());
                    Log.i(TAG,"REGION_CODE::"+barcode.getREGION_CODE());

                    Log.i(TAG,"TransactionDetails.trxAmount::"+TransactionDetails.trxAmount);
                    Log.i(TAG,"getHDT_TERMINAL_ID::"+hostData.getHDT_TERMINAL_ID());
                    Log.i(TAG,"getMERCHANT_NAME::"+merchantData.getMERCHANT_NAME());


                    CreateTLVFields(1, Constants.MTI.Financial);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest);
                    CreateTLVFields(4, barcode.getPARTNER_ID());
                    CreateTLVFields(5,barcode.getSELLER_ID());


                    CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"));
                    CreateTLVFields(9,currModel.getCURR_LABEL());
                    CreateTLVFields(10,TransactionDetails.trxAmount);
                    CreateTLVFields(11,trDetails.getPAN());
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID());

                    String staddinfo ="merchant_name:";
                    staddinfo = staddinfo + merchantData.getMERCHANT_NAME();
                    staddinfo = staddinfo + ",merchant_no:";
                    staddinfo = staddinfo + hostData.getHDT_MERCHANT_ID();
                    staddinfo = staddinfo + ",business_no:";
                    staddinfo = staddinfo + "0";
                    staddinfo = staddinfo + ",terminal_id:";
                    staddinfo = staddinfo + hostData.getHDT_TERMINAL_ID();
                    staddinfo = staddinfo + ",mcc:";
                    staddinfo = staddinfo + "0";
                    staddinfo = staddinfo + ",region_code:";
                    staddinfo = staddinfo + barcode.getREGION_CODE();
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
        if(TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE)
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
            Log.i(TAG,"\ninProcessPacket_1:");
            if(globalVar.getGTransactionType() != Constants.TransType.ALIPAY_SALE) {
                isoMsg.unpack(FinalData);
                // print the DE list
                logISOMsg(isoMsg);
            }else{
                Log.i(TAG,"\ninProcessPacket_2:");
                inParceAlipyResponse(FinalData,inFinalLength);

                Log.i(TAG,"\ninProcessPacket_3:");
                Log.i(TAG,"\nTerminal-ID:"+trDetails.getResTerminalId());

                if(!trDetails.getResTerminalId().equals("10000008")){
                    Log.i(TAG,"\ninProcessPacket_4:");
                    return 0;
                }
            }
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

                result = "";
                for (int k = 0; k < inFinalLength; k++) {
                    result = result + String.format("%02x", FinalData[k]);
                }
                Log.i(TAG,"\nRECEIVED:");
                Log.i(TAG,result);
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

    int inParceAlipyResponse(byte[] input,int inLen){

        int incurrentposition = 0;
        String stTag;
        String stLen;
        int inTag = 0;
        int inLength = 0;
        //byte[] chtemp = new byte[1024];
        //for (int i = 0; i < 5; i++) {
            //chtemp[i] = input[i];
        //}
        //tpdu = new String(chtemp);
        //incurrentposition = 5;

        while (true) {
            inTag = (input[incurrentposition] * 256) + ((input[incurrentposition + 1]));
            incurrentposition = incurrentposition + 2;
            inLength = (input[incurrentposition] * 256) + ((input[incurrentposition + 1]));
            incurrentposition = incurrentposition + 2;
            byte[] chtemp = new byte[inLength];
            Log.i(TAG,"inTag actual Len Initially :: "+chtemp.length);
            for (int lp = 0; lp < inLength; lp++) {
                chtemp[lp] = input[incurrentposition + lp];
            }
            incurrentposition = incurrentposition + inLength;

            Log.i(TAG,"inTag :: "+inTag);
            Log.i(TAG,"inTag_inLength :: "+inLength);
            Log.i(TAG,"inTag actual Len :: "+chtemp.length);
            Log.i(TAG,"inTag actual Value :: "+chtemp);

            switch (inTag) {
                case 1:// Message type
                    trDetails.setMessageType( new String(chtemp));
                    break;
                case 3:// Processing code
                    trDetails.setProcessingCode( new String(chtemp));
                    break;
                case 4:// partner id
                    trDetails.setPartnerId( new String(chtemp));
                    break;
                case 5:// sellerid
                    trDetails.setSellerId( new String(chtemp));
                    break;
                case 8:// partnertransid
                    trDetails.setPartnerTransId( new String(chtemp));
                    break;
                case 9:// currency
                    trDetails.setCurrency( new String(chtemp));
                    break;
                case 0x0a:// paymentamount
                    //trDetails.settrxAmount( new String(chtemp));
                    break;
                case 0x0b:// buyerid
                    //trDetails.setBuyerId( new String(chtemp));
                    break;
                case 0x0c:// refundid
                    trDetails.setRefundId( new String(chtemp));
                    break;
                case 0x0d:// refundreason
                    trDetails.setRefundReason( new String(chtemp));
                    break;
                case 0x26:// alipaytransid
                    trDetails.setAlipayTransId( new String(chtemp));
                    break;
                case 0x27:// responsecode
                    trDetails.setResponseCode( new String(chtemp));
                    break;
                case 0x28:// responsemesage
                    trDetails.setRefundReason( new String(chtemp));
                    break;
                case 0x29:// terminalid
                    trDetails.setResTerminalId( new String(chtemp));
                    break;

            }
            if (incurrentposition >= inLen)
                break;

        }
        return 0;
    }

    void vdSaveRecord(){

    }
    private void inPrintReceipt(){
        Log.i(TAG,"\nPRINTING-BUILDING:");
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
            Log.i(TAG,"\nPRINTING-BUILDING_ERR!:");
            return;
        }
        Log.i(TAG,"\nPRINTING-BUILDING_2:");
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!",
                    Toast.LENGTH_LONG).show();
            Log.i(TAG,"\nPRINTING-BUILDING:_ERR@");
            return;
        }
        Log.i(TAG,"\nPRINTING-BUILDING_2:");

        Log.i(TAG,"\nPRINTING-BUILDING_STRING STRING:");
        // 3: Generate a order data
        byte[] data = generateMockData1();
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            Log.i(TAG,"\nPRINTING-BUILDING_23:");
            BluetoothUtil.sendData(data, socket);
            Log.i(TAG,"\nPRINTING-BUILDING_3:");
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    Log.i(TAG,"\nPRINTING-BUILDING:___ERRR#");
                    e1.printStackTrace();
                }
            }
        }
        return;
    }

    private byte[] generateMockData1() {
        String inPrintBuffer="";
        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);

        Log.i(TAG,"generateMockData1 getHDT_TERMINAL_ID::"+hostData.getHDT_TERMINAL_ID());
        Log.i(TAG,"generateMockData1 getMERCHANT_NAME::"+merchantData.getMERCHANT_NAME());


        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "The menu（launch）**wanda plaza".getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] Focus = "Web 507".getBytes("gb2312");
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);

            byte[] left = ESCUtil.alignLeft();
            byte[] orderSerinum = "Order No.11234".getBytes("gb2312");
            boldOn = ESCUtil.boldOn();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] FocusOrderContent = "Big hamburger(single)".getBytes("gb2312");
            boldOff = ESCUtil.boldOff();
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

            next2Line = ESCUtil.nextLine(2);

            byte[] priceInfo = "Receivable:$22  Discount：$2.5 ".getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);

            byte[] priceShouldPay = "Actual collection:$19.5".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);

            byte[] takeTime = "Pickup time:2015-02-13 12:51:59".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] setOrderTime = "Order time：2015-02-13 12:35:15".getBytes("gb2312");

            byte[] tips_1 = "Follow twitter\"**\"order for $1 discount".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] tips_2 = "Commentary reward 50 cents".getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(center, "UTF-8")+new String(boldOn, "UTF-8")+new String(fontSize1Big, "UTF-8");
            inPrintBuffer = inPrintBuffer + merchantData.getMERCHANT_NAME()+new String(boldOff, "UTF-8");

            if(merchantData.getMERCHANT_HEADER1().length() !=0) {
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getMERCHANT_HEADER1();
            }
            if(merchantData.getMERCHANT_HEADER2().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getMERCHANT_HEADER2();
            }
            if(merchantData.getADDRESS_LINE1().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getADDRESS_LINE1();
            }if(merchantData.getADDRESS_LINE2().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getADDRESS_LINE2();
            }
            if(merchantData.getADDRESS_LINE3().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getADDRESS_LINE3();
            }
            if(merchantData.getADDRESS_LINE4().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantData.getADDRESS_LINE4();
            }
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(center, "UTF-8")+new String(boldOn, "UTF-8")+new String(fontSize1Big, "UTF-8");
            inPrintBuffer = inPrintBuffer + "ALIPAY SALE" + new String(boldOff, "UTF-8");
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            String dateTime = "DATE/TIME : " + TransactionDetails.trxDateTime.substring(2,4)+"/"+TransactionDetails.trxDateTime.substring(4,6)+"/"+TransactionDetails.trxDateTime.substring(6,8)+" "+
                    TransactionDetails.trxDateTime.substring(8,10) +":"+TransactionDetails.trxDateTime.substring(10,12)+":"+TransactionDetails.trxDateTime.substring(12,14);
            inPrintBuffer = inPrintBuffer + dateTime;
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + "MID:" + hostData.getHDT_MERCHANT_ID()+" "+"TID:"+hostData.getHDT_TERMINAL_ID();
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+" ";
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + "BUYER IDENTITY CODE\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + TransactionDetails.PAN;
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + "PTNR TRANS ID:"+TransactionDetails.trxDateTime  + "\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ TransactionDetails.trxDateTime + "00";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + "PARTNER ID   :"+ trDetails.getPartnerId()+"\n";
           // inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getPartnerId();
           // inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer +"SELLER ID    :"+ trDetails.getSellerId()+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getSellerId();
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer +"ALIPAY ID    :"+ trDetails.getAlipayTransId()+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getAlipayTransId();
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+" ";
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(boldOn, "UTF-8")+new String(fontSize1Big, "UTF-8");
            int inAmount = Integer.parseInt(TransactionDetails.trxAmount);

            Log.i(TAG,"inAmount:"+inAmount);

            String stAmount = String.format("%01d.%02d", (inAmount/100),(inAmount%100));
            Log.i(TAG,"stAmount:"+stAmount);

            inPrintBuffer = inPrintBuffer + "TOTAL SGD " + stAmount + new String(boldOff, "UTF-8");
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
            inPrintBuffer = inPrintBuffer + "MERCHANT COPY"+new String(next4Line, "UTF-8")+new String(breakPartial, "UTF-8");

            return inPrintBuffer.getBytes();
            //return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
