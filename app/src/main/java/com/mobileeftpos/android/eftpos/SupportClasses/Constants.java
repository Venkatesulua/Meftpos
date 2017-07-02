package com.mobileeftpos.android.eftpos.SupportClasses;

/**
 * Created by venkat on 5/18/2017.
 */
public class Constants {
	public static final String TMS_DEFAULT_MMS_FAMILY = "TELIUM";

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

	}

	public class ResponseCodes {
		public static final String DEF_DE39_V_APPROVE = "00";
	}

	public class ReturnValues {
		public static final int RETURN_OK = 0;
		public static final int RETURN_TIMEOUT = 1;
		public static final int RETURN_CANCEL = 2;
		public static final int RETURN_ERROR = 3;
		public static final int RETURN_BATCH_TRANSFER = 4;
		public static final int RETURN_UNKNOWN = 5;
		public static final int NO_TRANSCATION = 6;
		public static final int TRANSACTION_NOT_SUPPORTED = 7;
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
}
