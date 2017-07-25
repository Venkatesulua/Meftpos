package com.mobileeftpos.android.eftpos.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "CURRENCY_TABLE".
 */
@Entity
public class CurrencyTable {

    @Id(autoincrement = true)
    private Long id;
    private String CURRENCY_ID;
    private String CURR_LABEL;
    private String CURR_EXPONENT;
    private String CURR_CODE;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public CurrencyTable() {
    }

    public CurrencyTable(Long id) {
        this.id = id;
    }

    @Generated
    public CurrencyTable(Long id, String CURRENCY_ID, String CURR_LABEL, String CURR_EXPONENT, String CURR_CODE) {
        this.id = id;
        this.CURRENCY_ID = CURRENCY_ID;
        this.CURR_LABEL = CURR_LABEL;
        this.CURR_EXPONENT = CURR_EXPONENT;
        this.CURR_CODE = CURR_CODE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
