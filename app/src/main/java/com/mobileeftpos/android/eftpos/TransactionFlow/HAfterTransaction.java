package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;

/**
 * Created by venkat on 8/2/2017.
 */

public class HAfterTransaction extends GPrintReceipt {
    private Activity locontext;
    public HAfterTransaction(Activity context){
        super(context);
        locontext=context;
    }
}
