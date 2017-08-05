package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.app.EftposApp;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.AlipayModel;
import com.mobileeftpos.android.eftpos.db.AlipayModelDao;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.BatchModelDao;
import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardBinModelDao;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModelDao;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.CommsModelDao;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.CurrencyModelDao;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.EthernetModel;
import com.mobileeftpos.android.eftpos.db.EthernetModelDao;
import com.mobileeftpos.android.eftpos.db.EzlinkModel;
import com.mobileeftpos.android.eftpos.db.EzlinkModelDao;
import com.mobileeftpos.android.eftpos.db.HTTModel;
import com.mobileeftpos.android.eftpos.db.HTTModelDao;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.HostModelDao;
import com.mobileeftpos.android.eftpos.db.LimitModel;
import com.mobileeftpos.android.eftpos.db.LimitModelDao;
import com.mobileeftpos.android.eftpos.db.MaskingModel;
import com.mobileeftpos.android.eftpos.db.MaskingModelDao;
import com.mobileeftpos.android.eftpos.db.MerchantModel;
import com.mobileeftpos.android.eftpos.db.MerchantModelDao;
import com.mobileeftpos.android.eftpos.db.PasswordModel;
import com.mobileeftpos.android.eftpos.db.PasswordModelDao;
import com.mobileeftpos.android.eftpos.db.ReceiptModel;
import com.mobileeftpos.android.eftpos.db.ReceiptModelDao;
import com.mobileeftpos.android.eftpos.db.ReportModel;
import com.mobileeftpos.android.eftpos.db.ReportModelDao;
import com.mobileeftpos.android.eftpos.db.TraceModel;
import com.mobileeftpos.android.eftpos.db.TraceModelDao;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModelDao;
import com.mobileeftpos.android.eftpos.db.UtilityModel;
import com.mobileeftpos.android.eftpos.db.UtilityModelDao;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.EFTPOSLog;
import com.mobileeftpos.android.eftpos.utils.NetworkTypes;

import org.greenrobot.greendao.query.QueryBuilder;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Prathap on 5/21/17.
 */

public class AdminActivity extends Activity {

