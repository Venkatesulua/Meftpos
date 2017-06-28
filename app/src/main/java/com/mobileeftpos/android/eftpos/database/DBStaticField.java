package com.mobileeftpos.android.eftpos.database;

public class DBStaticField {


	// Table Names
	public static final String TABLE_HOST ="HostTable";
	public static final String TABLE_CBT ="CardBinTable";
	public static final String TABLE_CTT ="CardTypeTable";
	public static final String TABLE_PWD ="PasswordTable";
	public static final String TABLE_TCT="TransactionControlTable";
	public static final String TABLE_ETHERNET="EthernetTable";
	public static final String TABLE_CURRENCY="CurrencyTable";
	public static final String TABLE_LIMIT="LimitTable";
	public static final String TABLE_MASKING="MaskingTable";
	public static final String TABLE_RECEIPT="ReceiptTable";
	public static final String TABLE_UTILITY="UtilityTable";
	public static final String TABLE_MERCHANT="MerchantTable";
	public static final String TABLE_HTT="TABLE_HTT";


	public static final String TABLE_REPORT="TABLE_REPORT";
	public static final String EZLINK_TABLE="EZLINK_TABLE";
	public static final String COMMS_TABLE="COMMS_TABLE";
	public static final String ALIPAY_TABLE="ALIPAY";

	public static final String TABLE_BATCH="Batch";
	public static final String TABLE_TRACE="Trace";


	// Host Table Fields
	// Host Table Fields
	public static final String HDT_HOST_ID="HDT_HOST_ID";
	public static final String HDT_HOST_ENABLED="HDT_HOST_ENABLED";
	public static final String HDT_COM_INDEX="HDT_COM_INDEX";
	public static final String HDT_REFERRAL_NUMBER="HDT_REFERRAL_NUMBER";
	public static final String HDT_TERMINAL_ID="HDT_TERMINAL_ID";
	public static final String HDT_MERCHANT_ID="HDT_MERCHANT_ID";
	public static final String HDT_TPDU="HDT_TPDU";
	public static final String HDT_BATCH_NUMBER="HDT_BATCH_NUMBER";
	public static final String HDT_INVOICE_NUMBER="HDT_INVOICE_NUMBER";
	public static final String HDT_PROCESSING_CODE="HDT_PROCESSING_CODE";
	public static final String HDT_HOST_TYPE="HDT_HOST_TYPE";
	public static final String HDT_HOST_LABEL="HDT_HOST_LABEL";
	public static final String HDT_MANUAL_ENTRY_FLAG="HDT_MANUAL_ENTRY_FLAG";
	public static final String HDT_REVERSAL_FLAG="HDT_REVERSAL_FLAG";
	public static final String HDT_SETTLEMENT_FLAG="HDT_SETTLEMENT_FLAG";
	public static final String HDT_BATCH_MAX_TOTAL="HDT_BATCH_MAX_TOTAL";
	public static final String HDT_BATCH_STL_LAST="HDT_BATCH_STL_LAST";
	public static final String HDT_BATCH_CURR_TOTAL="HDT_BATCH_CURR_TOTAL";
	public static final String HDT_BATCH_NO_TRANS="HDT_BATCH_NO_TRANS";
	public static final String HDT_DESCRIPTION="HDT_DESCRIPTION";
	public static final String HDT_PAY_TERM="HDT_PAY_TERM";
	public static final String HDT_PEK="HDT_PEK";
	public static final String HDT_MEK="HDT_MEK";
	public static final String HDT_MAC_INDEX="HDT_MAC_INDEX";
	public static final String HDT_CUSTOM_OPTIONS="HDT_CUSTOM_OPTIONS";
	public static final String HDT_CURR_INDEX="HDT_CURR_INDEX";
	public static final String HDT_PIGGYBACK_FLAG="HDT_PIGGYBACK_FLAG";
	public static final String HDT_MINIMUM_AMT="HDT_MINIMUM_AMT";
	public static final String HDT_RATE="HDT_RATE";
	public static final String HDT_REDIRECT_IF_DISABLE="HDT_REDIRECT_IF_DISABLE";
	public static final String HDT_REVERSAL_COUNT="HDT_REVERSAL_COUNT";
	public static final String HDT_SIGCAP_INDEX="HDT_SIGCAP_INDEX";
	public static final String HDT_BATCH_GROUP_NUMBER="HDT_BATCH_GROUP_NUMBER";


////CARD BIN TABLE

	public static final String CDT_ID="CDT_ID";
	public static final String CDT_LO_RANGE="CDT_LO_RANGE";
	public static final String CDT_HI_RANGE="CDT_HI_RANGE";
	public static final String CDT_HDT_REFERENCE="CDT_HDT_REFERENCE";
	public static final String CDT_CARD_TYPE_ARRAY="CDT_CARD_TYPE_ARRAY";
	public static final String CDT_CARD_NAME = "CDT_CARD_NAME";



///Cart Type Table

