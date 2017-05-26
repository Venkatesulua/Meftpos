package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class LimitModel implements Serializable{

     String LIMIT_ID,MAXIMUM_SALE_AMOUNT,MAXIMUM_OFFLINE_AMOUNT,MAXIMUM_PREAUTH_AMOUNT,MAXIMUM_REFUND_AMOUNT;

    public String getLIMIT_ID() {
        return LIMIT_ID;
    }

    public void setLIMIT_ID(String LIMIT_ID) {
        this.LIMIT_ID = LIMIT_ID;
    }

    public String getMAXIMUM_SALE_AMOUNT() {
        return MAXIMUM_SALE_AMOUNT;
    }

    public void setMAXIMUM_SALE_AMOUNT(String MAXIMUM_SALE_AMOUNT) {
        this.MAXIMUM_SALE_AMOUNT = MAXIMUM_SALE_AMOUNT;
    }

    public String getMAXIMUM_OFFLINE_AMOUNT() {
        return MAXIMUM_OFFLINE_AMOUNT;
    }

    public void setMAXIMUM_OFFLINE_AMOUNT(String MAXIMUM_OFFLINE_AMOUNT) {
        this.MAXIMUM_OFFLINE_AMOUNT = MAXIMUM_OFFLINE_AMOUNT;
    }

    public String getMAXIMUM_PREAUTH_AMOUNT() {
        return MAXIMUM_PREAUTH_AMOUNT;
    }

    public void setMAXIMUM_PREAUTH_AMOUNT(String MAXIMUM_PREAUTH_AMOUNT) {
        this.MAXIMUM_PREAUTH_AMOUNT = MAXIMUM_PREAUTH_AMOUNT;
    }

    public String getMAXIMUM_REFUND_AMOUNT() {
        return MAXIMUM_REFUND_AMOUNT;
    }

    public void setMAXIMUM_REFUND_AMOUNT(String MAXIMUM_REFUND_AMOUNT) {
        this.MAXIMUM_REFUND_AMOUNT = MAXIMUM_REFUND_AMOUNT;
    }
}
