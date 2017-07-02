package com.mobileeftpos.android.eftpos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CardBinModel;
import com.mobileeftpos.android.eftpos.model.CardTypeModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.EthernetLabel;
import com.mobileeftpos.android.eftpos.model.EzlinkModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
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

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper {

    private static final String DATABASE_NAME = "EFTPOS.db";

    private final String TAG = "my_custom_msg";
    private static final int DATABASE_VERSION = 1;


    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase myDataBase;

    public DBHelper(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }


    private static String Table_HOST = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_HOST
            + "] (["
            + DBStaticField.HDT_HOST_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.HDT_HOST_ENABLED
            + "] TEXT,["
            + DBStaticField.HDT_COM_INDEX
            + "]TEXT, ["
            + DBStaticField.HDT_REFERRAL_NUMBER
            + "] TEXT, ["
            + DBStaticField.HDT_TERMINAL_ID
            + "] TEXT, ["
            + DBStaticField.HDT_MERCHANT_ID
            + "] TEXT, ["
            + DBStaticField.HDT_TPDU
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_NUMBER
            + "] TEXT, [" + DBStaticField.HDT_INVOICE_NUMBER
            + "] TEXT, ["
            + DBStaticField.HDT_PROCESSING_CODE
            + "] TEXT, ["
            + DBStaticField.HDT_HOST_TYPE
            + "] TEXT, ["
            + DBStaticField.HDT_HOST_LABEL
            + "] TEXT, ["
            + DBStaticField.HDT_MANUAL_ENTRY_FLAG
            + "] TEXT, ["
            + DBStaticField.HDT_SETTLEMENT_FLAG
            + "] TEXT, ["
            + DBStaticField.HDT_REVERSAL_FLAG
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_MAX_TOTAL
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_STL_LAST
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_CURR_TOTAL
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_NO_TRANS
            + "] TEXT, [" + DBStaticField.HDT_DESCRIPTION
            + "] TEXT, ["
            + DBStaticField.HDT_PAY_TERM
            + "] TEXT, ["
            + DBStaticField.HDT_PEK
            + "] TEXT, ["
            + DBStaticField.HDT_MEK
            + "] TEXT, ["
            + DBStaticField.HDT_MAC_INDEX
            + "] TEXT, ["
            + DBStaticField.HDT_CUSTOM_OPTIONS
            + "] TEXT, ["
            + DBStaticField.HDT_CURR_INDEX
            + "] TEXT, ["
            + DBStaticField.HDT_PIGGYBACK_FLAG
            + "] TEXT, ["
            + DBStaticField.HDT_MINIMUM_AMT
            + "] TEXT, ["
            + DBStaticField.HDT_RATE
            + "] TEXT, ["
            + DBStaticField.HDT_REDIRECT_IF_DISABLE
            + "] TEXT, ["
            + DBStaticField.HDT_REVERSAL_COUNT
            + "] TEXT, [" + DBStaticField.HDT_SIGCAP_INDEX
            + "] TEXT, ["
            + DBStaticField.HDT_BATCH_GROUP_NUMBER
            + "] TEXT)";


    private static String Table_CBT = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_CBT
            + "] (["
            + DBStaticField.CDT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.CDT_LO_RANGE
            + "] TEXT,["
            + DBStaticField.CDT_HI_RANGE
            + "] TEXT,["
            + DBStaticField.CDT_HDT_REFERENCE
            + "] TEXT,["
            + DBStaticField.CDT_CARD_TYPE_ARRAY
            + "] TEXT,["
            + DBStaticField.CDT_CARD_NAME + "] TEXT)";


    private static String Table_CTT = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_CTT
            + "] (["
            + DBStaticField.CTT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.CTT_CARD_TYPE
            + "] TEXT,["
            + DBStaticField.CTT_CARD_LABEL
            + "] TEXT,["
            + DBStaticField.CTT_CARD_FORMAT
            + "] TEXT,["
            + DBStaticField.CTT_MASK_FORMAT
            + "] TEXT,["
            + DBStaticField.CTT_MAGSTRIPE_FLOOR_LIMIT
            + "] TEXT,["
            + DBStaticField.CTT_DISABLE_LUHN
            + "] TEXT,["
            + DBStaticField.CTT_CUSTOM_OPTIONS
            + "] TEXT,["
            + DBStaticField.CTT_CVV_FDBC_ENABLE
            + "] TEXT,["
            + DBStaticField.CTT_PAN_MASK_ARRAY
            + "] TEXT,["
            + DBStaticField.CTT_EXPIRY_MASK_ARRAY
            + "] TEXT,["
            + DBStaticField.CTT_QPSL
            + "] TEXT,["
            + DBStaticField.CTT_DISABLE_EXPIRY_CHECK
            + "] TEXT,["
            + DBStaticField.CTT_MC501 + "] TEXT)";



    private static String Table_Password = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_PWD
            + "] (["
            + DBStaticField.PWD_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.DEFAULT_PASSWORD
            + "] TEXT,["
            + DBStaticField.REFUND_PASWORD
            + "] TEXT,["
            + DBStaticField.PRE_AUTH_PASSWORD
            + "] TEXT,["
            + DBStaticField.BALANCE_PASSWORD
            + "] TEXT,["
            + DBStaticField.TIP_ADJUST_PASSWORD
             + "] TEXT,["
            + DBStaticField.OFFLINE_PASSWORD
            + "] TEXT,["
            + DBStaticField.SETTLEMENT_PASSWORD
            + "] TEXT,["
            + DBStaticField.EDITOR_PASSWORD
            + "] TEXT,["
            + DBStaticField.VOID_PASSWORD
            + "] TEXT,["
            + DBStaticField.MANUAL_ENTRY_PASSWORD
            + "] TEXT,["
            + DBStaticField.CASH_ADVANCED_PASSWORD
            + "] TEXT,["
            + DBStaticField.TERMINAL_POWERON_PASSWORD + "] TEXT)";


    private static String Table_TransactionControlTable = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_TCT
            + "] (["
            + DBStaticField.TCT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.VOID_CTRL
            + "] TEXT,["
            + DBStaticField.SETTLEMENT_CTRL
            + "] TEXT,["
            + DBStaticField.SALE_CTRL
            + "] TEXT,["
            + DBStaticField.AUTH_CTRL
            + "] TEXT,["
            + DBStaticField.REFUND_CTRL
            + "] TEXT,["
            + DBStaticField.ADJUSTMENT_CTRL
            + "] TEXT,["
            + DBStaticField.OFFLINE_CTRL
            + "] TEXT,["
            + DBStaticField.MANUAL_ENTRY_CTRL
            + "] TEXT,["
            + DBStaticField.BALANCE_CTRL
            + "] TEXT,["
            + DBStaticField.CASH_ADVANCE_CTRL
            + "] TEXT,["
            + DBStaticField.PURCHASE_TIP_REQUEST_CTRL
            + "] TEXT,["
            + DBStaticField.TIP_CTRL + "] TEXT)";


    private static String Table_EZLINK = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.EZLINK_TABLE
            + "] (["
            + DBStaticField.Ezlink_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.EZLINK_ENABLE
            + "] TEXT,["
            + DBStaticField.EZLINK_SAM_KEY
            + "] TEXT,["
            + DBStaticField.EZLINK_PAYMENT_TRP
            + "] TEXT,["
            + DBStaticField.EZLINK_TOPUP_TRP
            + "] TEXT,["
            + DBStaticField.EZLINK_PAYMENT_DEVICE_TYPE
            + "] TEXT,["
            + DBStaticField.EZLINK_TOPUP_DEVICE_TYPE
            + "] TEXT,["
            + DBStaticField.EZLINK_BLACK_LIST_LAST_UPDATE
            + "] TEXT,["
            + DBStaticField.EZLINK_TOPUP_PAYMENT_MODE + "] TEXT)";


    private static String Table_ETHERNET = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_ETHERNET
            + "] (["
            + DBStaticField.ETHERNET_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.LOCAL_IP
            + "] TEXT,["
            + DBStaticField.SUBNET_MASK
            + "] TEXT,["
            + DBStaticField.GATEWAY
            + "] TEXT,["
            + DBStaticField.DNS1
            + "] TEXT,["
            + DBStaticField.DNS2 + "] TEXT)";


    private static String Table_CURRENCY = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_CURRENCY
            + "] (["
            + DBStaticField.CURRENCY_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.CURR_LABEL
            + "] TEXT,["
            + DBStaticField.CURR_EXPONENT
            + "] TEXT,["
            + DBStaticField.CURR_CODE + "] TEXT)";


    private static String Table_COMMOS = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.COMMS_TABLE
            + "] (["
            + DBStaticField.COMMOS_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.COM_DESCRIPTION
            + "] TEXT,["
            + DBStaticField.COM_PRIMARY_TYPE
            + "] TEXT,["
            + DBStaticField.COM_SECONDARY_TYPE
            + "] TEXT,["
            + DBStaticField.COM_MODEM_PRIMARY_NUMBER
            + "] TEXT,["
            + DBStaticField.COM_MODEM_SECONDARY_NUMBER
            + "] TEXT,["
            + DBStaticField.COM_MODEM_STRING
            + "] TEXT,["
            + DBStaticField.COM_MODEM_DISABLE_LINE_DETECT
            + "] TEXT,["
            + DBStaticField.COM_MODEM_TIMEOUT
            + "] TEXT,["
            + DBStaticField.COM_PRIMARY_IP_PORT
            + "] TEXT,["
            + DBStaticField.COM_SECONDARY_IP_PORT
            + "] TEXT,["
            + DBStaticField.COM_IP_TIMEOUT
            + "] TEXT,["
            + DBStaticField.COM_CONNECT_SECONDARY
            + "] TEXT,["
            + DBStaticField.COM_SSL_INDEX
            + "] TEXT,["
            + DBStaticField.COM_MODEM_INDEX
            + "] TEXT,["
            + DBStaticField.COM_PPP_USER_ID
            + "] TEXT,["
            + DBStaticField.COM_PPP_PASSWORD
            + "] TEXT,["
            + DBStaticField.COM_PPP_MODEM_STRING
            + "] TEXT,["
            + DBStaticField.COM_PPP_TIMEOUT + "] TEXT)";


    private static String Table_Limit = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_LIMIT
            + "] (["
            + DBStaticField.LIMIT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.MAXIMUM_SALE_AMOUNT
            + "] TEXT,["
            + DBStaticField.MAXIMUM_OFFLINE_AMOUNT
            + "] TEXT,["
            + DBStaticField.MAXIMUM_PREAUTH_AMOUNT
            + "] TEXT,["
            + DBStaticField.MAXIMUM_REFUND_AMOUNT + "] TEXT)";


    private static String Table_Masking = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_MASKING
            + "] (["
            + DBStaticField.MASKING_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.DR_PAN_UNMASK
            + "] TEXT,["
            + DBStaticField.DR_EXP_UNMASK
            + "] TEXT,["
            + DBStaticField.DISPLAY_UNMASK + "] TEXT)";

    private static String Table_Receipt = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_RECEIPT
            + "] (["
            + DBStaticField.RECEIPT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.PRINT_TIMEOUT
            + "] TEXT,["
            + DBStaticField.AUTO_PRINT
            + "] TEXT,["
            + DBStaticField.PRINTER_INTENSITY_CFG
            + "] TEXT,["
            + DBStaticField.PRINTER_CFG + "] TEXT)";


    private static String Table_Trace = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_TRACE
            + "] (["
            + DBStaticField.TRACE_UNIQUE_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.SYSTEM_TRACE
            + "] TEXT)";

    private static String Table_Utility = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_UTILITY
            + "] (["
            + DBStaticField.UTILITY_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.ADDITIONAL_PROMPT
            + "] TEXT,["
            + DBStaticField.DAILY_SETTLEMENT_FLAG
            + "] TEXT,["
            + DBStaticField.LAST_4_DIGIT_PROMPT_FLAG
            + "] TEXT,["
            + DBStaticField.INSERT_2_SWIPE
            + "] TEXT,["
            + DBStaticField.PIGGYBACK_FLAG
            + "] TEXT,["
            + DBStaticField.PINBYPASS
            + "] TEXT,["
            + DBStaticField.AUTO_SETTLE_TIME
            + "] TEXT,["
            + DBStaticField.LAST_AUTO_SETTLEMENT_DATETIME
            + "] TEXT,["
            + DBStaticField.UTRN_PREFIX
            + "] TEXT,["
            + DBStaticField.DEFAULT_APPROVAL_CODE + "] TEXT)";


    private static String Table_Merchant = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_MERCHANT
            + "] (["
            + DBStaticField.MERCHANT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.MERCHANT_NAME
            + "] TEXT,["
            + DBStaticField.MERCHANT_HEADER1
            + "] TEXT,["
            + DBStaticField.MERCHANT_HEADER2
            + "] TEXT,["
            + DBStaticField.ADDRESS_LINE1
            + "] TEXT,["
            + DBStaticField.ADDRESS_LINE2
            + "] TEXT,["
            + DBStaticField.ADDRESS_LINE3
            + "] TEXT,["
            + DBStaticField.ADDRESS_LINE4
            + "] TEXT,["
            + DBStaticField.MERCHANT_FOOTER1
            + "] TEXT,["
            + DBStaticField.MERCHANT_FOOTER2 + "] TEXT)";


    private static String Table_HTT = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_HTT
            + "] (["
            + DBStaticField.HTT_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.TRANSMISSION_MODE
            + "] TEXT,["
            + DBStaticField.CONNECTION_TIMEOUT
            + "] TEXT,["
            + DBStaticField.REDIAL_TIMEOUT
            + "] TEXT,["
            + DBStaticField.PABX
            + "] TEXT,["
            + DBStaticField.MODEM_STRING + "] TEXT)";


    private static String Table_ReportTable = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_REPORT
            + "] (["
            + DBStaticField.REPORTTABLE_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.DETAILED_REPORT
            + "] TEXT,["
            + DBStaticField.TIP_REPORT
            + "] TEXT,["
            + DBStaticField.TOTAL_REPORT + "] TEXT)";

    private static String Table_Alipay = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.ALIPAY_TABLE
            + "] (["
            + DBStaticField.ALIPAY_ID
            + "] INTEGER PRIMARY KEY AUTOINCREMENT,["
            + DBStaticField.PARTNER_ID
            + "] TEXT,["
            + DBStaticField.SELLER_ID
            + "] TEXT,["
            + DBStaticField.REGION_CODE + "] TEXT)";

    private static String Table_Batch = "CREATE TABLE IF NOT EXISTS ["
            + DBStaticField.TABLE_BATCH
            + "] (["+ DBStaticField.BATCH_ID + "] INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "[" +DBStaticField.HDT_INDEX+ "] TEXT,"
            + "[" + DBStaticField.TRANS_TYPE + "] TEXT,"
            + "[" + DBStaticField.TRANS_MODE + "] TEXT,"
            + "[" + DBStaticField.VOIDED + "] TEXT,"
            + "[" + DBStaticField.UPLOADED + "] TEXT,"
            + "[" + DBStaticField.PROC_CODE + "] TEXT,"
            + "[" + DBStaticField.INVOICE_NUMBER + "] TEXT,"
            + "[" + DBStaticField.AMOUNT + "] TEXT,"
            + "[" + DBStaticField.TIP_AMOUNT + "] TEXT,"
            + "[" + DBStaticField.TIME + "] TEXT,"
            + "[" + DBStaticField.DATE + "] TEXT,"
            + "[" + DBStaticField.YEAR + "] TEXT,"
            + "[" + DBStaticField.ORG_MESS_ID + "] TEXT,"
            + "[" + DBStaticField.SYS_TRACE_NUM + "] TEXT,"
            + "[" + DBStaticField.DATE_EXP + "] TEXT,"
            + "[" + DBStaticField.RETR_REF_NUM + "] TEXT,"
            + "[" + DBStaticField.AUTH_ID_RESP + "] TEXT,"
            + "[" + DBStaticField.RESP_CODE + "] TEXT,"
            + "[" + DBStaticField.ACCT_NUMBER + "] TEXT,"
            + "[" + DBStaticField.PERSON_NAME + "] TEXT,"
            + "[" + DBStaticField.ORIGINAL_AMOUNT + "] TEXT,"
            + "[" + DBStaticField.ADDITIONAL_DATA + "] TEXT,"
            + "[" + DBStaticField.PAYMENT_TERM_INFO + "] TEXT,"
            + "[" + DBStaticField.PRIMARY_ACC_NUM + "] TEXT,"
            + "[" + DBStaticField.POS_ENT_MODE + "] TEXT,"
            + "[" + DBStaticField.NII + "] TEXT,"
            + "[" + DBStaticField.POS_COND_CODE + "] TEXT,"
            + "[" + DBStaticField.ADD_AMOUNT + "] TEXT,"
            + "[" + DBStaticField.CARD_TYPE + "] TEXT,"
            + "[" + DBStaticField.CARD_EQUENCE + "] TEXT,"
            + "[" + DBStaticField.CHIPDATA + "] TEXT,"
            + "[" + DBStaticField.TVRVALUE + "] TEXT,"
            + "[" + DBStaticField.TSIVALUE + "] TEXT,"
            + "[" + DBStaticField.TRANSCRYTO + "] TEXT,"
            + "[" + DBStaticField.TOTALSCRIPT71 + "] TEXT,"
            + "[" + DBStaticField.TOTALSCRIPT72 + "] TEXT,"
            + "[" + DBStaticField.SCRIPTRESULT71 + "] TEXT,"
            + "[" + DBStaticField.SCRIPTRESULT72 + "] TEXT,"
            + "[" + DBStaticField.CHAID + "] TEXT,"
            + "[" + DBStaticField.APPLICATION_LABEL + "] TEXT,"
            + "[" + DBStaticField.CLS_SCHEME_ID + "] TEXT,"
            + "[" + DBStaticField.SIGNATURE_REQ + "] TEXT)";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Table_HOST);
            db.execSQL(Table_CBT);
            db.execSQL(Table_CTT);
            db.execSQL(Table_Password);
            db.execSQL(Table_TransactionControlTable);
            db.execSQL(Table_EZLINK);
            db.execSQL(Table_ETHERNET);
            db.execSQL(Table_CURRENCY);
            db.execSQL(Table_COMMOS);
            db.execSQL(Table_Limit);
            db.execSQL(Table_Merchant);
            db.execSQL(Table_HTT);
            db.execSQL(Table_ReportTable);
            db.execSQL(Table_Masking);
            db.execSQL(Table_Utility);
            db.execSQL(Table_Receipt);
            db.execSQL(Table_Alipay);
            db.execSQL(Table_Batch);
            db.execSQL(Table_Trace);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_HOST);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_CBT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_CTT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_PWD);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_TCT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_ETHERNET);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_CURRENCY);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_LIMIT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_MASKING);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_RECEIPT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_UTILITY);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_TRACE);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_MERCHANT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_REPORT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.EZLINK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.COMMS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_HTT);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.ALIPAY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "
                    + DBStaticField.TABLE_BATCH);
            onCreate(db);
        }
    }

    public void deleteallvalues(String TABLE_NAME){
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME;
        try {
            db.execSQL(sql);
            Log.i(TAG,"DBHELPER::Delete ALL Data Table Executed");
        } catch (SQLException e) {
            Log.i(TAG,"DBHELPER::Delete ALL Data Exception"+e.getMessage().toString());
        }
    }

    public void dropTable(String TABLE_NAME){
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        String sql = "drop table " + TABLE_NAME;
        try {
            db.execSQL(sql);
            Log.i(TAG,"DBHELPER::Drop Table Executed");
        } catch (SQLException e) {
            Log.i(TAG,"DBHELPER::Drop Table Exception"+e.getMessage().toString());
        }
    }

    public void CreateTable(String tableName){
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        try {
            //if(tableName.equals("Table_Reversal_Batch"))
            // db.execSQL(Table_Reversal_Batch);
            ///else if(tableName.equals("Table_Upload_Batch"))
                //db.execSQL(Table_Upload_Batch);

            Log.i(TAG,"DBHELPER::Table Created Successfully");
        }catch (SQLException e) {
            Log.i(TAG,"DBHELPER::Table Created Exceptions:::"+ e.getMessage().toString());
        }

    }

    public void CreateTables(){
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        try {
            db.execSQL(Table_HOST);
            db.execSQL(Table_CBT);
            db.execSQL(Table_CTT);
            db.execSQL(Table_Password);
            db.execSQL(Table_TransactionControlTable);
            db.execSQL(Table_EZLINK);
            db.execSQL(Table_ETHERNET);
            db.execSQL(Table_CURRENCY);
            db.execSQL(Table_COMMOS);
            db.execSQL(Table_Limit);
            db.execSQL(Table_Merchant);
            db.execSQL(Table_HTT);
            db.execSQL(Table_ReportTable);
            db.execSQL(Table_Masking);
            db.execSQL(Table_Utility);
            db.execSQL(Table_Receipt);
            db.execSQL(Table_Alipay);
            db.execSQL(Table_Trace);
            db.execSQL(Table_Batch);
            Log.i(TAG,"DBHELPER::Tables Created Successfully");
        }catch (SQLException e) {
            Log.i(TAG,"DBHELPER::Tables Created Exceptions:::"+ e.getMessage().toString());
        }

    }


    // ---opens the database---
    public DBHelper open() throws SQLException {
        myDataBase = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }



    public boolean insertMerchantData(MerchantModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.MERCHANT_NAME, model.getMERCHANT_NAME());
        contentValues.put(DBStaticField.MERCHANT_HEADER1, model.getMERCHANT_HEADER1());
        contentValues.put(DBStaticField.MERCHANT_HEADER2, model.getMERCHANT_HEADER2());
        contentValues.put(DBStaticField.ADDRESS_LINE1, model.getADDRESS_LINE1());
        contentValues.put(DBStaticField.ADDRESS_LINE2, model.getADDRESS_LINE2());
        contentValues.put(DBStaticField.ADDRESS_LINE3, model.getADDRESS_LINE3());
        contentValues.put(DBStaticField.ADDRESS_LINE4, model.getADDRESS_LINE4());
        contentValues.put(DBStaticField.MERCHANT_FOOTER1, model.getMERCHANT_FOOTER1());
        contentValues.put(DBStaticField.MERCHANT_FOOTER2, model.getMERCHANT_FOOTER2());

        db.insert(DBStaticField.TABLE_MERCHANT, null, contentValues);
        return true;
    }

    public MerchantModel getMerchantData(int inRecord_Num) {
        MerchantModel merchantModel=new MerchantModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_MERCHANT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {

                merchantModel.setMERCHANT_NAME(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_NAME)));
                merchantModel.setMERCHANT_HEADER1(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_HEADER1)));
                merchantModel.setMERCHANT_HEADER2(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_HEADER2)));
                merchantModel.setADDRESS_LINE1(res.getString(res.getColumnIndex(DBStaticField.ADDRESS_LINE1)));
                merchantModel.setADDRESS_LINE2(res.getString(res.getColumnIndex(DBStaticField.ADDRESS_LINE2)));
                merchantModel.setADDRESS_LINE3(res.getString(res.getColumnIndex(DBStaticField.ADDRESS_LINE3)));
                merchantModel.setADDRESS_LINE4(res.getString(res.getColumnIndex(DBStaticField.ADDRESS_LINE4)));
                merchantModel.setMERCHANT_FOOTER1(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_FOOTER1)));
                merchantModel.setMERCHANT_FOOTER2(res.getString(res.getColumnIndex(DBStaticField.MERCHANT_FOOTER2)));
                break;
            }

           // array_list.add(pwdModel);
            res.moveToNext();
        }
        return merchantModel;
    }





    public boolean insertPasswordData(PasswordModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.DEFAULT_PASSWORD, model.getDEFAULT_PASSWORD());
        contentValues.put(DBStaticField.REFUND_PASWORD, model.getREFUND_PASWORD());
        contentValues.put(DBStaticField.DEFAULT_PASSWORD, model.getDEFAULT_PASSWORD());
        contentValues.put(DBStaticField.TIP_ADJUST_PASSWORD, model.getTIP_ADJUST_PASSWORD());
        contentValues.put(DBStaticField.PRE_AUTH_PASSWORD, model.getPRE_AUTH_PASSWORD());
        contentValues.put(DBStaticField.BALANCE_PASSWORD, model.getBALANCE_PASSWORD());
        contentValues.put(DBStaticField.OFFLINE_PASSWORD, model.getOFFLINE_PASSWORD());
        contentValues.put(DBStaticField.SETTLEMENT_PASSWORD, model.getSETTLEMENT_PASSWORD());
        contentValues.put(DBStaticField.EDITOR_PASSWORD, model.getEDITOR_PASSWORD());
        contentValues.put(DBStaticField.VOID_PASSWORD, model.getVOID_PASSWORD());
        contentValues.put(DBStaticField.MANUAL_ENTRY_PASSWORD, model.getMANUAL_ENTRY_PASSWORD());
        contentValues.put(DBStaticField.CASH_ADVANCED_PASSWORD, model.getCASH_ADVANCED_PASSWORD());
        contentValues.put(DBStaticField.TERMINAL_POWERON_PASSWORD, model.getTERMINAL_POWERON_PASSWORD());
        contentValues.put(DBStaticField.DEFAULT_PASSWORD, model.getDEFAULT_PASSWORD());


        db.insert(DBStaticField.TABLE_PWD, null, contentValues);
        return true;
    }


    public PasswordModel getPasswordData(int inRecord_Num) {
        PasswordModel pwdModel=new PasswordModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_PWD, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.PWD_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                pwdModel.setBALANCE_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.BALANCE_PASSWORD)));
                pwdModel.setCASH_ADVANCED_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.CASH_ADVANCED_PASSWORD)));
                pwdModel.setDEFAULT_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.DEFAULT_PASSWORD)));
                pwdModel.setEDITOR_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.EDITOR_PASSWORD)));
                pwdModel.setMANUAL_ENTRY_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.MANUAL_ENTRY_PASSWORD)));
                pwdModel.setBALANCE_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.BALANCE_PASSWORD)));
                pwdModel.setOFFLINE_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.OFFLINE_PASSWORD)));
                pwdModel.setREFUND_PASWORD(res.getString(res.getColumnIndex(DBStaticField.REFUND_PASWORD)));
                pwdModel.setTERMINAL_POWERON_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.TERMINAL_POWERON_PASSWORD)));
                break;
            }

            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return pwdModel;
    }

    public boolean InsertTransactionCtrlData(TransactionControlModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.VOID_CTRL, model.getVOID_CTRL());
        contentValues.put(DBStaticField.SETTLEMENT_CTRL, model.getSETTLEMENT_CTRL());
        contentValues.put(DBStaticField.SALE_CTRL, model.getSALE_CTRL());
        contentValues.put(DBStaticField.AUTH_CTRL, model.getAUTH_CTRL());
        contentValues.put(DBStaticField.REFUND_CTRL, model.getREFUND_CTRL());
        contentValues.put(DBStaticField.ADJUSTMENT_CTRL, model.getADJUSTMENT_CTRL());
        contentValues.put(DBStaticField.OFFLINE_CTRL, model.getOFFLINE_CTRL());
        contentValues.put(DBStaticField.MANUAL_ENTRY_CTRL, model.getMANUAL_ENTRY_CTRL());
        contentValues.put(DBStaticField.BALANCE_CTRL, model.getBALANCE_CTRL());
        contentValues.put(DBStaticField.CASH_ADVANCE_CTRL, model.getCASH_ADVANCE_CTRL());
        contentValues.put(DBStaticField.PURCHASE_TIP_REQUEST_CTRL, model.getPURCHASE_TIP_REQUEST_CTRL());
        contentValues.put(DBStaticField.TIP_CTRL, model.getTIP_CTRL());


        db.insert(DBStaticField.TABLE_TCT, null, contentValues);
        return true;
    }


    public TransactionControlModel getTransactionCtrlData(int inRecord_Num) {
        TransactionControlModel transCtrlModel=new TransactionControlModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_TCT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.TCT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                transCtrlModel.setVOID_CTRL(res.getString(res.getColumnIndex(DBStaticField.VOID_CTRL)));
                transCtrlModel.setSETTLEMENT_CTRL(res.getString(res.getColumnIndex(DBStaticField.SETTLEMENT_CTRL)));
                transCtrlModel.setSALE_CTRL(res.getString(res.getColumnIndex(DBStaticField.SALE_CTRL)));
                transCtrlModel.setAUTH_CTRL(res.getString(res.getColumnIndex(DBStaticField.AUTH_CTRL)));
                transCtrlModel.setREFUND_CTRL(res.getString(res.getColumnIndex(DBStaticField.REFUND_CTRL)));
                transCtrlModel.setADJUSTMENT_CTRL(res.getString(res.getColumnIndex(DBStaticField.ADJUSTMENT_CTRL)));
                transCtrlModel.setOFFLINE_CTRL(res.getString(res.getColumnIndex(DBStaticField.OFFLINE_CTRL)));
                transCtrlModel.setMANUAL_ENTRY_CTRL(res.getString(res.getColumnIndex(DBStaticField.MANUAL_ENTRY_CTRL)));
                transCtrlModel.setBALANCE_CTRL(res.getString(res.getColumnIndex(DBStaticField.BALANCE_CTRL)));
                transCtrlModel.setCASH_ADVANCE_CTRL(res.getString(res.getColumnIndex(DBStaticField.CASH_ADVANCE_CTRL)));
                transCtrlModel.setPURCHASE_TIP_REQUEST_CTRL(res.getString(res.getColumnIndex(DBStaticField.PURCHASE_TIP_REQUEST_CTRL)));
                transCtrlModel.setTIP_CTRL(res.getString(res.getColumnIndex(DBStaticField.TIP_CTRL)));
                break;
            }

            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return transCtrlModel;
    }

    public boolean InsertReportCtrlData(ReportsModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.DETAILED_REPORT, model.getDETAILED_REPORT());
        contentValues.put(DBStaticField.TIP_REPORT, model.getTIP_REPORT());
        contentValues.put(DBStaticField.TOTAL_REPORT, model.getTOTAL_REPORT());
        db.insert(DBStaticField.TABLE_REPORT, null, contentValues);
        return true;
    }


    public ReportsModel getReportCtrlData(int inRecord_Num) {
        ReportsModel reportCtrlModel=new ReportsModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_REPORT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.REPORTTABLE_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                reportCtrlModel.setDETAILED_REPORT(res.getString(res.getColumnIndex(DBStaticField.DETAILED_REPORT)));
                reportCtrlModel.setTIP_REPORT(res.getString(res.getColumnIndex(DBStaticField.TIP_REPORT)));
                reportCtrlModel.setTOTAL_REPORT(res.getString(res.getColumnIndex(DBStaticField.TOTAL_REPORT)));
                break;
            }

            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return reportCtrlModel;
    }

    public boolean InsertHostTransmissionModelData(HostTransmissionModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.TRANSMISSION_MODE, model.getTRANSMISSION_MODE());
        contentValues.put(DBStaticField.CONNECTION_TIMEOUT, model.getCONNECTION_TIMEOUT());
        contentValues.put(DBStaticField.REDIAL_TIMEOUT, model.getREDIAL_TIMEOUT());
        contentValues.put(DBStaticField.PABX, model.getPABX());
        contentValues.put(DBStaticField.MODEM_STRING, model.getMODEM_STRING());

        db.insert(DBStaticField.TABLE_HTT, null, contentValues);
        return true;
    }


    public HostTransmissionModel getHostTransmissionModelData(int inRecord_Num) {
        HostTransmissionModel httModel=new HostTransmissionModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_HTT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.HTT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                httModel.setTRANSMISSION_MODE(res.getString(res.getColumnIndex(DBStaticField.TRANSMISSION_MODE)));
                httModel.setCONNECTION_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.CONNECTION_TIMEOUT)));
                httModel.setREDIAL_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.REDIAL_TIMEOUT)));
                httModel.setPABX(res.getString(res.getColumnIndex(DBStaticField.PABX)));
                httModel.setMODEM_STRING(res.getString(res.getColumnIndex(DBStaticField.MODEM_STRING)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return httModel;
    }

    public boolean InsertReceiptModelData(ReceiptModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.PRINT_TIMEOUT, model.getPRINT_TIMEOUT());
        contentValues.put(DBStaticField.AUTO_PRINT, model.getAUTO_PRINT());
        contentValues.put(DBStaticField.PRINTER_INTENSITY_CFG, model.getPRINTER_INTENSITY_CFG());
        contentValues.put(DBStaticField.PRINTER_CFG, model.getPRINTER_CFG());

        db.insert(DBStaticField.TABLE_RECEIPT, null, contentValues);
        return true;
    }


    public ReceiptModel getReceiptModelData(int inRecord_Num) {
        ReceiptModel receiptModel=new ReceiptModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_RECEIPT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.RECEIPT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                receiptModel.setPRINT_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.PRINT_TIMEOUT)));
                receiptModel.setAUTO_PRINT(res.getString(res.getColumnIndex(DBStaticField.AUTO_PRINT)));
                receiptModel.setPRINTER_INTENSITY_CFG(res.getString(res.getColumnIndex(DBStaticField.PRINTER_INTENSITY_CFG)));
                receiptModel.setPRINTER_CFG(res.getString(res.getColumnIndex(DBStaticField.PRINTER_CFG)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return receiptModel;
    }



    public boolean InsertMaskingModelData(MaskingModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.DR_PAN_UNMASK, model.getDR_PAN_UNMASK());
        contentValues.put(DBStaticField.DR_EXP_UNMASK, model.getDR_EXP_UNMASK());
        contentValues.put(DBStaticField.DISPLAY_UNMASK, model.getDISPLAY_UNMASK());

        db.insert(DBStaticField.TABLE_MASKING, null, contentValues);
        return true;
    }


    public MaskingModel getMaskingModelData(int inRecord_Num) {
        MaskingModel masktModel=new MaskingModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_MASKING, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.MASKING_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                masktModel.setDR_PAN_UNMASK(res.getString(res.getColumnIndex(DBStaticField.DR_PAN_UNMASK)));
                masktModel.setDR_EXP_UNMASK(res.getString(res.getColumnIndex(DBStaticField.DR_EXP_UNMASK)));
                masktModel.setDISPLAY_UNMASK(res.getString(res.getColumnIndex(DBStaticField.DISPLAY_UNMASK)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return masktModel;
    }
    public boolean InsertLimitModelData(LimitModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.MAXIMUM_SALE_AMOUNT, model.getMAXIMUM_SALE_AMOUNT());
        contentValues.put(DBStaticField.MAXIMUM_OFFLINE_AMOUNT, model.getMAXIMUM_OFFLINE_AMOUNT());
        contentValues.put(DBStaticField.MAXIMUM_PREAUTH_AMOUNT, model.getMAXIMUM_PREAUTH_AMOUNT());
        contentValues.put(DBStaticField.MAXIMUM_REFUND_AMOUNT, model.getMAXIMUM_REFUND_AMOUNT());

        db.insert(DBStaticField.TABLE_LIMIT, null, contentValues);
        return true;
    }


    public LimitModel getLimitModelData(int inRecord_Num) {
        LimitModel limitModel=new LimitModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_LIMIT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.LIMIT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                limitModel.setMAXIMUM_SALE_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.MAXIMUM_SALE_AMOUNT)));
                limitModel.setMAXIMUM_OFFLINE_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.MAXIMUM_OFFLINE_AMOUNT)));
                limitModel.setMAXIMUM_PREAUTH_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.MAXIMUM_PREAUTH_AMOUNT)));
                limitModel.setMAXIMUM_REFUND_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.MAXIMUM_REFUND_AMOUNT)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return limitModel;
    }


    public boolean InsertUtilityTablelData(UtilityTable model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.ADDITIONAL_PROMPT, model.getADDITIONAL_PROMPT());
        contentValues.put(DBStaticField.DAILY_SETTLEMENT_FLAG, model.getDAILY_SETTLEMENT_FLAG());
        contentValues.put(DBStaticField.LAST_4_DIGIT_PROMPT_FLAG, model.getLAST_4_DIGIT_PROMPT_FLAG());
        contentValues.put(DBStaticField.INSERT_2_SWIPE, model.getINSERT_2_SWIPE());
        contentValues.put(DBStaticField.PIGGYBACK_FLAG, model.getPIGGYBACK_FLAG());
        contentValues.put(DBStaticField.PINBYPASS, model.getPINBYPASS());
        contentValues.put(DBStaticField.AUTO_SETTLE_TIME, model.getAUTO_SETTLE_TIME());
        contentValues.put(DBStaticField.UTRN_PREFIX, model.getUTRN_PREFIX());
       // contentValues.put(DBStaticField.SYSTEM_TRACE, model.getSYSTEM_TRACE());
        contentValues.put(DBStaticField.DEFAULT_APPROVAL_CODE, model.getDEFAULT_APPROVAL_CODE());

        db.insert(DBStaticField.TABLE_UTILITY, null, contentValues);
        return true;
    }


    public UtilityTable getUtilityTableData(int inRecord_Num) {
        UtilityTable utilityModel=new UtilityTable();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_UTILITY, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.UTILITY_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                utilityModel.setADDITIONAL_PROMPT(res.getString(res.getColumnIndex(DBStaticField.ADDITIONAL_PROMPT)));
                utilityModel.setDAILY_SETTLEMENT_FLAG(res.getString(res.getColumnIndex(DBStaticField.DAILY_SETTLEMENT_FLAG)));
                utilityModel.setLAST_4_DIGIT_PROMPT_FLAG(res.getString(res.getColumnIndex(DBStaticField.LAST_4_DIGIT_PROMPT_FLAG)));
                utilityModel.setINSERT_2_SWIPE(res.getString(res.getColumnIndex(DBStaticField.INSERT_2_SWIPE)));
                utilityModel.setPIGGYBACK_FLAG(res.getString(res.getColumnIndex(DBStaticField.PIGGYBACK_FLAG)));
                utilityModel.setPINBYPASS(res.getString(res.getColumnIndex(DBStaticField.PINBYPASS)));
                utilityModel.setAUTO_SETTLE_TIME(res.getString(res.getColumnIndex(DBStaticField.AUTO_SETTLE_TIME)));
                utilityModel.setUTRN_PREFIX(res.getString(res.getColumnIndex(DBStaticField.UTRN_PREFIX)));
                // utilityModel.setSYSTEM_TRACE(res.getString(res.getColumnIndex(DBStaticField.SYSTEM_TRACE)));
                utilityModel.setDEFAULT_APPROVAL_CODE(res.getString(res.getColumnIndex(DBStaticField.DEFAULT_APPROVAL_CODE)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return utilityModel;
    }

    public boolean InsertTraceNumberData(TraceNumberModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Log.i(TAG,"DBHELPER::InsertTraceNumberData_1");
        contentValues.put(DBStaticField.SYSTEM_TRACE, model.getSYSTEM_TRACE());
        Log.i(TAG,"DBHELPER::INSERT::"+model.getSYSTEM_TRACE());
        Log.i(TAG,"DBHELPER::getTRACE_UNIQUE_ID::"+model.getTRACE_UNIQUE_ID());

        db.insert(DBStaticField.TABLE_TRACE, null, contentValues);
        Log.i(TAG,"DBHELPER::Inserted Successful");
        return true;
    }
    public boolean UpdateTraceNumberData(TraceNumberModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.SYSTEM_TRACE, model.getSYSTEM_TRACE());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData::"+model.getSYSTEM_TRACE());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData:getTRACE_UNIQUE_ID::"+model.getTRACE_UNIQUE_ID());
        db.update(DBStaticField.TABLE_TRACE,contentValues,"TRACE_UNIQUE_ID="+model.getTRACE_UNIQUE_ID(),null);

        return true;
    }


    public TraceNumberModel getTraceNumberData(int inRecord_Num) {
        TraceNumberModel traceModel=new TraceNumberModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        try {

            Log.i(TAG,"DBHELPER::SELECT TRACE NUMBER");
            Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_TRACE, null);

            Log.i(TAG,"DBHELPER::SELECT TRACE NUMBER_1");
            res.moveToFirst();

            if (res.isAfterLast() == false) {
                Log.i(TAG,"DBHELPER::SELECT TRACE NUMBER_2");
                Log.i(TAG,"DBHELPER::DBStaticField.SYSTEM_TRACE"+res.getString(res.getColumnIndex(DBStaticField.SYSTEM_TRACE)));
                Log.i(TAG,"DBHELPER::DBStaticField.TRACE_UNIQUE_ID"+res.getString(res.getColumnIndex(DBStaticField.TRACE_UNIQUE_ID)));
                //if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.UTILITY_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
                {
                    traceModel.setSYSTEM_TRACE(res.getString(res.getColumnIndex(DBStaticField.SYSTEM_TRACE)));
                    traceModel.setTRACE_UNIQUE_ID(res.getString(res.getColumnIndex(DBStaticField.TRACE_UNIQUE_ID)));
                    //break;
                }
                //array_list.add(pwdModel);
                //res.moveToNext();
            }
        }catch(SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                Log.e(TAG, "Creating table " + DBStaticField.TABLE_TRACE + "because it doesn't exist!");
                SQLiteDatabase db1 = DBHelper.getWritableDatabase();
                db1.execSQL(Table_Trace);
                return null;
                // create table
                // re-run query, etc.
            }
        }
        return traceModel;
    }

    public boolean UpdateHostData(HostModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.HDT_BATCH_NUMBER, model.getHDT_BATCH_NUMBER());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData::"+model.getHDT_BATCH_NUMBER());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData:getTRACE_UNIQUE_ID::"+model.getHDT_HOST_ID());
        db.update(DBStaticField.TABLE_HOST,contentValues,"HDT_HOST_ID="+model.getHDT_HOST_ID(),null);

        return true;
    }
    public boolean InsertHostTablelData(HostModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.HDT_HOST_ID, model.getHDT_HOST_ID());
        contentValues.put(DBStaticField.HDT_HOST_ENABLED, model.getHDT_HOST_ENABLED());
        contentValues.put(DBStaticField.HDT_COM_INDEX, model.getHDT_COM_INDEX());
        contentValues.put(DBStaticField.HDT_REFERRAL_NUMBER, model.getHDT_REFERRAL_NUMBER());
        contentValues.put(DBStaticField.HDT_TERMINAL_ID, model.getHDT_TERMINAL_ID());
        contentValues.put(DBStaticField.HDT_MERCHANT_ID, model.getHDT_MERCHANT_ID());
        contentValues.put(DBStaticField.HDT_TPDU, model.getHDT_TPDU());
        contentValues.put(DBStaticField.HDT_BATCH_NUMBER, model.getHDT_BATCH_NUMBER());
        contentValues.put(DBStaticField.HDT_INVOICE_NUMBER, model.getHDT_INVOICE_NUMBER());
        contentValues.put(DBStaticField.HDT_PROCESSING_CODE, model.getHDT_PROCESSING_CODE());
        contentValues.put(DBStaticField.HDT_HOST_TYPE, model.getHDT_HOST_TYPE());
        contentValues.put(DBStaticField.HDT_HOST_LABEL, model.getHDT_HOST_LABEL());
        contentValues.put(DBStaticField.HDT_MANUAL_ENTRY_FLAG, model.getHDT_MANUAL_ENTRY_FLAG());
        contentValues.put(DBStaticField.HDT_REVERSAL_FLAG, model.getHDT_REVERSAL_FLAG());
        contentValues.put(DBStaticField.HDT_SETTLEMENT_FLAG, model.getHDT_SETTLEMENT_FLAG());
        contentValues.put(DBStaticField.HDT_BATCH_MAX_TOTAL, model.getHDT_BATCH_MAX_TOTAL());
        contentValues.put(DBStaticField.HDT_BATCH_STL_LAST, model.getHDT_BATCH_STL_LAST());
        contentValues.put(DBStaticField.HDT_BATCH_CURR_TOTAL, model.getHDT_BATCH_CURR_TOTAL());
        contentValues.put(DBStaticField.HDT_BATCH_NO_TRANS, model.getHDT_BATCH_NO_TRANS());
        contentValues.put(DBStaticField.HDT_DESCRIPTION, model.getHDT_DESCRIPTION());
        contentValues.put(DBStaticField.HDT_PAY_TERM, model.getHDT_PAY_TERM());
        contentValues.put(DBStaticField.HDT_PEK, model.getHDT_PEK());
        contentValues.put(DBStaticField.HDT_MEK, model.getHDT_MEK());
        contentValues.put(DBStaticField.HDT_MAC_INDEX, model.getHDT_MAC_INDEX());
        contentValues.put(DBStaticField.HDT_CUSTOM_OPTIONS, model.getHDT_CUSTOM_OPTIONS());
        contentValues.put(DBStaticField.HDT_CURR_INDEX, model.getHDT_CURR_INDEX());
        contentValues.put(DBStaticField.HDT_PIGGYBACK_FLAG, model.getHDT_PIGGYBACK_FLAG());
        contentValues.put(DBStaticField.HDT_MINIMUM_AMT, model.getHDT_MINIMUM_AMT());
        contentValues.put(DBStaticField.HDT_RATE, model.getHDT_RATE());
        contentValues.put(DBStaticField.HDT_REDIRECT_IF_DISABLE, model.getHDT_REDIRECT_IF_DISABLE());
        contentValues.put(DBStaticField.HDT_REVERSAL_COUNT, model.getHDT_REVERSAL_COUNT());
        contentValues.put(DBStaticField.HDT_SIGCAP_INDEX, model.getHDT_SIGCAP_INDEX());
        contentValues.put(DBStaticField.HDT_BATCH_GROUP_NUMBER, model.getHDT_BATCH_GROUP_NUMBER());

        db.insert(DBStaticField.TABLE_HOST, null, contentValues);
        return true;
    }


    public HostModel getHostTableData(int inRecord_Num) {
        HostModel hostModel=new HostModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_HOST, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                hostModel.setHDT_HOST_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_ID)));
                hostModel.setHDT_HOST_ENABLED(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_ENABLED)));
                hostModel.setHDT_COM_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_COM_INDEX)));
                hostModel.setHDT_REFERRAL_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_REFERRAL_NUMBER)));
                hostModel.setHDT_TERMINAL_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_TERMINAL_ID)));
                hostModel.setHDT_MERCHANT_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_MERCHANT_ID)));
                hostModel.setHDT_TPDU(res.getString(res.getColumnIndex(DBStaticField.HDT_TPDU)));
                hostModel.setHDT_BATCH_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_NUMBER)));
                hostModel.setHDT_INVOICE_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_INVOICE_NUMBER)));
                hostModel.setHDT_PROCESSING_CODE(res.getString(res.getColumnIndex(DBStaticField.HDT_PROCESSING_CODE)));
                hostModel.setHDT_HOST_TYPE(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_TYPE)));
                hostModel.setHDT_HOST_LABEL(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_LABEL)));
                hostModel.setHDT_MANUAL_ENTRY_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_MANUAL_ENTRY_FLAG)));
                hostModel.setHDT_REVERSAL_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_REVERSAL_FLAG)));
                hostModel.setHDT_SETTLEMENT_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_SETTLEMENT_FLAG)));
                hostModel.setHDT_BATCH_MAX_TOTAL(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_MAX_TOTAL)));
                hostModel.setHDT_BATCH_STL_LAST(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_STL_LAST)));
                hostModel.setHDT_BATCH_CURR_TOTAL(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_CURR_TOTAL)));
                hostModel.setHDT_BATCH_NO_TRANS(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_NO_TRANS)));
                hostModel.setHDT_DESCRIPTION(res.getString(res.getColumnIndex(DBStaticField.HDT_DESCRIPTION)));
                hostModel.setHDT_PAY_TERM(res.getString(res.getColumnIndex(DBStaticField.HDT_PAY_TERM)));
                hostModel.setHDT_PEK(res.getString(res.getColumnIndex(DBStaticField.HDT_PEK)));
                hostModel.setHDT_MEK(res.getString(res.getColumnIndex(DBStaticField.HDT_MEK)));
                hostModel.setHDT_MAC_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_MAC_INDEX)));
                hostModel.setHDT_CUSTOM_OPTIONS(res.getString(res.getColumnIndex(DBStaticField.HDT_CUSTOM_OPTIONS)));
                hostModel.setHDT_CURR_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_CURR_INDEX)));
                hostModel.setHDT_PIGGYBACK_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_PIGGYBACK_FLAG)));
                hostModel.setHDT_MINIMUM_AMT(res.getString(res.getColumnIndex(DBStaticField.HDT_MINIMUM_AMT)));
                hostModel.setHDT_RATE(res.getString(res.getColumnIndex(DBStaticField.HDT_RATE)));
                hostModel.setHDT_REDIRECT_IF_DISABLE(res.getString(res.getColumnIndex(DBStaticField.HDT_REDIRECT_IF_DISABLE)));
                hostModel.setHDT_REVERSAL_COUNT(res.getString(res.getColumnIndex(DBStaticField.HDT_REVERSAL_COUNT)));
                hostModel.setHDT_SIGCAP_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_SIGCAP_INDEX)));
                hostModel.setHDT_BATCH_GROUP_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_GROUP_NUMBER)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return hostModel;
    }

    public ArrayList<HostModel> getAllHostTableData() {

        ArrayList<HostModel> hostModelObjList = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_HOST, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {


            HostModel hostModel=new HostModel();
            hostModel.setHDT_HOST_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_ID)));
            hostModel.setHDT_HOST_ENABLED(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_ENABLED)));
            hostModel.setHDT_COM_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_COM_INDEX)));
            hostModel.setHDT_REFERRAL_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_REFERRAL_NUMBER)));
            hostModel.setHDT_TERMINAL_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_TERMINAL_ID)));
            hostModel.setHDT_MERCHANT_ID(res.getString(res.getColumnIndex(DBStaticField.HDT_MERCHANT_ID)));
            hostModel.setHDT_TPDU(res.getString(res.getColumnIndex(DBStaticField.HDT_TPDU)));
            hostModel.setHDT_BATCH_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_NUMBER)));
            hostModel.setHDT_INVOICE_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_INVOICE_NUMBER)));
            hostModel.setHDT_PROCESSING_CODE(res.getString(res.getColumnIndex(DBStaticField.HDT_PROCESSING_CODE)));
            hostModel.setHDT_HOST_TYPE(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_TYPE)));
            hostModel.setHDT_HOST_LABEL(res.getString(res.getColumnIndex(DBStaticField.HDT_HOST_LABEL)));
            hostModel.setHDT_MANUAL_ENTRY_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_MANUAL_ENTRY_FLAG)));
            hostModel.setHDT_REVERSAL_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_REVERSAL_FLAG)));
            hostModel.setHDT_SETTLEMENT_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_SETTLEMENT_FLAG)));
            hostModel.setHDT_BATCH_MAX_TOTAL(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_MAX_TOTAL)));
            hostModel.setHDT_BATCH_STL_LAST(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_STL_LAST)));
            hostModel.setHDT_BATCH_CURR_TOTAL(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_CURR_TOTAL)));
            hostModel.setHDT_BATCH_NO_TRANS(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_NO_TRANS)));
            hostModel.setHDT_DESCRIPTION(res.getString(res.getColumnIndex(DBStaticField.HDT_DESCRIPTION)));
            hostModel.setHDT_PAY_TERM(res.getString(res.getColumnIndex(DBStaticField.HDT_PAY_TERM)));
            hostModel.setHDT_PEK(res.getString(res.getColumnIndex(DBStaticField.HDT_PEK)));
            hostModel.setHDT_MEK(res.getString(res.getColumnIndex(DBStaticField.HDT_MEK)));
            hostModel.setHDT_MAC_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_MAC_INDEX)));
            hostModel.setHDT_CUSTOM_OPTIONS(res.getString(res.getColumnIndex(DBStaticField.HDT_CUSTOM_OPTIONS)));
            hostModel.setHDT_CURR_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_CURR_INDEX)));
            hostModel.setHDT_PIGGYBACK_FLAG(res.getString(res.getColumnIndex(DBStaticField.HDT_PIGGYBACK_FLAG)));
            hostModel.setHDT_MINIMUM_AMT(res.getString(res.getColumnIndex(DBStaticField.HDT_MINIMUM_AMT)));
            hostModel.setHDT_RATE(res.getString(res.getColumnIndex(DBStaticField.HDT_RATE)));
            hostModel.setHDT_REDIRECT_IF_DISABLE(res.getString(res.getColumnIndex(DBStaticField.HDT_REDIRECT_IF_DISABLE)));
            hostModel.setHDT_REVERSAL_COUNT(res.getString(res.getColumnIndex(DBStaticField.HDT_REVERSAL_COUNT)));
            hostModel.setHDT_SIGCAP_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_SIGCAP_INDEX)));
            hostModel.setHDT_BATCH_GROUP_NUMBER(res.getString(res.getColumnIndex(DBStaticField.HDT_BATCH_GROUP_NUMBER)));

            hostModelObjList.add(hostModel);
            res.moveToNext();
        }


        return hostModelObjList;
    }

    public boolean InsertCardBinData(CardBinModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.CDT_ID, model.getCDT_ID());
        contentValues.put(DBStaticField.CDT_LO_RANGE, model.getCDT_LO_RANGE());
        contentValues.put(DBStaticField.CDT_HI_RANGE, model.getCDT_HI_RANGE());
        contentValues.put(DBStaticField.CDT_HDT_REFERENCE, model.getCDT_HDT_REFERENCE());
        contentValues.put(DBStaticField.CDT_CARD_TYPE_ARRAY, model.getCDT_CARD_TYPE_ARRAY());
        contentValues.put(DBStaticField.CDT_CARD_NAME, model.getCDT_CARD_NAME());

        db.insert(DBStaticField.TABLE_CBT, null, contentValues);
        return true;
    }


    public CardBinModel getCardBinData(int inRecord_Num) {
        int infound=0;
        CardBinModel cardBinModel=new CardBinModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_CBT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.CDT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                cardBinModel.setCDT_ID(res.getString(res.getColumnIndex(DBStaticField.CDT_ID)));
                cardBinModel.setCDT_LO_RANGE(res.getString(res.getColumnIndex(DBStaticField.CDT_LO_RANGE)));
                cardBinModel.setCDT_HI_RANGE(res.getString(res.getColumnIndex(DBStaticField.CDT_HI_RANGE)));
                cardBinModel.setCDT_HDT_REFERENCE(res.getString(res.getColumnIndex(DBStaticField.CDT_HDT_REFERENCE)));
                cardBinModel.setCDT_CARD_TYPE_ARRAY(res.getString(res.getColumnIndex(DBStaticField.CDT_CARD_TYPE_ARRAY)));
                cardBinModel.setCDT_CARD_NAME(res.getString(res.getColumnIndex(DBStaticField.CDT_CARD_NAME)));
                infound=1;
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        if(infound == 0)
            return null;
        return cardBinModel;
    }

    public boolean InsertCardTypeData(CardTypeModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.CTT_ID, model.getCTT_ID());
        contentValues.put(DBStaticField.CTT_CARD_TYPE, model.getCTT_CARD_TYPE());
        contentValues.put(DBStaticField.CTT_CARD_LABEL, model.getCTT_CARD_LABEL());
        contentValues.put(DBStaticField.CTT_CARD_FORMAT, model.getCTT_CARD_FORMAT());
        contentValues.put(DBStaticField.CTT_MASK_FORMAT, model.getCTT_MASK_FORMAT());
        contentValues.put(DBStaticField.CTT_MAGSTRIPE_FLOOR_LIMIT, model.getCTT_MAGSTRIPE_FLOOR_LIMIT());
        contentValues.put(DBStaticField.CTT_DISABLE_LUHN, model.getCTT_DISABLE_LUHN());
        contentValues.put(DBStaticField.CTT_CUSTOM_OPTIONS, model.getCTT_CUSTOM_OPTIONS());
        contentValues.put(DBStaticField.CTT_CVV_FDBC_ENABLE, model.getCTT_CVV_FDBC_ENABLE());
        contentValues.put(DBStaticField.CTT_PAN_MASK_ARRAY, model.getCTT_PAN_MASK_ARRAY());
        contentValues.put(DBStaticField.CTT_EXPIRY_MASK_ARRAY, model.getCTT_EXPIRY_MASK_ARRAY());
        contentValues.put(DBStaticField.CTT_QPSL, model.getCTT_QPSL());
        contentValues.put(DBStaticField.CTT_DISABLE_EXPIRY_CHECK, model.getCTT_DISABLE_EXPIRY_CHECK());
        contentValues.put(DBStaticField.CTT_MC501, model.getCTT_MC501());


        db.insert(DBStaticField.TABLE_CTT, null, contentValues);
        return true;
    }


    public CardTypeModel getCardTypeData(int inRecord_Num) {
        CardTypeModel CardTypeData=new CardTypeModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_CTT, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.CTT_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                CardTypeData.setCTT_ID(res.getString(res.getColumnIndex(DBStaticField.CTT_ID)));
                CardTypeData.setCTT_CARD_TYPE(res.getString(res.getColumnIndex(DBStaticField.CTT_CARD_TYPE)));
                CardTypeData.setCTT_CARD_LABEL(res.getString(res.getColumnIndex(DBStaticField.CTT_CARD_LABEL)));
                CardTypeData.setCTT_CARD_FORMAT(res.getString(res.getColumnIndex(DBStaticField.CTT_CARD_FORMAT)));
                CardTypeData.setCTT_MASK_FORMAT(res.getString(res.getColumnIndex(DBStaticField.CTT_MASK_FORMAT)));
                CardTypeData.setCTT_MAGSTRIPE_FLOOR_LIMIT(res.getString(res.getColumnIndex(DBStaticField.CTT_MAGSTRIPE_FLOOR_LIMIT)));
                CardTypeData.setCTT_DISABLE_LUHN(res.getString(res.getColumnIndex(DBStaticField.CTT_DISABLE_LUHN)));
                CardTypeData.setCTT_CUSTOM_OPTIONS(res.getString(res.getColumnIndex(DBStaticField.CTT_CUSTOM_OPTIONS)));
                CardTypeData.setCTT_CVV_FDBC_ENABLE(res.getString(res.getColumnIndex(DBStaticField.CTT_CVV_FDBC_ENABLE)));
                CardTypeData.setCTT_PAN_MASK_ARRAY(res.getString(res.getColumnIndex(DBStaticField.CTT_PAN_MASK_ARRAY)));
                CardTypeData.setCTT_EXPIRY_MASK_ARRAY(res.getString(res.getColumnIndex(DBStaticField.CTT_EXPIRY_MASK_ARRAY)));
                CardTypeData.setCTT_QPSL(res.getString(res.getColumnIndex(DBStaticField.CTT_QPSL)));
                CardTypeData.setCTT_DISABLE_EXPIRY_CHECK(res.getString(res.getColumnIndex(DBStaticField.CTT_DISABLE_EXPIRY_CHECK)));
                CardTypeData.setCTT_MC501(res.getString(res.getColumnIndex(DBStaticField.CTT_MC501)));
                break;
            }

            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return CardTypeData;
    }

    public boolean InsertCommsData(CommsModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.COMMOS_ID, model.getCOMMOS_ID());
        contentValues.put(DBStaticField.COM_DESCRIPTION, model.getCOM_DESCRIPTION());
        contentValues.put(DBStaticField.COM_PRIMARY_TYPE, model.getCOM_PRIMARY_TYPE());
        contentValues.put(DBStaticField.COM_SECONDARY_TYPE, model.getCOM_SECONDARY_TYPE());
        contentValues.put(DBStaticField.COM_MODEM_PRIMARY_NUMBER, model.getCOM_MODEM_PRIMARY_NUMBER());
        contentValues.put(DBStaticField.COM_MODEM_SECONDARY_NUMBER, model.getCOM_MODEM_SECONDARY_NUMBER());
        contentValues.put(DBStaticField.COM_MODEM_STRING, model.getCOM_MODEM_STRING());
        contentValues.put(DBStaticField.COM_MODEM_DISABLE_LINE_DETECT, model.getCOM_MODEM_DISABLE_LINE_DETECT());
        contentValues.put(DBStaticField.COM_MODEM_TIMEOUT, model.getCOM_MODEM_TIMEOUT());
        contentValues.put(DBStaticField.COM_PRIMARY_IP_PORT, model.getCOM_PRIMARY_IP_PORT());
        contentValues.put(DBStaticField.COM_SECONDARY_IP_PORT, model.getCOM_SECONDARY_IP_PORT());
        contentValues.put(DBStaticField.COM_IP_TIMEOUT, model.getCOM_IP_TIMEOUT());
        contentValues.put(DBStaticField.COM_CONNECT_SECONDARY, model.getCOM_CONNECT_SECONDARY());
        contentValues.put(DBStaticField.COM_SSL_INDEX, model.getCOM_SSL_INDEX());
        contentValues.put(DBStaticField.COM_MODEM_INDEX, model.getCOM_MODEM_INDEX());
        contentValues.put(DBStaticField.COM_PPP_USER_ID, model.getCOM_PPP_USER_ID());
        contentValues.put(DBStaticField.COM_PPP_PASSWORD, model.getCOM_PPP_PASSWORD());
        contentValues.put(DBStaticField.COM_PPP_MODEM_STRING, model.getCOM_PPP_MODEM_STRING());
        contentValues.put(DBStaticField.COM_PPP_TIMEOUT, model.getCOM_PPP_TIMEOUT());


        db.insert(DBStaticField.COMMS_TABLE, null, contentValues);
        return true;
    }


    public CommsModel getCommsData(int inRecord_Num) {
        CommsModel commsData=new CommsModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.COMMS_TABLE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.COMMOS_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                commsData.setCOMMOS_ID(res.getString(res.getColumnIndex(DBStaticField.COMMOS_ID)));
                commsData.setCOM_DESCRIPTION(res.getString(res.getColumnIndex(DBStaticField.COM_DESCRIPTION)));
                commsData.setCOM_PRIMARY_TYPE(res.getString(res.getColumnIndex(DBStaticField.COM_PRIMARY_TYPE)));
                commsData.setCOM_SECONDARY_TYPE(res.getString(res.getColumnIndex(DBStaticField.COM_SECONDARY_TYPE)));
                commsData.setCOM_MODEM_PRIMARY_NUMBER(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_PRIMARY_NUMBER)));
                commsData.setCOM_MODEM_SECONDARY_NUMBER(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_SECONDARY_NUMBER)));
                commsData.setCOM_MODEM_STRING(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_STRING)));
                commsData.setCOM_MODEM_DISABLE_LINE_DETECT(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_DISABLE_LINE_DETECT)));
                commsData.setCOM_MODEM_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_TIMEOUT)));
                commsData.setCOM_PRIMARY_IP_PORT(res.getString(res.getColumnIndex(DBStaticField.COM_PRIMARY_IP_PORT)));
                commsData.setCOM_SECONDARY_IP_PORT(res.getString(res.getColumnIndex(DBStaticField.COM_SECONDARY_IP_PORT)));
                commsData.setCOM_IP_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.COM_IP_TIMEOUT)));
                commsData.setCOM_CONNECT_SECONDARY(res.getString(res.getColumnIndex(DBStaticField.COM_CONNECT_SECONDARY)));
                commsData.setCOM_SSL_INDEX(res.getString(res.getColumnIndex(DBStaticField.COM_SSL_INDEX)));
                commsData.setCOM_MODEM_INDEX(res.getString(res.getColumnIndex(DBStaticField.COM_MODEM_INDEX)));
                commsData.setCOM_PPP_USER_ID(res.getString(res.getColumnIndex(DBStaticField.COM_PPP_USER_ID)));
                commsData.setCOM_PPP_PASSWORD(res.getString(res.getColumnIndex(DBStaticField.COM_PPP_PASSWORD)));
                commsData.setCOM_PPP_MODEM_STRING(res.getString(res.getColumnIndex(DBStaticField.COM_PPP_MODEM_STRING)));
                commsData.setCOM_PPP_TIMEOUT(res.getString(res.getColumnIndex(DBStaticField.COM_PPP_TIMEOUT)));
                break;
            }

            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return commsData;
    }

    public boolean InsertCurrencyData(CurrencyModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.CURRENCY_ID, model.getCURRENCY_ID());
        contentValues.put(DBStaticField.CURR_LABEL, model.getCURR_LABEL());
        contentValues.put(DBStaticField.CURR_EXPONENT, model.getCURR_EXPONENT());
        contentValues.put(DBStaticField.CURR_CODE, model.getCURR_CODE());

        db.insert(DBStaticField.TABLE_CURRENCY, null, contentValues);
        return true;
    }


    public CurrencyModel getCurrencyData(int inRecord_Num) {
        CurrencyModel currenyModel=new CurrencyModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_CURRENCY, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.CURRENCY_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                currenyModel.setCURRENCY_ID(res.getString(res.getColumnIndex(DBStaticField.CURRENCY_ID)));
                currenyModel.setCURR_LABEL(res.getString(res.getColumnIndex(DBStaticField.CURR_LABEL)));
                currenyModel.setCURR_EXPONENT(res.getString(res.getColumnIndex(DBStaticField.CURR_EXPONENT)));
                currenyModel.setCURR_CODE(res.getString(res.getColumnIndex(DBStaticField.CURR_CODE)));
                if(inRecord_Num ==0 && res.getString(res.getColumnIndex(DBStaticField.CURR_LABEL)).equals(Constants.DEFAULT_CURRENCY))
                    break;
                else
                    break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return currenyModel;
    }

    public boolean InsertEthernetData(EthernetLabel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.LOCAL_IP, model.getLOCAL_IP());
        contentValues.put(DBStaticField.SUBNET_MASK, model.getSUBNET_MASK());
        contentValues.put(DBStaticField.GATEWAY, model.getGATEWAY());
        contentValues.put(DBStaticField.DNS1, model.getDNS1());
        contentValues.put(DBStaticField.DNS2, model.getDNS2());

        db.insert(DBStaticField.TABLE_ETHERNET, null, contentValues);
        return true;
    }


    public EthernetLabel getEthernetData(int inRecord_Num) {
        EthernetLabel ethernetModel=new EthernetLabel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_ETHERNET, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.ETHERNET_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                ethernetModel.setLOCAL_IP(res.getString(res.getColumnIndex(DBStaticField.LOCAL_IP)));
                ethernetModel.setSUBNET_MASK(res.getString(res.getColumnIndex(DBStaticField.SUBNET_MASK)));
                ethernetModel.setGATEWAY(res.getString(res.getColumnIndex(DBStaticField.GATEWAY)));
                ethernetModel.setDNS1(res.getString(res.getColumnIndex(DBStaticField.DNS1)));
                ethernetModel.setDNS2(res.getString(res.getColumnIndex(DBStaticField.DNS2)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();
        }
        return ethernetModel;
    }

    public boolean InsertEzlinkData(EzlinkModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.EZLINK_ENABLE, model.getEZLINK_ENABLE());
        contentValues.put(DBStaticField.EZLINK_SAM_KEY, model.getEZLINK_SAM_KEY());
        contentValues.put(DBStaticField.EZLINK_PAYMENT_TRP, model.getEZLINK_PAYMENT_TRP());
        contentValues.put(DBStaticField.EZLINK_TOPUP_TRP, model.getEZLINK_TOPUP_TRP());
        contentValues.put(DBStaticField.EZLINK_PAYMENT_DEVICE_TYPE, model.getEZLINK_PAYMENT_DEVICE_TYPE());
        contentValues.put(DBStaticField.EZLINK_TOPUP_DEVICE_TYPE, model.getEZLINK_TOPUP_DEVICE_TYPE());
        contentValues.put(DBStaticField.EZLINK_BLACK_LIST_LAST_UPDATE, model.getEZLINK_BLACK_LIST_LAST_UPDATE());
        contentValues.put(DBStaticField.EZLINK_TOPUP_PAYMENT_MODE, model.getEZLINK_TOPUP_PAYMENT_MODE());

        db.insert(DBStaticField.EZLINK_TABLE, null, contentValues);
        return true;
    }


    public EzlinkModel getEzlinkData(int inRecord_Num) {
        EzlinkModel ezlinkModel=new EzlinkModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.EZLINK_TABLE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.Ezlink_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {
                ezlinkModel.setEZLINK_ENABLE(res.getString(res.getColumnIndex(DBStaticField.EZLINK_ENABLE)));
                ezlinkModel.setEZLINK_SAM_KEY(res.getString(res.getColumnIndex(DBStaticField.EZLINK_SAM_KEY)));
                ezlinkModel.setEZLINK_PAYMENT_TRP(res.getString(res.getColumnIndex(DBStaticField.EZLINK_PAYMENT_TRP)));
                ezlinkModel.setEZLINK_TOPUP_TRP(res.getString(res.getColumnIndex(DBStaticField.EZLINK_TOPUP_TRP)));
                ezlinkModel.setEZLINK_PAYMENT_DEVICE_TYPE(res.getString(res.getColumnIndex(DBStaticField.EZLINK_PAYMENT_DEVICE_TYPE)));
                ezlinkModel.setEZLINK_TOPUP_DEVICE_TYPE(res.getString(res.getColumnIndex(DBStaticField.EZLINK_TOPUP_DEVICE_TYPE)));
                ezlinkModel.setEZLINK_BLACK_LIST_LAST_UPDATE(res.getString(res.getColumnIndex(DBStaticField.EZLINK_BLACK_LIST_LAST_UPDATE)));
                ezlinkModel.setEZLINK_TOPUP_PAYMENT_MODE(res.getString(res.getColumnIndex(DBStaticField.EZLINK_TOPUP_PAYMENT_MODE)));
                break;
            }
            //array_list.add(pwdModel);
            res.moveToNext();

        }
        return ezlinkModel;
    }

    public boolean insertBarocdeData(BarcodeModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.ALIPAY_ID, model.getALIPAY_ID());
        contentValues.put(DBStaticField.PARTNER_ID, model.getPARTNER_ID());
        contentValues.put(DBStaticField.SELLER_ID, model.getSELLER_ID());
        contentValues.put(DBStaticField.REGION_CODE, model.getREGION_CODE());

        db.insert(DBStaticField.ALIPAY_TABLE, null, contentValues);
        return true;
    }

    public BarcodeModel getBarcodeData(int inRecord_Num) {
        BarcodeModel barcodeModel=new BarcodeModel();
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.ALIPAY_TABLE, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.ALIPAY_ID))) == inRecord_Num || inRecord_Num == 0)//Read the respective record needed
            {

                barcodeModel.setALIPAY_ID(res.getString(res.getColumnIndex(DBStaticField.ALIPAY_ID)));
                barcodeModel.setPARTNER_ID(res.getString(res.getColumnIndex(DBStaticField.PARTNER_ID)));
                barcodeModel.setSELLER_ID(res.getString(res.getColumnIndex(DBStaticField.SELLER_ID)));
                barcodeModel.setREGION_CODE(res.getString(res.getColumnIndex(DBStaticField.REGION_CODE)));
                break;
            }

            // array_list.add(pwdModel);
            res.moveToNext();
        }
        return barcodeModel;
    }
    public boolean insertBatchData(BatchModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DBStaticField.BATCH_ID, model.getBATCH_ID());
        contentValues.put(DBStaticField.HDT_INDEX, model.getHDT_INDEX());
        contentValues.put(DBStaticField.TRANS_TYPE, model.getTRANS_TYPE());
        contentValues.put(DBStaticField.TRANS_MODE, model.getTRANS_MODE());
        contentValues.put(DBStaticField.VOIDED, model.getVOIDED());
        contentValues.put(DBStaticField.UPLOADED, model.getUPLOADED());
        contentValues.put(DBStaticField.PROC_CODE, model.getPROC_CODE());
        contentValues.put(DBStaticField.INVOICE_NUMBER, model.getINVOICE_NUMBER());
        contentValues.put(DBStaticField.AMOUNT, model.getAMOUNT());
        contentValues.put(DBStaticField.TIP_AMOUNT, model.getTIP_AMOUNT());
        contentValues.put(DBStaticField.TIME, model.getTIME());
        contentValues.put(DBStaticField.DATE, model.getDATE());
        contentValues.put(DBStaticField.YEAR, model.getYEAR());
        contentValues.put(DBStaticField.ORG_MESS_ID, model.getORG_MESS_ID());
        contentValues.put(DBStaticField.SYS_TRACE_NUM, model.getSYS_TRACE_NUM());
        contentValues.put(DBStaticField.DATE_EXP, model.getDATE_EXP());
        contentValues.put(DBStaticField.RETR_REF_NUM, model.getRETR_REF_NUM());
        contentValues.put(DBStaticField.AUTH_ID_RESP, model.getAUTH_ID_RESP());
        contentValues.put(DBStaticField.RESP_CODE, model.getRESP_CODE());
        contentValues.put(DBStaticField.ACCT_NUMBER, model.getACCT_NUMBER());
        contentValues.put(DBStaticField.PERSON_NAME, model.getPERSON_NAME());
        contentValues.put(DBStaticField.ORIGINAL_AMOUNT, model.getORIGINAL_AMOUNT());
        contentValues.put(DBStaticField.ADDITIONAL_DATA, model.getADDITIONAL_DATA());
        contentValues.put(DBStaticField.PAYMENT_TERM_INFO, model.getPAYMENT_TERM_INFO());
        contentValues.put(DBStaticField.PRIMARY_ACC_NUM, model.getPRIMARY_ACC_NUM());
        contentValues.put(DBStaticField.POS_ENT_MODE, model.getPOS_ENT_MODE());
        contentValues.put(DBStaticField.NII, model.getNII());
        contentValues.put(DBStaticField.POS_COND_CODE, model.getPOS_COND_CODE());
        contentValues.put(DBStaticField.ADD_AMOUNT, model.getADD_AMOUNT());
        contentValues.put(DBStaticField.CARD_TYPE, model.getCARD_TYPE());
        contentValues.put(DBStaticField.CARD_EQUENCE, model.getCARD_EQUENCE());
        contentValues.put(DBStaticField.CHIPDATA, model.getCHIPDATA());
        contentValues.put(DBStaticField.TVRVALUE, model.getTVRVALUE());
        contentValues.put(DBStaticField.TSIVALUE, model.getTSIVALUE());
        contentValues.put(DBStaticField.TRANSCRYTO, model.getTRANSCRYTO());
        contentValues.put(DBStaticField.TOTALSCRIPT71, model.getTOTALSCRIPT71());
        contentValues.put(DBStaticField.TOTALSCRIPT72, model.getTOTALSCRIPT72());
        contentValues.put(DBStaticField.SCRIPTRESULT71, model.getSCRIPTRESULT71());
        contentValues.put(DBStaticField.SCRIPTRESULT72, model.getSCRIPTRESULT72());
        contentValues.put(DBStaticField.CHAID, model.getCHAID());
        contentValues.put(DBStaticField.APPLICATION_LABEL, model.getAPPLICATION_LABEL());
        contentValues.put(DBStaticField.CLS_SCHEME_ID, model.getCLS_SCHEME_ID());
        contentValues.put(DBStaticField.SIGNATURE_REQ, model.getSIGNATURE_REQ());


        db.insert(DBStaticField.TABLE_BATCH, null, contentValues);

        return true;
    }

    public boolean UpdateBatchData(BatchModel model) {

        SQLiteDatabase db = DBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStaticField.VOIDED, model.getVOIDED());
        contentValues.put(DBStaticField.UPLOADED, model.getUPLOADED());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData::"+model.getRETR_REF_NUM());
        Log.i(TAG,"DBHELPER::UpdateTraceNumberData:getTRACE_UNIQUE_ID::"+model.getRETR_REF_NUM());
        db.update(DBStaticField.TABLE_BATCH,contentValues,"retr_ref_num="+model.getRETR_REF_NUM(),null);

        return true;
    }

    public ArrayList<BatchModel> getBatchData(String inRecord_Num) {
        ArrayList<BatchModel> batchModelObjList = new ArrayList<>();

        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_BATCH +" where "+DBStaticField.HDT_INDEX+"= "+inRecord_Num, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            BatchModel batchModel=new BatchModel();
            //if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.HDT_INDEX))) == inRecord_Num )//Read the respective record needed
            {


                batchModel.setBATCH_ID(res.getString(res.getColumnIndex(DBStaticField.BATCH_ID)));
                batchModel.setHDT_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_INDEX)));
                batchModel.setTRANS_TYPE(res.getString(res.getColumnIndex(DBStaticField.TRANS_TYPE)));
                batchModel.setTRANS_MODE(res.getString(res.getColumnIndex(DBStaticField.TRANS_MODE)));
                batchModel.setVOIDED(res.getString(res.getColumnIndex(DBStaticField.VOIDED)));
                batchModel.setUPLOADED(res.getString(res.getColumnIndex(DBStaticField.UPLOADED)));
                batchModel.setPROC_CODE(res.getString(res.getColumnIndex(DBStaticField.PROC_CODE)));
                batchModel.setINVOICE_NUMBER(res.getString(res.getColumnIndex(DBStaticField.INVOICE_NUMBER)));
                batchModel.setAMOUNT(res.getString(res.getColumnIndex(DBStaticField.AMOUNT)));
                batchModel.setTIP_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.TIP_AMOUNT)));
                batchModel.setTIME(res.getString(res.getColumnIndex(DBStaticField.TIME)));
                batchModel.setDATE(res.getString(res.getColumnIndex(DBStaticField.DATE)));
                batchModel.setYEAR(res.getString(res.getColumnIndex(DBStaticField.YEAR)));
                batchModel.setORG_MESS_ID(res.getString(res.getColumnIndex(DBStaticField.ORG_MESS_ID)));
                batchModel.setSYS_TRACE_NUM(res.getString(res.getColumnIndex(DBStaticField.SYS_TRACE_NUM)));
                batchModel.setDATE_EXP(res.getString(res.getColumnIndex(DBStaticField.DATE_EXP)));
                batchModel.setRETR_REF_NUM(res.getString(res.getColumnIndex(DBStaticField.RETR_REF_NUM)));
                batchModel.setAUTH_ID_RESP(res.getString(res.getColumnIndex(DBStaticField.AUTH_ID_RESP)));
                batchModel.setRESP_CODE(res.getString(res.getColumnIndex(DBStaticField.RESP_CODE)));
                batchModel.setACCT_NUMBER(res.getString(res.getColumnIndex(DBStaticField.ACCT_NUMBER)));
                batchModel.setPERSON_NAME(res.getString(res.getColumnIndex(DBStaticField.PERSON_NAME)));
                batchModel.setORIGINAL_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.ORIGINAL_AMOUNT)));
                batchModel.setADDITIONAL_DATA(res.getString(res.getColumnIndex(DBStaticField.ADDITIONAL_DATA)));
                batchModel.setPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
                batchModel.setPRIMARY_ACC_NUM(res.getString(res.getColumnIndex(DBStaticField.PRIMARY_ACC_NUM)));
                batchModel.setPOS_ENT_MODE(res.getString(res.getColumnIndex(DBStaticField.POS_ENT_MODE)));
                batchModel.setNII(res.getString(res.getColumnIndex(DBStaticField.NII)));
                batchModel.setPOS_COND_CODE(res.getString(res.getColumnIndex(DBStaticField.POS_COND_CODE)));
                batchModel.setADD_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.ADD_AMOUNT)));
                batchModel.setCARD_TYPE(res.getString(res.getColumnIndex(DBStaticField.CARD_TYPE)));
                batchModel.setCARD_EQUENCE(res.getString(res.getColumnIndex(DBStaticField.CARD_EQUENCE)));
                batchModel.setCHIPDATA(res.getString(res.getColumnIndex(DBStaticField.CHIPDATA)));
                batchModel.setTVRVALUE(res.getString(res.getColumnIndex(DBStaticField.TVRVALUE)));
                batchModel.setTSIVALUE(res.getString(res.getColumnIndex(DBStaticField.TSIVALUE)));
                batchModel.setTRANSCRYTO(res.getString(res.getColumnIndex(DBStaticField.TRANSCRYTO)));
                batchModel.setTOTALSCRIPT71(res.getString(res.getColumnIndex(DBStaticField.TOTALSCRIPT71)));
                batchModel.setTOTALSCRIPT72(res.getString(res.getColumnIndex(DBStaticField.TOTALSCRIPT72)));
                batchModel.setSCRIPTRESULT71(res.getString(res.getColumnIndex(DBStaticField.SCRIPTRESULT71)));
                batchModel.setSCRIPTRESULT72(res.getString(res.getColumnIndex(DBStaticField.SCRIPTRESULT72)));
                batchModel.setCHAID(res.getString(res.getColumnIndex(DBStaticField.CHAID)));
                batchModel.setAPPLICATION_LABEL(res.getString(res.getColumnIndex(DBStaticField.APPLICATION_LABEL)));
                batchModel.setCLS_SCHEME_ID(res.getString(res.getColumnIndex(DBStaticField.CLS_SCHEME_ID)));
                batchModel.setSIGNATURE_REQ(res.getString(res.getColumnIndex(DBStaticField.SIGNATURE_REQ)));


                //break;
            }

            batchModelObjList.add(batchModel);
            res.moveToNext();
        }
        return batchModelObjList;
    }

    public BatchModel getBatchDataUsngInvoice(String inRecord_Num) {
        //BatchModel batchModelObjList = new BatchModel();
        BatchModel batchModel=new BatchModel();
        int inError =0;

        SQLiteDatabase db = DBHelper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + DBStaticField.TABLE_BATCH +" where "+DBStaticField.INVOICE_NUMBER+"= '"+ inRecord_Num +"'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            //if(Integer.parseInt(res.getString(res.getColumnIndex(DBStaticField.HDT_INDEX))) == inRecord_Num )//Read the respective record needed
            {
                inError=1;

                batchModel.setBATCH_ID(res.getString(res.getColumnIndex(DBStaticField.BATCH_ID)));
                batchModel.setHDT_INDEX(res.getString(res.getColumnIndex(DBStaticField.HDT_INDEX)));
                batchModel.setTRANS_TYPE(res.getString(res.getColumnIndex(DBStaticField.TRANS_TYPE)));
                batchModel.setTRANS_MODE(res.getString(res.getColumnIndex(DBStaticField.TRANS_MODE)));
                batchModel.setVOIDED(res.getString(res.getColumnIndex(DBStaticField.VOIDED)));
                batchModel.setUPLOADED(res.getString(res.getColumnIndex(DBStaticField.UPLOADED)));
                batchModel.setPROC_CODE(res.getString(res.getColumnIndex(DBStaticField.PROC_CODE)));
                batchModel.setINVOICE_NUMBER(res.getString(res.getColumnIndex(DBStaticField.INVOICE_NUMBER)));
                batchModel.setAMOUNT(res.getString(res.getColumnIndex(DBStaticField.AMOUNT)));
                batchModel.setTIP_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.TIP_AMOUNT)));
                batchModel.setTIME(res.getString(res.getColumnIndex(DBStaticField.TIME)));
                batchModel.setDATE(res.getString(res.getColumnIndex(DBStaticField.DATE)));
                batchModel.setYEAR(res.getString(res.getColumnIndex(DBStaticField.YEAR)));
                batchModel.setORG_MESS_ID(res.getString(res.getColumnIndex(DBStaticField.ORG_MESS_ID)));
                batchModel.setSYS_TRACE_NUM(res.getString(res.getColumnIndex(DBStaticField.SYS_TRACE_NUM)));
                batchModel.setDATE_EXP(res.getString(res.getColumnIndex(DBStaticField.DATE_EXP)));
                batchModel.setRETR_REF_NUM(res.getString(res.getColumnIndex(DBStaticField.RETR_REF_NUM)));
                batchModel.setAUTH_ID_RESP(res.getString(res.getColumnIndex(DBStaticField.AUTH_ID_RESP)));
                batchModel.setRESP_CODE(res.getString(res.getColumnIndex(DBStaticField.RESP_CODE)));
                batchModel.setACCT_NUMBER(res.getString(res.getColumnIndex(DBStaticField.ACCT_NUMBER)));
                batchModel.setPERSON_NAME(res.getString(res.getColumnIndex(DBStaticField.PERSON_NAME)));
                batchModel.setORIGINAL_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.ORIGINAL_AMOUNT)));
                batchModel.setADDITIONAL_DATA(res.getString(res.getColumnIndex(DBStaticField.ADDITIONAL_DATA)));
                batchModel.setPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
                batchModel.setPRIMARY_ACC_NUM(res.getString(res.getColumnIndex(DBStaticField.PRIMARY_ACC_NUM)));
                batchModel.setPOS_ENT_MODE(res.getString(res.getColumnIndex(DBStaticField.POS_ENT_MODE)));
                batchModel.setNII(res.getString(res.getColumnIndex(DBStaticField.NII)));
                batchModel.setPOS_COND_CODE(res.getString(res.getColumnIndex(DBStaticField.POS_COND_CODE)));
                batchModel.setADD_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.ADD_AMOUNT)));
                batchModel.setCARD_TYPE(res.getString(res.getColumnIndex(DBStaticField.CARD_TYPE)));
                batchModel.setCARD_EQUENCE(res.getString(res.getColumnIndex(DBStaticField.CARD_EQUENCE)));
                batchModel.setCHIPDATA(res.getString(res.getColumnIndex(DBStaticField.CHIPDATA)));
                batchModel.setTVRVALUE(res.getString(res.getColumnIndex(DBStaticField.TVRVALUE)));
                batchModel.setTSIVALUE(res.getString(res.getColumnIndex(DBStaticField.TSIVALUE)));
                batchModel.setTRANSCRYTO(res.getString(res.getColumnIndex(DBStaticField.TRANSCRYTO)));
                batchModel.setTOTALSCRIPT71(res.getString(res.getColumnIndex(DBStaticField.TOTALSCRIPT71)));
                batchModel.setTOTALSCRIPT72(res.getString(res.getColumnIndex(DBStaticField.TOTALSCRIPT72)));
                batchModel.setSCRIPTRESULT71(res.getString(res.getColumnIndex(DBStaticField.SCRIPTRESULT71)));
                batchModel.setSCRIPTRESULT72(res.getString(res.getColumnIndex(DBStaticField.SCRIPTRESULT72)));
                batchModel.setCHAID(res.getString(res.getColumnIndex(DBStaticField.CHAID)));
                batchModel.setAPPLICATION_LABEL(res.getString(res.getColumnIndex(DBStaticField.APPLICATION_LABEL)));
                batchModel.setCLS_SCHEME_ID(res.getString(res.getColumnIndex(DBStaticField.CLS_SCHEME_ID)));
                batchModel.setSIGNATURE_REQ(res.getString(res.getColumnIndex(DBStaticField.SIGNATURE_REQ)));


                //break;
            }

           // batchModelObjList.add(batchModel);
            res.moveToNext();
        }
        if(inError == 0)
            return null;
        return batchModel;
    }



}