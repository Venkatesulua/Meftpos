package com.mobileeftpos.android.eftpos.SupportClasses;

import android.content.Context;
import android.util.Log;

import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.CardBinModel;
import com.mobileeftpos.android.eftpos.model.CardTypeModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

/**
 * Created by venkat on 5/23/2017.
 */
//20170523: Venkat added transaction details table
public class TransactionDetails {
    private final String TAG = "my_custom_msg";
    public static String trxAmount;
    public static String trxDateTime;//yyyyMMddHHmmssSS
    public static String PAN;
    public static int trxType;
    public static String chApprovalCode;
    public static String EntryMode;
    public static int inGHDT;
    public static int inGCDT;
    public static int inGCTT;
    public static int inGCOM;
    public static int inGCURR;
   // public static int inGHDT ;
    int inGNoOfValidHosts;
    //public static String EntryMode;
    //private DBHelper databaseObj;
    //public Context context;

    public int inSortPAN(DBHelper databaseObj)
    {
        int i=0;
        String chCardTypeIndex = "";
       // String ui8GlistOfValidHosts = "";//new char[100];
        String[] ui8GlistOfValidHosts = new String[50];
        //memset(chCardTypeIndex,0,sizeof(chCardTypeIndex));
        //S_FS_FILE *pxFid;

        //memset(Pantemp,0,sizeof(Pantemp));
        //memcpy(Pantemp,keystr,strlen(keystr));


        Log.i(TAG,"inSortPAN:");
        while (true)
        {
		/* get the index */
            i++;

            //if (i>stGlobalex.MAX_CDT)
           // goto lblKO;

            CardBinModel cardbinModeldata = new CardBinModel();
            CardTypeModel cardTypeModeldata = new CardTypeModel();
            cardbinModeldata = databaseObj.getCardBinData(i);
            Log.i(TAG,"LLow Value:"+cardbinModeldata.getCDT_LO_RANGE());
            Log.i(TAG,"High Value:"+cardbinModeldata.getCDT_HI_RANGE());
            Log.i(TAG,"PAN:"+this.PAN);

            if(cardbinModeldata==null)
                break;

            Log.i(TAG,"LLow Value_2:"+cardbinModeldata.getCDT_LO_RANGE());
            Log.i(TAG,"High Value_2:"+cardbinModeldata.getCDT_HI_RANGE());
            Log.i(TAG,"getCDT_HDT_REFERENCE:"+cardbinModeldata.getCDT_HDT_REFERENCE());
            Log.i(TAG,"getCDT_CARD_TYPE_ARRAY:"+cardbinModeldata.getCDT_CARD_TYPE_ARRAY());
            Log.i(TAG,"PAN_2:"+this.PAN);


            Log.i(TAG,"Checking Loop");
            if((this.PAN.substring(0,cardbinModeldata.getCDT_LO_RANGE().length()).compareTo(cardbinModeldata.getCDT_LO_RANGE())>=0) &&
                    (this.PAN.substring(0,cardbinModeldata.getCDT_HI_RANGE().length()).compareTo(cardbinModeldata.getCDT_HI_RANGE())<=0))
            //if(Integer.parseInt(this.PAN.substring(0,cardbinModeldata.getCDT_LO_RANGE().length())) >= Integer.parseInt(cardbinModeldata.getCDT_LO_RANGE()) &&
                //Integer.parseInt(this.PAN.substring(0,cardbinModeldata.getCDT_HI_RANGE().length())) <= Integer.parseInt(cardbinModeldata.getCDT_HI_RANGE()) &&
                    //Integer.parseInt(cardbinModeldata.getCDT_LO_RANGE()) != 0 &&
                    //Integer.parseInt(cardbinModeldata.getCDT_HI_RANGE())!=0)
            {
                Log.i(TAG,"Inside the Loop:");
                inGNoOfValidHosts = vecGetValidCurrVsHDTList( 1
                        , cardbinModeldata.getCDT_HDT_REFERENCE()
                        , cardbinModeldata.getCDT_CARD_TYPE_ARRAY()
                        , cardbinModeldata.getCDT_HDT_REFERENCE().length()
                        ,ui8GlistOfValidHosts,databaseObj);

                chCardTypeIndex = cardbinModeldata.getCDT_CARD_TYPE_ARRAY().substring(0,2);

                Log.i(TAG,"chCardTypeIndex:"+chCardTypeIndex);


                //TODO TECK: Use Only first 2 bytes of the array for now... need to expand to dynamic later
                chCardTypeIndex = cardbinModeldata.getCDT_CARD_TYPE_ARRAY().substring(0,2);
                Log.i(TAG,"chCardTypeIndex:"+chCardTypeIndex);

                cardTypeModeldata = databaseObj.getCardTypeData(Integer.parseInt(chCardTypeIndex));
                inGCDT = Integer.parseInt(cardbinModeldata.getCDT_ID());

                Log.i(TAG,"inGCDT:"+inGCDT);

               break;
            }

            Log.i(TAG,"come out of loop");
            if(cardbinModeldata.getCDT_LO_RANGE().length() == 0 || cardbinModeldata.getCDT_HI_RANGE().length() == 0) {
                Log.i(TAG,"cardbinModeldata.getCDT_LO_RANGE().length()");
                return 1;
            }
        }

        Log.i(TAG,"Find Valid ");
        HostModel hostdata = new HostModel();
        //inGHDT = inSelectHost();
        for (i = 0; i < inGNoOfValidHosts; i++) {

            String localHdtIndex=ui8GlistOfValidHosts[i];
            Log.i(TAG,"localHdtIndex:"+localHdtIndex);


            int inlocalHdtIndex = Integer.parseInt(localHdtIndex);
            hostdata = databaseObj.getHostTableData(inlocalHdtIndex);
            if(!hostdata.getHDT_HOST_ENABLED().equals("1"))
            {
                Log.i(TAG,"Continue...");
                continue;
            }
            if(hostdata.getHDT_DESCRIPTION().contains("ALI"))
            {
                Log.i(TAG,"ALIPAY FOUND ...");
                inGHDT = Integer.parseInt(hostdata.getHDT_HOST_ID());
                inGCOM = Integer.parseInt(hostdata.getHDT_COM_INDEX());
                inGCURR = Integer.parseInt(hostdata.getHDT_CURR_INDEX());
                Log.i(TAG,"inGHDT1..."+inGHDT);
                Log.i(TAG,"inGCOM..."+inGCOM);
                Log.i(TAG,"inGCURR..."+inGCURR);
                inFindGetCTT(inGHDT, databaseObj);
                return 0;

            }else {
                Log.i(TAG, "inlocalHdtIndex2..." + inlocalHdtIndex);
                inGHDT = Integer.parseInt(hostdata.getHDT_HOST_ID());
            }
            Log.i(TAG,"inGHDT..."+inGHDT);

        }

    return 1;
    }

