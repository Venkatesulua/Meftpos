package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class HostTransmissionModel implements Serializable {

    String HTT_ID,TRANSMISSION_MODE,CONNECTION_TIMEOUT,REDIAL_TIMEOUT,PABX,MODEM_STRING;

    public String getHTT_ID() {
        return HTT_ID;
    }

    public void setHTT_ID(String HTT_ID) {
        this.HTT_ID = HTT_ID;
    }

    public String getTRANSMISSION_MODE() {
        return TRANSMISSION_MODE;
    }

    public void setTRANSMISSION_MODE(String TRANSMISSION_MODE) {
        this.TRANSMISSION_MODE = TRANSMISSION_MODE;
    }

    public String getCONNECTION_TIMEOUT() {
        return CONNECTION_TIMEOUT;
    }

    public void setCONNECTION_TIMEOUT(String CONNECTION_TIMEOUT) {
        this.CONNECTION_TIMEOUT = CONNECTION_TIMEOUT;
    }

    public String getREDIAL_TIMEOUT() {
        return REDIAL_TIMEOUT;
    }

    public void setREDIAL_TIMEOUT(String REDIAL_TIMEOUT) {
        this.REDIAL_TIMEOUT = REDIAL_TIMEOUT;
    }

    public String getPABX() {
        return PABX;
    }

    public void setPABX(String REDIAL_TIMEOUT) {
        this.PABX = PABX;
    }

    public String getMODEM_STRING() {
        return MODEM_STRING;
    }

    public void setMODEM_STRING(String MODEM_STRING) {
        this.MODEM_STRING = MODEM_STRING;
    }
}
