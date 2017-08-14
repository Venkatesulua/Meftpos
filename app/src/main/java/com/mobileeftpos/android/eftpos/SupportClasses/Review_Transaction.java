package com.mobileeftpos.android.eftpos.SupportClasses;

import android.app.Activity;

import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by venkat on 6/13/2017.
 */
public class Review_Transaction {

    private CurrencyModel currModelData = new CurrencyModel();
    private TransactionControlModel transCtrlModelData = new TransactionControlModel();
    private final String TAG = "my_custom_msg";

    public BatchModel lgReviewAllTrans(Activity activity, String STDisplayTranstype, BatchModel batchModeldata){
        //BatchModel batchModeldata = new BatchModel();
        HostModel hostModel = new HostModel();

        currModelData = GreenDaoSupport.getCurrencyTableModelOBJ(activity);

        //currModelData = databaseObj.getCurrencyData(0);
        /*transCtrlModelData = databaseObj.getTransactionCtrlData(0);

        if(!transCtrlModelData.getVOID_CTRL().equals("1"))
        {
            return Constants.TRANSCATION_NOT_SUPPORTED;
        }*/
        //List<BatchModel> batchData = GreenDaoSupport.getBatchModelOBJList(activity,"23");

        batchModeldata = GreenDaoSupport.getBatchModelbyInvoiceOBJ(activity,STDisplayTranstype);//Read Transaction


        // details
        //batchModeldata = databaseObj.getBatchDataUsngInvoice(STDisplayTranstype);//Read Transaction details
        if(batchModeldata == null)
            return null;
        if(batchModeldata.getInvoice_number() == null)
            return null;
        String StHDT = batchModeldata.getHdt_index();
        if(batchModeldata.getVoided().equals(Integer.toString(Constants.TRUE))){

           return null;
       }
        TransactionDetails.inOritrxType = Integer.parseInt(batchModeldata.getTrans_type());
        TransactionDetails.inGTrxMode = Integer.parseInt(batchModeldata.getTrans_type());
        TransactionDetails.processingcode = batchModeldata.getProc_code();
        TransactionDetails.trxAmount = batchModeldata.getAmount();
        TransactionDetails.tipAmount = batchModeldata.getTip_amount();
        TransactionDetails.trxDateTime = batchModeldata.getYear();
        TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getDate();
        TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getTime();
        TransactionDetails.messagetype = batchModeldata.getOrg_mess_id();
//        batchModeldata.getSYS_TRACE_NUM(payServices.pGetSystemTrace(databaseObj));
        TransactionDetails.ExpDate = batchModeldata.getDate_exp();
        TransactionDetails.RetrievalRefNumber = batchModeldata.getRetr_ref_num();
        TransactionDetails.chApprovalCode = batchModeldata.getAuth_id_resp();
        TransactionDetails.ResponseCode = batchModeldata.getResp_code();
        TransactionDetails.PAN = batchModeldata.getAcct_number();
        TransactionDetails.PersonName = batchModeldata.getPerson_name();
        TransactionDetails.trxAmount = batchModeldata.getOriginal_amount();
        TransactionDetails.responseMessge = batchModeldata.getAdditional_data();
        //batchModeldata.getPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
        TransactionDetails.PAN = batchModeldata.getPri_acct_num();
        TransactionDetails.POSEntryMode = batchModeldata.getPos_ent_mode();
        TransactionDetails.NII = batchModeldata.getNII();
        TransactionDetails.POS_COND_CODE = batchModeldata.getPos_cond_code();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String stDate = dateFormat.format(date);
        TransactionDetails.trxDateTime=stDate;



        return batchModeldata;
    }
}
