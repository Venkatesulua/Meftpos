package com.mobileeftpos.android.eftpos.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "TRANSACTION_CONTROL_MODEL".
 */
@Entity
public class TransactionControlModel {

    @Id(autoincrement = true)
    private Long id;
    private String TCT_ID;
    private String VOID_CTRL;
    private String SETTLEMENT_CTRL;
    private String SALE_CTRL;
    private String AUTH_CTRL;
    private String REFUND_CTRL;
    private String ADJUSTMENT_CTRL;
    private String OFFLINE_CTRL;
    private String MANUAL_ENTRY_CTRL;
    private String BALANCE_CTRL;
    private String CASH_ADVANCE_CTRL;
    private String PURCHASE_TIP_REQUEST_CTRL;
    private String TIP_CTRL;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public TransactionControlModel() {
    }

    public TransactionControlModel(Long id) {
        this.id = id;
    }

    @Generated
    public TransactionControlModel(Long id, String TCT_ID, String VOID_CTRL, String SETTLEMENT_CTRL, String SALE_CTRL, String AUTH_CTRL, String REFUND_CTRL, String ADJUSTMENT_CTRL, String OFFLINE_CTRL, String MANUAL_ENTRY_CTRL, String BALANCE_CTRL, String CASH_ADVANCE_CTRL, String PURCHASE_TIP_REQUEST_CTRL, String TIP_CTRL) {
        this.id = id;
        this.TCT_ID = TCT_ID;
        this.VOID_CTRL = VOID_CTRL;
        this.SETTLEMENT_CTRL = SETTLEMENT_CTRL;
        this.SALE_CTRL = SALE_CTRL;
        this.AUTH_CTRL = AUTH_CTRL;
        this.REFUND_CTRL = REFUND_CTRL;
        this.ADJUSTMENT_CTRL = ADJUSTMENT_CTRL;
        this.OFFLINE_CTRL = OFFLINE_CTRL;
        this.MANUAL_ENTRY_CTRL = MANUAL_ENTRY_CTRL;
        this.BALANCE_CTRL = BALANCE_CTRL;
        this.CASH_ADVANCE_CTRL = CASH_ADVANCE_CTRL;
        this.PURCHASE_TIP_REQUEST_CTRL = PURCHASE_TIP_REQUEST_CTRL;
        this.TIP_CTRL = TIP_CTRL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}