package com.mobileeftpos.android.eftpos.model;

/**
 * Created by venkat on 6/8/2017.
 */
public class BatchModel {
    String BATCH_ID,HDT_INDEX,TRANS_TYPE ,TRANS_MODE ,VOIDED ,UPLOADED ,PROC_CODE ,INVOICE_NUMBER ,AMOUNT ,TIP_AMOUNT ,TIME ,DATE ,YEAR ,ORG_MESS_ID ,SYS_TRACE_NUM ,DATE_EXP ,RETR_REF_NUM ,AUTH_ID_RESP ,RESP_CODE ,ACCT_NUMBER ,PERSON_NAME ,ORIGINAL_AMOUNT ,ADDITIONAL_DATA ,PAYMENT_TERM_INFO ,PRIMARY_ACC_NUM ,POS_ENT_MODE ,NII ,POS_COND_CODE ,ADD_AMOUNT ,CARD_TYPE ,CARD_EQUENCE ,CHIPDATA ,TVRVALUE ,TSIVALUE ,TRANSCRYTO ,TOTALSCRIPT71 ,TOTALSCRIPT72 ,SCRIPTRESULT71 ,SCRIPTRESULT72 ,CHAID ,APPLICATION_LABEL ,CLS_SCHEME_ID ,SIGNATURE_REQ;

    public void setBATCH_ID(String BATCH_ID) {this.BATCH_ID = BATCH_ID;}
    public void setHDT_INDEX(String HDT_INDEX) {
        this.HDT_INDEX = HDT_INDEX;
    }
    public void setTRANS_TYPE(String TRANS_TYPE){this.TRANS_TYPE= TRANS_TYPE; }
    public void setTRANS_MODE(String TRANS_MODE){this.TRANS_MODE= TRANS_MODE; }
    public void setVOIDED(String VOIDED){this.VOIDED=VOIDED ; }
    public void setUPLOADED(String UPLOADED){this.UPLOADED=UPLOADED ; }
    public void setPROC_CODE(String PROC_CODE){this.PROC_CODE=PROC_CODE ; }
    public void setINVOICE_NUMBER(String INVOICE_NUMBER){this.INVOICE_NUMBER= INVOICE_NUMBER; }
    public void setAMOUNT(String AMOUNT){this.AMOUNT= AMOUNT; }
    public void setTIP_AMOUNT(String TIP_AMOUNT){this.TIP_AMOUNT= TIP_AMOUNT; }
    public void setTIME(String TIME){this.TIME= TIME; }
    public void setDATE(String DATE){this.DATE= DATE; }
    public void setYEAR(String YEAR){this.YEAR= YEAR; }
    public void setORG_MESS_ID(String ORG_MESS_ID){this.ORG_MESS_ID=ORG_MESS_ID ; }
    public void setSYS_TRACE_NUM(String SYS_TRACE_NUM){this.SYS_TRACE_NUM=SYS_TRACE_NUM ; }
    public void setDATE_EXP(String DATE_EXP){this.DATE_EXP= DATE_EXP; }
    public void setRETR_REF_NUM(String RETR_REF_NUM){this.RETR_REF_NUM=RETR_REF_NUM ; }
    public void setAUTH_ID_RESP(String AUTH_ID_RESP){this.AUTH_ID_RESP= AUTH_ID_RESP; }
    public void setRESP_CODE(String RESP_CODE){this.RESP_CODE=RESP_CODE ; }
    public void setACCT_NUMBER(String ACCT_NUMBER){this.ACCT_NUMBER=ACCT_NUMBER ; }
    public void setPERSON_NAME(String PERSON_NAME){this.PERSON_NAME=PERSON_NAME ; }
    public void setORIGINAL_AMOUNT(String ORIGINAL_AMOUNT){this.ORIGINAL_AMOUNT=ORIGINAL_AMOUNT ; }
    public void setADDITIONAL_DATA(String ADDITIONAL_DATA){this.ADDITIONAL_DATA=ADDITIONAL_DATA ; }
    public void setPAYMENT_TERM_INFO(String PAYMENT_TERM_INFO){this.PAYMENT_TERM_INFO=PAYMENT_TERM_INFO ; }
    public void setPRIMARY_ACC_NUM(String PRIMARY_ACC_NUM){this.PRIMARY_ACC_NUM= PRIMARY_ACC_NUM; }
    public void setPOS_ENT_MODE(String POS_ENT_MODE){this.POS_ENT_MODE= POS_ENT_MODE; }
    public void setNII(String NII){this.NII=NII ; }
    public void setPOS_COND_CODE(String POS_COND_CODE){this.POS_COND_CODE=POS_COND_CODE ; }
    public void setADD_AMOUNT(String ADD_AMOUNT){this.ADD_AMOUNT= ADD_AMOUNT; }
    public void setCARD_TYPE(String CARD_TYPE){this.CARD_TYPE= CARD_TYPE; }
    public void setCARD_EQUENCE(String CARD_EQUENCE){this.CARD_EQUENCE=CARD_EQUENCE ; }
    public void setCHIPDATA(String CHIPDATA){this.CHIPDATA= CHIPDATA; }
    public void setTVRVALUE(String TVRVALUE){this.TVRVALUE=TVRVALUE ; }
    public void setTSIVALUE(String TSIVALUE){this.TSIVALUE= TSIVALUE; }
    public void setTRANSCRYTO(String TRANSCRYTO){this.TRANSCRYTO= TRANSCRYTO; }
    public void setTOTALSCRIPT71(String TOTALSCRIPT71){this.TOTALSCRIPT71=TOTALSCRIPT71 ; }
    public void setTOTALSCRIPT72(String TOTALSCRIPT72){this.TOTALSCRIPT72=TOTALSCRIPT72 ; }
    public void setSCRIPTRESULT71(String SCRIPTRESULT71){this.SCRIPTRESULT71=SCRIPTRESULT71 ; }
    public void setSCRIPTRESULT72(String SCRIPTRESULT72){this.SCRIPTRESULT72=SCRIPTRESULT72 ; }
    public void setCHAID(String CHAID){this.CHAID=CHAID ; }
    public void setAPPLICATION_LABEL(String APPLICATION_LABEL){this.APPLICATION_LABEL= APPLICATION_LABEL; }
    public void setCLS_SCHEME_ID(String CLS_SCHEME_ID){this.CLS_SCHEME_ID= CLS_SCHEME_ID; }
    public void setSIGNATURE_REQ(String SIGNATURE_REQ){this.SIGNATURE_REQ=SIGNATURE_REQ ; }

