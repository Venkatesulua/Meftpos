package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class PasswordModel implements Serializable {

    String PWD_ID, DEFAULT_PASSWORD, REFUND_PASWORD, TIP_ADJUST_PASSWORD, PRE_AUTH_PASSWORD,
            BALANCE_PASSWORD, OFFLINE_PASSWORD, SETTLEMENT_PASSWORD, EDITOR_PASSWORD,
            VOID_PASSWORD, MANUAL_ENTRY_PASSWORD,
            CASH_ADVANCED_PASSWORD, TERMINAL_POWERON_PASSWORD;

    public String getPWD_ID() {
        return PWD_ID;
    }

    public void setPWD_ID(String PWD_ID) {
        this.PWD_ID = PWD_ID;
    }

    public String getDEFAULT_PASSWORD() {
        return DEFAULT_PASSWORD;
    }

    public void setDEFAULT_PASSWORD(String DEFAULT_PASSWORD) {
        this.DEFAULT_PASSWORD = DEFAULT_PASSWORD;
    }

    public String getREFUND_PASWORD() {
        return REFUND_PASWORD;
    }

    public void setREFUND_PASWORD(String REFUND_PASWORD) {
        this.REFUND_PASWORD = REFUND_PASWORD;
    }

    public String getTIP_ADJUST_PASSWORD() {
        return TIP_ADJUST_PASSWORD;
    }

    public void setTIP_ADJUST_PASSWORD(String TIP_ADJUST_PASSWORD) {
        this.TIP_ADJUST_PASSWORD = TIP_ADJUST_PASSWORD;
    }

    public String getPRE_AUTH_PASSWORD() {
        return PRE_AUTH_PASSWORD;
    }

    public void setPRE_AUTH_PASSWORD(String PRE_AUTH_PASSWORD) {
        this.PRE_AUTH_PASSWORD = PRE_AUTH_PASSWORD;
    }

    public String getBALANCE_PASSWORD() {
        return BALANCE_PASSWORD;
    }

    public void setBALANCE_PASSWORD(String BALANCE_PASSWORD) {
        this.BALANCE_PASSWORD = BALANCE_PASSWORD;
    }

    public String getOFFLINE_PASSWORD() {
        return OFFLINE_PASSWORD;
    }

    public void setOFFLINE_PASSWORD(String OFFLINE_PASSWORD) {
        this.OFFLINE_PASSWORD = OFFLINE_PASSWORD;
    }

    public String getSETTLEMENT_PASSWORD() {
        return SETTLEMENT_PASSWORD;
    }

    public void setSETTLEMENT_PASSWORD(String SETTLEMENT_PASSWORD) {
        this.SETTLEMENT_PASSWORD = SETTLEMENT_PASSWORD;
    }

    public String getEDITOR_PASSWORD() {
        return EDITOR_PASSWORD;
    }

    public void setEDITOR_PASSWORD(String EDITOR_PASSWORD) {
        this.EDITOR_PASSWORD = EDITOR_PASSWORD;
    }

    public String getVOID_PASSWORD() {
        return VOID_PASSWORD;
    }

    public void setVOID_PASSWORD(String VOID_PASSWORD) {
        this.VOID_PASSWORD = VOID_PASSWORD;
    }

    public String getMANUAL_ENTRY_PASSWORD() {
        return MANUAL_ENTRY_PASSWORD;
    }

    public void setMANUAL_ENTRY_PASSWORD(String MANUAL_ENTRY_PASSWORD) {
        this.MANUAL_ENTRY_PASSWORD = MANUAL_ENTRY_PASSWORD;
    }

    public String getCASH_ADVANCED_PASSWORD() {
        return CASH_ADVANCED_PASSWORD;
    }

    public void setCASH_ADVANCED_PASSWORD(String CASH_ADVANCED_PASSWORD) {
        this.CASH_ADVANCED_PASSWORD = CASH_ADVANCED_PASSWORD;
    }

    public String getTERMINAL_POWERON_PASSWORD() {
        return TERMINAL_POWERON_PASSWORD;
    }

    public void setTERMINAL_POWERON_PASSWORD(String TERMINAL_POWERON_PASSWORD) {
        this.TERMINAL_POWERON_PASSWORD = TERMINAL_POWERON_PASSWORD;
    }
}


