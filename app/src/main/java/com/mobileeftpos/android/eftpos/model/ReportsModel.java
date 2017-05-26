package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class ReportsModel implements Serializable {

    String REPORTTABLE_ID,DETAILED_REPORT,TIP_REPORT,TOTAL_REPORT;

    public String getREPORTTABLE_ID() {
        return REPORTTABLE_ID;
    }

    public void setREPORTTABLE_ID(String REPORTTABLE_ID) {
        this.REPORTTABLE_ID = REPORTTABLE_ID;
    }

    public String getDETAILED_REPORT() {
        return DETAILED_REPORT;
    }

    public void setDETAILED_REPORT(String DETAILED_REPORT) {
        this.DETAILED_REPORT = DETAILED_REPORT;
    }

    public String getTIP_REPORT() {
        return TIP_REPORT;
    }

    public void setTIP_REPORT(String TIP_REPORT) {
        this.TIP_REPORT = TIP_REPORT;
    }

    public String getTOTAL_REPORT() {
        return TOTAL_REPORT;
    }

    public void setTOTAL_REPORT(String TOTAL_REPORT) {
        this.TOTAL_REPORT = TOTAL_REPORT;
    }
}