    int inFindGetCTT(int inHDTIndex, DBHelper databaseObj)
    {
        //HDT_STRUCT localHDT;
        String chHDTIndex="";
        String chCTTIndex="";
        //int inRedirectHDTIndex =0;
        //open CTT file based on HDT & CDT
        int i=0;
        Log.i(TAG,"inFindGetCTT_1");
        Log.i(TAG,"inFindGetCTT_1");
        Log.i(TAG,"inFindGetCTT_1");
        CardBinModel cardBindata = databaseObj.getCardBinData(inGCDT);
        HostModel localHDT = new HostModel();
        CardTypeModel cardTypeData = new CardTypeModel();

        Log.i(TAG,"cardBindata.getCDT_HDT_REFERENCE()"+cardBindata.getCDT_HDT_REFERENCE());

        for (i=0;i<(cardBindata.getCDT_HDT_REFERENCE().length()/2); i++)
        {
            //memcpy(chHDTIndex,stGCDTStruct.CDT_HDT_REFERENCE+(i*2),2);

            Log.i(TAG,"inside-Loop_1");
            chHDTIndex = cardBindata.getCDT_HDT_REFERENCE().substring((i*2),((i*2)+2));
            localHDT = databaseObj.getHostTableData(inGHDT);

            Log.i(TAG,"getHDT_HOST_LABEL::"+localHDT.getHDT_HOST_LABEL());
            Log.i(TAG,"getHDT_HOST_LABEL::"+localHDT.getHDT_DESCRIPTION());
            //memset(&localHDT, 0, sizeof(HDT_STRUCT));
            //inGetHDTConfig(GET, atoi(chHDTIndex), &localHDT);

            // 2014031300 Teck: Handle HDTs that are redirected to reflect the correct card type
            /*while (atoi(localHDT.HDT_REDIRECT_IF_DISABLE) > 0 && localHDT.HDT_HOST_ENABLED[0] == HDT_DISABLED) {
                inRedirectHDTIndex = atoi(localHDT.HDT_REDIRECT_IF_DISABLE);
                memset(&localHDT, 0, sizeof(HDT_STRUCT));
                inGetHDTConfig(GET, inRedirectHDTIndex, (HDT_STRUCT*) &localHDT);

                if (localHDT.HDT_HOST_ENABLED[0] == HDT_ENABLED)
                    sprintf(chHDTIndex, "%02d", inRedirectHDTIndex);
            }

            if (localHDT.HDT_HOST_ENABLED[0] == HDT_DISABLED)
                continue;*/

            // compare the HDT indexes
            if (Integer.parseInt(chHDTIndex) == inHDTIndex)
            {
                Log.i(TAG,"inside-Loop_1 Matched HDT");
                // extract CTT index in CDT_CARD_TYPE_ARRAY from the same position as CDT_HDT_REFERENCE
                //memcpy(chCTTIndex,stGCDTStruct.CDT_CARD_TYPE_ARRAY+(i*2),2 );
                chCTTIndex = cardBindata.getCDT_CARD_TYPE_ARRAY().substring((i*2),((i*2)+2));
                cardTypeData =databaseObj.getCardTypeData(Integer.parseInt(chCTTIndex));
                Log.i(TAG,"CTT_CARD_LABEL::"+cardTypeData.getCTT_CARD_LABEL());
                Log.i(TAG,"CTT_CARD_FORMAT::"+cardTypeData.getCTT_CARD_FORMAT());
                inGCTT = Integer.parseInt(chCTTIndex);

                Log.i(TAG,"inGCTT::"+inGCTT);
                //open CTT
                //if(inGetSetCTTConfig(GET,atoi(chCTTIndex)) == FALSE)
                    //return FALSE;

                return 0;
            }

        }

        return 1;
    }

