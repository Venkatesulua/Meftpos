package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class TransactionControlModel implements Serializable {


    String TCT_ID,VOID_CTRL,SETTLEMENT_CTRL,SALE_CTRL,
            AUTH_CTRL,REFUND_CTRL,ADJUSTMENT_CTRL,OFFLINE_CTRL,
            MANUAL_ENTRY_CTRL,BALANCE_CTRL,CASH_ADVANCE_CTRL,
            PURCHASE_TIP_REQUEST_CTRL,TIP_CTRL;

    public String getTCT_ID() {
        return TCT_ID;
    }

    public void setTCT_ID(String TCT_ID) {
        this.TCT_ID = TCT_ID;
    }

    public String getVOID_CTRL() {
        return VOID_CTRL;
    }

    public void setVOID_CTRL(String VOID_CTRL) {
        this.VOID_CTRL = VOID_CTRL;
    }

    public String getSETTLEMENT_CTRL() {
        return SETTLEMENT_CTRL;
    }

    public void setSETTLEMENT_CTRL(String SETTLEMENT_CTRL) {
        this.SETTLEMENT_CTRL = SETTLEMENT_CTRL;
    }

    public String getSALE_CTRL() {
        return SALE_CTRL;
    }

    public void setSALE_CTRL(String SALE_CTRL) {
        this.SALE_CTRL = SALE_CTRL;
    }

    public String getAUTH_CTRL() {
        return AUTH_CTRL;
    }

    public void setAUTH_CTRL(String AUTH_CTRL) {
        this.AUTH_CTRL = AUTH_CTRL;
    }

    public String getREFUND_CTRL() {
        return REFUND_CTRL;
    }

    public void setREFUND_CTRL(String REFUND_CTRL) {
        this.REFUND_CTRL = REFUND_CTRL;
    }

    public String getADJUSTMENT_CTRL() {
        return ADJUSTMENT_CTRL;
    }

    public void setADJUSTMENT_CTRL(String ADJUSTMENT_CTRL) {
        this.ADJUSTMENT_CTRL = ADJUSTMENT_CTRL;
    }

    public String getOFFLINE_CTRL() {
        return OFFLINE_CTRL;
    }

    public void setOFFLINE_CTRL(String OFFLINE_CTRL) {
        this.OFFLINE_CTRL = OFFLINE_CTRL;
    }

    public String getMANUAL_ENTRY_CTRL() {
        return MANUAL_ENTRY_CTRL;
    }

    public void setMANUAL_ENTRY_CTRL(String MANUAL_ENTRY_CTRL) {
        this.MANUAL_ENTRY_CTRL = MANUAL_ENTRY_CTRL;
    }

    public String getBALANCE_CTRL() {
        return BALANCE_CTRL;
    }

    public void setBALANCE_CTRL(String BALANCE_CTRL) {
        this.BALANCE_CTRL = BALANCE_CTRL;
    }

    public String getCASH_ADVANCE_CTRL() {
        return CASH_ADVANCE_CTRL;
    }

    public void setCASH_ADVANCE_CTRL(String CASH_ADVANCE_CTRL) {
        this.CASH_ADVANCE_CTRL = CASH_ADVANCE_CTRL;
    }

    public String getPURCHASE_TIP_REQUEST_CTRL() {
        return PURCHASE_TIP_REQUEST_CTRL;
    }

    public void setPURCHASE_TIP_REQUEST_CTRL(String PURCHASE_TIP_REQUEST_CTRL) {
        this.PURCHASE_TIP_REQUEST_CTRL = PURCHASE_TIP_REQUEST_CTRL;
    }

    public String getTIP_CTRL() {
        return TIP_CTRL;
    }

    public void setTIP_CTRL(String TIP_CTRL) {
        this.TIP_CTRL = TIP_CTRL;
    }
}
