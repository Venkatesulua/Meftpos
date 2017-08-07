package com.mobileeftpos.android.eftpos.SupportClasses;

/**
 * Created by venkat on 5/18/2017.
 */
public class Constants {

	public static final String TMS_DEFAULT_MMS_FAMILY = "TELIUM";

	//Transaction Manager
	public static final int inGetCardInfo=001;
	public static final int SortPan=002;
	public static final int CheckReversal=003;
	public static final int CheckUpload=004;
	public static final int HostConnectivity=005;
	public static final int SaveRecord=006;
	public static final int PrintReceipt=007;
	public static final int ClearGlobals= 8;


	public static final int TRUE = 1;
	public static final int FALSE = 0;

	//transcations display
	public static String VOID="VOID";
	public static String DEFAULT_CURRENCY = "SGD";

	//ERROR
	public static String TRANSCATION_NOT_SUPPORTED = "TRANSCATION_NOT_SUPPORTED";
	public static String ALREADY_VOIDED = "ALREADY_VOIDED";

	public class TransMode {
		public static final int SWIPE = 0;
		public static final int ICC = 1;
		public static final int NFC = 2;
		public static final int BARCODE = 3;

	}

	public class TransType {
		public static final int TMS_INITIAL_PACKET = 0;
		public static final int TMS_SUBSEQUENT_PACKET = 1;
		public static final int PAYMENT_SALE = 2;
		public static final int REFUND = 3;
		public static final int VOID = 4;

		public static final int REVERSAL = 5;
		public static final int ALIPAY_SALE = 6;
		public static final int ALIPAY_REFUND = 7;
		public static final int INIT_SETTLEMENT = 8;
		public static final int FINAL_SETTLEMENT = 9;
		public static final int ALIPAY_UPLOAD = 10;
		public static final int BATCH_TRANSFER = 11;
		public static final int ALIPAY_SALE_REPEAT = 12;



		public static final int BLACKLIST_FIRST_DOWNLOAD = 13;
		public static final int BLACKLIST_SUBSEQUENT_DOWNLOAD = 14;
		public static final int EZLINK_SALE = 15;
		public static final int EZ_iWallet = 16;

	}

	public class ResponseCodes {
		public static final String DEF_DE39_V_APPROVE = "00";
		public static final String BATCH_TRANSFER = "95";
		public static final String DO_NOT_HONOR = "05";
	}

	public class ReturnValues {
		public static final int RETURN_FAILED = -1;
		public static final int RETURN_OK = 0;
		public static final int RETURN_TIMEOUT = 1;
		public static final int RETURN_CANCEL = 2;
		public static final int RETURN_ERROR = 3;
		public static final int RETURN_BATCH_TRANSFER = 4;
		public static final int RETURN_UNKNOWN = 5;
		public static final int NO_TRANSCATION = 6;
		public static final int TRANSACTION_NOT_SUPPORTED = 7;
		public static final int RETURN_NOTIFICATION  = 8;
		public static final int RETURN_SETTLEMENT_NEEDED  = 9;
		public static final int RETURN_CONNECTION_ERROR  = 10;
		public static final int RETURN_SEND_RECV_FAILED  = 11;
		public static final int RETURN_NETWORK_MESSAGE = 12;
		public static final int RETURN_ADMIN_MESSAGE= 13;
		public static final int RETURN_REVERSAL_FAILED = 14;
		public static final int RETURN_UPLOAD_FAILED = 15;
	}

	public class TMSReturnValues {
		public static final int TMS_RESPONSE_KO = 0;
		public static final int TMS_RESPONSE_DL_NOT_REQUIRED = 1;
		public static final int TMS_RESPONSE_DL_REQUIRED = 2;
		public static final int TMS_RESPONSE_OK = 3;
		public static final int TMS_RESPONSE_DL_CONTINUE = 4;
	}

