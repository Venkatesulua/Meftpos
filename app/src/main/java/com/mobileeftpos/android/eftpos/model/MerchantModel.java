package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class MerchantModel implements Serializable {


    String MERCHANT_ID,MERCHANT_NAME,MERCHANT_HEADER1,MERCHANT_HEADER2,ADDRESS_LINE1,
            ADDRESS_LINE2,ADDRESS_LINE3,ADDRESS_LINE4,MERCHANT_FOOTER1,MERCHANT_FOOTER2;


    public String getMERCHANT_ID() {
        return MERCHANT_ID;
    }

    public void setMERCHANT_ID(String MERCHANT_ID) {
        this.MERCHANT_ID = MERCHANT_ID;
    }

    public String getMERCHANT_NAME() {
        return MERCHANT_NAME;
    }

    public void setMERCHANT_NAME(String MERCHANT_NAME) {
        this.MERCHANT_NAME = MERCHANT_NAME;
    }

    public String getMERCHANT_HEADER1() {
        return MERCHANT_HEADER1;
    }

    public void setMERCHANT_HEADER1(String MERCHANT_HEADER1) {
        this.MERCHANT_HEADER1 = MERCHANT_HEADER1;
    }

    public String getMERCHANT_HEADER2() {
        return MERCHANT_HEADER2;
    }

    public void setMERCHANT_HEADER2(String MERCHANT_HEADER2) {
        this.MERCHANT_HEADER2 = MERCHANT_HEADER2;
    }

    public String getADDRESS_LINE1() {
        return ADDRESS_LINE1;
    }

    public void setADDRESS_LINE1(String ADDRESS_LINE1) {
        this.ADDRESS_LINE1 = ADDRESS_LINE1;
    }

    public String getADDRESS_LINE2() {
        return ADDRESS_LINE2;
    }

    public void setADDRESS_LINE2(String ADDRESS_LINE2) {
        this.ADDRESS_LINE2 = ADDRESS_LINE2;
    }

    public String getADDRESS_LINE3() {
        return ADDRESS_LINE3;
    }

    public void setADDRESS_LINE3(String ADDRESS_LINE3) {
        this.ADDRESS_LINE3 = ADDRESS_LINE3;
    }

    public String getADDRESS_LINE4() {
        return ADDRESS_LINE4;
    }

    public void setADDRESS_LINE4(String ADDRESS_LINE4) {
        this.ADDRESS_LINE4 = ADDRESS_LINE4;
    }

    public String getMERCHANT_FOOTER1() {
        return MERCHANT_FOOTER1;
    }

    public void setMERCHANT_FOOTER1(String MERCHANT_FOOTER1) {
        this.MERCHANT_FOOTER1 = MERCHANT_FOOTER1;
    }

    public String getMERCHANT_FOOTER2() {
        return MERCHANT_FOOTER2;
    }

    public void setMERCHANT_FOOTER2(String MERCHANT_FOOTER2) {
        this.MERCHANT_FOOTER2 = MERCHANT_FOOTER2;
    }
}
