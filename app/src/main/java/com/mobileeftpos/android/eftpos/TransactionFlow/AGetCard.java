package com.mobileeftpos.android.eftpos.TransactionFlow;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;

/**
 * Created by venkat on 8/2/2017.
 */

public class AGetCard extends ParamLoading{

    protected final String TAG = "my_custom_msg";
    public int readCard(){
        return Constants.ReturnValues.RETURN_OK;
    }

}
