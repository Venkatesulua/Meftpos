package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by venkat on 8/2/2017.
 */

public class CPacketHandling extends BValidateCard{

    public ISOPackager1 packager = new ISOPackager1();
    public ISOMsg isoMsg = new ISOMsg();
    private GlobalVar globalVar = new GlobalVar();
    long SaleCount=0,SaleAmount=0,RefundCount=0,RefundAmount=0;
    int inVoided=0;

    private String Field47="";

    public void setField47(String Field47)
    {
        this.Field47=Field47;
    }
    public String getField47()
    {
        return this.Field47;
    }

    public int inFCreatePacket( byte[] FinalData, int inTrxType, Activity activity)
    {
        String stde27 = "";
        String stde29 = "";

        TransactionDetails.inFinalLength=0;

        //alipayModel =GreenDaoSupport.getAlipayModelOBJ(activity); //databaseObj.getalipayModelData(0);
        //currencyModel = GreenDaoSupport.getCurrencyTableModelOBJ(activity);
        //databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        //GetHostModel() = GreenDaoSupport.getHostTableModelOBJ(activity);
        //databaseObj.getHostTableData(TransactionDetails.inGHDT);
        //commData = GreenDaoSupport.getCommsModelOBJ(activity);
        //databaseObj.getCommsData(TransactionDetails.inGCOM);
        //merchantModel = GreenDaoSupport.getMerchantModelOBJ(activity);
        //databaseObj.getmerchantModel(0);

        //currencyModel.setCURR_LABEL("MYR");
        //currencyModel.setCURR_EXPONENT("2");
        //currencyModel.setCURR_CODE("458");


        String stAlipayExtData="";
        isoMsg.setPackager(packager);
        try {

            Log.i(TAG,"PacketCreation:::inCreatePacket : ");
            Log.i(TAG,Integer.toString(TransactionDetails.trxType));

            String staddinfo="";
            switch (inTrxType) {
                case Constants.TransType.EZLINK_SALE:
                    isoMsg.setHeader(GetHostModel().getHDT_TPDU().getBytes());
                    isoMsg.setMTI(Constants.MTI.Financial);
                    isoMsg.set(2, TransactionDetails.PAN);
                    isoMsg.set(3, Constants.PROCESSINGCODE.EZLINK_PAYMENT_PROC);
                    isoMsg.set(4, TransactionDetails.trxAmount);
                    isoMsg.set(11, TransactionDetails.InvoiceNumber);
                    isoMsg.set(12, TransactionDetails.trxDateTime.substring(8,14));
                    isoMsg.set(13, TransactionDetails.trxDateTime.substring(4,8));
                    isoMsg.set(24, GetHostModel().getHDT_TPDU().substring(2,6));
                    isoMsg.set(25, "00");
                    isoMsg.set(37, TransactionDetails.RetrievalRefNumber);
                    isoMsg.set(41, GetHostModel().getHDT_TERMINAL_ID());
                    isoMsg.set(42, GetHostModel().getHDT_MERCHANT_ID());
                    isoMsg.set(63, TransactionDetails.responseMessge);

                    break;
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
                    Log.i(TAG,"PacketCreation:::PARTNER_ID::"+alipayModel.getPARTNER_ID());
                    Log.i(TAG,"PacketCreation:::SELLER_ID::"+alipayModel.getSELLER_ID());
                    Log.i(TAG,"PacketCreation:::REGION_CODE::"+alipayModel.getREGION_CODE());

                    Log.i(TAG,"PacketCreation:::TransactionDetails.trxAmount::"+TransactionDetails.trxAmount);
                    Log.i(TAG,"PacketCreation:::getHDT_TERMINAL_ID::"+GetHostModel().getHDT_TERMINAL_ID());
                    Log.i(TAG,"PacketCreation:::getMERCHANT_NAME::"+merchantModel.getMERCHANT_NAME());

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
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);
                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;
                case Constants.TransType.ALIPAY_REFUND:
                    TransactionDetails.processingcode = Constants.PROCESSINGCODE.pcRefund;
                    if(inTrxType == Constants.TransType.ALIPAY_REFUND) {
                        TransactionDetails.messagetype = Constants.MTI.Financial;
                    }
                    else {
                        TransactionDetails.messagetype = Constants.MTI.Financial_Repeat;

                    }
                    CreateTLVFields(1, TransactionDetails.messagetype,FinalData);
                    CreateTLVFields(2, TransactionDetails.stOriAmount,FinalData);
                    CreateTLVFields(3, TransactionDetails.processingcode,FinalData);
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);
                    CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);
                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;

