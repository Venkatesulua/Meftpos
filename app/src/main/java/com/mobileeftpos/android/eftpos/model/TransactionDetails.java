package com.mobileeftpos.android.eftpos.model;

/**
 * Created by venkat on 5/23/2017.
 */
//20170523: Venkat added transaction details table
public class TransactionDetails {
    private String trxAmount;
    private String PAN;


    public void settrxAmount(String trxAmount) {this.trxAmount = trxAmount;}
    public String gettrxAmount() {return trxAmount;}

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }
    public String getPAN() {return PAN;}

}
