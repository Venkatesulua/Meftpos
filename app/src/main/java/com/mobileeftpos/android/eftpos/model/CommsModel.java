package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class CommsModel implements Serializable {

    String COMMOS_ID,COM_DESCRIPTION,COM_PRIMARY_TYPE,COM_SECONDARY_TYPE,
            COM_MODEM_PRIMARY_NUMBER,COM_MODEM_SECONDARY_NUMBER,
            COM_MODEM_STRING,
            COM_MODEM_DISABLE_LINE_DETECT,COM_MODEM_TIMEOUT,
            COM_PRIMARY_IP_PORT,COM_SECONDARY_IP_PORT,
            COM_IP_TIMEOUT,COM_CONNECT_SECONDARY,COM_SSL_INDEX,
            COM_MODEM_INDEX,COM_PPP_USER_ID,COM_PPP_PASSWORD,
            COM_PPP_MODEM_STRING,COM_PPP_TIMEOUT;

    public String getCOMMOS_ID() {
        return COMMOS_ID;
    }

    public void setCOMMOS_ID(String COMMOS_ID) {
        this.COMMOS_ID = COMMOS_ID;
    }

    public String getCOM_DESCRIPTION() {
        return COM_DESCRIPTION;
    }

    public void setCOM_DESCRIPTION(String COM_DESCRIPTION) {
        this.COM_DESCRIPTION = COM_DESCRIPTION;
    }

    public String getCOM_PRIMARY_TYPE() {
        return COM_PRIMARY_TYPE;
    }

    public void setCOM_PRIMARY_TYPE(String COM_PRIMARY_TYPE) {
        this.COM_PRIMARY_TYPE = COM_PRIMARY_TYPE;
    }

    public String getCOM_SECONDARY_TYPE() {
        return COM_SECONDARY_TYPE;
    }

    public void setCOM_SECONDARY_TYPE(String COM_SECONDARY_TYPE) {
        this.COM_SECONDARY_TYPE = COM_SECONDARY_TYPE;
    }

    public String getCOM_MODEM_PRIMARY_NUMBER() {
        return COM_MODEM_PRIMARY_NUMBER;
    }

    public void setCOM_MODEM_PRIMARY_NUMBER(String COM_MODEM_PRIMARY_NUMBER) {
        this.COM_MODEM_PRIMARY_NUMBER = COM_MODEM_PRIMARY_NUMBER;
    }

    public String getCOM_MODEM_SECONDARY_NUMBER() {
        return COM_MODEM_SECONDARY_NUMBER;
    }

    public void setCOM_MODEM_SECONDARY_NUMBER(String COM_MODEM_SECONDARY_NUMBER) {
        this.COM_MODEM_SECONDARY_NUMBER = COM_MODEM_SECONDARY_NUMBER;
    }

    public String getCOM_MODEM_STRING() {
        return COM_MODEM_STRING;
    }

    public void setCOM_MODEM_STRING(String COM_MODEM_STRING) {
        this.COM_MODEM_STRING = COM_MODEM_STRING;
    }

    public String getCOM_MODEM_DISABLE_LINE_DETECT() {
        return COM_MODEM_DISABLE_LINE_DETECT;
    }

    public void setCOM_MODEM_DISABLE_LINE_DETECT(String COM_MODEM_DISABLE_LINE_DETECT) {
        this.COM_MODEM_DISABLE_LINE_DETECT = COM_MODEM_DISABLE_LINE_DETECT;
    }

    public String getCOM_MODEM_TIMEOUT() {
        return COM_MODEM_TIMEOUT;
    }

    public void setCOM_MODEM_TIMEOUT(String COM_MODEM_TIMEOUT) {
        this.COM_MODEM_TIMEOUT = COM_MODEM_TIMEOUT;
    }

    public String getCOM_PRIMARY_IP_PORT() {
        return COM_PRIMARY_IP_PORT;
    }

    public void setCOM_PRIMARY_IP_PORT(String COM_PRIMARY_IP_PORT) {
        this.COM_PRIMARY_IP_PORT = COM_PRIMARY_IP_PORT;
    }

    public String getCOM_SECONDARY_IP_PORT() {
        return COM_SECONDARY_IP_PORT;
    }

    public void setCOM_SECONDARY_IP_PORT(String COM_SECONDARY_IP_PORT) {
        this.COM_SECONDARY_IP_PORT = COM_SECONDARY_IP_PORT;
    }

    public String getCOM_IP_TIMEOUT() {
        return COM_IP_TIMEOUT;
    }

    public void setCOM_IP_TIMEOUT(String COM_IP_TIMEOUT) {
        this.COM_IP_TIMEOUT = COM_IP_TIMEOUT;
    }

    public String getCOM_CONNECT_SECONDARY() {
        return COM_CONNECT_SECONDARY;
    }

    public void setCOM_CONNECT_SECONDARY(String COM_CONNECT_SECONDARY) {
        this.COM_CONNECT_SECONDARY = COM_CONNECT_SECONDARY;
    }

    public String getCOM_SSL_INDEX() {
        return COM_SSL_INDEX;
    }

    public void setCOM_SSL_INDEX(String COM_SSL_INDEX) {
        this.COM_SSL_INDEX = COM_SSL_INDEX;
    }

    public String getCOM_MODEM_INDEX() {
        return COM_MODEM_INDEX;
    }

    public void setCOM_MODEM_INDEX(String COM_MODEM_INDEX) {
        this.COM_MODEM_INDEX = COM_MODEM_INDEX;
    }

    public String getCOM_PPP_USER_ID() {
        return COM_PPP_USER_ID;
    }

    public void setCOM_PPP_USER_ID(String COM_PPP_USER_ID) {
        this.COM_PPP_USER_ID = COM_PPP_USER_ID;
    }

    public String getCOM_PPP_PASSWORD() {
        return COM_PPP_PASSWORD;
    }

    public void setCOM_PPP_PASSWORD(String COM_PPP_PASSWORD) {
        this.COM_PPP_PASSWORD = COM_PPP_PASSWORD;
    }

    public String getCOM_PPP_MODEM_STRING() {
        return COM_PPP_MODEM_STRING;
    }

    public void setCOM_PPP_MODEM_STRING(String COM_PPP_MODEM_STRING) {
        this.COM_PPP_MODEM_STRING = COM_PPP_MODEM_STRING;
    }

    public String getCOM_PPP_TIMEOUT() {
        return COM_PPP_TIMEOUT;
    }

    public void setCOM_PPP_TIMEOUT(String COM_PPP_TIMEOUT) {
        this.COM_PPP_TIMEOUT = COM_PPP_TIMEOUT;
    }
}