                case Constants.TransType.PAYMENT_SALE:
                    break;
                case Constants.TransType.REVERSAL:
                    CreateTLVFields(1, Constants.MTI.Reversal,FinalData);

                    if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                        CreateTLVFields(2, TransactionDetails.stOriAmount,FinalData);

                    if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);
                    else if(TransactionDetails.trxType == Constants.TransType.VOID)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                    else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcRefund,FinalData);
                    else
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);

                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);
                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    break;
                case Constants.TransType.ALIPAY_UPLOAD:

                    CreateTLVFields(1, Constants.MTI.FinancialAdvice,FinalData);
                    if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                        CreateTLVFields(2, TransactionDetails.stOriAmount,FinalData);

                    if(TransactionDetails.trxType == Constants.TransType.VOID)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                    else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcRefund,FinalData);
                    else
                        CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest,FinalData);

                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);

                    CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);
                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);
                    CreateTLVFields(72,TransactionDetails.AlipayTag72,FinalData);

                    break;
                case Constants.TransType.VOID:
                    CreateTLVFields(1, Constants.MTI.Financial,FinalData);
                    CreateTLVFields(3, Constants.PROCESSINGCODE.stVoid,FinalData);
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);
                    CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    //CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);
                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);

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
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    //CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    //CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);



                    vdScanRecord(activity);


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
                    if(Integer.parseInt(GetHostModel().getHDT_BATCH_NUMBER()) != 0) {
                        long inLong = Long.parseLong(GetHostModel().getHDT_BATCH_NUMBER());
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
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);


                    //CreateTLVFields(8,(TransactionDetails.trxDateTime+"00"),FinalData);
                    CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    //CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    //CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);



                    vdScanRecord(activity);

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
                    if(Integer.parseInt(GetHostModel().getHDT_BATCH_NUMBER()) != 0) {
                        long inLong = Long.parseLong(GetHostModel().getHDT_BATCH_NUMBER());
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
                    CreateTLVFields(3, Constants.PROCESSINGCODE.pcFinancialRequest, FinalData);
                    CreateTLVFields(4, alipayModel.getPARTNER_ID(),FinalData);
                    CreateTLVFields(5,alipayModel.getSELLER_ID(),FinalData);


                    CreateTLVFields(8,TransactionDetails.RetrievalRefNumber,FinalData);
                    CreateTLVFields(9,currencyModel.getCURR_LABEL(),FinalData);
                    if(inVoided == 1) {
                        TransactionDetails.trxAmount="0";
                        CreateTLVFields(10, TransactionDetails.trxAmount, FinalData);
                    }
                    else
                        CreateTLVFields(10,TransactionDetails.trxAmount,FinalData);
                    CreateTLVFields(11,TransactionDetails.PAN,FinalData);
                    CreateTLVFields(41,GetHostModel().getHDT_TERMINAL_ID(),FinalData);

                    CreateTLVFields(47,AlipayExtendedData(stAlipayExtData),FinalData);

                    break;
                case Constants.TransType.BLACKLIST_FIRST_DOWNLOAD:
                case Constants.TransType.BLACKLIST_SUBSEQUENT_DOWNLOAD:
                    isoMsg.setHeader(GetHostModel().getHDT_TPDU().getBytes());
                    isoMsg.setMTI(Constants.MTI.NetworkManagement);
                    if(TransactionDetails.trxType == Constants.TransType.BLACKLIST_FIRST_DOWNLOAD)
                        isoMsg.set(3, Constants.PROCESSINGCODE.BLACKLIST_FIRST_PROC);
                    else
                        isoMsg.set(3, Constants.PROCESSINGCODE.BLACKLIST_NEXT_PROC);

                    isoMsg.set(11, TransactionDetails.InvoiceNumber);
                    String Bii =GetHostModel().getHDT_TPDU().substring(2,6);
                    isoMsg.set(24, Bii);
                    isoMsg.set(25, "00");
                    isoMsg.set(41, GetHostModel().getHDT_TERMINAL_ID());
                    isoMsg.set(42,GetHostModel().getHDT_MERCHANT_ID());

                    if(Field47.isEmpty())
                        isoMsg.set(47,"FULL000000");
                    else
                        isoMsg.set(47,Field47);
                   /* field_47[0] = 0x00; //Ezlink: ABL Changes
                    field_47[1] = 0x10; //Ezlink: ABL Changes
                    if (strlen(stGlobalex.chGAdditionalPrompt) > 0) {
                        memcpy(field_47 + 2, stGlobalex.chGAdditionalPrompt, 10); //Ezlink: ABL Changes
                    } else {
                        if (field_47[2] == 'P' && field_47[3] == 'A' && field_47[4] == 'R' && field_47[5] == 'L') //Ezlink: ABL Changes
                            strncpy((char*) field_47 + 2, "PARL", 4);
			else
                        strncpy((char*) field_47 + 2, "FULL", 4);
                        strncpy((char*) field_47 + 6, "000000", 6);

                    }*/

                    break;

            }

            //String result;


            //  Log.i(TAG,"PacketCreation:::PACK:");
            // Get and print the output result
            try {
                /*if(TransactionDetails.trxType != Constants.TransType.INIT_SETTLEMENT &&
                        TransactionDetails.trxType != Constants.TransType.FINAL_SETTLEMENT&&
                        TransactionDetails.trxType !=  Constants.TransType.ALIPAY_REFUND && TransactionDetails.inOritrxType != Constants.TransType.ALIPAY_REFUND &&
                        TransactionDetails.trxType != Constants.TransType.ALIPAY_SALE && TransactionDetails.inOritrxType != Constants.TransType.ALIPAY_SALE)*/
                if(!(GetHostModel().getHDT_HOST_TYPE().equals(Constants.HostType.ALIPAY_HOST))){
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

    String AlipayExtendedData(String staddinfo)
    {
        staddinfo ="merchant_name:";
        staddinfo = staddinfo + merchantModel.getMERCHANT_NAME();
        staddinfo = staddinfo + ",merchant_no:";
        staddinfo = staddinfo + GetHostModel().getHDT_MERCHANT_ID();
        staddinfo = staddinfo + ",business_no:";
        staddinfo = staddinfo + "0";
        staddinfo = staddinfo + ",terminal_id:";
        staddinfo = staddinfo + GetHostModel().getHDT_TERMINAL_ID();
        staddinfo = staddinfo + ",mcc:";
        staddinfo = staddinfo + "0";
        staddinfo = staddinfo + ",region_code:";
        staddinfo = staddinfo + alipayModel.getREGION_CODE();
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

    public int vdScanRecord(Activity activity){
        //GetHostModel() = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        BatchModel batchData;
        SaleCount=SaleAmount=RefundCount=RefundAmount=0;
        // GetHostModel() = GreenDaoSupport.getHostTableModelOBJ(activity);
        //databaseObj.getHostTableData(TransactionDetails.inGHDT);
        List<BatchModel> batchModelList= GreenDaoSupport.getBatchModelOBJList(activity,GetHostModel().getHDT_HOST_ID());
        //databaseObj.getBatchData(GetHostModel().getHDT_HOST_ID());
        if(batchModelList.size() ==0 )
        {
            return Constants.ReturnValues.NO_TRANSCATION;
        }
        for(int i=0;i<batchModelList.size();i++) {
            batchData = batchModelList.get(i);
            if( !(batchData ==null || batchData.equals(""))){
                int trxType = Integer.parseInt(batchData.getTrans_type());
                inVoided = Integer.parseInt(batchData.getVoided());
                if(trxType == Constants.TransType.ALIPAY_SALE && inVoided != Constants.TRUE)
                {
                    SaleCount = SaleCount+1;
                    SaleAmount = SaleAmount + Long.parseLong(batchData.getAmount());
                }else if(trxType == Constants.TransType.ALIPAY_REFUND && inVoided != Constants.TRUE){
                    RefundCount = RefundCount+1;
                    RefundAmount = RefundAmount + Long.parseLong(batchData.getAmount());
                }
            }

        }

        return Constants.ReturnValues.RETURN_OK;
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
        if(TransactionDetails.trxType == Constants.TransType.TMS_INITIAL_PACKET || TransactionDetails.trxType == Constants.TransType.TMS_SUBSEQUENT_PACKET)
            tpdu = new BigInteger(globalVar.tmsParam.getTMS_TPDU(), 16).toByteArray();
        else
            tpdu = new BigInteger(GetHostModel().getHDT_TPDU(), 16).toByteArray();

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
        if(TransactionDetails.trxType == Constants.TransType.TMS_INITIAL_PACKET || TransactionDetails.trxType == Constants.TransType.TMS_SUBSEQUENT_PACKET ||
                TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE ||TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE ||
                TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND ||TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_REFUND
                ) {
            FinalData[0] = (byte) ((inPacLen + tpdu.length) / 256);
            FinalData[1] = (byte) ((inPacLen + tpdu.length) % 256);
        }else
        {
            String hexString = String.format("%04d",(inPacLen+tpdu.length));
            byte[] value = StringByteUtils.HexString2Bytes(hexString);
            FinalData[0] = value[0];
            FinalData[1] = value[1];
        }

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
}
