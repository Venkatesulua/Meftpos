package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class MaskingModel implements Serializable {


    String MASKING_ID,DR_PAN_UNMASK,DR_EXP_UNMASK,DISPLAY_UNMASK;

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
}
