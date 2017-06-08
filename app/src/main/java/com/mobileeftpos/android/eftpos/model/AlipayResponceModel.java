package com.mobileeftpos.android.eftpos.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Prathap on 07-06-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class AlipayResponceModel {


    private String respcode,errmsg,reqid,transid,alipaytransid,transdt,buyerid,cnyamt,convrate;

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getAlipaytransid() {
        return alipaytransid;
    }

    public void setAlipaytransid(String alipaytransid) {
        this.alipaytransid = alipaytransid;
    }

    public String getTransdt() {
        return transdt;
    }

    public void setTransdt(String transdt) {
        this.transdt = transdt;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }

    public String getCnyamt() {
        return cnyamt;
    }

    public void setCnyamt(String cnyamt) {
        this.cnyamt = cnyamt;
    }

    public String getConvrate() {
        return convrate;
    }

    public void setConvrate(String convrate) {
        this.convrate = convrate;
    }
}