	public static final String CTT_ID="CTT_ID";
	public static final String CTT_CARD_TYPE="CTT_CARD_TYPE";
	public static final String CTT_CARD_LABEL="CTT_CARD_LABEL";
	public static final String CTT_CARD_FORMAT="CTT_CARD_FORMAT";
	public static final String CTT_MASK_FORMAT="CTT_MASK_FORMAT";
	public static final String CTT_MAGSTRIPE_FLOOR_LIMIT="CTT_MAGSTRIPE_FLOOR_LIMIT";
	public static final String CTT_DISABLE_LUHN="CTT_DISABLE_LUHN";
	public static final String CTT_CUSTOM_OPTIONS="CTT_CUSTOM_OPTIONS";
	public static final String CTT_CVV_FDBC_ENABLE="CTT_CVV_FDBC_ENABLE";
	public static final String CTT_PAN_MASK_ARRAY="CTT_PAN_MASK_ARRAY";
	public static final String CTT_EXPIRY_MASK_ARRAY="CTT_EXPIRY_MASK_ARRAY";
	public static final String CTT_QPSL="CTT_QPSL";
	public static final String CTT_DISABLE_EXPIRY_CHECK="CTT_DISABLE_EXPIRY_CHECK";
	public static final String CTT_MC501="CTT_MC501";



//Password Table

	public static final String PWD_ID="PWD_ID";
	public static final String DEFAULT_PASSWORD="DEFAULT_PASSWORD";
	public static final String REFUND_PASWORD="REFUND_PASWORD";
	public static final String TIP_ADJUST_PASSWORD="TIP_ADJUST_PASSWORD";
	public static final String PRE_AUTH_PASSWORD="PRE_AUTH_PASSWORD";
	public static final String BALANCE_PASSWORD="BALANCE_PASSWORD";
	public static final String OFFLINE_PASSWORD="OFFLINE_PASSWORD";
	public static final String SETTLEMENT_PASSWORD="SETTLEMENT_PASSWORD";
	public static final String EDITOR_PASSWORD="EDITOR_PASSWORD";
	public static final String VOID_PASSWORD="VOID_PASSWORD";
	public static final String MANUAL_ENTRY_PASSWORD="MANUAL_ENTRY_PASSWORD";
	public static final String CASH_ADVANCED_PASSWORD="CASH_ADVANCED_PASSWORD";
	public static final String TERMINAL_POWERON_PASSWORD="TERMINAL_POWERON_PASSWORD";


	//Transaction Control Table

	public static final String TCT_ID="TCT_ID";
	public static final String VOID_CTRL="VOID_CTRL";
	public static final String SETTLEMENT_CTRL="SETTLEMENT_CTRL";
	public static final String SALE_CTRL="SALE_CTRL";
	public static final String AUTH_CTRL="AUTH_CTRL";
	public static final String REFUND_CTRL="REFUND_CTRL";
	public static final String ADJUSTMENT_CTRL="ADJUSTMENT_CTRL";
	public static final String OFFLINE_CTRL="OFFLINE_CTRL";
	public static final String MANUAL_ENTRY_CTRL="MANUAL_ENTRY_CTRL";
	public static final String BALANCE_CTRL="BALANCE_CTRL";
	public static final String CASH_ADVANCE_CTRL="CASH_ADVANCE_CTRL";
	public static final String PURCHASE_TIP_REQUEST_CTRL="PURCHASE_TIP_REQUEST_CTRL";
	public static final String TIP_CTRL="TIP_CTRL";


	//Transaction EZlink Table

	public static final String Ezlink_ID="Ezlink_ID";
	public static final String EZLINK_ENABLE="EZLINK_ENABLE";
	public static final String EZLINK_SAM_KEY="EZLINK_SAM_KEY";
	public static final String EZLINK_PAYMENT_TRP="EZLINK_PAYMENT_TRP";
	public static final String EZLINK_TOPUP_TRP="EZLINK_TOPUP_TRP";
	public static final String EZLINK_PAYMENT_DEVICE_TYPE="EZLINK_PAYMENT_DEVICE_TYPE";
	public static final String EZLINK_TOPUP_DEVICE_TYPE="EZLINK_TOPUP_DEVICE_TYPE";
	public static final String EZLINK_BLACK_LIST_LAST_UPDATE="EZLINK_BLACK_LIST_LAST_UPDATE";
	public static final String EZLINK_TOPUP_PAYMENT_MODE="EZLINK_TOPUP_PAYMENT_MODE";


// ETHERNET TABLE