    private EditText appName, terminalId;
    private Spinner connectionType, connectionTo;
    private Button clearBtn, submitBtn;
    private TextView backBtn;
    double progressValue=0;
    private ProgressDialog progress;
    private final String TAG = AdminActivity.class.getSimpleName();
    private GlobalVar globalVar = new GlobalVar();
    private byte[] FinalData = new byte[1512];
    private int inFinalLength = 0;
    private Socket smtpSocket = null;
    private ISOPackager1 packager = new ISOPackager1();
    private ISOMsg isoMsg = new ISOMsg();
    public static Activity context;
    private String connectToStr;
    private int TIME_OUT = 1000;
    private DaoSession daoSession;
    private List<HostModel> hostModelList;
    private List<BatchModel> batchModelList;
    private HostModelDao hostModelDao;
    private BatchModelDao batchModelDao;
    private CardTypeModelDao cttModelDao;
    private CommsModelDao comModelDao;
    private CurrencyModelDao currModelDao ;
    private EthernetModelDao ethernetModelDao ;
    private EzlinkModelDao ezlinkModelDao ;
    private HTTModelDao hostTransModelDao ;
    private LimitModelDao limitModelDao;
    private MaskingModelDao maskModelDao ;
    private AlipayModelDao alipayModelDao ;
    private MerchantModelDao merchantModelDao ;
    private ReceiptModelDao receiptModelDao ;
    private ReportModelDao reportModelDao ;
    private TransactionControlModelDao transctrlModelDao ;
    private UtilityModelDao utilityModelDao ;
    private TraceModelDao traceModelDao;
    private PasswordModelDao pwdModelDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_adminlayout);
        daoSession = ((EftposApp) getApplication()).getDaoSession();
        context = AdminActivity.this;
        initView();

    }

    //Initializing the view Components
   private void initView(){

       appName = (EditText) findViewById(R.id.etappname);
       terminalId = (EditText) findViewById(R.id.etterminalid);
       connectionType = (Spinner) findViewById(R.id.etconnectiontype);
       connectionTo = (Spinner) findViewById(R.id.etconnectionto);
       clearBtn = (Button) findViewById(R.id.btnClear);
       submitBtn = (Button) findViewById(R.id.btnSubmit);
       backBtn = (TextView) findViewById(R.id.backBtn);

       submitBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               MMSDownload();
           }
       });

       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

       connectionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               setConnections();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

   }

   //Loading database dao objets
   private void loadDaoObjects(){
       pwdModelDao = daoSession.getPasswordModelDao();
       cttModelDao = daoSession.getCardTypeModelDao();
       comModelDao = daoSession.getCommsModelDao();
       currModelDao = daoSession.getCurrencyModelDao();
       ethernetModelDao = daoSession.getEthernetModelDao();
       ezlinkModelDao = daoSession.getEzlinkModelDao();
       hostTransModelDao = daoSession.getHTTModelDao();
       limitModelDao = daoSession.getLimitModelDao();
       maskModelDao = daoSession.getMaskingModelDao();
       alipayModelDao = daoSession.getAlipayModelDao();
       merchantModelDao = daoSession.getMerchantModelDao();
       receiptModelDao =daoSession.getReceiptModelDao();
       reportModelDao = daoSession.getReportModelDao();
       transctrlModelDao = daoSession.getTransactionControlModelDao();
       utilityModelDao = daoSession.getUtilityModelDao();
       traceModelDao=daoSession.getTraceModelDao();
       GreenDaoSupport.deleteAllTablesData(AdminActivity.this);
   }


   // Initializing connection for connecting terminal
   private void setConnections(){

       if (!connectionType.getSelectedItem().toString().equalsIgnoreCase("PHONELINE")) {

           final ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_spinner_item, AppUtil.loadNetworks());
           localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           localAdapter.notifyDataSetChanged();
           connectionTo.setAdapter(localAdapter);
       } else {

           List<String> list = new ArrayList<String>();
           list.add("No Options");
           final ArrayAdapter<String> localAdapter = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_spinner_item, list);
           localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           localAdapter.notifyDataSetChanged();
           connectionTo.setAdapter(localAdapter);
       }

   }

   //Downloading all required data for transactions
   private void MMSDownload(){

       if (connectionTo.getSelectedItem().toString().equals("DIRECT 8585") && (connectionType.getSelectedItem().toString().equals("3G/GPRS") || connectionType.getSelectedItem().toString().equals("WIFI"))) {
           if (((appName.getText().toString().length() > 0) && (terminalId.getText().toString().length() > 0) &&
                   (connectionType.getSelectedItem().toString().length() > 0) && (!connectionTo.getSelectedItem().toString().equalsIgnoreCase("No Options")) && (connectionTo.getSelectedItem().toString().length() > 0))) {
               int BatchPresent = 0;
                HostModel hostModel = new HostModel();
               hostModelList = new ArrayList<>();
               hostModelDao = daoSession.getHostModelDao();
               batchModelDao = daoSession.getBatchModelDao();
               hostModelList.addAll(hostModelDao.loadAll());
               for (int i = 0; i < hostModelList.size(); i++) {
                   hostModel = hostModelList.get(i);
                   if (!(hostModel == null || hostModel.equals(""))) {
                       if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {

                           //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                           //Increment Batch Number
                           TransactionDetails.inGHDT = Integer.parseInt(hostModel.getHDT_HOST_ID());
                           QueryBuilder<BatchModel> qb = batchModelDao.queryBuilder();
                           qb.where(BatchModelDao.Properties.Hdt_index.eq(Integer.toString(TransactionDetails.inGHDT)));
                           batchModelList = qb.list();
                           if (batchModelList.size() >0) {
                               BatchPresent = 1;
                               break;
                           }

                       }
                   }
               }
               if (BatchPresent == 1) {
                   TransactionDetails.responseMessge = "PLZ SETTLE FIRST";
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(AdminActivity.this, PaymentFailure.class);
                           startActivity(intent);
                       }
                   }, TIME_OUT);
               } else
                   new AsyncTaskRunner().execute();
           } else {
               Toast.makeText(AdminActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
           }
       }
       else{
           Toast.makeText(AdminActivity.this,"COMM NOT SUPPORTED",Toast.LENGTH_SHORT).show();
       }
   }

   // Validating input params to process download
   private boolean validateInputParams(){
       if(((appName.getText().toString().length() > 0) && (terminalId.getText().toString().length() > 0) &&
               (connectionType.getSelectedItem().toString().length() > 0) && (connectionTo.getSelectedItem().toString().length() > 0))){
           return true;
       }else{
           return false;
       }

   }


   // Async background task in downloading data
    private String processRequest() {

        loadDaoObjects();
        //Validation for null
        if (validateInputParams()) {
            globalVar.tmsParam.setTMS_APPLICATION(appName.getText().toString());
            globalVar.tmsParam.setTMS_FILENAME(terminalId.getText().toString());
            globalVar.tmsParam.setTMS_TERMINAL_ID(terminalId.getText().toString());
            globalVar.tmsParam.setTMS_CONNECTION_TYPE(connectionType.getSelectedItem().toString());
            connectToStr=connectionTo.getSelectedItem().toString();

            if (connectToStr.equalsIgnoreCase(NetworkTypes.M2M_NETWORK)) {
                connectToStr = "202.160.225.218|15525";

            } else if (connectToStr.equalsIgnoreCase(NetworkTypes.PUBLIC_NETWORK)) {
                connectToStr = "203.125.211.121|15443";

            } else if (connectToStr.equalsIgnoreCase(NetworkTypes.OFFICE_NETWORK)) {
                connectToStr = " 192.168.0.2|8589";

            } else if (connectToStr.equalsIgnoreCase(NetworkTypes.DIRECT_NETWORK)) {
                connectToStr = "203.125.11.228|8585";

            } else if (connectToStr.equalsIgnoreCase(NetworkTypes.CLOUD_NETWORK)) {
                connectToStr = "52.163.63.18|8589";

            }


            globalVar.tmsParam.setTMS_CONNECTION_TO(connectToStr);
            globalVar.TmsData = "";
            isoMsg.setPackager(packager);
            TransactionDetails.trxType = (Constants.TransType.TMS_INITIAL_PACKET);
            boolean msg_ok = false;
            while (true) {
                if (TransactionDetails.trxType == Constants.TransType.TMS_SUBSEQUENT_PACKET) {
                    if (globalVar.getGTotalNumberofTMSparts() == globalVar.getGPartToDownload()) {
                        msg_ok = true;
                        break;
                    } else {
                        double a=globalVar.getGPartToDownload();
                        double b=globalVar.getGTotalNumberofTMSparts();
                        double c=(a/b)*100;
                        // increase counter to download next part
                        progressValue=progressValue+c;
                        final int percent=(int)Math.round(c);
                        AdminActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.setProgress(percent);
                                progress.incrementProgressBy(percent);
                            }
                        });
                        int inGPartToDownload = globalVar.getGPartToDownload();
                        inGPartToDownload++;
                        globalVar.setGPartToDownload(inGPartToDownload);
                    }
                }

                inCreatePacket();
                int indexOffset = connectToStr.indexOf("|");
                String ServerIP = connectToStr.substring(0,indexOffset);
                String Port = connectToStr.substring(indexOffset+1);
                // Connection to Host
                if (inConnection(ServerIP,Integer.parseInt(Port)) != 0) {
                    return "FAILED";
                }

                if (inSendRecvPacket() != 0) {
                    return "FAILED";
                }

                if (inProcessPacket() != 0) {
                    return "FAILED";
                }

                String stTemp = isoMsg.getString(39);
                if (stTemp.equals("00")) {
                    if (TransactionDetails.trxType == Constants.TransType.TMS_INITIAL_PACKET) {
                        globalVar.setGPartToDownload(0);
                        int inret = vdProcessDownloadResponse(Constants.TransType.TMS_INITIAL_PACKET);
                        if (inret == Constants.TMSReturnValues.TMS_RESPONSE_DL_NOT_REQUIRED) {
                            // save the date of when we last check for parameter
                            // updates
                            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date date = new Date();
                            String stDate = dateFormat.format(date).substring(2);
                            globalVar.setLastCheckedDate(stDate);
                            // todo save into file
                            // Nothing to download
                            globalVar.setGTotalNumberofTMSparts(0);
                            break;

                            // goto lblNoChanges;
                        } else if (inret == Constants.TMSReturnValues.TMS_RESPONSE_DL_REQUIRED) {
                            //globalVar.setGTransactionType(MenuConstants.TransType.TMS_SUBSEQUENT_PACKET);
                            TransactionDetails.trxType = Constants.TransType.TMS_SUBSEQUENT_PACKET;

                        } else {
                            msg_ok = false;
                            break;
                        }
                    } else {
                        int inret = vdProcessDownloadResponse(Constants.TransType.TMS_SUBSEQUENT_PACKET);
                        if (inret != Constants.TMSReturnValues.TMS_RESPONSE_OK) {
                            msg_ok = false;
                            break;
                        }
                    }
                }
                if (inDisconnection() != 0) {
                    return "FAILED";
                }
            }
            if (inDisconnection() != 0) {
                return "FAILED";
            }

            if (msg_ok == true) {
                // Parce the TMS data and keep the data in the DATABASE.
                EFTPOSLog.d(TAG, "FInal Output:\n");
                EFTPOSLog.d(TAG, globalVar.TmsData);
                vdStoreParameters();
                return "OK";
            }


        }else{
            EFTPOSLog.d(TAG, "ERROR MISSING TMS PARAMETERS");
            return "FAILED";
        }


        return "FAILED";
    }


    //vdStoreParameters
    void vdStoreParameters() {
        int l_iNumParams = 0;
        int inCounter = 0;
        int tms_offset = 0;
        int param, value;
        String stParam = "", stValue = "";

        int inindex = globalVar.TmsData.indexOf("NUMBER_OF_LINES=");
        String stLines = globalVar.TmsData.substring(inindex + ("NUMBER_OF_LINES=".length()));
        l_iNumParams = Integer.parseInt(stLines);

     

         PasswordModel pwdModel = new PasswordModel();
         HTTModel hostTransModel = new HTTModel();
        LimitModel limitModel = new LimitModel();
        MaskingModel maskModel = new MaskingModel();
         MerchantModel merchantModel = new MerchantModel();
        ReceiptModel receiptModel = new ReceiptModel();
        ReportModel reportModel = new ReportModel();
        TransactionControlModel transctrlModel = new TransactionControlModel();
        UtilityModel utilModel = new UtilityModel();



        EFTPOSLog.d(TAG, "DELETE ALL THE CONTENT FROM  THE FILE");



        for (inCounter = 0; inCounter < l_iNumParams; inCounter++) {
            CardBinModel cbinModel = new CardBinModel();
            CardTypeModel cttModel = new CardTypeModel();
            CommsModel comModel = new CommsModel();
            CurrencyModel currModel = new CurrencyModel();
            EthernetModel ethernetModel = new EthernetModel();
            EzlinkModel ezlinkModel = new EzlinkModel();
            HostModel hostModel = new HostModel();
            AlipayModel alipayModel =new AlipayModel();
            TraceModel traceModel = new TraceModel();

            CardBinModelDao cbinModelDao = daoSession.getCardBinModelDao();
            globalVar.TmsData = globalVar.TmsData.substring(tms_offset);
            param = globalVar.TmsData.indexOf('=');
            value = globalVar.TmsData.indexOf(0x0A);

            stParam = globalVar.TmsData.substring(0, param);
            stValue = globalVar.TmsData.substring(param + 1, value);
            tms_offset = value + 1;

            EFTPOSLog.d(TAG, "Param:" + stParam);
            EFTPOSLog.d(TAG, "Value:" + stValue);

            if (stParam.length() == 2) {

            }
            if (stParam.equals("TON"))
                merchantModel.setMERCHANT_NAME(stValue);
            else if (stParam.equals("HDRA"))
                merchantModel.setMERCHANT_HEADER1(stValue);
            else if (stParam.equals("HDRB"))
                merchantModel.setMERCHANT_HEADER2(stValue);
            else if (stParam.equals("HDR1"))
                merchantModel.setADDRESS_LINE1(stValue);
            else if (stParam.equals("HDR2"))
                merchantModel.setADDRESS_LINE2(stValue);
            else if (stParam.equals("HDR3"))
                merchantModel.setADDRESS_LINE3(stValue);
            else if (stParam.equals("HDR4"))
                merchantModel.setADDRESS_LINE4(stValue);
            else if (stParam.equals("FTR1"))
                merchantModel.setMERCHANT_FOOTER1(stValue);
            else if (stParam.equals("FTR2"))
                merchantModel.setMERCHANT_FOOTER2(stValue);
            else if (stParam.equals("DPW")) {
                pwdModel.setDEFAULT_PASSWORD(stValue);
            } else if (stParam.equals("RPW")) {
                pwdModel.setREFUND_PASWORD(stValue);
            } else if (stParam.equals("TPW")) {
                pwdModel.setTIP_ADJUST_PASSWORD(stValue);
            } else if (stParam.equals("APW")) {
                pwdModel.setPRE_AUTH_PASSWORD(stValue);
            } else if (stParam.equals("QPW")) {
                pwdModel.setBALANCE_PASSWORD(stValue);
            } else if (stParam.equals("OPW")) {
                pwdModel.setOFFLINE_PASSWORD(stValue);
            } else if (stParam.equals("SPW")) {
                pwdModel.setSETTLEMENT_PASSWORD(stValue);
            } else if (stParam.equals("EPW")) {
                pwdModel.setEDITOR_PASSWORD(stValue);
            } else if (stParam.equals("VPW")) {
                pwdModel.setVOID_PASSWORD(stValue);
            } else if (stParam.equals("MPW")) {
                pwdModel.setMANUAL_ENTRY_PASSWORD(stValue);
            } else if (stParam.equals("HPW")) {
                pwdModel.setCASH_ADVANCED_PASSWORD(stValue);
            } else if (stParam.equals("ZPW")) {
                pwdModel.setTERMINAL_POWERON_PASSWORD(stValue);
            } else if (stParam.equals("VOIDCTRL")) {
                transctrlModel.setVOID_CTRL(stValue);
            } else if (stParam.equals("SETTCTRL")) {
                transctrlModel.setSETTLEMENT_CTRL(stValue);
            } else if (stParam.equals("SALECTRL")) {
                transctrlModel.setSALE_CTRL(stValue);
            } else if (stParam.equals("AUTHC")) {
                transctrlModel.setAUTH_CTRL(stValue);
            } else if (stParam.equals("RFND")) {
                transctrlModel.setREFUND_CTRL(stValue);
            } else if (stParam.equals("ADJS")) {
                transctrlModel.setADJUSTMENT_CTRL(stValue);
            } else if (stParam.equals("OFFL")) {
                transctrlModel.setOFFLINE_CTRL(stValue);
            } else if (stParam.equals("ME")) {
                transctrlModel.setMANUAL_ENTRY_CTRL(stValue);
            } else if (stParam.equals("BAL")) {
                transctrlModel.setBALANCE_CTRL(stValue);
            } else if (stParam.equals("ADV")) {
                transctrlModel.setCASH_ADVANCE_CTRL(stValue);
            } else if (stParam.equals("PTR")) {
                transctrlModel.setPURCHASE_TIP_REQUEST_CTRL(stValue);
            } else if (stParam.equals("TIP")) {
                transctrlModel.setTIP_CTRL(stValue);
            } else if (stParam.equals("SDR")) {
                reportModel.setDETAILED_REPORT(stValue);
            } else if (stParam.equals("SIR")) {
                reportModel.setTIP_REPORT(stValue);
            } else if (stParam.equals("STR")) {
                reportModel.setTOTAL_REPORT(stValue);
            } else if (stParam.equals("TRMODE")) {
                hostTransModel.setTRANSMISSION_MODE(stValue);
            } else if (stParam.equals("CT")) {
                hostTransModel.setCONNECTION_TIMEOUT(stValue);
            } else if (stParam.equals("REDL")) {
                hostTransModel.setREDIAL_TIMEOUT(stValue);
            } else if (stParam.equals("PABX")) {
                hostTransModel.setPABX(stValue);
            } else if (stParam.equals("MODEM_STRING")) {
                hostTransModel.setMODEM_STRING(stValue);
            } else if (stParam.equals("PRINT_TIMEOUT")) {
                receiptModel.setPRINT_TIMEOUT(stValue);
            } else if (stParam.equals("AUTO_PRINT")) {
                receiptModel.setAUTO_PRINT(stValue);
            } else if (stParam.equals("PRINTER_INTENSITY_CFG")) {
                receiptModel.setPRINTER_INTENSITY_CFG(stValue);
            } else if (stParam.equals("PRINTER_CFG")) {
                receiptModel.setPRINTER_CFG(stValue);
            } else if (stParam.equals("PRINTER_CFG")) {
                receiptModel.setPRINTER_CFG(stValue);
            } else if (stParam.equals("DR_PAN_UNMASK")) {
                maskModel.setDR_PAN_UNMASK(stValue);
            } else if (stParam.equals("DR_EXP_UNMASK")) {
                maskModel.setDR_EXP_UNMASK(stValue);
            } else if (stParam.equals("DISPLAY_UNMASK")) {
                maskModel.setDISPLAY_UNMASK(stValue);
            } else if (stParam.equals("MAXIMUM_SALE")) {
                limitModel.setMAXIMUM_SALE_AMOUNT(stValue);
            } else if (stParam.equals("MAXIMUM_OFFLINE")) {
                limitModel.setMAXIMUM_OFFLINE_AMOUNT(stValue);
            } else if (stParam.equals("MAXIMUM_PREAUTH")) {
                limitModel.setMAXIMUM_PREAUTH_AMOUNT(stValue);
            } else if (stParam.equals("MAXIMUM_REFUND")) {
                limitModel.setMAXIMUM_REFUND_AMOUNT(stValue);
            } else if (stParam.equals("ADD1")) {
                utilModel.setADDITIONAL_PROMPT(stValue);
            } else if (stParam.equals("BDTE")) {
                utilModel.setDAILY_SETTLEMENT_FLAG(stValue);
            } else if (stParam.equals("LFDC")) {
                utilModel.setLAST_4_DIGIT_PROMPT_FLAG(stValue);
            } else if (stParam.equals("2INS")) {
                utilModel.setINSERT_2_SWIPE(stValue);
            } else if (stParam.equals("PIGGY")) {
                utilModel.setPIGGYBACK_FLAG(stValue);
            } else if (stParam.equals("PINBY")) {
                utilModel.setPINBYPASS(stValue);
            } else if (stParam.equals("AUTO_SETTLE")) {
                utilModel.setAUTO_SETTLE_TIME(stValue);
            } else if (stParam.equals("UTRN")) {
                utilModel.setUTRN_PREFIX(stValue);
            } else if (stParam.equals("ST")) {
                traceModel.setSYSTEM_TRACE(stValue);
                traceModelDao.insert(traceModel);
            } else if (stParam.equals("DAPP")) {
                utilModel.setDEFAULT_APPROVAL_CODE(stValue);
            } else if (stParam.contains("HDT")) {

                EFTPOSLog.d(TAG, "HOST ID::" + stParam.substring(3, 5));
                hostModel.setHDT_HOST_ID(stParam.substring(3, 5)+"");


                inindex = stValue.indexOf(' ');
                hostModel.setHDT_HOST_ENABLED(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_COM_INDEX(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_REFERRAL_NUMBER(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_TERMINAL_ID(stValue.substring(0, inindex)+"");
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_MERCHANT_ID(stValue.substring(0, inindex)+"");
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_TPDU(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_BATCH_NUMBER(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_INVOICE_NUMBER(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_PROCESSING_CODE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_HOST_TYPE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_HOST_LABEL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_MANUAL_ENTRY_FLAG(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_REVERSAL_FLAG(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_SETTLEMENT_FLAG(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_BATCH_MAX_TOTAL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_BATCH_STL_LAST(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_BATCH_CURR_TOTAL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_BATCH_NO_TRANS(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_DESCRIPTION(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_PAY_TERM(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_PEK(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_MEK(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_MAC_INDEX(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                hostModel.setHDT_CUSTOM_OPTIONS(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_CURR_INDEX(stValue);
                    else
                        hostModel.setHDT_CURR_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_PIGGYBACK_FLAG(stValue);
                    else
                        hostModel.setHDT_PIGGYBACK_FLAG(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_MINIMUM_AMT(stValue);
                    else
                        hostModel.setHDT_MINIMUM_AMT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_RATE(stValue);
                    else
                        hostModel.setHDT_RATE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_REDIRECT_IF_DISABLE(stValue);
                    else
                        hostModel.setHDT_REDIRECT_IF_DISABLE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_REVERSAL_COUNT(stValue);
                    else
                        hostModel.setHDT_REVERSAL_COUNT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_SIGCAP_INDEX(stValue);
                    else
                        hostModel.setHDT_SIGCAP_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        hostModel.setHDT_BATCH_GROUP_NUMBER(stValue);
                    else
                        hostModel.setHDT_BATCH_GROUP_NUMBER(stValue.substring(0, inindex));
                    // stValue = stValue.substring(inindex + 1);
                }
                hostModelDao.insert(hostModel);
            } else if (stParam.contains("CDT")) {

                EFTPOSLog.d(TAG, "CDT ID::" + stParam.substring(3, 5));
                cbinModel.setCDT_ID(stParam.substring(3, 5)+"");

                inindex = stValue.indexOf(' ');
                cbinModel.setCDT_LO_RANGE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cbinModel.setCDT_HI_RANGE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cbinModel.setCDT_HDT_REFERENCE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cbinModel.setCDT_CARD_TYPE_ARRAY(stValue);
                    else
                        cbinModel.setCDT_CARD_TYPE_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cbinModel.setCDT_CARD_NAME(stValue);
                    else
                        cbinModel.setCDT_CARD_NAME(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                cbinModelDao.insert(cbinModel);
            } else if (stParam.contains("CTT")) {

                EFTPOSLog.d(TAG, "CTT ID::" + stParam.substring(3, 5));
                cttModel.setCTT_ID(stParam.substring(3, 5)+"");

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_CARD_TYPE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_CARD_LABEL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_CARD_FORMAT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_MASK_FORMAT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_MAGSTRIPE_FLOOR_LIMIT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_DISABLE_LUHN(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_CUSTOM_OPTIONS(stValue);
                    else
                        cttModel.setCTT_CUSTOM_OPTIONS(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_CVV_FDBC_ENABLE(stValue);
                    else
                        cttModel.setCTT_CVV_FDBC_ENABLE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_PAN_MASK_ARRAY(stValue);
                    else
                        cttModel.setCTT_PAN_MASK_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_EXPIRY_MASK_ARRAY(stValue);
                    else
                        cttModel.setCTT_EXPIRY_MASK_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_QPSL(stValue);
                    else
                        cttModel.setCTT_QPSL(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_DISABLE_EXPIRY_CHECK(stValue);
                    else
                        cttModel.setCTT_DISABLE_EXPIRY_CHECK(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        cttModel.setCTT_MC501(stValue);
                    else
                        cttModel.setCTT_MC501(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }

                cttModelDao.insert(cttModel);
            } else if (stParam.contains("COM")) {

                EFTPOSLog.d(TAG, "COM ID::" + stParam.substring(3, 5));
                comModel.setCOMMOS_ID(stParam.substring(3, 5)+"");

                inindex = stValue.indexOf(' ');
                comModel.setCOM_DESCRIPTION(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_PRIMARY_TYPE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_SECONDARY_TYPE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_MODEM_PRIMARY_NUMBER(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_MODEM_SECONDARY_NUMBER(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_MODEM_STRING(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_MODEM_DISABLE_LINE_DETECT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                comModel.setCOM_MODEM_TIMEOUT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_PRIMARY_IP_PORT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_SECONDARY_IP_PORT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_IP_TIMEOUT(stValue);
                    else
                        comModel.setCOM_IP_TIMEOUT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_CONNECT_SECONDARY(stValue);
                    else
                        comModel.setCOM_CONNECT_SECONDARY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_SSL_INDEX(stValue);
                    else
                        comModel.setCOM_SSL_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_MODEM_INDEX(stValue);
                    else
                        comModel.setCOM_MODEM_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_PPP_USER_ID(stValue+"");
                    else
                        comModel.setCOM_PPP_USER_ID(stValue.substring(0, inindex)+"");
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_PPP_PASSWORD(stValue);
                    else
                        comModel.setCOM_PPP_PASSWORD(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_PPP_MODEM_STRING(stValue);
                    else
                        comModel.setCOM_PPP_MODEM_STRING(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        comModel.setCOM_PPP_TIMEOUT(stValue);
                    else
                        comModel.setCOM_PPP_TIMEOUT(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }

                comModelDao.insert(comModel);

            } else if (stParam.contains("ETH")) {
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ethernetModel.setLOCAL_IP(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ethernetModel.setSUBNET_MASK(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ethernetModel.setGATEWAY(stValue);
                    else
                        ethernetModel.setGATEWAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ethernetModel.setDNS1(stValue);
                    else
                        ethernetModel.setDNS1(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ethernetModel.setDNS2(stValue);
                    else
                        ethernetModel.setDNS2(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }
                ethernetModelDao.insert(ethernetModel);

            } else if (stParam.contains("CUR")) {
                EFTPOSLog.d(TAG, "CURR ID::" + stParam.substring(4, 6));
                currModel.setCURRENCY_ID(stParam.substring(4, 6)+"");

                inindex = stValue.indexOf(' ');
                currModel.setCURR_LABEL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    //currModel.setCURR_CODE(stValue.substring(0, inindex));
                    if (inindex == -1)
                        currModel.setCURR_EXPONENT(stValue);
                    else
                        currModel.setCURR_EXPONENT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    //currModel.setCURR_CODE(stValue.substring(0, inindex));
                    if (inindex == -1)
                        currModel.setCURR_CODE(stValue);
                    else
                        currModel.setCURR_CODE(stValue.substring(0, inindex));
                }
                //stValue = stValue.substring(inindex + 1);
                currModelDao.insert(currModel);

            } else if (stParam.contains("EZL")) {
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_ENABLE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_PAYMENT_MAC_KEY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_TOPUP_MAC_KEY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_SAM_KEY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_PAYMENT_TRP(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_TOPUP_TRP(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ezlinkModel.setEZLINK_PAYMENT_DEVICE_TYPE(stValue);
                    else
                        ezlinkModel.setEZLINK_PAYMENT_DEVICE_TYPE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ezlinkModel.setEZLINK_TOPUP_DEVICE_TYPE(stValue);
                    else
                        ezlinkModel.setEZLINK_TOPUP_DEVICE_TYPE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ezlinkModel.setEZLINK_BLACK_LIST_LAST_UPDATE(stValue);
                    else
                        ezlinkModel.setEZLINK_BLACK_LIST_LAST_UPDATE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        ezlinkModel.setEZLINK_TOPUP_PAYMENT_MODE(stValue);
                    else
                        ezlinkModel.setEZLINK_TOPUP_PAYMENT_MODE(stValue.substring(0, inindex));
                }

                ezlinkModelDao.insert(ezlinkModel);
            } else if (stParam.contains("BARCODE")) {

                EFTPOSLog.d(TAG, "BAR DOCDE::" + stValue);
                inindex = stValue.indexOf(' ');
                alipayModel.setPARTNER_ID(stValue.substring(0, inindex)+"");
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                alipayModel.setSELLER_ID(stValue.substring(0, inindex)+"");
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    if (inindex == -1)
                        alipayModel.setREGION_CODE(stValue);
                    else
                        alipayModel.setREGION_CODE(stValue.substring(0, inindex));
                }
                alipayModelDao.insert(alipayModel);
            }
        }

        merchantModelDao.insert(merchantModel);
        pwdModelDao.insert(pwdModel);
        transctrlModelDao.insert(transctrlModel);
        reportModelDao.insert(reportModel);
        hostTransModelDao.insert(hostTransModel);
        receiptModelDao.insert(receiptModel);
        maskModelDao.insert(maskModel);
        limitModelDao.insert(limitModel);
        utilityModelDao.insert(utilModel);



    }

    private int inProcessPacket() {

        try {
            isoMsg.unpack(FinalData);
            // print the DE list
            logISOMsg(isoMsg);
        } catch (ISOException ex) {
            EFTPOSLog.d(TAG, "ISO EXCEPTION");
            EFTPOSLog.d(TAG, ex.getMessage());
        }

        return 0;
    }

    private int inConnection(String ServerIP,int Port) {
        EFTPOSLog.d(TAG, "Connection");
        EFTPOSLog.d(TAG, connectToStr);

        try {

            smtpSocket = new Socket(ServerIP, Port);
        } catch (UnknownHostException e) {
            EFTPOSLog.d(TAG, "UnknownHostException");
            EFTPOSLog.d(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            EFTPOSLog.d(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }

    private int inDisconnection() {
        try {
            if (smtpSocket != null)
                smtpSocket.close();
        } catch (UnknownHostException e) {
            EFTPOSLog.d(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            EFTPOSLog.d(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }

    private void logISOMsg(ISOMsg msg) {
        EFTPOSLog.d(TAG, "----ISO MESSAGE-----");
        try {
            EFTPOSLog.d(TAG, "  MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    EFTPOSLog.d(TAG, "    Field-" + i + " : " + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            EFTPOSLog.d(TAG, "--------------------");
        }

    }


    private int inSendRecvPacket() {
        OutputStream os = null;
        InputStream is = null;

        String result;
        result = "";
        for (int k = 0; k < inFinalLength; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        EFTPOSLog.d(TAG,"\nSendings:");
        EFTPOSLog.d(TAG,result);

        try {
            os = smtpSocket.getOutputStream();
            is = smtpSocket.getInputStream();
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
            EFTPOSLog.d(TAG, "Don't know about host: hostname");
            return 1;
        } catch (IOException e) {
            EFTPOSLog.d(TAG, "Couldn't get I/O for the connection to: hostname");
            return 1;
        }
        return 0;
    }


    private int inCreatePacket() {
        String stde27 = "";
        String stde29 = "";
        try {
            
            switch (TransactionDetails.trxType) {
                
                case Constants.TransType.TMS_INITIAL_PACKET:
                    isoMsg.setHeader(globalVar.tmsParam.getTMS_TPDU().getBytes());
                    isoMsg.setMTI("0100");
                    isoMsg.set(3, Constants.PROCESSINGCODE.pcTmsInitial);
                    isoMsg.set(11, "000001");
                    isoMsg.set(24, "0700");
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
                    isoMsg.set(24, "0700");
                    isoMsg.set(26, globalVar.tmsParam.getTMS_FILENAME());
                    stde27 = Constants.TMS_DEFAULT_MMS_FAMILY + "\\" + globalVar.tmsParam.getTMS_APPLICATION();
                    isoMsg.set(27, stde27);
                    stde29 = String.format("%02d", globalVar.getGPartToDownload());
                    isoMsg.set(29, stde29);
                    isoMsg.set(41, globalVar.tmsParam.getTMS_TERMINAL_ID());
                   
                    break;
                case Constants.TransType.ALIPAY_SALE:
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

            }

            //  EFTPOSLog.d(TAG,"AdminActivity::PACK:");
            // Get and print the output result
            try {
                logISOMsg(isoMsg);
                byte[] data = isoMsg.pack();
                inFinalLength = AddLength_Tpdu(data, FinalData);
             } catch (Exception ex) {
                EFTPOSLog.d(TAG, "IOException EXCEPTION");
                EFTPOSLog.d(TAG, ex.getMessage());
            }

        } catch (ISOException ex) {
            EFTPOSLog.d(TAG, "ISO EXCEPTION");
            EFTPOSLog.d(TAG, ex.getMessage());
        }
        return Constants.ReturnValues.RETURN_OK;
    }


    public int AddLength_Tpdu(byte[] data, byte[] FinalData) {
        int inOffset = 0;
        // byte[] FinalData = new byte[data.length + 7];
        int inPacLen = (data.length);

        inOffset = inOffset + 2;
        // Copy the respective TPDU Value
        byte[] tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();

        for (int i = 0; i < tpdu.length; i++) {
            FinalData[inOffset++] = tpdu[i];
        }

        // Check the length in BCD or HEX
        FinalData[0] = (byte) ((inPacLen + tpdu.length) / 256);
        FinalData[1] = (byte) ((inPacLen + tpdu.length) % 256);

        for (int i = 0; i < inPacLen; i++) {
            FinalData[inOffset++] = data[i];
        }

        return inOffset;
    }

    public int vdProcessDownloadResponse(int inRequestType) {

        String applicationname;

        if (inRequestType == Constants.TransType.TMS_INITIAL_PACKET) // first
        // download
        {


            if (!(isoMsg.getString(26).equals(globalVar.tmsParam.getTMS_FILENAME()))) {
                return Constants.TMSReturnValues.TMS_RESPONSE_KO;
            }

            if ((isoMsg.getString(30).equals(globalVar.getActivationDate()))) {
                return Constants.TMSReturnValues.TMS_RESPONSE_DL_NOT_REQUIRED;
            }
            if (Integer.parseInt(isoMsg.getString(28)) == 0)
                return Constants.TMSReturnValues.TMS_RESPONSE_KO;
            globalVar.setGTotalNumberofTMSparts(Integer.parseInt(isoMsg.getString(28)));

            applicationname = Constants.TMS_DEFAULT_MMS_FAMILY + "\\" + globalVar.tmsParam.getTMS_APPLICATION();
            if (!(isoMsg.getString(27).equals(applicationname)))
                return Constants.TMSReturnValues.TMS_RESPONSE_KO;
            globalVar.setfilename(isoMsg.getString(26));
            globalVar.setdirectory(isoMsg.getString(27));
            globalVar.setActivationDate(isoMsg.getString(30));

            return Constants.TMSReturnValues.TMS_RESPONSE_DL_REQUIRED;

        } else if (inRequestType == Constants.TransType.TMS_SUBSEQUENT_PACKET) {
            globalVar.TmsData = globalVar.TmsData + isoMsg.getString(31);

            //EFTPOSLog.d(TAG,"AdminActivity::globalVar.TmsData:::"+globalVar.TmsData);
            EFTPOSLog.d(TAG, "globalVar.TmsData_Len:::" + globalVar.TmsData.length());

            return Constants.TMSReturnValues.TMS_RESPONSE_OK;
        }

        return Constants.TMSReturnValues.TMS_RESPONSE_OK;
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp="";

        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(AdminActivity.this);
            progress.setTitle("EFTPOS");
            progress.setMessage("Downloading..");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setMax(100);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.setIndeterminate(false);
            progress.show();

        }


        @Override
        protected String doInBackground(String... params) {
            try {

                resp = processRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

            // execution of result of Long time consuming operation

            if(result.equals("OK")) {
                progress.dismiss();
                Toast.makeText(AdminActivity.this, "DOWNLOAD SUCCESSFUL", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AdminActivity.this, HomePagerActivity.class));

            }else{

                TransactionDetails.responseMessge = "TMS DOWNLOAD FAILED";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminActivity.this, PaymentFailure.class);
                        startActivity(intent);
                    }
                }, TIME_OUT);
                        progress.dismiss();

            }

        }




    }
}