    public String getBATCH_ID(){return BATCH_ID; }
    public String getHDT_INDEX(){return HDT_INDEX; }
    public String getTRANS_TYPE(){return TRANS_TYPE; }
    public String getTRANS_MODE(){return TRANS_MODE; }
    public String getVOIDED(){return VOIDED; }
    public String getUPLOADED(){return UPLOADED; }
    public String getPROC_CODE(){return PROC_CODE; }
    public String getINVOICE_NUMBER(){return INVOICE_NUMBER; }
    public String getAMOUNT(){return AMOUNT; }
    public String getTIP_AMOUNT(){return TIP_AMOUNT; }
    public String getTIME(){return TIME; }
    public String getDATE(){return DATE; }
    public String getYEAR(){return YEAR; }
    public String getORG_MESS_ID(){return ORG_MESS_ID; }
    public String getSYS_TRACE_NUM(){return SYS_TRACE_NUM; }
    public String getDATE_EXP(){return DATE_EXP; }
    public String getRETR_REF_NUM(){return RETR_REF_NUM; }
    public String getAUTH_ID_RESP(){return AUTH_ID_RESP; }
    public String getRESP_CODE(){return RESP_CODE; }
    public String getACCT_NUMBER(){return ACCT_NUMBER; }
    public String getPERSON_NAME(){return PERSON_NAME; }
    public String getORIGINAL_AMOUNT(){return ORIGINAL_AMOUNT; }
    public String getADDITIONAL_DATA(){return ADDITIONAL_DATA; }
    public String getPAYMENT_TERM_INFO(){return PAYMENT_TERM_INFO; }
    public String getPRIMARY_ACC_NUM(){return PRIMARY_ACC_NUM; }
    public String getPOS_ENT_MODE(){return POS_ENT_MODE; }
    public String getNII(){return NII; }
    public String getPOS_COND_CODE(){return POS_COND_CODE; }
    public String getADD_AMOUNT(){return ADD_AMOUNT; }
    public String getCARD_TYPE(){return CARD_TYPE; }
    public String getCARD_EQUENCE(){return CARD_EQUENCE; }
    public String getCHIPDATA(){return CHIPDATA; }
    public String getTVRVALUE(){return TVRVALUE; }
    public String getTSIVALUE(){return TSIVALUE; }
    public String getTRANSCRYTO(){return TRANSCRYTO; }
    public String getTOTALSCRIPT71(){return TOTALSCRIPT71; }
    public String getTOTALSCRIPT72(){return TOTALSCRIPT72; }
    public String getSCRIPTRESULT71(){return SCRIPTRESULT71; }
    public String getSCRIPTRESULT72(){return SCRIPTRESULT72; }
    public String getCHAID(){return CHAID; }
    public String getAPPLICATION_LABEL(){return APPLICATION_LABEL; }
    public String getCLS_SCHEME_ID(){return CLS_SCHEME_ID; }
    public String getSIGNATURE_REQ(){return SIGNATURE_REQ; }


}