	public static final String ETHERNET_ID="ETHERNET_ID";
	public static final String LOCAL_IP="LOCAL_IP";
	public static final String SUBNET_MASK="SUBNET_MASK";
	public static final String GATEWAY="GATEWAY";
	public static final String DNS1="DNS1";
	public static final String DNS2="DNS2";

//Currency Table

	public static final String CURRENCY_ID="CURRENCY_ID";
	public static final String CURR_LABEL="CURR_LABEL";
	public static final String CURR_EXPONENT="CURR_EXPONENT";
	public static final String CURR_CODE="CURR_CODE";




//Commos Table

	public static final String COMMOS_ID="COMMOS_ID";
	public static final String COM_DESCRIPTION="COM_DESCRIPTION";
	public static final String COM_PRIMARY_TYPE="COM_PRIMARY_TYPE";
	public static final String COM_SECONDARY_TYPE="COM_SECONDARY_TYPE";
	public static final String COM_MODEM_PRIMARY_NUMBER="COM_MODEM_PRIMARY_NUMBER";
	public static final String COM_MODEM_SECONDARY_NUMBER="COM_MODEM_SECONDARY_NUMBER";
	public static final String COM_MODEM_STRING="COM_MODEM_STRING";
	public static final String COM_MODEM_DISABLE_LINE_DETECT="COM_MODEM_DISABLE_LINE_DETECT";
	public static final String COM_MODEM_TIMEOUT="COM_MODEM_TIMEOUT";
	public static final String COM_PRIMARY_IP_PORT="COM_PRIMARY_IP_PORT";
	public static final String COM_SECONDARY_IP_PORT="COM_SECONDARY_IP_PORT";
	public static final String COM_IP_TIMEOUT="COM_IP_TIMEOUT";
	public static final String COM_CONNECT_SECONDARY="COM_CONNECT_SECONDARY";
	public static final String COM_SSL_INDEX="COM_SSL_INDEX";
	public static final String COM_MODEM_INDEX="COM_MODEM_INDEX";
	public static final String COM_PPP_USER_ID="COM_PPP_USER_ID";
	public static final String COM_PPP_PASSWORD="COM_PPP_PASSWORD";
	public static final String COM_PPP_MODEM_STRING="COM_PPP_MODEM_STRING";
	public static final String COM_PPP_TIMEOUT="COM_PPP_TIMEOUT";


	// Limit Table

	public static final String LIMIT_ID="LIMIT_ID";
	public static final String MAXIMUM_SALE_AMOUNT="MAXIMUM_SALE_AMOUNT";
	public static final String MAXIMUM_OFFLINE_AMOUNT="MAXIMUM_OFFLINE_AMOUNT";
	public static final String MAXIMUM_PREAUTH_AMOUNT="MAXIMUM_PREAUTH_AMOUNT";
	public static final String MAXIMUM_REFUND_AMOUNT="MAXIMUM_REFUND_AMOUNT";



//Masking Table

	public static final String MASKING_ID="MASKING_ID";
	public static final String DR_PAN_UNMASK="DR_PAN_UNMASK";
	public static final String DR_EXP_UNMASK="DR_EXP_UNMASK";
	public static final String DISPLAY_UNMASK="DISPLAY_UNMASK";



// Reciept Table

	public static final String RECEIPT_ID="RECEIPT_ID";
	public static final String PRINT_TIMEOUT="PRINT_TIMEOUT";
	public static final String AUTO_PRINT="AUTO_PRINT";
	public static final String PRINTER_INTENSITY_CFG="PRINTER_INTENSITY_CFG";
	public static final String PRINTER_CFG="PRINTER_CFG";
//Table
public static final String TRACE_UNIQUE_ID="TRACE_UNIQUE_ID";
public static final String SYSTEM_TRACE="SYSTEM_TRACE";

// Utility Table

	public static final String UTILITY_ID="UTILITY_ID";
	public static final String ADDITIONAL_PROMPT="ADDITIONAL_PROMPT";
	public static final String DAILY_SETTLEMENT_FLAG="DAILY_SETTLEMENT_FLAG";
	public static final String LAST_4_DIGIT_PROMPT_FLAG="LAST_4_DIGIT_PROMPT_FLAG";
	public static final String INSERT_2_SWIPE="INSERT_2_SWIPE";
	public static final String PIGGYBACK_FLAG="PIGGYBACK_FLAG";
	public static final String PINBYPASS="PINBYPASS";
	public static final String AUTO_SETTLE_TIME="AUTO_SETTLE_TIME";
	public static final String LAST_AUTO_SETTLEMENT_DATETIME="LAST_AUTO_SETTLEMENT_DATETIME";
	public static final String UTRN_PREFIX="UTRN_PREFIX";

