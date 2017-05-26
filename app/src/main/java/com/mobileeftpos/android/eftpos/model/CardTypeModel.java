package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class CardTypeModel implements Serializable {

    String CTT_CARD_TYPE,
            CTT_CARD_LABEL,
            CTT_CARD_FORMAT,
            CTT_MASK_FORMAT,
            CTT_MAGSTRIPE_FLOOR_LIMIT,
            CTT_DISABLE_LUHN,
            CTT_CUSTOM_OPTIONS,
            CTT_CVV_FDBC_ENABLE,
            CTT_PAN_MASK_ARRAY,
            CTT_EXPIRY_MASK_ARRAY,
            CTT_QPSL,
            CTT_DISABLE_EXPIRY_CHECK,
            CTT_MC501, CTT_ID;

    public String getCTT_CARD_TYPE() {
        return CTT_CARD_TYPE;
    }

    public void setCTT_CARD_TYPE(String CTT_CARD_TYPE) {
        this.CTT_CARD_TYPE = CTT_CARD_TYPE;
    }

    public String getCTT_CARD_LABEL() {
        return CTT_CARD_LABEL;
    }

    public void setCTT_CARD_LABEL(String CTT_CARD_LABEL) {
        this.CTT_CARD_LABEL = CTT_CARD_LABEL;
    }

    public String getCTT_CARD_FORMAT() {
        return CTT_CARD_FORMAT;
    }

    public void setCTT_CARD_FORMAT(String CTT_CARD_FORMAT) {
        this.CTT_CARD_FORMAT = CTT_CARD_FORMAT;
    }

    public String getCTT_MASK_FORMAT() {
        return CTT_MASK_FORMAT;
    }

    public void setCTT_MASK_FORMAT(String CTT_MASK_FORMAT) {
        this.CTT_MASK_FORMAT = CTT_MASK_FORMAT;
    }

    public String getCTT_MAGSTRIPE_FLOOR_LIMIT() {
        return CTT_MAGSTRIPE_FLOOR_LIMIT;
    }

    public void setCTT_MAGSTRIPE_FLOOR_LIMIT(String CTT_MAGSTRIPE_FLOOR_LIMIT) {
        this.CTT_MAGSTRIPE_FLOOR_LIMIT = CTT_MAGSTRIPE_FLOOR_LIMIT;
    }

    public String getCTT_DISABLE_LUHN() {
        return CTT_DISABLE_LUHN;
    }

    public void setCTT_DISABLE_LUHN(String CTT_DISABLE_LUHN) {
        this.CTT_DISABLE_LUHN = CTT_DISABLE_LUHN;
    }

    public String getCTT_CUSTOM_OPTIONS() {
        return CTT_CUSTOM_OPTIONS;
    }

    public void setCTT_CUSTOM_OPTIONS(String CTT_CUSTOM_OPTIONS) {
        this.CTT_CUSTOM_OPTIONS = CTT_CUSTOM_OPTIONS;
    }

    public String getCTT_CVV_FDBC_ENABLE() {
        return CTT_CVV_FDBC_ENABLE;
    }

    public void setCTT_CVV_FDBC_ENABLE(String CTT_CVV_FDBC_ENABLE) {
        this.CTT_CVV_FDBC_ENABLE = CTT_CVV_FDBC_ENABLE;
    }

    public String getCTT_PAN_MASK_ARRAY() {
        return CTT_PAN_MASK_ARRAY;
    }

    public void setCTT_PAN_MASK_ARRAY(String CTT_PAN_MASK_ARRAY) {
        this.CTT_PAN_MASK_ARRAY = CTT_PAN_MASK_ARRAY;
    }

    public String getCTT_EXPIRY_MASK_ARRAY() {
        return CTT_EXPIRY_MASK_ARRAY;
    }

    public void setCTT_EXPIRY_MASK_ARRAY(String CTT_EXPIRY_MASK_ARRAY) {
        this.CTT_EXPIRY_MASK_ARRAY = CTT_EXPIRY_MASK_ARRAY;
    }

    public String getCTT_QPSL() {
        return CTT_QPSL;
    }

    public void setCTT_QPSL(String CTT_QPSL) {
        this.CTT_QPSL = CTT_QPSL;
    }

    public String getCTT_DISABLE_EXPIRY_CHECK() {
        return CTT_DISABLE_EXPIRY_CHECK;
    }

    public void setCTT_DISABLE_EXPIRY_CHECK(String CTT_DISABLE_EXPIRY_CHECK) {
        this.CTT_DISABLE_EXPIRY_CHECK = CTT_DISABLE_EXPIRY_CHECK;
    }

    public String getCTT_MC501() {
        return CTT_MC501;
    }

    public void setCTT_MC501(String CTT_MC501) {
        this.CTT_MC501 = CTT_MC501;
    }

    public String getCTT_ID() {
        return CTT_ID;
    }

    public void setCTT_ID(String CTT_ID) {
        this.CTT_ID = CTT_ID;
    }
}
