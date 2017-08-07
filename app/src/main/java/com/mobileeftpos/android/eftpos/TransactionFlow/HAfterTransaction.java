package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;

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
}
