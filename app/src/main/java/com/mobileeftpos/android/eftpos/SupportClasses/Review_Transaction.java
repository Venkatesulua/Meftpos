package com.mobileeftpos.android.eftpos.SupportClasses;

import android.util.Log;

import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.BatchModel;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.TransactionControlModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by venkat on 6/13/2017.
 */
public class Review_Transaction {

    private CurrencyModel currModelData = new CurrencyModel();
    private TransactionControlModel transCtrlModelData = new TransactionControlModel();
    private final String TAG = "my_custom_msg";

    public String lgReviewAllTrans(DBHelper databaseObj, String STDisplayTranstype,BatchModel batchModeldata){
        //BatchModel batchModeldata = new BatchModel();
        HostModel hostModel = new HostModel();

        PayServices payService = new PayServices();
        RemoteHost remoteHost = new RemoteHost();

        currModelData = databaseObj.getCurrencyData(0);
        /*transCtrlModelData = databaseObj.getTransactionCtrlData(0);

        if(!transCtrlModelData.getVOID_CTRL().equals("1"))
        {
            return Constants.TRANSCATION_NOT_SUPPORTED;
        }*/
        batchModeldata = databaseObj.getBatchDataUsngInvoice(STDisplayTranstype);//Read Transaction details
        String StHDT = batchModeldata.getHDT_INDEX();
        hostModel =databaseObj.getHostTableData(Integer.parseInt(StHDT));
       if(batchModeldata.getVOIDED().equals(Integer.toString(Constants.TRUE))){
           return Constants.ALREADY_VOIDED;
       }
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



        return null;
    }
}