	public static final String DEFAULT_APPROVAL_CODE="DEFAULT_APPROVAL_CODE";


	public static final String MERCHANT_ID="MERCHANT_ID";
	public static final String MERCHANT_NAME="ADDITIONAL_PROMPT";
	public static final String MERCHANT_HEADER1="DAILY_SETTLEMENT_FLAG";
	public static final String MERCHANT_HEADER2="LAST_4_DIGIT_PROMPT_FLAG";
	public static final String ADDRESS_LINE1="INSERT_2_SWIPE";
	public static final String ADDRESS_LINE2="PIGGYBACK_FLAG";
	public static final String ADDRESS_LINE3="PINBYPASS";
	public static final String ADDRESS_LINE4="AUTO_SETTLE_TIME";
	public static final String MERCHANT_FOOTER1="LAST_AUTO_SETTLEMENT_DATETIME";
	public static final String MERCHANT_FOOTER2="UTRN_PREFIX";

	public static final String HTT_ID="HTT_ID";
	public static final String TRANSMISSION_MODE="TRANSMISSION_MODE";
	public static final String CONNECTION_TIMEOUT="CONNECTION_TIMEOUT";
	public static final String REDIAL_TIMEOUT="REDIAL_TIMEOUT";
	public static final String PABX="PABX";
	public static final String MODEM_STRING="MODEM_STRING";



	public static final String REPORTTABLE_ID="REPORTTABLE_ID";
	public static final String DETAILED_REPORT="DETAILED_REPORT";
	public static final String TIP_REPORT="TIP_REPORT";
	public static final String TOTAL_REPORT="TOTAL_REPORT";


	//ALIPAY
	public static final String ALIPAY_ID="ALIPAY_ID";
	public static final String PARTNER_ID="PARTNER_ID";
	public static final String SELLER_ID="SELLER_ID";
	public static final String REGION_CODE="REGION_CODE";


	//BATCh FILE
	public static final String BATCH_ID="BATCH_ID";
	public static final String HDT_INDEX="hdt_index";
	public static final String TRANS_TYPE="trans_type";//SALE,REFUND
	public static final String TRANS_MODE="trans_mode";// ENTRY MODE ie in
	public static final String VOIDED="voided";
	public static final String REVERSED="reversed";
	public static final String UPLOADED="uploaded";
	public static final String PROC_CODE="proc_code";
	public static final String INVOICE_NUMBER="invoice_number";
	public static final String AMOUNT="amount";
	public static final String TIP_AMOUNT="tip_amount";
	public static final String TIME="time";
	public static final String DATE="date";
	public static final String YEAR="year"; //YY, TECK ADDED DUE TO REPRINT FAULT REPORTED EVERY BEGINNING OF YEAR.
	public static final String ORG_MESS_ID="org_mess_id";
	public static final String SYS_TRACE_NUM="sys_trace_num";
	public static final String DATE_EXP="date_exp";
	public static final String RETR_REF_NUM="retr_ref_num";
	public static final String AUTH_ID_RESP="auth_id_resp";
	public static final String RESP_CODE="resp_code";
	public static final String ACCT_NUMBER="acct_number";
	public static final String PERSON_NAME="person_name";
	public static final String ORIGINAL_AMOUNT="original_amount";
	public static final String ADDITIONAL_DATA="additional_data"; // Additional prompt data input
	public static final String PAYMENT_TERM_INFO="payment_term_info";// For LMS IPP Etc.
	public static final String PRIMARY_ACC_NUM="pri_acct_num";
	public static final String POS_ENT_MODE="pos_ent_mode";
	public static final String NII="NII";
	public static final String POS_COND_CODE="pos_cond_code";
	public static final String ADD_AMOUNT="add_amount";//Venkat:new balance amount: Ezlink
	public static final String CARD_TYPE="card_type";
	public static final String CARD_EQUENCE="card_sequence";
	public static final String CHIPDATA="ChipData";
	public static final String TVRVALUE="TVRValue";
	public static final String TSIVALUE="TSIValue";
	public static final String TRANSCRYTO="TransCryto";
	public static final String TOTALSCRIPT71="TotalScript71";
	public static final String TOTALSCRIPT72="TotalScript72";
	public static final String SCRIPTRESULT71="ScriptRslt71";
	public static final String SCRIPTRESULT72="ScriptRslt72";
	public static final String CHAID="chAid";  //for EMV Aid selected
	public static final String APPLICATION_LABEL="application_label";
	public static final String CLS_SCHEME_ID="ui8ClsSchemeid";
	public static final String SIGNATURE_REQ="ui8SignReq";













































	


	
	
			
}
