package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.BluetoothUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.ESCUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by venkat on 8/2/2017.
 */

public class GPrintReceipt extends FSaveRecord {

    private Activity locontext;
    private static int MerchantCopy =0;


    public GPrintReceipt(Activity context){
        super(context);
        locontext=context;
    }

    public int inPrintReceipt( ){
        Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING:");
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            //Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
            Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_ERR!:");
            return 1;
        }
        Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_2:");
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            //Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!",
            //Toast.LENGTH_LONG).show();
            Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING:_ERR@");
            return 1;
        }
        Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_2:");

        Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_STRING STRING:");
        // 3: Generate a order data
        byte[] data = generateMockData1();
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_23:");
            BluetoothUtil.sendData(data, socket);
            Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING_3:");
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    Log.i(TAG,"\nPrintReceiptPRINTING-BUILDING:___ERRR#");
                    e1.printStackTrace();
                }
            }
        }
        return 0;
    }

    private byte[] generateMockData1() {
        String inPrintBuffer="";
       

        Log.i(TAG,"generateMockData1 getHDT_TERMINAL_ID::"+hostModel.getHDT_TERMINAL_ID());
        Log.i(TAG,"generateMockData1 getMERCHANT_NAME::"+merchantModel.getMERCHANT_NAME());

        //CurrencyModel curr = new CurrencyModel();
        //curr =currModelDao.loadAll().get(0);// databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        String cuLabel = currencyModel.getCURR_LABEL();

        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "The menu（launch）**wanda plaza".getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] Focus = "Web 507".getBytes("gb2312");
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);

            byte[] left = ESCUtil.alignLeft();
            byte[] orderSerinum = "Order No.11234".getBytes("gb2312");
            boldOn = ESCUtil.boldOn();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] FocusOrderContent = "Big hamburger(single)".getBytes("gb2312");
            boldOff = ESCUtil.boldOff();
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

            next2Line = ESCUtil.nextLine(2);

            byte[] priceInfo = "Receivable:$22  Discount：$2.5 ".getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);

            byte[] priceShouldPay = "Actual collection:$19.5".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);

            byte[] takeTime = "Pickup time:2015-02-13 12:51:59".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] setOrderTime = "Order time：2015-02-13 12:35:15".getBytes("gb2312");

            byte[] tips_1 = "Follow twitter\"**\"order for $1 discount".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] tips_2 = "Commentary reward 50 cents".getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(center, "UTF-8")+new String(boldOn, "UTF-8")+new String(fontSize1Big, "UTF-8");
            inPrintBuffer = inPrintBuffer + merchantModel.getMERCHANT_NAME()+new String(boldOff, "UTF-8");

            if(merchantModel.getMERCHANT_HEADER1().length() !=0) {
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getMERCHANT_HEADER1();
            }
            if(merchantModel.getMERCHANT_HEADER2().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getMERCHANT_HEADER2();
            }
            if(merchantModel.getADDRESS_LINE1().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getADDRESS_LINE1();
            }if(merchantModel.getADDRESS_LINE2().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getADDRESS_LINE2();
            }
            if(merchantModel.getADDRESS_LINE3().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getADDRESS_LINE3();
            }
            if(merchantModel.getADDRESS_LINE4().length() !=0){
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8")+new String(fontSize2Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + merchantModel.getADDRESS_LINE4();
            }
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(center, "UTF-8")+new String(boldOn, "UTF-8")+new String(fontSize1Big, "UTF-8");

            if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE)
                inPrintBuffer = inPrintBuffer + "EZLINK PAYMENT" + new String(boldOff, "UTF-8");
            else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE)
                inPrintBuffer = inPrintBuffer + "ALIPAY SALE" + new String(boldOff, "UTF-8");
            else if(TransactionDetails.trxType == Constants.TransType.VOID)
                inPrintBuffer = inPrintBuffer + "VOID" + new String(boldOff, "UTF-8");
            else if(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)
                inPrintBuffer = inPrintBuffer + "SETTLEMENT" + new String(boldOff, "UTF-8");
            else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                inPrintBuffer = inPrintBuffer + "REFUND" + new String(boldOff, "UTF-8");
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            String dateTime = "DATE/TIME : " + TransactionDetails.trxDateTime.substring(2,4)+"/"+TransactionDetails.trxDateTime.substring(4,6)+"/"+TransactionDetails.trxDateTime.substring(6,8)+" "+
                    TransactionDetails.trxDateTime.substring(8,10) +":"+TransactionDetails.trxDateTime.substring(10,12)+":"+TransactionDetails.trxDateTime.substring(12,14);
            inPrintBuffer = inPrintBuffer + dateTime;
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            String stTemp1 = "MID:" + hostModel.getHDT_MERCHANT_ID();
            String stTemp2 = "TID:"+hostModel.getHDT_TERMINAL_ID();
            String Temp3 ="";
            for(int i=0; i< (31- stTemp1.length() - stTemp2.length()); i++){
                Temp3 = Temp3+" ";
            }

            inPrintBuffer = inPrintBuffer + stTemp1 + Temp3+  stTemp2 + "\n";
            //inPrintBuffer = inPrintBuffer + "MID:" + hostModel.getHDT_MERCHANT_ID()+" "+"TID:"+hostModel.getHDT_TERMINAL_ID()+"\n";

            stTemp1 = "INVOICE:" + TransactionDetails.InvoiceNumber;
            stTemp2 = "BATCH:"+hostModel.getHDT_BATCH_NUMBER();
            Temp3 ="";
            for(int i=0; i< (31-stTemp1.length() - stTemp2.length()); i++){
                Temp3 = Temp3+" ";
            }
            inPrintBuffer = inPrintBuffer + stTemp1 + Temp3+  stTemp2+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+" ";
            inPrintBuffer = inPrintBuffer + "HOST: "+hostModel.getHDT_HOST_LABEL()+"\n";
            inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE)
                inPrintBuffer = inPrintBuffer +TransactionDetails.PAN+"\n";
            else if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND))
                inPrintBuffer = inPrintBuffer + "BUYER IDENTITY CODE\n"+TransactionDetails.PAN+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + TransactionDetails.PAN;
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + "PTNR TRANS ID:"+TransactionDetails.PartnerTransID + "\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ TransactionDetails.trxDateTime + "00";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + "PARTNER ID   :"+ barcode.getPARTNER_ID()+"\n";
            // inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getPartnerId();
            // inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer +"SELLER ID    :"+ barcode.getSELLER_ID()+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getSellerId();
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer +"ALIPAY ID    :"+ TransactionDetails.chApprovalCode+"\n";
            if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE){
                int previousBal = (TransactionDetails.bOrigBal[0] * 256 * 256) + (TransactionDetails.bOrigBal[1] * 256) + TransactionDetails.bOrigBal[2];
                String stPreAmount = String.format("%01d.%02d", (previousBal / 100), (previousBal % 100));
                inPrintBuffer = inPrintBuffer +"PREVIOUS BALANCE "+cuLabel +" "+ stPreAmount+"\n";

                int newBal = (TransactionDetails.PurseBalance[0] * 256 * 256) + (TransactionDetails.PurseBalance[1] * 256) + TransactionDetails.PurseBalance[2];
                String stPostAmount = String.format("%01d.%02d", (newBal / 100), (newBal % 100));
                inPrintBuffer = inPrintBuffer +"NEW BALANCE      "+cuLabel +" "+ stPostAmount+"\n";
            }
            else
                inPrintBuffer = inPrintBuffer + TransactionDetails.responseMessge+"\n";
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+new String(left, "UTF-8")+new String(fontSize2Small, "UTF-8");
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+ trDetails.getAlipayTransId();
            //inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8")+" ";

            if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)) {
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(left, "UTF-8") + new String(boldOn, "UTF-8") + new String(fontSize1Big, "UTF-8");
                int inAmount = Integer.parseInt(TransactionDetails.trxAmount);

                Log.i(TAG, "inAmount:" + inAmount);

                String stAmount = String.format("%01d.%02d", (inAmount / 100), (inAmount % 100));
                Log.i(TAG, "stAmount:" + stAmount);


                inPrintBuffer = inPrintBuffer + "AMT "+ cuLabel +" "+ stAmount + new String(boldOff, "UTF-8");
            }


            if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)) {

                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8") + new String(fontSize1Small, "UTF-8");
                inPrintBuffer = inPrintBuffer + "I AGREE TO PAY THE ABOVE AMOUNT" + "\n";
                inPrintBuffer = inPrintBuffer + "ACCORDING TO ISSUER AGREEMENT";
                inPrintBuffer = inPrintBuffer + new String(nextLine, "UTF-8") + new String(center, "UTF-8") + new String(fontSize2Small, "UTF-8");
                if (MerchantCopy == 0) {
                    inPrintBuffer = inPrintBuffer + "MERCHANT COPY" + new String(next4Line, "UTF-8") + new String(breakPartial, "UTF-8");
                    MerchantCopy++;
                } else {
                    MerchantCopy = 0;
                    inPrintBuffer = inPrintBuffer + "CUSTOMER COPY" + new String(next4Line, "UTF-8") + new String(breakPartial, "UTF-8");
                }
            }else{
                inPrintBuffer = inPrintBuffer + "" + new String(next4Line, "UTF-8") + new String(breakPartial, "UTF-8");
            }

            KeyValueDB.setLastReceipt(locontext,inPrintBuffer);


            return inPrintBuffer.getBytes();
            //return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
