package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by venkat on 5/31/2017.
 */
public class BarcodeModel implements Serializable {

    String ALIPAY_ID,PARTNER_ID,SELLER_ID,REGION_CODE;

    public String getALIPAY_ID() {
        return ALIPAY_ID;
    }

    public void setALIPAY_ID(String ALIPAY_ID) {
        this.ALIPAY_ID = ALIPAY_ID;
    }

    public String getPARTNER_ID() {
        return PARTNER_ID;
    }

    public void setPARTNER_ID(String PARTNER_ID) {
        this.PARTNER_ID = PARTNER_ID;
    }

    public String getSELLER_ID() {
        return SELLER_ID;
    }

    public void setSELLER_ID(String SELLER_ID) {
        this.SELLER_ID = SELLER_ID;
    }

    public String getREGION_CODE() {
        return REGION_CODE;
    }

    public void setREGION_CODE(String REGION_CODE) {
        this.REGION_CODE = REGION_CODE;
    }

}
