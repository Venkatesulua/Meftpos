package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.BluetoothUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.ESCUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.database.DBStaticField;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.BatchModel;
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
import com.mobileeftpos.android.eftpos.model.TraceNumberModel;
import com.mobileeftpos.android.eftpos.model.TransactionControlModel;
import com.mobileeftpos.android.eftpos.model.UtilityTable;

import java.io.IOException;
import java.util.List;

/**
 * Created by Prathap on 6/4/17.
 */

public class TecnicianMenuActivity extends Activity implements View.OnClickListener{
    private final String TAG = "my_custom_msg";
    private LinearLayout reversalBtn, batchBtn, printConfigBtn, mmsBtn, secureBtn, KeyManagementBtn,LoadDefaultBtn;

    private static DBHelper databaseObj;
    public static Context context;
    private PayServices payService = new PayServices();
    HostModel hostModel = new HostModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_three);

        databaseObj = new DBHelper(TecnicianMenuActivity.this);
        context = TecnicianMenuActivity.this;

        reversalBtn = (LinearLayout) findViewById(R.id.clrreverseItem);
        batchBtn = (LinearLayout) findViewById(R.id.clrbatchItem);
        printConfigBtn = (LinearLayout) findViewById(R.id.printconfigitem);
        mmsBtn = (LinearLayout) findViewById(R.id.mmsdownloaditem);
        secureBtn = (LinearLayout) findViewById(R.id.secureeditoritem);
        KeyManagementBtn = (LinearLayout) findViewById(R.id.keyManagmentitem);
        LoadDefaultBtn = (LinearLayout) findViewById(R.id.loaddefaultitem);
        reversalBtn.setOnClickListener(this);
        batchBtn.setOnClickListener(this);
        printConfigBtn.setOnClickListener(this);
        mmsBtn.setOnClickListener(this);
        secureBtn.setOnClickListener(this);
        KeyManagementBtn.setOnClickListener(this);
        LoadDefaultBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        ESCUtil cc = new ESCUtil();
        switch (view.getId()) {
            case R.id.clrreverseItem:
                KeyValueDB.removeReversal(context);
                Toast.makeText(TecnicianMenuActivity.this, "REVERSAL CLEARED", Toast.LENGTH_LONG).show();
                startActivity(new Intent(TecnicianMenuActivity.this, HomeActivity.class));
                break;

            case R.id.clrbatchItem:
                //delete batch

                int BatchPresent=0;
                BatchModel batchdata = new BatchModel();
                List<HostModel> hostModelList = databaseObj.getAllHostTableData();
                for (int i = 0; i < hostModelList.size(); i++) {
                    hostModel = hostModelList.get(i);
                    if (!(hostModel == null || hostModel.equals(""))) {
                        if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {

                //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                //Increment Batch Number
                TransactionDetails.inGHDT=Integer.parseInt(hostModel.getHDT_HOST_ID());
                            if(batchdata.getHDT_INDEX() != null)
                            {
                                BatchPresent =1;
                            }
                payService.vdUpdateSystemBatch(databaseObj);
                    }
                }
            }
                databaseObj.deleteallvalues(DBStaticField.TABLE_BATCH);
                KeyValueDB.removeReversal(context);
                KeyValueDB.removeUpload(context);
                //ALL BATCH DELETED

                Toast.makeText(TecnicianMenuActivity.this, "ALL HOST DELETED", Toast.LENGTH_LONG).show();
                startActivity(new Intent(TecnicianMenuActivity.this, HomeActivity.class));

                break;

            case R.id.printconfigitem:

                break;

            case R.id.mmsdownloaditem:
                startActivity(new Intent(TecnicianMenuActivity.this, AdminActivity.class));
                break;

            case R.id.secureeditoritem:
                break;

            case R.id.keyManagmentitem:
                break;

            case R.id.loaddefaultitem:
                Log.i(TAG, "DELETE ALL THE CONTENT FROM  THE FILE");
                databaseObj.deleteallvalues(DBStaticField.TABLE_HOST);//="HostTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_CBT);//="CardBinTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_CTT);//CardTypeTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_PWD);//PasswordTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_TCT);//TransactionControlTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_ETHERNET);//EthernetTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_CURRENCY);//CurrencyTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_LIMIT);//LimitTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_MASKING);//MaskingTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_RECEIPT);//ReceiptTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_UTILITY);//UtilityTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_MERCHANT);//MerchantTable";
                databaseObj.deleteallvalues(DBStaticField.TABLE_HTT);//TABLE_HTT";
                databaseObj.deleteallvalues(DBStaticField.TABLE_REPORT);//TABLE_REPORT";
                databaseObj.deleteallvalues(DBStaticField.EZLINK_TABLE);//EZLINK_TABLE";
                databaseObj.deleteallvalues(DBStaticField.COMMS_TABLE);//COMMS_TABLE";
                databaseObj.deleteallvalues(DBStaticField.ALIPAY_TABLE);//COMMS_TABLE";
                PasswordModel pwdModel = new PasswordModel();
                CardBinModel cbinModel = new CardBinModel();
                CardTypeModel cttModel = new CardTypeModel();
                CommsModel comModel = new CommsModel();
                CurrencyModel currModel = new CurrencyModel();
                EthernetLabel ethernetModel = new EthernetLabel();
                EzlinkModel ezlinkModel = new EzlinkModel();

                HostTransmissionModel hostTransModel = new HostTransmissionModel();
                LimitModel limitModel = new LimitModel();
                MaskingModel maskModel = new MaskingModel();
                MerchantModel merchantModel = new MerchantModel();
                ReceiptModel receiptModel = new ReceiptModel();
                ReportsModel reportModel = new ReportsModel();
                TransactionControlModel transctrlModel = new TransactionControlModel();
                UtilityTable utilModel = new UtilityTable();
                BarcodeModel barcodeModel = new BarcodeModel();
                TraceNumberModel traceNumber = new TraceNumberModel();
                //Trace number
                traceNumber.setSYSTEM_TRACE("000001");
                //Password Values
                pwdModel.setVOID_PASSWORD("0000");
                //Card BIN
                cbinModel.setCDT_LO_RANGE("0000000000");
                cbinModel.setCDT_HI_RANGE("9999999999");
                cbinModel.setCDT_HDT_REFERENCE("01");
                cbinModel.setCDT_CARD_TYPE_ARRAY("01");
                cbinModel.setCDT_ID("1");
                //CTT Params
                cttModel.setCTT_ID("1");
                cttModel.setCTT_CARD_TYPE("Q");
                cttModel.setCTT_CARD_LABEL("BARCODE");
                cttModel.setCTT_CARD_FORMAT("4444");
                cttModel.setCTT_MASK_FORMAT("88");
                cttModel.setCTT_MAGSTRIPE_FLOOR_LIMIT("0");
                cttModel.setCTT_DISABLE_LUHN("1");
                cttModel.setCTT_CUSTOM_OPTIONS("110000001000000000");
                cttModel.setCTT_CVV_FDBC_ENABLE("0");
                cttModel.setCTT_PAN_MASK_ARRAY("0");
                cttModel.setCTT_EXPIRY_MASK_ARRAY("0");
                //COMM Model
                comModel.setCOMMOS_ID("1");
                comModel.setCOM_DESCRIPTION("ALIPAY");
                comModel.setCOM_PRIMARY_TYPE("1");
                comModel.setCOM_SECONDARY_TYPE("1");
                comModel.setCOM_MODEM_PRIMARY_NUMBER("67373989");
                comModel.setCOM_MODEM_SECONDARY_NUMBER("67362481");
                comModel.setCOM_MODEM_STRING("0");
                comModel.setCOM_MODEM_DISABLE_LINE_DETECT("0");
                comModel.setCOM_MODEM_TIMEOUT("30");
                comModel.setCOM_PRIMARY_IP_PORT("171.99.133.30|10975");
                comModel.setCOM_SECONDARY_IP_PORT("171.99.133.30|10975");
                comModel.setCOM_IP_TIMEOUT("30");
                comModel.setCOM_CONNECT_SECONDARY("0");
                comModel.setCOM_SSL_INDEX("0");
                comModel.setCOM_MODEM_INDEX("0");
                comModel.setCOM_PPP_USER_ID("0");
                comModel.setCOM_PPP_PASSWORD("0");
                comModel.setCOM_PPP_MODEM_STRING("0");
                comModel.setCOM_PPP_TIMEOUT("0");
                //COMM PARAMS
                currModel.setCURRENCY_ID("1");
                currModel.setCURR_LABEL("THB");
                currModel.setCURR_EXPONENT("2");
                currModel.setCURR_CODE("0764");
                //Host Parameters
                hostModel.setHDT_HOST_ID("1");
                hostModel.setHDT_HOST_ENABLED("1");
                hostModel.setHDT_COM_INDEX("1");
                hostModel.setHDT_REFERRAL_NUMBER("18008350706");
                hostModel.setHDT_TERMINAL_ID("55555555");
                hostModel.setHDT_MERCHANT_ID("6666666666");
                hostModel.setHDT_TPDU("6002540000");
                hostModel.setHDT_BATCH_NUMBER("000001");
                hostModel.setHDT_INVOICE_NUMBER("000001");
                hostModel.setHDT_PROCESSING_CODE("1");
                hostModel.setHDT_HOST_TYPE("Q");
                hostModel.setHDT_HOST_LABEL("ALI");
                hostModel.setHDT_MANUAL_ENTRY_FLAG("0");
                hostModel.setHDT_REVERSAL_FLAG("0");
                hostModel.setHDT_SETTLEMENT_FLAG("0");
                hostModel.setHDT_BATCH_MAX_TOTAL("0");
                hostModel.setHDT_BATCH_STL_LAST("0");
                hostModel.setHDT_BATCH_CURR_TOTAL("0");
                hostModel.setHDT_BATCH_NO_TRANS("0");
                hostModel.setHDT_DESCRIPTION("ALIPAY");
                hostModel.setHDT_PAY_TERM("0");
                hostModel.setHDT_PEK("0");
                hostModel.setHDT_MEK("0");
                hostModel.setHDT_MAC_INDEX("0");
                hostModel.setHDT_CUSTOM_OPTIONS("0");
                hostModel.setHDT_CURR_INDEX("1");
                hostModel.setHDT_PIGGYBACK_FLAG("0");
                hostModel.setHDT_MINIMUM_AMT("0");
                hostModel.setHDT_RATE("0");
                hostModel.setHDT_REDIRECT_IF_DISABLE("0");
                //ALIPAY BARCode

                barcodeModel.setALIPAY_ID("1");
                barcodeModel.setPARTNER_ID("0000000000000000");
                barcodeModel.setSELLER_ID("0000000000000000");
                barcodeModel.setREGION_CODE("RETAIL");
                //Merchant
                merchantModel.setMERCHANT_ID("1");
                merchantModel.setMERCHANT_NAME("TEST TRANSACTION");
                merchantModel.setMERCHANT_HEADER1("HEADER1");
                merchantModel.setMERCHANT_HEADER2("HEADER2");
                merchantModel.setADDRESS_LINE1("ADDRESSLINE1");
                merchantModel.setADDRESS_LINE2("ADDRESSLINE2");
                merchantModel.setADDRESS_LINE3("ADDRESSLINE3");
                merchantModel.setADDRESS_LINE4("ADDRESSLINE4");


                databaseObj.InsertHostTablelData(hostModel);
                databaseObj.InsertCurrencyData(currModel);
                databaseObj.InsertCardBinData(cbinModel);
                databaseObj.InsertCardTypeData(cttModel);
                databaseObj.InsertCommsData(comModel);
                databaseObj.insertPasswordData(pwdModel);
                databaseObj.insertBarocdeData(barcodeModel);
                databaseObj.insertMerchantData(merchantModel);
                databaseObj.InsertTraceNumberData(traceNumber);

                Toast.makeText(TecnicianMenuActivity.this,"LOADED DEFAULT SETTINGS",Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TecnicianMenuActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();    }
}
