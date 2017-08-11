package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.AlipayModel;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by venkat on 8/2/2017.
 */

public class FSaveRecord extends EHostConnectivity {

    private Activity locontext;

    
    //public FSaveRecord(Activity context){
        //super(context);
        //locontext=context;
    //}

    public BatchModel vdSaveRecord(){
        // alipayModel = databaseObj.getalipayModelData(0);


        BatchModel batchModel = new BatchModel();
        //Save transaction
        if(TransactionDetails.trxType != Constants.TransType.VOID) {
            batchModel.setHdt_index(String.format("%02d",TransactionDetails.inGHDT));
            batchModel.setTrans_type(Integer.toString(TransactionDetails.trxType));
            batchModel.setTrans_mode(Integer.toString(TransactionDetails.inGTrxMode));
            batchModel.setVoided(Integer.toString(Constants.FALSE));
            batchModel.setUploaded(Integer.toString(Constants.FALSE));
            batchModel.setProc_code(TransactionDetails.processingcode);

            String aa = payServices.pGetSystemTrace(locontext);
            batchModel.setInvoice_number(payServices.pGetSystemTrace(locontext));
            batchModel.setAmount(TransactionDetails.trxAmount);
            batchModel.setTip_amount(TransactionDetails.tipAmount);
            batchModel.setTime(TransactionDetails.trxDateTime.substring(8, 14));
            batchModel.setDate(TransactionDetails.trxDateTime.substring(4, 8));
            batchModel.setYear(TransactionDetails.trxDateTime.substring(0, 4));
            batchModel.setOrg_mess_id(TransactionDetails.messagetype);
            batchModel.setSys_trace_num(payServices.pGetSystemTrace(locontext));
            batchModel.setDate_exp(TransactionDetails.ExpDate);

            batchModel.setRetr_ref_num(TransactionDetails.RetrievalRefNumber);
            batchModel.setAuth_id_resp(TransactionDetails.chApprovalCode);
            batchModel.setResp_code(TransactionDetails.ResponseCode);
            batchModel.setAcct_number(TransactionDetails.PAN);
            batchModel.setPerson_name(TransactionDetails.PersonName);
            batchModel.setOriginal_amount(TransactionDetails.trxAmount);
            batchModel.setAdditional_data(TransactionDetails.responseMessge);
            //batchModel.setPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
            batchModel.setPri_acct_num(TransactionDetails.PAN);
            batchModel.setPos_ent_mode(TransactionDetails.POSEntryMode);
            batchModel.setNII(TransactionDetails.NII);
            batchModel.setPos_cond_code(TransactionDetails.POS_COND_CODE);
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
            batchModel.setVoided(Integer.toString(Constants.TRUE));
            GreenDaoSupport.insertBatchModelOBJ(locontext,batchModel);
            //databaseObj.UpdateBatchData(batchModel);
            if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE) {
                //Create a update table since Alipay ned to send upload transaction
                GreenDaoSupport.insertBatchModelOBJ(locontext,batchModel);
                // databaseObj.insertBatchData(batchModel);
            }

        }else
            //databaseObj.
            GreenDaoSupport.insertBatchModelOBJ(locontext, batchModel);//.insertBatchData(batchModel);


        return batchModel;
    }


}
