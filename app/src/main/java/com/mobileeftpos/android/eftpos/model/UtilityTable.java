package com.mobileeftpos.android.eftpos.model;

import java.io.Serializable;

/**
 * Created by Prathap on 5/21/17.
 */

public class UtilityTable implements Serializable {

    String UTILITY_ID,ADDITIONAL_PROMPT,DAILY_SETTLEMENT_FLAG,
            LAST_4_DIGIT_PROMPT_FLAG,INSERT_2_SWIPE,PIGGYBACK_FLAG,PINBYPASS,
            AUTO_SETTLE_TIME,LAST_AUTO_SETTLEMENT_DATETIME,UTRN_PREFIX,
            DEFAULT_APPROVAL_CODE;

    public String getUTILITY_ID() {
        return UTILITY_ID;
    }

    public void setUTILITY_ID(String UTILITY_ID) {
        this.UTILITY_ID = UTILITY_ID;
    }

    public String getADDITIONAL_PROMPT() {
        return ADDITIONAL_PROMPT;
    }

    public void setADDITIONAL_PROMPT(String ADDITIONAL_PROMPT) {
        this.ADDITIONAL_PROMPT = ADDITIONAL_PROMPT;
    }

    public String getDAILY_SETTLEMENT_FLAG() {
        return DAILY_SETTLEMENT_FLAG;
    }

    public void setDAILY_SETTLEMENT_FLAG(String DAILY_SETTLEMENT_FLAG) {
        this.DAILY_SETTLEMENT_FLAG = DAILY_SETTLEMENT_FLAG;
    }

    public String getLAST_4_DIGIT_PROMPT_FLAG() {
        return LAST_4_DIGIT_PROMPT_FLAG;
    }

    public void setLAST_4_DIGIT_PROMPT_FLAG(String LAST_4_DIGIT_PROMPT_FLAG) {
        this.LAST_4_DIGIT_PROMPT_FLAG = LAST_4_DIGIT_PROMPT_FLAG;
    }

    public String getINSERT_2_SWIPE() {
        return INSERT_2_SWIPE;
    }

    public void setINSERT_2_SWIPE(String INSERT_2_SWIPE) {
        this.INSERT_2_SWIPE = INSERT_2_SWIPE;
    }

    public String getPIGGYBACK_FLAG() {
        return PIGGYBACK_FLAG;
    }

    public void setPIGGYBACK_FLAG(String PIGGYBACK_FLAG) {
        this.PIGGYBACK_FLAG = PIGGYBACK_FLAG;
    }

    public String getPINBYPASS() {
        return PINBYPASS;
    }

    public void setPINBYPASS(String PINBYPASS) {
        this.PINBYPASS = PINBYPASS;
    }

    public String getAUTO_SETTLE_TIME() {
        return AUTO_SETTLE_TIME;
    }

    public void setAUTO_SETTLE_TIME(String AUTO_SETTLE_TIME) {
        this.AUTO_SETTLE_TIME = AUTO_SETTLE_TIME;
    }

    public String getLAST_AUTO_SETTLEMENT_DATETIME() {
        return LAST_AUTO_SETTLEMENT_DATETIME;
    }

    public void setLAST_AUTO_SETTLEMENT_DATETIME(String LAST_AUTO_SETTLEMENT_DATETIME) {
        this.LAST_AUTO_SETTLEMENT_DATETIME = LAST_AUTO_SETTLEMENT_DATETIME;
    }

    public String getUTRN_PREFIX() {
        return UTRN_PREFIX;
    }

    public void setUTRN_PREFIX(String UTRN_PREFIX) {
        this.UTRN_PREFIX = UTRN_PREFIX;
    }



    public String getDEFAULT_APPROVAL_CODE() {
        return DEFAULT_APPROVAL_CODE;
    }

    public void setDEFAULT_APPROVAL_CODE(String DEFAULT_APPROVAL_CODE) {
        this.DEFAULT_APPROVAL_CODE = DEFAULT_APPROVAL_CODE;
    }
}
