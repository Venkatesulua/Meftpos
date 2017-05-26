package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class EzlinkModel implements Serializable {


    String Ezlink_ID,EZLINK_ENABLE,EZLINK_SAM_KEY,EZLINK_PAYMENT_TRP,
            EZLINK_TOPUP_TRP,EZLINK_PAYMENT_DEVICE_TYPE,
            EZLINK_TOPUP_DEVICE_TYPE,
            EZLINK_BLACK_LIST_LAST_UPDATE,
            EZLINK_TOPUP_PAYMENT_MODE;

    public String getEzlink_ID() {
        return Ezlink_ID;
    }

    public void setEzlink_ID(String ezlink_ID) {
        Ezlink_ID = ezlink_ID;
    }

    public String getEZLINK_ENABLE() {
        return EZLINK_ENABLE;
    }

    public void setEZLINK_ENABLE(String EZLINK_ENABLE) {
        this.EZLINK_ENABLE = EZLINK_ENABLE;
    }

    public String getEZLINK_SAM_KEY() {
        return EZLINK_SAM_KEY;
    }

    public void setEZLINK_SAM_KEY(String EZLINK_SAM_KEY) {
        this.EZLINK_SAM_KEY = EZLINK_SAM_KEY;
    }

    public String getEZLINK_PAYMENT_TRP() {
        return EZLINK_PAYMENT_TRP;
    }

    public void setEZLINK_PAYMENT_TRP(String EZLINK_PAYMENT_TRP) {
        this.EZLINK_PAYMENT_TRP = EZLINK_PAYMENT_TRP;
    }

    public String getEZLINK_TOPUP_TRP() {
        return EZLINK_TOPUP_TRP;
    }

    public void setEZLINK_TOPUP_TRP(String EZLINK_TOPUP_TRP) {
        this.EZLINK_TOPUP_TRP = EZLINK_TOPUP_TRP;
    }

    public String getEZLINK_PAYMENT_DEVICE_TYPE() {
        return EZLINK_PAYMENT_DEVICE_TYPE;
    }

    public void setEZLINK_PAYMENT_DEVICE_TYPE(String EZLINK_PAYMENT_DEVICE_TYPE) {
        this.EZLINK_PAYMENT_DEVICE_TYPE = EZLINK_PAYMENT_DEVICE_TYPE;
    }

    public String getEZLINK_TOPUP_DEVICE_TYPE() {
        return EZLINK_TOPUP_DEVICE_TYPE;
    }

    public void setEZLINK_TOPUP_DEVICE_TYPE(String EZLINK_TOPUP_DEVICE_TYPE) {
        this.EZLINK_TOPUP_DEVICE_TYPE = EZLINK_TOPUP_DEVICE_TYPE;
    }

    public String getEZLINK_BLACK_LIST_LAST_UPDATE() {
        return EZLINK_BLACK_LIST_LAST_UPDATE;
    }

    public void setEZLINK_BLACK_LIST_LAST_UPDATE(String EZLINK_BLACK_LIST_LAST_UPDATE) {
        this.EZLINK_BLACK_LIST_LAST_UPDATE = EZLINK_BLACK_LIST_LAST_UPDATE;
    }

    public String getEZLINK_TOPUP_PAYMENT_MODE() {
        return EZLINK_TOPUP_PAYMENT_MODE;
    }

    public void setEZLINK_TOPUP_PAYMENT_MODE(String EZLINK_TOPUP_PAYMENT_MODE) {
        this.EZLINK_TOPUP_PAYMENT_MODE = EZLINK_TOPUP_PAYMENT_MODE;
    }
}
