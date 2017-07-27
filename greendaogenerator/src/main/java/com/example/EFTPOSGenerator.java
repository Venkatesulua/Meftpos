package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class EFTPOSGenerator {

    public static final String TABLE_HOST ="HostModel";
    public static final String TABLE_CBT ="CardBinModel";
    public static final String TABLE_CTT ="CardTypeModel";
    public static final String TABLE_PWD ="PasswordModel";
    public static final String TABLE_TCT="TransactionControlModel";
    public static final String TABLE_ETHERNET="EthernetModel";
    public static final String TABLE_CURRENCY="CurrencyModel";
    public static final String TABLE_LIMIT="LimitModel";
    public static final String TABLE_MASKING="MaskingModel";
    public static final String TABLE_MERCHANT="MerchantModel";
    public static final String TABLE_HTT="HTTModel";
    public static final String TABLE_REPORT="ReportModel";
    public static final String COMMS_TABLE="CommsModel";
    public static final String TABLE_TRACE="TraceModel";
    public static final String EZLINK_TABLE="EzlinkModel";
    public static final String ALIPAY_TABLE="AlipayModel";
    public static final String TABLE_BATCH="BatchModel";
    public static final String TABLE_RECEIPT="ReceiptModel";
    public static final String TABLE_UTILITY="UtilityModel";

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.mobileeftpos.android.eftpos.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addHostEntity(schema);
        addCardBinEntity(schema);
        addCardTypeEntity(schema);
        addPasswordEntity(schema);
        addBatchFileEntity(schema);
        addAlipayEntity(schema);
        addReceiptEntity(schema);
        addUtilityEntity(schema);
        addMaskingEntity(schema);
        addLimitEntity(schema);
        addEthernetEntity(schema);
        addCurrencyEntity(schema);
        addTransactionControlEntity(schema);
        addEzlinkEntity(schema);
        addCommosEntity(schema);
        addTraceEntity(schema);
        addHTTEntity(schema);
        addMerchantEntity(schema);
        addReportEntity(schema);
    }


    private static Entity addHostEntity(final Schema schema) {
        Entity hostTable = schema.addEntity(TABLE_HOST);
        hostTable.addIdProperty().primaryKey().autoincrement();
        hostTable.addStringProperty("HDT_HOST_ID");
        hostTable.addStringProperty("HDT_HOST_ENABLED");
        hostTable.addStringProperty("HDT_COM_INDEX");
        hostTable.addStringProperty("HDT_REFERRAL_NUMBER");
        hostTable.addStringProperty("HDT_TERMINAL_ID");
        hostTable.addStringProperty("HDT_MERCHANT_ID");
        hostTable.addStringProperty("HDT_TPDU");
        hostTable.addStringProperty("HDT_BATCH_NUMBER");
        hostTable.addStringProperty("HDT_INVOICE_NUMBER");
        hostTable.addStringProperty("HDT_PROCESSING_CODE");
        hostTable.addStringProperty("HDT_HOST_TYPE");
        hostTable.addStringProperty("HDT_HOST_LABEL");
        hostTable.addStringProperty("HDT_MANUAL_ENTRY_FLAG");
        hostTable.addStringProperty("HDT_REVERSAL_FLAG");
        hostTable.addStringProperty("HDT_SETTLEMENT_FLAG");
        hostTable.addStringProperty("HDT_BATCH_MAX_TOTAL");
        hostTable.addStringProperty("HDT_BATCH_STL_LAST");
        hostTable.addStringProperty("HDT_BATCH_CURR_TOTAL");
        hostTable.addStringProperty("HDT_BATCH_NO_TRANS");
        hostTable.addStringProperty("HDT_DESCRIPTION");
        hostTable.addStringProperty("HDT_PAY_TERM");
        hostTable.addStringProperty("HDT_PEK");
        hostTable.addStringProperty("HDT_MEK");
        hostTable.addStringProperty("HDT_MAC_INDEX");
        hostTable.addStringProperty("HDT_CUSTOM_OPTIONS");
        hostTable.addStringProperty("HDT_CURR_INDEX");
        hostTable.addStringProperty("HDT_PIGGYBACK_FLAG");
        hostTable.addStringProperty("HDT_MINIMUM_AMT");
        hostTable.addStringProperty("HDT_RATE");
        hostTable.addStringProperty("HDT_REDIRECT_IF_DISABLE");
        hostTable.addStringProperty("HDT_REVERSAL_COUNT");
        hostTable.addStringProperty("HDT_SIGCAP_INDEX");
        hostTable.addStringProperty("HDT_BATCH_GROUP_NUMBER");
        return hostTable;

    }



    private static Entity addCardBinEntity(final Schema schema) {
        Entity cardBinTable = schema.addEntity(TABLE_CBT);
        cardBinTable.addIdProperty().primaryKey().autoincrement();
        cardBinTable.addStringProperty("CDT_ID");
        cardBinTable.addStringProperty("CDT_LO_RANGE");
        cardBinTable.addStringProperty("CDT_HI_RANGE");
        cardBinTable.addStringProperty("CDT_HDT_REFERENCE");
        cardBinTable.addStringProperty("CDT_CARD_TYPE_ARRAY");
        cardBinTable.addStringProperty("CDT_CARD_NAME");
        return cardBinTable;

    }

    private static Entity addLimitEntity(final Schema schema) {
        Entity limitTable = schema.addEntity(TABLE_LIMIT);
        limitTable.addIdProperty().primaryKey().autoincrement();
        limitTable.addStringProperty("LIMIT_ID");
        limitTable.addStringProperty("MAXIMUM_SALE_AMOUNT");
        limitTable.addStringProperty("MAXIMUM_OFFLINE_AMOUNT");
        limitTable.addStringProperty("MAXIMUM_PREAUTH_AMOUNT");
        limitTable.addStringProperty("MAXIMUM_REFUND_AMOUNT");
        return limitTable;

    }


    private static Entity addTraceEntity(final Schema schema) {
        Entity traceTable = schema.addEntity(TABLE_TRACE);
        traceTable.addIdProperty().primaryKey().autoincrement();
         traceTable.addStringProperty("TRACE_UNIQUE_ID");
        traceTable.addStringProperty("SYSTEM_TRACE");
        return traceTable;

    }




    private static Entity addCommosEntity(final Schema schema) {
        Entity commosTable = schema.addEntity(COMMS_TABLE);
        commosTable.addIdProperty().primaryKey().autoincrement();
        commosTable.addStringProperty("COMMOS_ID");
        commosTable.addStringProperty("COM_DESCRIPTION");
        commosTable.addStringProperty("COM_PRIMARY_TYPE");
        commosTable.addStringProperty("COM_SECONDARY_TYPE");
        commosTable.addStringProperty("COM_MODEM_PRIMARY_NUMBER");
        commosTable.addStringProperty("COM_MODEM_SECONDARY_NUMBER");
        commosTable.addStringProperty("COM_MODEM_STRING");
        commosTable.addStringProperty("COM_MODEM_DISABLE_LINE_DETECT");
        commosTable.addStringProperty("COM_MODEM_TIMEOUT");
        commosTable.addStringProperty("COM_PRIMARY_IP_PORT");
        commosTable.addStringProperty("COM_SECONDARY_IP_PORT");
        commosTable.addStringProperty("COM_IP_TIMEOUT");
        commosTable.addStringProperty("COM_CONNECT_SECONDARY");
        commosTable.addStringProperty("COM_SSL_INDEX");
        commosTable.addStringProperty("COM_MODEM_INDEX");
        commosTable.addStringProperty("COM_PPP_USER_ID");
        commosTable.addStringProperty("COM_PPP_PASSWORD");
        commosTable.addStringProperty("COM_PPP_MODEM_STRING");
        commosTable.addStringProperty("COM_PPP_TIMEOUT");

        return commosTable;

    }



    private static Entity addTransactionControlEntity(final Schema schema) {
        Entity transactionControlTable = schema.addEntity(TABLE_TCT);
        transactionControlTable.addIdProperty().primaryKey().autoincrement();
        transactionControlTable.addStringProperty("TCT_ID");
        transactionControlTable.addStringProperty("VOID_CTRL");
        transactionControlTable.addStringProperty("SETTLEMENT_CTRL");
        transactionControlTable.addStringProperty("SALE_CTRL");
        transactionControlTable.addStringProperty("AUTH_CTRL");
        transactionControlTable.addStringProperty("REFUND_CTRL");
        transactionControlTable.addStringProperty("ADJUSTMENT_CTRL");
        transactionControlTable.addStringProperty("OFFLINE_CTRL");
        transactionControlTable.addStringProperty("MANUAL_ENTRY_CTRL");
        transactionControlTable.addStringProperty("BALANCE_CTRL");
        transactionControlTable.addStringProperty("CASH_ADVANCE_CTRL");
        transactionControlTable.addStringProperty("PURCHASE_TIP_REQUEST_CTRL");
        transactionControlTable.addStringProperty("TIP_CTRL");
        return transactionControlTable;

    }

    private static Entity addEzlinkEntity(final Schema schema) {
        Entity ezlinkTable = schema.addEntity(EZLINK_TABLE);
        ezlinkTable.addIdProperty().primaryKey().autoincrement();
        ezlinkTable.addStringProperty("Ezlink_ID");
        ezlinkTable.addStringProperty("EZLINK_ENABLE");
        ezlinkTable.addStringProperty("EZLINK_SAM_KEY");
        ezlinkTable.addStringProperty("EZLINK_PAYMENT_TRP");
        ezlinkTable.addStringProperty("EZLINK_TOPUP_TRP");
        ezlinkTable.addStringProperty("EZLINK_PAYMENT_DEVICE_TYPE");
        ezlinkTable.addStringProperty("EZLINK_TOPUP_DEVICE_TYPE");
        ezlinkTable.addStringProperty("EZLINK_BLACK_LIST_LAST_UPDATE");
        ezlinkTable.addStringProperty("EZLINK_TOPUP_PAYMENT_MODE");
        return ezlinkTable;

    }


    //Transaction Control Table






    private static Entity addCurrencyEntity(final Schema schema) {
        Entity currencyTable = schema.addEntity(TABLE_CURRENCY);
        currencyTable.addIdProperty().primaryKey().autoincrement();
        currencyTable.addStringProperty("CURRENCY_ID");
        currencyTable.addStringProperty("CURR_LABEL");
        currencyTable.addStringProperty("CURR_EXPONENT");
        currencyTable.addStringProperty("CURR_CODE");
        return currencyTable;

    }


    // ETHERNET TABLE

    private static Entity addEthernetEntity(final Schema schema) {
        Entity ethernetTable = schema.addEntity(TABLE_ETHERNET);
        ethernetTable.addIdProperty().primaryKey().autoincrement();
        ethernetTable.addStringProperty("ETHERNET_ID");
        ethernetTable.addStringProperty("LOCAL_IP");
        ethernetTable.addStringProperty("SUBNET_MASK");
        ethernetTable.addStringProperty("GATEWAY");
        ethernetTable.addStringProperty("DNS1");
        ethernetTable.addStringProperty("DNS2");
        return ethernetTable;

    }





    private static Entity addCardTypeEntity(final Schema schema) {
        Entity cardTypeTable = schema.addEntity(TABLE_CTT);
        cardTypeTable.addIdProperty().primaryKey().autoincrement();
        cardTypeTable.addStringProperty("CTT_ID");
        cardTypeTable.addStringProperty("CTT_CARD_TYPE");
        cardTypeTable.addStringProperty("CTT_CARD_LABEL");
        cardTypeTable.addStringProperty("CTT_CARD_FORMAT");
        cardTypeTable.addStringProperty("CTT_MASK_FORMAT");
        cardTypeTable.addStringProperty("CTT_MAGSTRIPE_FLOOR_LIMIT");
        cardTypeTable.addStringProperty("CTT_DISABLE_LUHN");
        cardTypeTable.addStringProperty("CTT_CUSTOM_OPTIONS");
        cardTypeTable.addStringProperty("CTT_CVV_FDBC_ENABLE");
        cardTypeTable.addStringProperty("CTT_PAN_MASK_ARRAY");
        cardTypeTable.addStringProperty("CTT_EXPIRY_MASK_ARRAY");
        cardTypeTable.addStringProperty("CTT_QPSL");
        cardTypeTable.addStringProperty("CTT_DISABLE_EXPIRY_CHECK");
        cardTypeTable.addStringProperty("CTT_MC501");
        return cardTypeTable;
    }


    private static Entity addPasswordEntity(final Schema schema) {
        Entity passwordTable = schema.addEntity(TABLE_PWD);
        passwordTable.addIdProperty().primaryKey().autoincrement();
        passwordTable.addStringProperty("PWD_ID");
        passwordTable.addStringProperty("DEFAULT_PASSWORD");
        passwordTable.addStringProperty("REFUND_PASWORD");
        passwordTable.addStringProperty("TIP_ADJUST_PASSWORD");
        passwordTable.addStringProperty("PRE_AUTH_PASSWORD");
        passwordTable.addStringProperty("BALANCE_PASSWORD");
        passwordTable.addStringProperty("OFFLINE_PASSWORD");
        passwordTable.addStringProperty("SETTLEMENT_PASSWORD");
        passwordTable.addStringProperty("EDITOR_PASSWORD");
        passwordTable.addStringProperty("VOID_PASSWORD");
        passwordTable.addStringProperty("MANUAL_ENTRY_PASSWORD");
        passwordTable.addStringProperty("CASH_ADVANCED_PASSWORD");
        passwordTable.addStringProperty("TERMINAL_POWERON_PASSWORD");
        return passwordTable;

    }



    private static Entity addBatchFileEntity(final Schema schema) {
        Entity batchFileTable = schema.addEntity(TABLE_BATCH);
        batchFileTable.addIdProperty().primaryKey().autoincrement();
        batchFileTable.addStringProperty("BATCH_ID");
        batchFileTable.addStringProperty("hdt_index");
        batchFileTable.addStringProperty("trans_type");//SALE,REFUND
        batchFileTable.addStringProperty("trans_mode");// ENTRY MODE ie in
        batchFileTable.addStringProperty("voided");
        batchFileTable.addStringProperty("reversed");
        batchFileTable.addStringProperty("uploaded");
        batchFileTable.addStringProperty("proc_code");
        batchFileTable.addStringProperty("invoice_number");
        batchFileTable.addStringProperty("amount");
        batchFileTable.addStringProperty("tip_amount");
        batchFileTable.addStringProperty("time");
        batchFileTable.addStringProperty("date");
        batchFileTable.addStringProperty("year"); //YY, TECK ADDED DUE TO REPRINT FAULT REPORTED EVERY BEGINNING OF YEAR.
        batchFileTable.addStringProperty("org_mess_id");
        batchFileTable.addStringProperty("sys_trace_num");
        batchFileTable.addStringProperty("date_exp");
        batchFileTable.addStringProperty("retr_ref_num");
        batchFileTable.addStringProperty("auth_id_resp");
        batchFileTable.addStringProperty("resp_code");
        batchFileTable.addStringProperty("acct_number");
        batchFileTable.addStringProperty("person_name");
        batchFileTable.addStringProperty("original_amount");
        batchFileTable.addStringProperty("additional_data"); // Additional prompt data input
        batchFileTable.addStringProperty("payment_term_info");// For LMS IPP Etc.
        batchFileTable.addStringProperty("pri_acct_num");
        batchFileTable.addStringProperty("pos_ent_mode");
        batchFileTable.addStringProperty("NII");
        batchFileTable.addStringProperty("pos_cond_code");
        batchFileTable.addStringProperty("add_amount");//Venkat:new balance amount: Ezlink
        batchFileTable.addStringProperty("card_type");
        batchFileTable.addStringProperty("card_sequence");
        batchFileTable.addStringProperty("ChipData");
        batchFileTable.addStringProperty("TVRValue");
        batchFileTable.addStringProperty("TSIValue");
        batchFileTable.addStringProperty("TransCryto");
        batchFileTable.addStringProperty("TotalScript71");
        batchFileTable.addStringProperty("TotalScript72");
        batchFileTable.addStringProperty("ScriptRslt71");
        batchFileTable.addStringProperty("ScriptRslt72");
        batchFileTable.addStringProperty("chAid");  //for EMV Aid selected
        batchFileTable.addStringProperty("application_label");
        batchFileTable.addStringProperty("ui8ClsSchemeid");
        batchFileTable.addStringProperty("ui8SignReq");
        return batchFileTable;

    }

    private static Entity addAlipayEntity(final Schema schema) {
        Entity alipayTable = schema.addEntity(ALIPAY_TABLE);
        alipayTable.addIdProperty().primaryKey().autoincrement();
        alipayTable.addStringProperty("ALIPAY_ID");
        alipayTable.addStringProperty("PARTNER_ID");
        alipayTable.addStringProperty("SELLER_ID");
        alipayTable.addStringProperty("REGION_CODE");
        return alipayTable;

    }

    private static Entity addReceiptEntity(final Schema schema) {
        Entity receiptTable = schema.addEntity(TABLE_RECEIPT);
        receiptTable.addIdProperty().primaryKey().autoincrement();
        receiptTable.addStringProperty("RECEIPT_ID");
        receiptTable.addStringProperty("PRINT_TIMEOUT");
        receiptTable.addStringProperty("AUTO_PRINT");
        receiptTable.addStringProperty("PRINTER_INTENSITY_CFG");
        receiptTable.addStringProperty("PRINTER_CFG");
        return receiptTable;

    }

    private static Entity addMaskingEntity(final Schema schema) {
        Entity maskingTable = schema.addEntity(TABLE_MASKING);
        maskingTable.addIdProperty().primaryKey().autoincrement();
        maskingTable.addStringProperty("MASKING_ID");
        maskingTable.addStringProperty("DR_PAN_UNMASK");
        maskingTable.addStringProperty("DR_EXP_UNMASK");
        maskingTable.addStringProperty("DISPLAY_UNMASK");
        return maskingTable;

    }







    private static Entity addUtilityEntity(final Schema schema) {
        Entity utilityTable = schema.addEntity(TABLE_UTILITY);
        utilityTable.addIdProperty().primaryKey().autoincrement();

        utilityTable.addStringProperty("UTILITY_ID");
        utilityTable.addStringProperty("ADDITIONAL_PROMPT");
        utilityTable.addStringProperty("DAILY_SETTLEMENT_FLAG");
        utilityTable.addStringProperty("LAST_4_DIGIT_PROMPT_FLAG");
        utilityTable.addStringProperty("INSERT_2_SWIPE");
        utilityTable.addStringProperty("PIGGYBACK_FLAG");
        utilityTable.addStringProperty("PINBYPASS");
        utilityTable.addStringProperty("AUTO_SETTLE_TIME");
        utilityTable.addStringProperty("LAST_AUTO_SETTLEMENT_DATETIME");
        utilityTable.addStringProperty("UTRN_PREFIX");
        utilityTable.addStringProperty("DEFAULT_APPROVAL_CODE");


        return utilityTable;

    }


    private static Entity addMerchantEntity(final Schema schema) {
        Entity merchantTable = schema.addEntity(TABLE_MERCHANT);
        merchantTable.addIdProperty().primaryKey().autoincrement();
        merchantTable.addStringProperty("MERCHANT_ID");
        merchantTable.addStringProperty("ADDITIONAL_PROMPT");
        merchantTable.addStringProperty("DAILY_SETTLEMENT_FLAG");
        merchantTable.addStringProperty("LAST_4_DIGIT_PROMPT_FLAG");
        merchantTable.addStringProperty("INSERT_2_SWIPE");
        merchantTable.addStringProperty("PIGGYBACK_FLAG");
        merchantTable.addStringProperty("PINBYPASS");
        merchantTable.addStringProperty("AUTO_SETTLE_TIME");
        merchantTable.addStringProperty("LAST_AUTO_SETTLEMENT_DATETIME");
        merchantTable.addStringProperty("UTRN_PREFIX");
        return merchantTable;

    }



    private static Entity addHTTEntity(final Schema schema) {
        Entity httTable = schema.addEntity(TABLE_HTT);
        httTable.addIdProperty().primaryKey().autoincrement();
        httTable.addStringProperty("HTT_ID");
        httTable.addStringProperty("TRANSMISSION_MODE");
        httTable.addStringProperty("CONNECTION_TIMEOUT");
        httTable.addStringProperty("REDIAL_TIMEOUT");
        httTable.addStringProperty("PABX");
        httTable.addStringProperty("MODEM_STRING");
        httTable.addStringProperty("REPORTTABLE_ID");
        httTable.addStringProperty("DETAILED_REPORT");
        httTable.addStringProperty("TIP_REPORT");
        httTable.addStringProperty("TOTAL_REPORT");

        return httTable;

    }



    private static Entity addReportEntity(final Schema schema) {
        Entity reportTable = schema.addEntity(TABLE_REPORT);
        reportTable.addIdProperty().primaryKey().autoincrement();
        reportTable.addStringProperty("REPORTTABLE_ID");
        reportTable.addStringProperty("DETAILED_REPORT");
        reportTable.addStringProperty("TIP_REPORT");
        reportTable.addStringProperty("TOTAL_REPORT");

        return reportTable;

    }



}