    int vecGetValidCurrVsHDTList( int ui16CurrencyIndex, String CDT_HDT_REFERENCEVal, String CDT_CARD_TYPE_ARRAYVal, int CHReferenceSize, String[] listOfValidHosts, DBHelper databaseObj)
    {
        //HDT_STRUCT localHDTStruct;
        int localHDTIndex,i,inTotalValidInvalidHosts = 0;
        int inTotalValidHosts=0;
        //byte[] ui8ListOfValinInvalidHosts=new byte[100];//[100];
        //byte[] chlistOfValidHosts=new byte[100];
        String[] ui8ListOfValinInvalidHosts = new String[50];
        String[] chlistOfValidHosts = new String[50];
        String HDTRef;//[2+1]; //TECK:Renamed to something more meaningful
        String CTTRef;//[2+1];
        //ui8ListOfValinInvalidHosts="";
        int inRedirectHDTIndex = 0;
        HostModel hostdata = new HostModel();

        Log.i(TAG,"vecGetValidCurrVsHDTList_1");




        //Calculate how many host(valid & invalid) are there.
        //List down all valid and invalid host
        CHReferenceSize = CDT_HDT_REFERENCEVal.length();
        for (i = 0; i < CHReferenceSize; i += 2) {

            //memset(HDTRef, 0, sizeof(HDTRef));
            HDTRef="";
            CTTRef="";
            HDTRef = CDT_HDT_REFERENCEVal.substring(i,i+2);
            Log.i(TAG,"HDTRef:"+HDTRef);
            //memcpy(HDTRef, CDT_HDT_REFERENCEVal + i, 2);
            //memset(CTTRef, 0, sizeof(CTTRef));
            CTTRef = CDT_CARD_TYPE_ARRAYVal.substring(i,i+2);
            Log.i(TAG,"CTTRef:"+CTTRef);
            //memcpy(CTTRef, CDT_CARD_TYPE_ARRAYVal + i, 2);

            if (Integer.parseInt(HDTRef) > 0 && Integer.parseInt(HDTRef) < 100) {
                ui8ListOfValinInvalidHosts[inTotalValidInvalidHosts]= HDTRef;
                Log.i(TAG,"ui8ListOfValinInvalidHosts:"+(ui8ListOfValinInvalidHosts[inTotalValidInvalidHosts]));
                inTotalValidInvalidHosts++;
            }
        }




        //early exit
        if(inTotalValidInvalidHosts == 0){
            return 0;
        }

        //since we now have a list of both valids and invalids,
        //we can now go through each of them to find out which is valid
        for( i=0; i< inTotalValidInvalidHosts; i++)
        {
            localHDTIndex= Integer.parseInt(ui8ListOfValinInvalidHosts[i]);

            //Open HDT file and read structure
            hostdata = databaseObj.getHostTableData(localHDTIndex);
            //if(inGetHDTConfig(GET,localHDTIndex, (HDT_STRUCT*)&localHDTStruct)==FALSE)
            if(hostdata == null)
                continue;


            Log.i(TAG,"HOST_DESCRIPTION:::"+ hostdata.getHDT_DESCRIPTION());
            Log.i(TAG,"getHDT_HOST_LABEL:::"+ hostdata.getHDT_HOST_LABEL());
            //Teck: Commented because currency matching is no should no longer be required
            //if(atoi(localHDTStruct.HDT_CURR_INDEX) == ui16CurrencyIndex)
            if(hostdata.getHDT_HOST_ENABLED().substring(0,1).equals("1"))
            {
                Log.i(TAG,"ENABLED HOST:::");
                //2014030400 Teck: Fix issue where terminal is not validating entrymode if the HDT is redirected.
                //if(boValidateHDTEntryMode(localHDTStruct) == FALSE)//Implement later
                  //  continue;

                // Do not include DCC Travelex host if the NoDCC indicator is true
                // NoDCC is usually raised when the chip's 5F28 matches with the terminal's 9F1A
                //if(stGlobalex.inGNoDCC == TRUE && localHDTStruct.HDT_HOST_TYPE[0] == TRAVELEX_HOST)
                   // continue;


                listOfValidHosts[inTotalValidHosts] = ui8ListOfValinInvalidHosts[i];
                Log.i(TAG,"listOfValidHosts:"+ listOfValidHosts[inTotalValidHosts]);
                inTotalValidHosts++;

            }
            //listOfValidHosts = chlistOfValidHosts;//.toString();


            /*else if( atoi(localHDTStruct.HDT_REDIRECT_IF_DISABLE) > 0 && localHDTStruct.HDT_HOST_ENABLED[0] == HDT_DISABLED )
            {

                // loop into nested hdt
                while( atoi(localHDTStruct.HDT_REDIRECT_IF_DISABLE) > 0 && localHDTStruct.HDT_HOST_ENABLED[0] ==  HDT_DISABLED )
                {
                    inRedirectHDTIndex = atoi(localHDTStruct.HDT_REDIRECT_IF_DISABLE);
                    memset(&localHDTStruct,0,sizeof(HDT_STRUCT));
                    inGetHDTConfig(GET, inRedirectHDTIndex, (HDT_STRUCT*)&localHDTStruct );
                }

                // check if the redirected hdt is also enabled
                if(localHDTStruct.HDT_HOST_ENABLED[0] == HDT_ENABLED)
                {
                    //2014030400 Teck: Fix issue where terminal is not validating entrymode if the HDT is redirected.
                    if(boValidateHDTEntryMode(localHDTStruct) == FALSE)
                        continue;

                    listOfValidHosts[inTotalValidHosts] = inRedirectHDTIndex;
                    inTotalValidHosts++;
                }
            }*/
        }
        return inTotalValidHosts;

    }//end of vecGetValidCurrVsHDTList



