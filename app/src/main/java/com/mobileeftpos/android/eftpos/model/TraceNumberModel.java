package com.mobileeftpos.android.eftpos.model;

/**
 * Created by venkat on 6/9/2017.
 */
public class TraceNumberModel {
    String TRACE_UNIQUE_ID,SYSTEM_TRACE;
    public String getSYSTEM_TRACE() {
        return SYSTEM_TRACE;
    }

    public void setSYSTEM_TRACE(String SYSTEM_TRACE) {
        this.SYSTEM_TRACE = SYSTEM_TRACE;
    }
    public String getTRACE_UNIQUE_ID() {
        return TRACE_UNIQUE_ID;
    }

    public void setTRACE_UNIQUE_ID(String TRACE_UNIQUE_ID) {
        this.TRACE_UNIQUE_ID = TRACE_UNIQUE_ID;
    }
}
