package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class EthernetLabel implements Serializable {

    String ETHERNET_ID,LOCAL_IP,SUBNET_MASK,GATEWAY,DNS1,DNS2;

    public String getETHERNET_ID() {
        return ETHERNET_ID;
    }

    public void setETHERNET_ID(String ETHERNET_ID) {
        this.ETHERNET_ID = ETHERNET_ID;
    }

    public String getLOCAL_IP() {
        return LOCAL_IP;
    }

    public void setLOCAL_IP(String LOCAL_IP) {
        this.LOCAL_IP = LOCAL_IP;
    }

    public String getSUBNET_MASK() {
        return SUBNET_MASK;
    }

    public void setSUBNET_MASK(String SUBNET_MASK) {
        this.SUBNET_MASK = SUBNET_MASK;
    }

    public String getGATEWAY() {
        return GATEWAY;
    }

    public void setGATEWAY(String GATEWAY) {
        this.GATEWAY = GATEWAY;
    }

    public String getDNS1() {
        return DNS1;
    }

    public void setDNS1(String DNS1) {
        this.DNS1 = DNS1;
    }

    public String getDNS2() {
        return DNS2;
    }

    public void setDNS2(String DNS2) {
        this.DNS2 = DNS2;
    }
}
