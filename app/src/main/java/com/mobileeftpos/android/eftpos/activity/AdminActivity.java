package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.CardBinModel;
import com.mobileeftpos.android.eftpos.model.CardTypeModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.EthernetLabel;
import com.mobileeftpos.android.eftpos.model.EzlinkModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.HostTransmissionModel;
import com.mobileeftpos.android.eftpos.model.LimitModel;
import com.mobileeftpos.android.eftpos.model.MaskingModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;
import com.mobileeftpos.android.eftpos.model.PasswordModel;
import com.mobileeftpos.android.eftpos.model.ReceiptModel;
import com.mobileeftpos.android.eftpos.model.ReportsModel;
import com.mobileeftpos.android.eftpos.model.TransactionControlModel;
import com.mobileeftpos.android.eftpos.model.TransactionDetails;
import com.mobileeftpos.android.eftpos.model.UtilityTable;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

/**
 * Created by Prathap on 5/21/17.
 */

public class AdminActivity extends Activity {

    EditText appName, terminalId, connectionType, connectionTo;
    Button clearBtn, submitBtn;
    private final String TAG = "my_custom_msg";
    //public j8583Params j8583param = new j8583Params();
    public GlobalVar globalVar = new GlobalVar();

    public byte[] FinalData = new byte[1512];
    public int inFinalLength = 0;
    public Socket smtpSocket = null;
    ISOPackager1 packager = new ISOPackager1();
    ISOMsg isoMsg = new ISOMsg();
    private static DBHelper databaseObj;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlayout);
        databaseObj = new DBHelper(AdminActivity.this);
        context = AdminActivity.this;
        appName = (EditText) findViewById(R.id.etappname);
        terminalId = (EditText) findViewById(R.id.etterminalid);
        connectionType = (EditText) findViewById(R.id.etconnectiontype);
        connectionTo = (EditText) findViewById(R.id.etconnectionto);
        clearBtn = (Button) findViewById(R.id.btnClear);
        submitBtn = (Button) findViewById(R.id.btnSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskRunner().execute();
            }
        });


    }


    private void processRequest() {


        //Validation for null
        if (!((appName.getText().toString().length() > 0) && (terminalId.getText().toString().length() > 0) &&
                (connectionType.getText().toString().length() > 0) && (connectionTo.getText().toString().length() > 0))) {
            //ERROR MISSING TMS PARAMETERS
            Log.i(TAG, "ERROR MISSING TMS PARAMETERS");
            return;
        }
        globalVar.tmsParam.setTMS_APPLICATION(appName.getText().toString());
        globalVar.tmsParam.setTMS_FILENAME(terminalId.getText().toString());
        globalVar.tmsParam.setTMS_TERMINAL_ID(terminalId.getText().toString());
        globalVar.tmsParam.setTMS_CONNECTION_TYPE(connectionType.getText().toString());
        globalVar.tmsParam.setTMS_CONNECTION_TO(connectionTo.getText().toString());


        // Initialize at the booting time
        isoMsg.setPackager(packager);
        globalVar.setGTransactionType(Constants.TransType.TMS_INITIAL_PACKET);
        boolean msg_ok = false;
        while (true) {

            if (globalVar.getGTransactionType() == Constants.TransType.TMS_SUBSEQUENT_PACKET) {
                if (globalVar.getGTotalNumberofTMSparts() == globalVar.getGPartToDownload()) {
                    msg_ok = true;
                    break;
                } else {
                    // increase counter to download next part
                    int inGPartToDownload = globalVar.getGPartToDownload();
                    inGPartToDownload++;
                    globalVar.setGPartToDownload(inGPartToDownload);
                }
            }

            inCreatePacket();
            // Connection to Host
            if (inConnection() != 0) {
                return;
            }
            if (inSendRecvPacket() != 0) {
                return;
            }
            if (inProcessPacket() != 0) {
                return;
            }
            String stTemp = isoMsg.getString(39);
            if (stTemp.equals("00")) {
                if (globalVar.getGTransactionType() == Constants.TransType.TMS_INITIAL_PACKET) {
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
                        globalVar.setGTransactionType(Constants.TransType.TMS_SUBSEQUENT_PACKET);

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
        }

        if (inDisconnection() != 0) {
            return;
        }

        if (msg_ok == true) {
            // Parce the TMS data and keep the data in the DATABASE.
            Log.i(TAG, "FInal Output:\n");
            Log.i(TAG, globalVar.TmsData);
            vdStoreParameters();
        }

    }


    void vdStoreParameters() {
        int l_iNumParams = 0;
        int inCounter = 0;
        int tms_offset = 0;
        int param, value;
        String stParam = "", stValue = "";

        int inindex = globalVar.TmsData.indexOf("NUMBER_OF_LINES=");
        String stLines = globalVar.TmsData.substring(inindex + ("NUMBER_OF_LINES=".length()));
        l_iNumParams = Integer.parseInt(stLines);

  ///Intilizing arraylist objects

        /*ArrayList<PasswordModel> passwordModelObjList = new ArrayList<>();


        for (inCounter = 0; inCounter < l_iNumParams; inCounter++) {
            globalVar.TmsData = globalVar.TmsData.substring(tms_offset);
            param = globalVar.TmsData.indexOf('=');
            value = globalVar.TmsData.indexOf(0x0A);

            stParam = globalVar.TmsData.substring(0, param);
            stValue = globalVar.TmsData.substring(param + 1, value);
            tms_offset = value + 1;

            Log.i(TAG, "Param:" + stParam);
            Log.i(TAG, "Value:" + stValue);

            PasswordModel pwdModel = new PasswordModel();
            pwdModel.setBALANCE_PASSWORD("Test1");
            pwdModel.setCASH_ADVANCED_PASSWORD("Test2");
            pwdModel.setDEFAULT_PASSWORD("Test3");
            pwdModel.setEDITOR_PASSWORD("Test4");
            pwdModel.setMANUAL_ENTRY_PASSWORD("Test5");
            pwdModel.setBALANCE_PASSWORD("Test6");
            pwdModel.setOFFLINE_PASSWORD("Test7");
            pwdModel.setREFUND_PASWORD("Test8");
            pwdModel.setTERMINAL_POWERON_PASSWORD("Test9");

            //passwordModelObj.add(pwdModel);
            //or
            //For Data Insertion

            databaseObj.insertPasswordData(pwdModel);


            // to check whether data exist or not

            passwordModelObjList = databaseObj.getAllPasswordrelatedData();

            if (passwordModelObjList.size() > 0) {

                Toast.makeText(context, passwordModelObjList.size() + " Number of records exist", Toast.LENGTH_LONG).show();
            }*/

        PasswordModel pwdModel = new PasswordModel();
        CardBinModel cbinModel= new CardBinModel();
        CardTypeModel cttModel= new CardTypeModel();
        CommsModel comModel= new CommsModel();
        CurrencyModel currModel= new CurrencyModel();
        EthernetLabel ethernetModel= new EthernetLabel();
        EzlinkModel ezlinkModel= new EzlinkModel();
        HostModel hostModel= new HostModel();
        HostTransmissionModel hostTransModel= new HostTransmissionModel();
        LimitModel limitModel= new LimitModel();
        MaskingModel maskModel= new MaskingModel();
        MerchantModel merchantModel= new MerchantModel();
        ReceiptModel receiptModel = new ReceiptModel();
        ReportsModel reportModel = new ReportsModel();
        TransactionControlModel transctrlModel = new TransactionControlModel();
        UtilityTable utilModel = new UtilityTable();

        for (inCounter = 0; inCounter < l_iNumParams; inCounter++) {
            globalVar.TmsData = globalVar.TmsData.substring(tms_offset);
            param = globalVar.TmsData.indexOf('=');
            value = globalVar.TmsData.indexOf(0x0A);

            stParam = globalVar.TmsData.substring(0, param);
            stValue = globalVar.TmsData.substring(param + 1, value);
            tms_offset = value + 1;

            Log.i(TAG,"Param:" + stParam);
            Log.i(TAG,"Value:" + stValue);

            if(stParam.length()==2) {

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
            }else if (stParam.equals("ADD1")) {
                utilModel.setADDITIONAL_PROMPT(stValue);
            }else if (stParam.equals("BDTE")) {
                utilModel.setDAILY_SETTLEMENT_FLAG(stValue);
            }else if (stParam.equals("LFDC")) {
                utilModel.setLAST_4_DIGIT_PROMPT_FLAG(stValue);
            }else if (stParam.equals("2INS")) {
                utilModel.setINSERT_2_SWIPE(stValue);
            }else if (stParam.equals("PIGGY")) {
                utilModel.setPIGGYBACK_FLAG(stValue);
            }else if (stParam.equals("PINBY")) {
                utilModel.setPINBYPASS(stValue);
            }else if (stParam.equals("AUTO_SETTLE")) {
                utilModel.setAUTO_SETTLE_TIME(stValue);
            }else if (stParam.equals("UTRN")) {
                utilModel.setUTRN_PREFIX(stValue);
            }else if (stParam.equals("ST")) {
                utilModel.setSYSTEM_TRACE(stValue);
            }else if (stParam.equals("DAPP")) {
                utilModel.setDEFAULT_APPROVAL_CODE(stValue);
            }
            else if (stParam.contains("HDT")) {
                inindex = stValue.indexOf(' ');
                hostModel.setHDT_HOST_ENABLED(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_COM_INDEX(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_REFERRAL_NUMBER(stValue.substring(0, inindex));
                stValue=stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_TERMINAL_ID(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_MERCHANT_ID(stValue.substring(0, inindex));
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

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_CURR_INDEX(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_PIGGYBACK_FLAG(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_MINIMUM_AMT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_RATE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
               hostModel.setHDT_REDIRECT_IF_DISABLE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                   hostModel.setHDT_REVERSAL_COUNT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                   hostModel.setHDT_SIGCAP_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                   hostModel.setHDT_BATCH_GROUP_NUMBER(stValue.substring(0, inindex));
                   // stValue = stValue.substring(inindex + 1);
                }
                databaseObj.InsertHostTablelData(hostModel);
            } else if (stParam.contains("CDT")) {
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
                    cbinModel.setCDT_CARD_TYPE_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cbinModel.setCDT_CARD_NAME(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                databaseObj.InsertCardBinData(cbinModel);
            } else if (stParam.contains("CTT")) {
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
                    cttModel.setCTT_CUSTOM_OPTIONS(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                inindex = stValue.indexOf(' ');
                cttModel.setCTT_CVV_FDBC_ENABLE(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cttModel.setCTT_PAN_MASK_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cttModel.setCTT_EXPIRY_MASK_ARRAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cttModel.setCTT_QPSL(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cttModel.setCTT_DISABLE_EXPIRY_CHECK(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    cttModel.setCTT_MC501(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }

                databaseObj.InsertCardTypeData(cttModel);
            } else if (stParam.contains("COM")) {
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
                    comModel.setCOM_IP_TIMEOUT(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_CONNECT_SECONDARY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_SSL_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_MODEM_INDEX(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_PPP_USER_ID(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_PPP_PASSWORD(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_PPP_MODEM_STRING(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    comModel.setCOM_PPP_TIMEOUT(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }

                databaseObj.InsertCommsData(comModel);

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
                    ethernetModel.setGATEWAY(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ethernetModel.setDNS1(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ethernetModel.setDNS2(stValue.substring(0, inindex));
                    //stValue = stValue.substring(inindex + 1);
                }
                databaseObj.InsertEthernetData(ethernetModel);

            } else if (stParam.contains("CUR")) {
                inindex = stValue.indexOf(' ');
                currModel.setCURR_LABEL(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                currModel.setCURR_EXPONENT(stValue.substring(0, inindex));
                stValue = stValue.substring(inindex + 1);

                inindex = stValue.indexOf(' ');
                currModel.setCURR_CODE(stValue.substring(0, inindex));
                //stValue = stValue.substring(inindex + 1);
                databaseObj.InsertCurrencyData(currModel);

            } else if (stParam.contains("EZL")) {
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_ENABLE(stValue.substring(0, inindex));
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
                    ezlinkModel.setEZLINK_PAYMENT_DEVICE_TYPE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_TOPUP_DEVICE_TYPE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_BLACK_LIST_LAST_UPDATE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }
                if (!stValue.isEmpty()) {
                    inindex = stValue.indexOf(' ');
                    ezlinkModel.setEZLINK_TOPUP_PAYMENT_MODE(stValue.substring(0, inindex));
                    stValue = stValue.substring(inindex + 1);
                }

                databaseObj.InsertEzlinkData(ezlinkModel);
            }
		}

        databaseObj.insertMerchantData(merchantModel);
        databaseObj.insertPasswordData(pwdModel);
        databaseObj.InsertTransactionCtrlData(transctrlModel);
        databaseObj.InsertReportCtrlData(reportModel);
        databaseObj.InsertHostTransmissionModelData(hostTransModel);
        databaseObj.InsertReceiptModelData(receiptModel);
        databaseObj.InsertMaskingModelData(maskModel);
        databaseObj.InsertLimitModelData(limitModel);
        databaseObj.InsertUtilityTablelData(utilModel);

        MerchantModel merchantModeldata = new MerchantModel();
        merchantModeldata = databaseObj.getMerchantData(0);
        Log.i(TAG,"Merchant NAme"+merchantModeldata.getMERCHANT_NAME());



    }

    private int inProcessPacket() {
        // keep basic.xml in assets folder
        //  Log.i(TAG,"inProcessPacket");
        // InputStream inputstream =
        // getApplicationContext().getAssets().open("basic.xml");
        // MihirPackager packager = new MihirPackager(inputstream);
        try {
            // GenericPackager packager = new
            // GenericPackager("src/asset/basic.xml");

            //  Log.i(TAG,"Packager");

            isoMsg.unpack(FinalData);
            // print the DE list
            logISOMsg(isoMsg);
        } catch (ISOException ex) {
            Log.i(TAG, "ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }

        return 0;
    }

    private int inConnection() {
        Log.i(TAG, "Connection");
        Log.i(TAG, "203.125.11.228");
        try {

            smtpSocket = new Socket("203.125.11.228", 8585);
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

    private int inCreatePacket() {
        String stde27 = "";
        String stde29 = "";
        try {

            // keep basic.xml in assets folder
            //  Log.i(TAG,"inCreatePacket");
            // InputStream inputstream =
            // getApplicationContext().getAssets().open("basic.xml");
            //  Log.i(TAG,"Input Stream");
            // MihirPackager packager = new MihirPackager(inputstream);
            // GenericPackager packager = new
            // GenericPackager("src/asset/basic.xml");
            // ISOPackager1 packager = new ISOPackager1();
            //  Log.i(TAG,"Packager");
            // ISOMsg isoMsg = new ISOMsg();
            // isoMsg.setPackager(packager);
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
                    // strcpy((char *) message_id, DOWNLOAD_SUBSEQUENT_MSG);
                /*
				 * strcpy((char *) field_03, DOWNLOAD_SUBSEQUENT_PROC);
				 *
				 * strcpy((char *) field_11, "000001");
				 *
				 * strcpy((char *) field_24, stGlobalex.chGNII);
				 *
				 * memset((char*) field_26, 0, sizeof((char*) field_26));
				 * sprintf((char*) field_26, "%s", stGTMSStruct.TMS_FILENAME);
				 * //venkatt ADD_BCD_LENGTH_INFRONT(field_26);
				 *
				 * memset((char*) field_29, 0, sizeof((char*) field_29));
				 * sprintf((char*) field_29, "%02d", inGPartToDownload);
				 * //venkatt ADD_BCD_LENGTH_INFRONT(field_29);
				 *
				 * strcpy((char *) field_41, stGTMSStruct.TMS_TERMINAL_ID);
				 * //venkattt memset((char*) field_27, 0, sizeof((char*)
				 * field_27)); sprintf((char*) field_27, "%s\\%s",
				 * TMS_DEFAULT_MMS_FAMILY, stGTMSStruct.TMS_APPLICATION);
				 * //venkatttt ADD_BCD_LENGTH_INFRONT(field_27);
				 */
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
                case Constants.TransType.SETTLEMENT:
                    break;
            }

            //  Log.i(TAG,"PACK:");
            // Get and print the output result
            try {
                logISOMsg(isoMsg);
                byte[] data = isoMsg.pack();
                inFinalLength = AddLength_Tpdu(data, FinalData);
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

        inOffset = inOffset + 2;
        // Copy the respective TPDU Value
        byte[] tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();

        for (int i = 0; i < tpdu.length; i++) {
            FinalData[inOffset++] = tpdu[i];
        }
		/*
		 * byte[] byLen = new BigInteger(Integer.toString(inPacLen +
		 * tpdu.length), 16).toByteArray(); if (byLen.length == 1) {
		 * FinalData[0] = 0x00; FinalData[1] = byLen[0]; } else { FinalData[0] =
		 * byLen[0]; FinalData[1] = byLen[1]; }
		 */
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
        // S_FS_FILE* hTMSFile = 0;
        // int Field_31_length = 0;
        // char temp[2 + 1];
        // char[] temp1 = new char[4 + 1];

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

            // Delete(TEMP_DOWNLOAD_DATA_FILE, BATCH_DISK);
            // Create(BATCH_DISK, TEMP_DOWNLOAD_DATA_FILE);

            // memset((char *) &stLasttmsinfoStruct, 0, sizeof(LASTTMSINFO));
            // if(inGetSetLastTMSDownloadInfo(&stLasttmsinfoStruct, GET,
            // stGTMSStruct.TMS_FILENAME) == FALSE)
            // memset((char *) &stLasttmsinfoStruct, 0, sizeof(LASTTMSINFO));
            // Validate the info downloaded
            // if ((memcmp((char *) field_26 + 2, (char *)
            // stGTMSStruct.TMS_FILENAME, strlen((char *)
            // stGTMSStruct.TMS_FILENAME)) != 0)) {
            // return TMS_RESPONSE_KO; //validation field 26 failed
            // }
            // if ((memcmp((char *) field_30 + 2, (char *)
            // stLasttmsinfoStruct.ActivationDate, strlen((char *)
            // &field_30[2])) == 0)) {
            // second Download not required
            // return TMS_RESPONSE_DL_NOT_REQUIRED;
            // }
            // if (!(atoi((char*) field_28 + 2) > 0 && strlen((char*)
            // &field_28[2]))) {
            // field 28 failed
            // return TMS_RESPONSE_KO;
            // } else {
            // inGTotalNumberofTMSparts = atoi((char*) &field_28[2]);
            // }
            // sprintf(applicationname, "%s\\%s",TMS_DEFAULT_MMS_FAMILY,
            // stGTMSStruct.TMS_APPLICATION);

            // if ((memcmp((char *) field_27 + 2, (char *) applicationname,
            // strlen((char *) applicationname)) != 0)) {
            // field 27 failed
            // return TMS_RESPONSE_KO;
            // }
			/* Populate the filename field 26 into temp file */
            // memcpy((char*) stLasttmsinfoStruct.filename, (char *) field_26 +
            // 2, strlen((char*) &field_26[2])); // TID

			/* Populate the application name field 27 into temp file */
            // memcpy((char*) stLasttmsinfoStruct.directory, (char *) field_27 +
            // 2, strlen((char*) &field_27[2])); // PATH

            // Set the latest activation date into struct to be saved once the
            // data download is successful
            // memcpy((char*) stLasttmsinfoStruct.ActivationDate, (char *)
            // field_30 + 2, strlen((char*) &field_30[2])); // Date of when
            // parameter was changed

            return Constants.TMSReturnValues.TMS_RESPONSE_DL_REQUIRED;
        } else if (inRequestType == Constants.TransType.TMS_SUBSEQUENT_PACKET) {
            globalVar.TmsData = globalVar.TmsData + isoMsg.getString(31);
			/*
			 * memset(temp1, 0, sizeof(temp1)); vdHexToStr(temp1, field_31, 2);
			 *
			 * Field_31_length = atoi(temp1);
			 *
			 * hTMSFile = Open(BATCH_DISK, TEMP_DOWNLOAD_DATA_FILE); if
			 * (hTMSFile == NULL) return TMS_RESPONSE_KO; else {
			 * FS_seek(hTMSFile, 0, FS_SEEKEND); Write(hTMSFile, (char*)
			 * &field_31[2], Field_31_length); FS_close(hTMSFile); }
			 */

            return Constants.TMSReturnValues.TMS_RESPONSE_OK;
        }

        return Constants.TMSReturnValues.TMS_RESPONSE_OK;
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                processRequest();
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
            progressDialog = ProgressDialog.show(AdminActivity.this,
                    "EFTPOS",
                    "Please wait...");
            progressDialog.show();
        }

    }
}
