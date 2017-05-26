package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class CardBinModel implements Serializable {


    String CDT_ID,CDT_LO_RANGE,
            CDT_HI_RANGE,
            CDT_HDT_REFERENCE,
            CDT_CARD_TYPE_ARRAY,
            CDT_CARD_NAME;

    public String getCDT_ID() {
        return CDT_ID;
    }

    public void setCDT_ID(String CDT_ID) {
        this.CDT_ID = CDT_ID;
    }

    public String getCDT_LO_RANGE() {
        return CDT_LO_RANGE;
    }

    public void setCDT_LO_RANGE(String CDT_LO_RANGE) {
        this.CDT_LO_RANGE = CDT_LO_RANGE;
    }

    public String getCDT_HI_RANGE() {
        return CDT_HI_RANGE;
    }

    public void setCDT_HI_RANGE(String CDT_HI_RANGE) {
        this.CDT_HI_RANGE = CDT_HI_RANGE;
    }

    public String getCDT_HDT_REFERENCE() {
        return CDT_HDT_REFERENCE;
    }

    public void setCDT_HDT_REFERENCE(String CDT_HDT_REFERENCE) {
        this.CDT_HDT_REFERENCE = CDT_HDT_REFERENCE;
    }

    public String getCDT_CARD_TYPE_ARRAY() {
        return CDT_CARD_TYPE_ARRAY;
    }

    public void setCDT_CARD_TYPE_ARRAY(String CDT_CARD_TYPE_ARRAY) {
        this.CDT_CARD_TYPE_ARRAY = CDT_CARD_TYPE_ARRAY;
    }

    public String getCDT_CARD_NAME() {
        return CDT_CARD_NAME;
    }

    public void setCDT_CARD_NAME(String CDT_CARD_NAME) {
        this.CDT_CARD_NAME = CDT_CARD_NAME;
    }
}