	public class MTI {
		public static final String Authorization = "0100";
		public static final String AuthorizationAdvice = "0120";
		public static final String Financial = "0200";
		public static final String FinancialAdvice = "0220";
		public static final String BatchUpload = "0320";
		public static final String Reversal = "0400";
		public static final String Settlement = "0500";
		public static final String NetworkManagement = "0800";
		public static final String Financial_Repeat = "0201";
	}

	public class PROCESSINGCODE {
		public static final String pcTmsInitial = "930100";
		public static final String pcTmsSubsequent = "930110";
		public static final String pcFinancialRequest = "000000";
		public static final String stVoid = "020000";
		public static final String initsettrequest = "920000";
		public static final String finalsettrequest = "960000";
		public static final String pcRefund = "200000";
		public static final String BLACKLIST_FIRST_PROC = "301040";
		public static final String BLACKLIST_NEXT_PROC = "301041";
		public static final String EZLINK_PAYMENT_PROC = "403010";
	}

	public class PaymentStatus{
		public static final int PAYMENT_OK = 0;
	}
	public class QRCODE {
		public static final String BARCODE_INTENT_ACTION = "com.google.zxing.client.android.SCAN";
		public static final String BARCODE_DISABLE_HISTORY = "SAVE_HISTORY";
		public static final int BARCODE_RESULT_CODE = 999;
		public static final String BARCODE_INTENT_RESULT_KEY = "SCAN_RESULT";
	}
	public class Ezlink{
		public static final String User_Data = "MEFTPOS ";
		public static final String BLACKLIST_INFO_FILE="EZLINFO";
		public static final String BLACKLIST_IND_FILE="EZLINDI";
		public static final String BLACKLIST_RANGE_FILE="EZLRANGE";
		public static final String BLACKLIST_PARL_INFO_FILE="EZLPINFO";
		public static final String BLACKLIST_PARL_IIND_FILE="EZLPINDI";
		public static final String BLACKLIST_PARL_IRANGE_FILE="EZLPRNG";
	}

	public class HostType{
		public static final String AMEX_HOST ="0";
		public static final String PINBASED_CUP_HOST ="2";
		public static final String JCB_HOST ="J";
		public static final String E_GENTING ="E";
		public static final String DBS_IPP ="I";
		public static final String DBS_BANK ="3";
		public static final String DINERS_HOST ="N";
		public static final String TRAVELEX_HOST ="T";
		public static final String LOYALTY_HOST ="L";
		public static final String AXS_DPAY_HOST ="X";
		public static final String VPS_PREPAID_HOST ="P";
		public static final String VPS_DEBIT_HOST ="K";
		public static final String TAKA_STAFF_HOST ="Y";
		public static final String VOUCHER_HOST ="V";
		public static final String CASE_TRUST ="C";
		public static final String ALIPAY_HOST ="Q";
		public static final String EZLINK_PAYMENT_HOST="A";
		public static final String EZLINK_TOPUP_HOST="B";
		public static final String 	CEPAS_NETS_HOST="C";


/*
#define CT_CREDIT							'C' //CREDIT
#define CT_VISA								'V' //VISA
#define CT_MASTER							'M' //MASTERCARD
#define CT_AMEX								'A' //AMEX
#define CT_JCB								'J' //JCB
#define CT_DINERS							'N' //DINERS
//#define CT_DEBIT							'D'	//MEPS DEBIT (MALAYSIA)
#define CT_CUP								'U'	//CHINA UNION PAY
#define CT_EZ_PREPAID						'E' //EZLINK PREPAID CARD
#define CT_EZ_DEBIT							'D'	//EZLINK NARADA DEBIT (SINGAPORE)
#define CT_DP_DEBIT							'D'	//AXS DPAY(SINGAPORE)
#define CT_BLACK_HOLE						'0' //FUNNELS ALL NON-SUPPORTED CARD TYPES HERE, CAUSES THE TERMINAL TO PROMPT "CARD NOT SUPPORTED"
#define CT_EZLINK_CASETRUST					'T'
#define CT_ALIPAY							'Q'//Alipay Changes*/
	}
}
