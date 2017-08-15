package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.activity.AlipayCheckPrompt;
import com.mobileeftpos.android.eftpos.activity.PaymentFailure;
import com.mobileeftpos.android.eftpos.activity.PaymentSuccess;

/**
 * Created by venkat on 8/2/2017.
 */

public class HAfterTransaction extends GPrintReceipt {
    private Activity locontext;
    //public HAfterTransaction(Activity context){
        //super(context);
        //locontext=context;
    //}

    public int inAfterTrans()
    {
        vdUpdateSystemTrace();
        return Constants.ReturnValues.RETURN_OK;
    }
    public int inUpdateBatchNumber(){
        vdUpdateSystemBatch();
        return Constants.ReturnValues.RETURN_OK;
    }

    public int FinalStatusDisplay(Activity loContext, String result)
    {
        if(result != null) {
            if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_UNKNOWN) {
                loContext.startActivity(new Intent(loContext, AlipayCheckPrompt.class));
            }
            if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_OK) {
                loContext.startActivity(new Intent(loContext, PaymentSuccess.class));

            } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_ERROR ||
                    Integer.parseInt(result) == Constants.ReturnValues.RETURN_REVERSAL_FAILED ||
                    Integer.parseInt(result) == Constants.ReturnValues.RETURN_REVERSAL_FAILED) {
                loContext.startActivity(new Intent(loContext, PaymentFailure.class));
            } else if (Integer.parseInt(result) == Constants.ReturnValues.RETURN_SEND_RECV_FAILED) {

                TransactionDetails.responseMessge ="SEND RECV FAILED";
                loContext.startActivity(new Intent(loContext, PaymentFailure.class));
            }else
            {
                if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_REVERSAL_FAILED)
                    TransactionDetails.responseMessge ="REVERSAL FAILED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_SEND_RECV_FAILED)
                    TransactionDetails.responseMessge ="TRANSMISSION ERROR";
                else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_CONNECTION_ERROR)
                    TransactionDetails.responseMessge ="CONNECTION FAILED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_SETTLEMENT_NEEDED)
                    TransactionDetails.responseMessge ="SETTLEMENT NEEDED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.RETURN_UPLOAD_FAILED)
                    TransactionDetails.responseMessge ="TRANSACTIOn UPDATE FAILED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.NO_TRANSCATION)
                    TransactionDetails.responseMessge ="NO TRANSACTION TO UPDATE";
                else if(Integer.parseInt(result) == Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED)
                    TransactionDetails.responseMessge ="TRANSACTION NOT SUPPORTED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED)
                    TransactionDetails.responseMessge ="TRANSACTION NOT SUPPORTED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.HOST_NOT_SUPPORTED)
                    TransactionDetails.responseMessge ="HOST NOT SUPPORTED";
                else if(Integer.parseInt(result) == Constants.ReturnValues.SAM_INIT_ERROR)
                    TransactionDetails.responseMessge ="SAM INIT ERROR";
                else if(Integer.parseInt(result) == Constants.ReturnValues.EZLINK_SAM_ERROR)
                    TransactionDetails.responseMessge ="EZLINK SAM ERROR APP";
                loContext.startActivity(new Intent(loContext, PaymentFailure.class));
            }
        }
        return Constants.ReturnValues.RETURN_OK;
    }
}
