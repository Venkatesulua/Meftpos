package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class ReceiptModel implements Serializable {

    String RECEIPT_ID,PRINT_TIMEOUT,AUTO_PRINT,PRINTER_INTENSITY_CFG,PRINTER_CFG;

    public String getRECEIPT_ID() {
        return RECEIPT_ID;
    }

    public void setRECEIPT_ID(String RECEIPT_ID) {
        this.RECEIPT_ID = RECEIPT_ID;
    }

    public String getPRINT_TIMEOUT() {
        return PRINT_TIMEOUT;
    }

    public void setPRINT_TIMEOUT(String PRINT_TIMEOUT) {
        this.PRINT_TIMEOUT = PRINT_TIMEOUT;
    }

    public String getAUTO_PRINT() {
        return AUTO_PRINT;
    }

    public void setAUTO_PRINT(String AUTO_PRINT) {
        this.AUTO_PRINT = AUTO_PRINT;
    }

    public String getPRINTER_INTENSITY_CFG() {
        return PRINTER_INTENSITY_CFG;
    }

    public void setPRINTER_INTENSITY_CFG(String PRINTER_INTENSITY_CFG) {
        this.PRINTER_INTENSITY_CFG = PRINTER_INTENSITY_CFG;
    }

    public String getPRINTER_CFG() {
        return PRINTER_CFG;
    }

    public void setPRINTER_CFG(String PRINTER_CFG) {
        this.PRINTER_CFG = PRINTER_CFG;
    }
}
