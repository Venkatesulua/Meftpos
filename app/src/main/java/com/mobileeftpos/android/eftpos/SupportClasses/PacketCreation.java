package com.mobileeftpos.android.eftpos.SupportClasses;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

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
    private int inFinalLength=0;
    private TransactionDetails trDetails = new TransactionDetails();

    public int vdSaveRecord(DBHelper databaseObj){
        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);
        PayServices payServices= new PayServices();
        BatchModel batchModel = new BatchModel();
        //Save transaction
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
        batchModel.setTIME(TransactionDetails.trxDateTime.substring(8,14));
        batchModel.setDATE(TransactionDetails.trxDateTime.substring(4,8));
        batchModel.setYEAR(TransactionDetails.trxDateTime.substring(0,4));
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
        databaseObj.insertBatchData(batchModel);

        return 0;
    }

    public int inCreatePacket(DBHelper databaseObj,byte[] FinalData,int inTrxType) {
        String stde27 = "";
        String stde29 = "";



        barcode = databaseObj.getBarcodeData(0);
        currModel = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        commData = databaseObj.getCommsData(TransactionDetails.inGCOM);
        merchantData = databaseObj.getMerchantData(0);

        isoMsg.setPackager(packager);
        try {

            Log.i(TAG,"PacketCreation:::inCreatePacket : ");
            Log.i(TAG,Integer.toString(TransactionDetails.trxType));

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
                case Constants.TransType.ALIPAY_SALE:
                    Log.i(TAG,"PacketCreation:::ALIPAY_SALE : ");
                    Log.i(TAG,"PacketCreation:::PARTNER_ID::"+barcode.getPARTNER_ID());
                    Log.i(TAG,"PacketCreation:::SELLER_ID::"+barcode.getSELLER_ID());
                    Log.i(TAG,"PacketCreation:::REGION_CODE::"+barcode.getREGION_CODE());

                    Log.i(TAG,"PacketCreation:::TransactionDetails.trxAmount::"+TransactionDetails.trxAmount);
                    Log.i(TAG,"PacketCreation:::getHDT_TERMINAL_ID::"+hostData.getHDT_TERMINAL_ID());
                    Log.i(TAG,"PacketCreation:::getMERCHANT_NAME::"+merchantData.getMERCHANT_NAME());

                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.pcFinancialRequest;
                    TransactionDetails.messagetype = Constants.MTI.Financial;

                    CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    CreateTLVFields(4, barcode.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,barcode.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,hostData.getHDT_TERMINAL_ID(),FinalData);


                    String staddinfo ="merchant_name:";
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

                    staddinfo = staddinfo + ",scan:";
                    staddinfo = staddinfo + "y";

                    staddinfo = staddinfo + ",proto:";
                    staddinfo = staddinfo + "1";

                    staddinfo = staddinfo + ",sw:";
                    staddinfo = staddinfo + "V1.0";
                    staddinfo = staddinfo + ",";

                    CreateTLVFields(47,staddinfo,FinalData);
                    break;
                case Constants.TransType.ALIPAY_REFUND:
                    break;
                case Constants.TransType.PAYMENT_SALE:
                    break;
                case Constants.TransType.VOID:
                    CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                    break;
                case Constants.TransType.REVERSAL:
                    break;
                case Constants.TransType.REFUND:
                    break;
                case Constants.TransType.SETTLEMENT:
                    break;
            }

            String result;
            result = "";
            for (int k = 0; k < inFinalLength; k++) {
                result = result + String.format("%02x", FinalData[k]);
            }
            Log.i(TAG,"PacketCreation:::\nCreate Message:");
            Log.i(TAG,result);

            //  Log.i(TAG,"PacketCreation:::PACK:");
            // Get and print the output result
            try {
                if(TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE) {
                    Log.i(TAG,"PacketCreation:::NOT ALIPAY SALES ");
                    Log.i(TAG,"PacketCreation:::NOT ALIPAY SALES ");
                    logISOMsg(isoMsg);
                    byte[] data = isoMsg.pack();
                    inFinalLength = AddLength_Tpdu(data, FinalData);
                }else
                {
                    Log.i(TAG,"PacketCreation::: ALIPAY SALES ");
                    Log.i(TAG,"PacketCreation::: ALIPAY SALES ");
                    byte[] data = new byte[inFinalLength];
                    for(int ij=0;ij<inFinalLength;ij++){
                        data[ij] = FinalData[ij];
                    }
                    inFinalLength = AddLength_Tpdu(data, FinalData);
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
        return inFinalLength;
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
            FinalData[inFinalLength + i] = byValue[i];
        }
        //Log.i(TAG,"PacketCreation:::CreateTLVFields_11 : ");
        inFinalLength = inFinalLength + inposition;
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

    public int AddLength_Tpdu(byte[] data, byte[] FinalData) {
        int inOffset = 0;
        // byte[] FinalData = new byte[data.length + 7];
        int inPacLen = (data.length);

        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_1");
        inOffset = inOffset + 2;
        byte[] tpdu = new byte[10];
        // Copy the respective TPDU Value
        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_2");
        if(TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE)
            tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();
        else
            tpdu = new BigInteger("6002540000", 16).toByteArray();

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

        Log.i(TAG,"PacketCreation:::AddLength_Tpdu_6");

        String result;
        result = "";
        for (int k = 0; k < inOffset; k++) {
            result = result + String.format("%02x", FinalData[k]);
        }
        Log.i(TAG,"PacketCreation:::\nResults:");
        Log.i(TAG,result);
        return inOffset;
    }

    public int inProcessPacket(byte[] FinalData,int inFinalLength) {
        String result="";
        try {
            Log.i(TAG,"PacketCreation:::\ninProcessPacket_1:");
            if(TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE ) {
                isoMsg.unpack(FinalData);
                // print the DE list
                logISOMsg(isoMsg);
            }else{
                Log.i(TAG,"PacketCreation:::\ninProcessPacket_2:");

                result = "";
                for (int k = 0; k < inFinalLength; k++) {
                    result = result + String.format("%02x", FinalData[k]);
                }
                Log.i(TAG,"PacketCreation:::\nAlipay_inProcessPacket_Received_2:");
                Log.i(TAG,result);

                inParceAlipyResponse(FinalData,inFinalLength);

                Log.i(TAG,"PacketCreation:::\ninProcessPacket_3:");
                Log.i(TAG,"PacketCreation:::\nTerminal-ID:"+trDetails.getResTerminalId());

                if(!trDetails.getResponseCode().equals("00")){
                    Log.i(TAG,"PacketCreation:::\ninProcessPacket_4:");
                    return 1;//ERROR
                }

            }
        } catch (ISOException ex) {
            Log.i(TAG, "PacketCreation:::ISO EXCEPTION");
            Log.i(TAG, ex.getMessage());
        }

        return 0;
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

        String result = "";
        for (int k = 0; k < inLen; k++) {
            result = result + String.format("%02x", input[k]);
        }
        Log.i(TAG,"PacketCreation:::\nAlipay_inParceAlipyResponse:");
        Log.i(TAG,result);

        while (true) {
            inTag = (input[incurrentposition] * 256) + ((input[incurrentposition + 1]));
            incurrentposition = incurrentposition + 2;
            inLength = (input[incurrentposition] * 256) + ((input[incurrentposition + 1]));
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
                    TransactionDetails.PartnerTransID=new String(chtemp);
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

            }
            if (incurrentposition >= inLen)
                break;

        }
        return 0;
    }




}