    private String reqterminalid;
    private String resterminalid;
    private String currency;

    private String messagetype;
    private String processingcode;

    private String partnerid;
    private String sellerid;
    private String partnertransid;
    private String buyerid;
    private String refundid;
    private String refundreason;
    private String quantity;
    private String transactionname;
    private String userid;
    private String uiqueappid;
    private String androidserialid;
    private String responsecode;
    private String responsemesage;
    private String alipaytransid;

    public void setMessageType(String messagetype) {this.messagetype = messagetype;}
    public String getMessageType() {return messagetype;}

    public void setProcessingCode(String messagetype) {this.processingcode = processingcode;}
    public String getProcessingCode() {return processingcode;}

    public void setPartnerId(String partnerid) {this.partnerid = partnerid;}
    public String getPartnerId() {return partnerid;}

    public void setSellerId(String sellerid) {this.sellerid = sellerid;}
    public String getSellerId() {return sellerid;}

    public void setPartnerTransId(String partnertransid) {this.partnertransid = partnertransid;}
    public String getPartnerTransId() {return partnertransid;}

    public void setCurrency(String currency) {this.currency = currency;}
    public String getCurrency() {return currency;}


    public void setBuyerId(String buyerid) {this.buyerid = buyerid;}
    public String getBuyerId() {return buyerid;}

