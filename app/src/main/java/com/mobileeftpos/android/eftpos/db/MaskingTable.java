package com.mobileeftpos.android.eftpos.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "MASKING_TABLE".
 */
@Entity
public class MaskingTable {

    @Id(autoincrement = true)
    private Long id;
    private String MASKING_ID;
    private String DR_PAN_UNMASK;
    private String DR_EXP_UNMASK;
    private String DISPLAY_UNMASK;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public MaskingTable() {
    }

    public MaskingTable(Long id) {
        this.id = id;
    }

    @Generated
    public MaskingTable(Long id, String MASKING_ID, String DR_PAN_UNMASK, String DR_EXP_UNMASK, String DISPLAY_UNMASK) {
        this.id = id;
        this.MASKING_ID = MASKING_ID;
        this.DR_PAN_UNMASK = DR_PAN_UNMASK;
        this.DR_EXP_UNMASK = DR_EXP_UNMASK;
        this.DISPLAY_UNMASK = DISPLAY_UNMASK;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMASKING_ID() {
        return MASKING_ID;
    }

    public void setMASKING_ID(String MASKING_ID) {
        this.MASKING_ID = MASKING_ID;
    }

    public String getDR_PAN_UNMASK() {
        return DR_PAN_UNMASK;
    }

    public void setDR_PAN_UNMASK(String DR_PAN_UNMASK) {
        this.DR_PAN_UNMASK = DR_PAN_UNMASK;
    }

    public String getDR_EXP_UNMASK() {
        return DR_EXP_UNMASK;
    }

    public void setDR_EXP_UNMASK(String DR_EXP_UNMASK) {
        this.DR_EXP_UNMASK = DR_EXP_UNMASK;
    }

    public String getDISPLAY_UNMASK() {
        return DISPLAY_UNMASK;
    }

    public void setDISPLAY_UNMASK(String DISPLAY_UNMASK) {
        this.DISPLAY_UNMASK = DISPLAY_UNMASK;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}