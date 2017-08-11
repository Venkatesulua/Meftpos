package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.ESCUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.app.EftposApp;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.AlipayModel;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.MerchantModel;
import com.mobileeftpos.android.eftpos.db.PasswordModel;
import com.mobileeftpos.android.eftpos.db.TraceModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;

import java.util.List;

/**
 * Created by Prathap on 6/4/17.
 */

public class TecnicianMenuActivity extends Activity implements View.OnClickListener{
    private final String TAG = TecnicianMenuActivity.class.getSimpleName();
    private LinearLayout reversalBtn, batchBtn, printConfigBtn, mmsBtn, secureBtn, KeyManagementBtn,LoadDefaultBtn;
    public static Activity context;
    private PayServices payService = new PayServices();
    HostModel hostModel;
    private DaoSession daoSession;
    PasswordModel pwdModel;
    CardBinModel cbinModel ;
    CardTypeModel cttModel ;
    CommsModel comModel;
    CurrencyModel currModel;
    MerchantModel merchantModel ;
    AlipayModel barcodeModel;
    TraceModel traceNumber;
    BatchModel batchdata;
    TransactionControlModel transctrlModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_three);
        daoSession = ((EftposApp) getApplication()).getDaoSession();//GreenDaoSupport.getInstance(TecnicianMenuActivity.this);
        context = TecnicianMenuActivity.this;
        initView();

    }

    private void initView(){

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
                startActivity(new Intent(TecnicianMenuActivity.this, HomePagerActivity.class));
                break;

            case R.id.clrbatchItem:
                //delete batch

                int BatchPresent=0;
                batchdata = new BatchModel();
                List<HostModel> hostModelList = GreenDaoSupport.getHostModelOBJList(context);
                for (int i = 0; i < hostModelList.size(); i++) {
                    hostModel = hostModelList.get(i);

                    if (!(hostModel == null || hostModel.equals(""))) {
                        if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {

                //Dynamically add buttons now with the name of hostModel.getHDT_HOST_LABEL();
                //Increment Batch Number
                TransactionDetails.inGHDT=Integer.parseInt(hostModel.getHDT_HOST_ID());
                            if(batchdata.getHdt_index() != null)
                            {
                                BatchPresent =1;
                            }
                payService.vdUpdateSystemBatch(context);
                    }
                }
              }

                daoSession.getBatchModelDao().deleteAll();
                KeyValueDB.removeReversal(context);
                KeyValueDB.removeUpload(context);
                //ALL BATCH DELETED

                Toast.makeText(context, "ALL HOST DELETED", Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, HomePagerActivity.class));

                break;

            case R.id.printconfigitem:

                break;

            case R.id.mmsdownloaditem:
                startActivity(new Intent(context, AdminActivity.class));
                break;

            case R.id.secureeditoritem:
                break;

            case R.id.keyManagmentitem:
                break;

            case R.id.loaddefaultitem:
                Log.i(TAG, "DELETE ALL THE CONTENT FROM  THE FILE");
                GreenDaoSupport.deleteAllTablesData(context);

                 pwdModel = new PasswordModel();
                 cbinModel = new CardBinModel();
                 cttModel = new CardTypeModel();
                 comModel = new CommsModel();
                 currModel = new CurrencyModel();
                 merchantModel = new MerchantModel();
                 barcodeModel = new AlipayModel();
                 traceNumber = new TraceModel();
                 hostModel = new HostModel();
                transctrlModel = new TransactionControlModel();
                //Trace number
                traceNumber.setSYSTEM_TRACE("000001");
                //Password Values
                pwdModel.setVOID_PASSWORD("0000");
                //Card BIN
                cbinModel.setCDT_LO_RANGE("0000000000");
                cbinModel.setCDT_HI_RANGE("9999999999");
                cbinModel.setCDT_HDT_REFERENCE("01");
                cbinModel.setCDT_CARD_TYPE_ARRAY("01");
                cbinModel.setCDT_ID("01");
                //CTT Params
                cttModel.setCTT_ID("01");
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
                comModel.setCOMMOS_ID("01");
                comModel.setCOM_DESCRIPTION("ALIPAY");
                comModel.setCOM_PRIMARY_TYPE("1");
                comModel.setCOM_SECONDARY_TYPE("1");
                comModel.setCOM_MODEM_PRIMARY_NUMBER("67373989");
                comModel.setCOM_MODEM_SECONDARY_NUMBER("67362481");
                comModel.setCOM_MODEM_STRING("0");
                comModel.setCOM_MODEM_DISABLE_LINE_DETECT("0");
                comModel.setCOM_MODEM_TIMEOUT("30");
                comModel.setCOM_PRIMARY_IP_PORT("203.151.149.7|20975");
                comModel.setCOM_SECONDARY_IP_PORT("203.151.149.7|20975");
                comModel.setCOM_IP_TIMEOUT("30");
                comModel.setCOM_CONNECT_SECONDARY("0");
                comModel.setCOM_SSL_INDEX("0");
                comModel.setCOM_MODEM_INDEX("0");
                comModel.setCOM_PPP_USER_ID("0");
                comModel.setCOM_PPP_PASSWORD("0");
                comModel.setCOM_PPP_MODEM_STRING("0");
                comModel.setCOM_PPP_TIMEOUT("0");
                //COMM PARAMS
                currModel.setCURRENCY_ID("01");
                currModel.setCURR_LABEL("MYR");
                currModel.setCURR_EXPONENT("2");
                currModel.setCURR_CODE("0764");
                //Host Parameters
                hostModel.setHDT_HOST_ID("01");
                hostModel.setHDT_HOST_ENABLED("1");
                hostModel.setHDT_COM_INDEX("1");
                hostModel.setHDT_REFERRAL_NUMBER("18008350706");
                hostModel.setHDT_TERMINAL_ID("22000003");
                hostModel.setHDT_MERCHANT_ID("220000001");
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

                barcodeModel.setALIPAY_ID("01");
                barcodeModel.setPARTNER_ID("0000000000000000");
                barcodeModel.setSELLER_ID("0000000000000000");
                barcodeModel.setREGION_CODE("RETAIL");
                //Merchant
                merchantModel.setMERCHANT_ID("01");
                merchantModel.setMERCHANT_NAME("TEST TRANSACTION");
                merchantModel.setMERCHANT_HEADER1("HEADER1");
                merchantModel.setMERCHANT_HEADER2("HEADER2");
                merchantModel.setADDRESS_LINE1("ADDRESSLINE1");
                merchantModel.setADDRESS_LINE2("ADDRESSLINE2");
                merchantModel.setADDRESS_LINE3("ADDRESSLINE3");
                merchantModel.setADDRESS_LINE4("ADDRESSLINE4");
                //Control Parameters
                transctrlModel.setVOID_CTRL("1");
                transctrlModel.setREFUND_CTRL("1");
                transctrlModel.setSETTLEMENT_CTRL("1");



                daoSession.getHostModelDao().insert(hostModel);
	            daoSession.getCurrencyModelDao().insert(currModel);
	            daoSession.getCardBinModelDao().insert(cbinModel);
	            daoSession.getCardTypeModelDao().insert(cttModel);
	            daoSession.getCommsModelDao().insert(comModel);
	            daoSession.getPasswordModelDao().insert(pwdModel);
	            daoSession.getAlipayModelDao().insert(barcodeModel);
	            daoSession.getMerchantModelDao().insert(merchantModel);
	            daoSession.getTraceModelDao().insert(traceNumber);
                daoSession.getTransactionControlModelDao().insert(transctrlModel);

                Toast.makeText(context,"LOADED DEFAULT SETTINGS",Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, HomePagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();    }
}