    public void setRefundId(String refundid) {this.refundid = refundid;}
    public String getRefundId() {return refundid;}

    public void setRefundReason(String refundreason) {this.refundreason = refundreason;}
    public String getRefundReason() {return refundreason;}

    public void setQuantity(String quantity) {this.quantity = quantity;}
    public String getQuantity() {return quantity;}

    public void setTransactionName(String transactionname) {this.transactionname = transactionname;}
    public String getTransactionName() {return transactionname;}

    public void setReqTerminalId(String reqterminalid) {this.reqterminalid = reqterminalid;}
    public String getReqTerminalId() {return reqterminalid;}

    public void setResTerminalId(String resterminalid) {this.resterminalid = resterminalid;}
    public String getResTerminalId() {return resterminalid;}

    public void setUserId(String userid) {this.userid = userid;}
    public String getUserId() {return userid;}

    public void setUiqueAppid(String uiqueappid) {this.uiqueappid = uiqueappid;}
    public String getUiqueAppid() {return uiqueappid;}

    public void setAndroidSerialId(String androidserialid) {this.androidserialid = androidserialid;}
    public String getAndroidSerialId() {return androidserialid;}

    public void setResponseCode(String responsecode) {this.responsecode = responsecode;}
    public String getResponseCode() {return responsecode;}

    public void setResponseMesage(String responsemesage) {this.responsemesage = responsemesage;}
    public String getResponseMesage() {return responsemesage;}

    public void setAlipayTransId(String alipaytransid) {this.alipaytransid = alipaytransid;}
    public String getAlipayTransId() {return alipaytransid;}

    public void settrxAmount(String trxAmount) {this.trxAmount = trxAmount;}
    public String gettrxAmount() {return trxAmount;}

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }
    public String getPAN() {return PAN;}

}