package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class CurrencyModel implements Serializable{

    String CURRENCY_ID,CURR_LABEL,CURR_EXPONENT,CURR_CODE;

    public String getCURRENCY_ID() {
        return CURRENCY_ID;
    }

    public void setCURRENCY_ID(String CURRENCY_ID) {
        this.CURRENCY_ID = CURRENCY_ID;
    }

    public String getCURR_LABEL() {
        return CURR_LABEL;
    }

    public void setCURR_LABEL(String CURR_LABEL) {
        this.CURR_LABEL = CURR_LABEL;
    }

    public String getCURR_EXPONENT() {
        return CURR_EXPONENT;
    }

    public void setCURR_EXPONENT(String CURR_EXPONENT) {
        this.CURR_EXPONENT = CURR_EXPONENT;
    }

    public String getCURR_CODE() {
        return CURR_CODE;
    }

    public void setCURR_CODE(String CURR_CODE) {
        this.CURR_CODE = CURR_CODE;
    }
}
