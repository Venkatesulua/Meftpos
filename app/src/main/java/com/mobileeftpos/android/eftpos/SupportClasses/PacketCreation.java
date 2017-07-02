package com.mobileeftpos.android.eftpos.SupportClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.database.DBStaticField;
import com.mobileeftpos.android.eftpos.model.BarcodeModel;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CommsModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by venkat on 6/1/2017.
 */
public class PacketCreation {

    private GlobalVar globalVar = new GlobalVar();
    private BarcodeModel barcode = new BarcodeModel();
    private CurrencyModel currModel = new CurrencyModel();
    private HostModel hostData = new HostModel();
    private CommsModel commData = new CommsModel();
    private MerchantModel merchantData = new MerchantModel();
    private ISOPackager1 packager = new ISOPackager1();
    private ISOMsg isoMsg = new ISOMsg();
    private final String TAG = "my_custom_msg";
    //private int TransactionDetails.inFinalLength=0;
    private TransactionDetails trDetails = new TransactionDetails();
    public static final String REVERSALPREFERENCE = "reversal" ;
    SharedPreferences sharedpreferences;
    Gson gson = new Gson();
    int inVoided=0;
    long SaleCount=0,SaleAmount=0,RefundCount=0,RefundAmount=0;


    public BatchModel vdSaveRecord(DBHelper databaseObj){
        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);
        PayServices payServices= new PayServices();
        BatchModel batchModel = new BatchModel();
        //Save transaction
        if(TransactionDetails.trxType != Constants.TransType.VOID) {
            batchModel.setHDT_INDEX(Integer.toString(TransactionDetails.inGHDT));
            batchModel.setTRANS_TYPE(Integer.toString(TransactionDetails.trxType));
            batchModel.setTRANS_MODE(Integer.toString(TransactionDetails.inGTrxMode));
            batchModel.setVOIDED(Integer.toString(Constants.FALSE));
            batchModel.setUPLOADED(Integer.toString(Constants.FALSE));
            batchModel.setPROC_CODE(TransactionDetails.processingcode);

            String aa = payServices.pGetSystemTrace(databaseObj);
            batchModel.setINVOICE_NUMBER(payServices.pGetSystemTrace(databaseObj));
            batchModel.setAMOUNT(TransactionDetails.trxAmount);
            batchModel.setTIP_AMOUNT(TransactionDetails.tipAmount);
            batchModel.setTIME(TransactionDetails.trxDateTime.substring(8, 14));
            batchModel.setDATE(TransactionDetails.trxDateTime.substring(4, 8));
            batchModel.setYEAR(TransactionDetails.trxDateTime.substring(0, 4));
            batchModel.setORG_MESS_ID(TransactionDetails.messagetype);
            batchModel.setSYS_TRACE_NUM(payServices.pGetSystemTrace(databaseObj));
            batchModel.setDATE_EXP(TransactionDetails.ExpDate);

            batchModel.setRETR_REF_NUM(TransactionDetails.RetrievalRefNumber);
            batchModel.setAUTH_ID_RESP(TransactionDetails.chApprovalCode);
            batchModel.setRESP_CODE(TransactionDetails.ResponseCode);
            batchModel.setACCT_NUMBER(TransactionDetails.PAN);
            batchModel.setPERSON_NAME(TransactionDetails.PersonName);
            batchModel.setORIGINAL_AMOUNT(TransactionDetails.trxAmount);
            batchModel.setADDITIONAL_DATA(TransactionDetails.responseMessge);
            //batchModel.setPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
            batchModel.setPRIMARY_ACC_NUM(TransactionDetails.PAN);
            batchModel.setPOS_ENT_MODE(TransactionDetails.POSEntryMode);
            batchModel.setNII(TransactionDetails.NII);
            batchModel.setPOS_COND_CODE(TransactionDetails.POS_COND_CODE);
        /*batchModel.setADD_AMOUNT(res.getString(res.getColumnIndex(DBStaticField.ADD_AMOUNT)));
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
        batchModel.setSIGNATURE_REQ(res.getString(res.getColumnIndex(DBStaticField.SIGNATURE_REQ)));*/
        }
       if(TransactionDetails.trxType == Constants.TransType.VOID)
        {
            batchModel.setVOIDED(Integer.toString(Constants.TRUE));
            databaseObj.UpdateBatchData(batchModel);
            if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE) {
                //Create a update table since Alipay ned to send upload transaction
                databaseObj.insertBatchData(batchModel);
            }

        }else
            //databaseObj.
        databaseObj.insertBatchData(batchModel);

        return batchModel;
    }

    public int vdScanRecord(DBHelper databaseObj){
        //hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        BatchModel batchData;
        SaleCount=SaleAmount=RefundCount=RefundAmount=0;
        HostModel hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        List<BatchModel> batchModelList=databaseObj.getBatchData(hostData.getHDT_HOST_ID());
        if(batchModelList.size() ==0 )
        {
            return Constants.ReturnValues.NO_TRANSCATION;
        }
        for(int i=0;i<batchModelList.size();i++) {
            batchData = batchModelList.get(i);
            if( !(batchData ==null || batchData.equals(""))){
                int trxType = Integer.parseInt(batchData.getTRANS_TYPE());
                inVoided = Integer.parseInt(batchData.getVOIDED());
                if(trxType == Constants.TransType.ALIPAY_SALE && inVoided != Constants.TRUE)
                {
                    SaleCount = SaleCount+1;
                    SaleAmount = SaleAmount + Long.parseLong(batchData.getAMOUNT());
                }else if(trxType == Constants.TransType.ALIPAY_REFUND && inVoided != Constants.TRUE){
                    RefundCount = RefundCount+1;
                    RefundAmount = RefundAmount + Long.parseLong(batchData.getAMOUNT());
                }
            }

            }

        return Constants.ReturnValues.RETURN_OK;
    }
    String AlipayExtendedData(String staddinfo)
    {
        staddinfo ="merchant_name:";
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
        staddinfo = staddinfo + ",term_sn:";
        staddinfo = staddinfo + TransactionDetails.deviceId;

        staddinfo = staddinfo + ",trans_create_time:";
        staddinfo = staddinfo + TransactionDetails.trxDateTime.substring(2,14);

        staddinfo = staddinfo + ",ver:";
        staddinfo = staddinfo + "V1.0";


        if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE) {
            staddinfo = staddinfo + ",scan:";
            staddinfo = staddinfo + "y";
        }

        staddinfo = staddinfo + ",proto:";
        staddinfo = staddinfo + "1";

        staddinfo = staddinfo + ",sw:";
        staddinfo = staddinfo + "V1.0";
        staddinfo = staddinfo + ",";
        return staddinfo;

    }
    public int inCreatePacket(DBHelper databaseObj,byte[] FinalData,int inTrxType) {
        String stde27 = "";
        String stde29 = "";




        TransactionDetails.inFinalLength=0;

        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);

        currModel.setCURR_LABEL("THB");
        currModel.setCURR_EXPONENT("2");
        currModel.setCURR_CODE("764");



        String stAlipayExtData="";
        isoMsg.setPackager(packager);
        try {

            Log.i(TAG,"PacketCreation:::inCreatePacket : ");
            Log.i(TAG,Integer.toString(TransactionDetails.trxType));

            String staddinfo="";
            switch (inTrxType) {
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
                case Constants.TransType.ALIPAY_SALE_REPEAT:
                case Constants.TransType.ALIPAY_SALE:
                    Log.i(TAG,"PacketCreation:::ALIPAY_SALE : ");
                    Log.i(TAG,"PacketCreation:::PARTNER_ID::"+barcode.getPARTNER_ID());
                    Log.i(TAG,"PacketCreation:::SELLER_ID::"+barcode.getSELLER_ID());
                    Log.i(TAG,"PacketCreation:::REGION_CODE::"+barcode.getREGION_CODE());

                    Log.i(TAG,"PacketCreation:::TransactionDetails.trxAmount::"+TransactionDetails.trxAmount);
                    Log.i(TAG,"PacketCreation:::getHDT_TERMINAL_ID::"+hostData.getHDT_TERMINAL_ID());
                    Log.i(TAG,"PacketCreation:::getMERCHANT_NAME::"+merchantData.getMERCHANT_NAME());

                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.pcFinancialRequest;
                    if(inTrxType == Constants.TransType.ALIPAY_SALE) {
                        TransactionDetails.messagetype = Constants.MTI.Financial;
                        CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                    }
                    else {
                        TransactionDetails.messagetype = Constants.MTI.Financial_Repeat;
                        CreateTLVFields(1, Constants.MTI.Financial_Repeat,FinalData);
                    }

                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;
                case Constants.TransType.ALIPAY_REFUND:
                    break;
                case Constants.TransType.PAYMENT_SALE:
                    break;
                case Constants.TransType.ALIPAY_UPLOAD:
                case Constants.TransType.VOID:
                case Constants.TransType.REVERSAL:
                    if(inTrxType == Constants.TransType.VOID)
                        CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                    else if(inTrxType == Constants.TransType.REVERSAL)
                        CreateTLVFields(1, Constants.MTI.Reversal,FinalData);
                    else if(inTrxType == Constants.TransType.ALIPAY_UPLOAD)
                        CreateTLVFields(1, Constants.MTI.FinancialAdvice,FinalData);
                    else
                        CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                   if(inTrxType == Constants.TransType.VOID)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                    else if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE && inTrxType == Constants.TransType.REVERSAL)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    else if(TransactionDetails.inOritrxType == Constants.TransType.VOID && inTrxType == Constants.TransType.REVERSAL)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                   else if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE && inTrxType == Constants.TransType.ALIPAY_UPLOAD)
                       CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                   else if(TransactionDetails.inOritrxType == Constants.TransType.VOID && inTrxType == Constants.TransType.ALIPAY_UPLOAD)
                       CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                    else
                       CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    if(inTrxType != Constants.TransType.REVERSAL)
                        CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    //CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    if(inTrxType == Constants.TransType.REVERSAL)
                        CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);

                    if(inTrxType == Constants.TransType.ALIPAY_UPLOAD)
                        CreateTLVFields(72,TransactionDetails.AlipayTag72,FinalData);

                    break;
               // case Constants.TransType.REVERSAL:

                    //break;
                case Constants.TransType.REFUND:
                    break;
                case Constants.TransType.INIT_SETTLEMENT:

                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.initsettrequest;
                    TransactionDetails.messagetype = Constants.MTI.Settlement;

                    CreateTLVFields(1, Constants.MTI.Settlement,FinalData);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.initsettrequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    //CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    //CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);



                    vdScanRecord(databaseObj);


                    if(SaleCount!=0)
                        CreateTLVFields(42,String.format("%03d", SaleCount),FinalData);
                    else
                        CreateTLVFields(42,"000",FinalData);
                    if(SaleAmount !=0)
                        CreateTLVFields(43,String.format("%012d", SaleAmount),FinalData);
                    else
                        CreateTLVFields(43,"000000000000",FinalData);
                    if(RefundCount != 0)
                        CreateTLVFields(44,String.format("%03d", RefundCount),FinalData);
                    else
                        CreateTLVFields(44,"000",FinalData);
                    if(RefundAmount != 0)
                        CreateTLVFields(45,String.format("%012d", RefundAmount),FinalData);
                    else
                        CreateTLVFields(45,"000000000000",FinalData);
                    if(Integer.parseInt(hostData.getHDT_BATCH_NUMBER()) != 0) {
                        long inLong = Long.parseLong(hostData.getHDT_BATCH_NUMBER());
                        CreateTLVFields(46, String.format("%06d", inLong), FinalData);
                    }
                    else
                        CreateTLVFields(46,"000000",FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;
                case Constants.TransType.FINAL_SETTLEMENT:
                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.finalsettrequest;
                    TransactionDetails.messagetype = Constants.MTI.Settlement;

                    CreateTLVFields(1, Constants.MTI.Settlement,FinalData);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.finalsettrequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    //CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    //CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);



                    vdScanRecord(databaseObj);

                    if(SaleCount!=0)
                        CreateTLVFields(42,String.format("%03d", SaleCount),FinalData);
                    else
                        CreateTLVFields(42,"000",FinalData);
                    if(SaleAmount !=0)
                        CreateTLVFields(43,String.format("%012d", SaleAmount),FinalData);
                    else
                        CreateTLVFields(43,"000000000000",FinalData);
                    if(RefundCount != 0)
                        CreateTLVFields(44,String.format("%03d", RefundCount),FinalData);
                    else
                        CreateTLVFields(44,"000",FinalData);
                    if(RefundAmount != 0)
                        CreateTLVFields(45,String.format("%012d", RefundAmount),FinalData);
                    else
                        CreateTLVFields(45,"000000000000",FinalData);
                    if(Integer.parseInt(hostData.getHDT_BATCH_NUMBER()) != 0) {
                        long inLong = Long.parseLong(hostData.getHDT_BATCH_NUMBER());
                        CreateTLVFields(46, String.format("%06d", inLong), FinalData);
                    }
                    else
                        CreateTLVFields(46,"000000",FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;
                case Constants.TransType.BATCH_TRANSFER:
                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.pcFinancialRequest;
                    TransactionDetails.messagetype = Constants.MTI.BatchUpload;

                    CreateTLVFields(1, Constants.MTI.BatchUpload,FinalData);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    if(inVoided == 1) {
                        TransactionDetails.trxAmount="0";
                        CreateTLVFields(10, TransactionDetails.trxAmount, FinalData);
                    }
                    else
                        CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);

                    break;
            }

            //String result;


            //  Log.i(TAG,"PacketCreation:::PACK:");
            // Get and print the output result
            try {
                if(TransactionDetails.trxType != Constants.TransType.INIT_SETTLEMENT &&
                        TransactionDetails.trxType != Constants.TransType.FINAL_SETTLEMENT&&
                        TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE && TransactionDetails.inOritrxType != Constants.TransType.ALIPAY_SALE) {
                    Log.i(TAG,"PacketCreation:::NOT ALIPAY SALES ");
                    Log.i(TAG,"PacketCreation:::NOT ALIPAY SALES ");
                    logISOMsg(isoMsg);
                    byte[] data = isoMsg.pack();
                    TransactionDetails.inFinalLength = AddLength_Tpdu(data, FinalData);
                }else
                {
                    Log.i(TAG,"PacketCreation::: ALIPAY SALES ");
                    Log.i(TAG,"PacketCreation::: ALIPAY SALES ");
                    byte[] data = new byte[TransactionDetails.inFinalLength];
                    for(int ij=0;ij<TransactionDetails.inFinalLength;ij++){
                        data[ij] = FinalData[ij];
                    }
                    TransactionDetails.inFinalLength = AddLength_Tpdu(data, FinalData);
                }


                //  Log.i(TAG,"PacketCreation:::data" + new String(data));
                //  Log.i(TAG,"PacketCreation:::FINAL DATA" + new String(FinalData));
            } catch (Exception ex) {
                Log.i(TAG, "PacketCreation:::IOException EXCEPTION");
                Log.i(TAG, ex.getMessage());
            }

        } catch (ISOException ex) {
            Log.i(TAG, "PacketCreation:::ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }





        return TransactionDetails.inFinalLength;
    }

    public int CreateTLVFields(int inTag, String stValue, byte[] FinalData) {
        byte[] byValue = new byte[2000];
        int inposition = 0;

        Log.i(TAG,"PacketCreation:::CreateTLVFields_1 : ");
        byValue[0] = (byte) (inTag / 256);// 0x00;
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_2 : ");
        byValue[1] = (byte) (inTag % 256);
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_3: ");
        inposition = inposition + 2;
       // Log.i(TAG,"PacketCreation:::CreateTLVFields_4 : ");
        byValue[2] = (byte) (stValue.length() / 256);
       // Log.i(TAG,"PacketCreation:::CreateTLVFields_5 : ");
        byValue[3] = (byte) (stValue.length() % 256);
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_6 : ");
        inposition = inposition + 2;
        Log.i(TAG,"PacketCreation:::CreateTLVFields_7 : ");

        byte[] bytemp1 = stValue.getBytes();
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_8 : ");
        for (int i = 0; i < stValue.length(); i++) {
            byValue[i + 4] = bytemp1[i];
        }
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_9 : ");
        inposition = inposition + stValue.length();
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_10 : ");

        for (int i = 0; i < inposition; i++) {
            FinalData[TransactionDetails.inFinalLength + i] = byValue[i];
        }
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_11 : ");
        TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + inposition;
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_12 : ");
        return inposition;
    }

    private void logISOMsg(ISOMsg msg) {
        Log.i(TAG, "PacketCreation:::----ISO MESSAGE-----");
        try {
            Log.i(TAG, "PacketCreation:::  MTI : " + msg.getMTI());
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    Log.i(TAG, "PacketCreation:::    Field-" + i + " : " + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            Log.i(TAG, "PacketCreation:::--------------------");
        }

    }

   // public int unsignedToBytes(byte b) {
     //   return b & 0xFF;
    //}

    public int AddLength_Tpdu(byte[] data, byte[] FinalData) {
        int inOffset = 0;
        // byte[] FinalData = new byte[data.length + 7];
        int inPacLen = (data.length);

        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_1");
        inOffset = inOffset + 2;
        byte[] tpdu = new byte[10];
        // Copy the respective TPDU Value
        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_2");
        if(TransactionDetails.trxType == Constants.TransType.TMS_INITIAL_PACKET || TransactionDetails.trxType == Constants.TransType.TMS_SUBSEQUENT_PACKET)
            tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();
        else
            tpdu = new BigInteger(hostData.getHDT_TPDU(), 16).toByteArray();

        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_3");
        for (int i = 0; i < tpdu.length; i++) {
            FinalData[inOffset++] = tpdu[i];
        }
        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_4");
		/*
		 * byte[] byLen = new BigInteger(Integer.toString(inPacLen +
		 * tpdu.length), 16).toByteArray(); if (byLen.length == 1) {
		 * FinalData[0] = 0x00; FinalData[1] = byLen[0]; } else { FinalData[0] =
		 * byLen[0]; FinalData[1] = byLen[1]; }
		 */
        // Check the length in BCD or HEX
        FinalData[0] = (byte) ((inPacLen + tpdu.length) / 256);
        FinalData[1] = (byte) ((inPacLen + tpdu.length) % 256);

        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_5");
        for (int i = 0; i < inPacLen; i++) {
            FinalData[inOffset++] = data[i];
        }



       /* String result;
        result = "";
        for (int k = 0; k < inOffset; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        Log.i(TAG,"PacketCreation:::\nResults:");
        Log.i(TAG,result);*/
        return inOffset;
    }

    public int BatchTranfer(DBHelper databaseObj,String ServerIP,String Port){

        BatchModel batchModeldata;
        byte[] FinalData = new byte[1512];
        RemoteHost remoteHost = new RemoteHost();
        List<BatchModel> batchModelList=databaseObj.getBatchData(hostData.getHDT_HOST_ID());
        for(int i=0;i<batchModelList.size();i++) {
            batchModeldata = batchModelList.get(i);
            if( !(batchModeldata ==null || batchModeldata.equals(""))) {
                TransactionDetails.trxType = Integer.parseInt(batchModeldata.getTRANS_TYPE());
                inVoided = Integer.parseInt(batchModeldata.getVOIDED());
                TransactionDetails.inOritrxType = Integer.parseInt(batchModeldata.getTRANS_TYPE());
                TransactionDetails.inGTrxMode = Integer.parseInt(batchModeldata.getTRANS_MODE());
                TransactionDetails.processingcode = batchModeldata.getPROC_CODE();
                TransactionDetails.trxAmount = batchModeldata.getAMOUNT();
                TransactionDetails.tipAmount = batchModeldata.getTIP_AMOUNT();
                TransactionDetails.trxDateTime = batchModeldata.getYEAR();
                TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getDATE();
                TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getTIME();
                TransactionDetails.messagetype = batchModeldata.getORG_MESS_ID();
//        batchModeldata.getSYS_TRACE_NUM(payServices.pGetSystemTrace(databaseObj));
                TransactionDetails.ExpDate = batchModeldata.getDATE_EXP();
                TransactionDetails.RetrievalRefNumber = batchModeldata.getRETR_REF_NUM();
                TransactionDetails.chApprovalCode = batchModeldata.getAUTH_ID_RESP();
                TransactionDetails.ResponseCode = batchModeldata.getRESP_CODE();
                TransactionDetails.PAN = batchModeldata.getACCT_NUMBER();
                TransactionDetails.PersonName = batchModeldata.getPERSON_NAME();
                TransactionDetails.trxAmount = batchModeldata.getORIGINAL_AMOUNT();
                TransactionDetails.responseMessge = batchModeldata.getADDITIONAL_DATA();
                //batchModeldata.getPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
                TransactionDetails.PAN = batchModeldata.getPRIMARY_ACC_NUM();
                TransactionDetails.POSEntryMode = batchModeldata.getPOS_ENT_MODE();
                TransactionDetails.NII = batchModeldata.getNII();
                TransactionDetails.POS_COND_CODE = batchModeldata.getPOS_COND_CODE();

                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
                Date date = new Date();
                String stDate = dateFormat.format(date);
                TransactionDetails.trxDateTime=stDate;

                TransactionDetails.inFinalLength = inCreatePacket(databaseObj,FinalData, Constants.TransType.BATCH_TRANSFER);
                if(TransactionDetails.inFinalLength == 0)
                    return Constants.ReturnValues.RETURN_ERROR;

                if (remoteHost.inConnection(ServerIP, Port) != 0)
                        return Constants.ReturnValues.RETURN_ERROR;

                if ((FinalData = remoteHost.inSendRecvPacket(FinalData,TransactionDetails.inFinalLength)) ==null)
                    return Constants.ReturnValues.RETURN_ERROR;

                int inRet = inProcessPacket(FinalData,TransactionDetails.inFinalLength);
                 if(inRet != Constants.ReturnValues.RETURN_OK) {
                     return Constants.ReturnValues.RETURN_ERROR;
                }
                if (remoteHost.inDisconnection() != Constants.ReturnValues.RETURN_OK) {
                    return Constants.ReturnValues.RETURN_ERROR;
                }

            }
        }
        return Constants.ReturnValues.RETURN_OK;
    }
    public int inProcessPacket(byte[] FinalData,int nFinalLength) {
        //String result="";
        try {
            Log.i(TAG,"PacketCreation:::\ninProcessPacket_1:");
            if(TransactionDetails.trxType!= Constants.TransType.INIT_SETTLEMENT &&
                    TransactionDetails.trxType!= Constants.TransType.FINAL_SETTLEMENT &&
                    TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE && TransactionDetails.inOritrxType != Constants.TransType.ALIPAY_SALE) {
                isoMsg.unpack(FinalData);
                // print the DE list
                logISOMsg(isoMsg);
            }else{
                Log.i(TAG,"PacketCreation:::\ninProcessPacket_2:");



                inParceAlipyResponse(FinalData,TransactionDetails.inFinalLength);

                Log.i(TAG,"PacketCreation:::\ninProcessPacket_3:");
                Log.i(TAG,"PacketCreation:::\nTerminal-ID:"+trDetails.getResTerminalId());

                if(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT)
                {
                    if(TransactionDetails.ResponseCode.equals("95")){
                        Log.i(TAG,"PacketCreation:::\ninProcessPacket_4-BATCH TRANSFER:");
                        return Constants.ReturnValues.RETURN_BATCH_TRANSFER;//ERROR
                    }
                }
                if(trDetails.getResponseCode().equals("00")){
                    Log.i(TAG,"PacketCreation:::\ninProcessPacket_4:");
                    return Constants.ReturnValues.RETURN_OK;//ERROR
                }else if (TransactionDetails.ResponseCode.equals("UK")){
                    return Constants.ReturnValues.RETURN_UNKNOWN;//ERROR
                }else {
                    return Constants.ReturnValues.RETURN_ERROR;//ERROR
                }
            }
        } catch (ISOException ex) {
            Log.i(TAG, "PacketCreation:::ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }

        return Constants.ReturnValues.RETURN_OK;
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

        /*String result = "";
        for (int k = 0; k < inLen; k++) {
            result = result + String.format("%02x", input[k]);
        }
        Log.i(TAG,"PacketCreation:::\nAlipay_inParceAlipyResponse:");
        Log.i(TAG,result);*/

        while (true) {
            inTag = (input[incurrentposition] * 256) + ((input[incurrentposition + 1]));
            incurrentposition = incurrentposition + 2;
            inLength = ((input[incurrentposition] & 0xFF) * 256) + ((input[incurrentposition + 1])&0xFF);
            incurrentposition = incurrentposition + 2;
            byte[] chtemp = new byte[inLength];
            Log.i(TAG,"PacketCreation:::inTag inTag :: "+chtemp.length);
            Log.i(TAG,"PacketCreation:::inTag inLength :: "+inLength);
            Log.i(TAG,"PacketCreation:::inTag actual Len Initially :: "+chtemp.length);
            for (int lp = 0; lp < inLength; lp++) {
                chtemp[lp] = input[incurrentposition + lp];
            }
            incurrentposition = incurrentposition + inLength;

            Log.i(TAG,"PacketCreation:::inTag :: "+inTag);
            Log.i(TAG,"PacketCreation:::inTag_inLength :: "+inLength);
            Log.i(TAG,"PacketCreation:::inTag actual Len :: "+chtemp.length);
            Log.i(TAG,"PacketCreation:::inTag actual Value :: "+new String(chtemp));

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
                    TransactionDetails.RetrievalRefNumber=new String(chtemp);
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
                    TransactionDetails.chApprovalCode = new String(chtemp);
                    break;
                case 0x27:// responsecode
                    trDetails.setResponseCode( new String(chtemp));
                    TransactionDetails.ResponseCode=new String(chtemp);
                    break;
                case 0x28:// responsemesage
                    trDetails.setResponseMesage( new String(chtemp));
                    TransactionDetails.responseMessge=new String(chtemp);
                    break;
                case 0x29:// terminalid
                    trDetails.setResTerminalId( new String(chtemp));
                    break;
                case 63:// Host Message
                    TransactionDetails.responseMessge=new String(chtemp);
                    break;
                case 72:// Host Message
                    TransactionDetails.AlipayTag72=new String(chtemp);
                    break;

            }
            if (incurrentposition >= inLen)
                break;

        }
        return 0;
    }




}
