package com.mobileeftpos.android.eftpos.SupportClasses;

import android.util.Log;

import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardBinModelDao;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModelDao;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.HostModelDao;

import java.util.List;

/**
 * Created by venkat on 5/23/2017.
 */
//20170523: Venkat added transaction details table
public class TransactionDetails {
    public static final String TAG = "my_custom_msg";

    public static int inGHDT;
    public static int inGCDT;
    public static int inGCTT;
    public static int inGCOM;
    public static int inGCURR;

    public static int inFinalLength;
    public static  String deviceId;
    public static String trxAmount;
    public static String tipAmount;
    public static String trxDateTime;//yyyyMMddHHmmssSS
    public static String PAN;
    public static String ExpDate;
    public static int trxType;
    public static int inOritrxType;
    public static String stOriAmount;
    public static String chApprovalCode;
    public static String EntryMode;
    public static int inGTrxMode;
    public static String processingcode;
    public static String messagetype;
    public static String InvoiceNumber;
    public static String RetrievalRefNumber;
    public static String ResponseCode;
    public static String PersonName;
    public static String POSEntryMode;
    public static String NII;
    public static String POS_COND_CODE;

    public static String refundid;
    public static String refundreason;
    public static String responseMessge;
    public static String AlipayTag72;

    public static String ConnectionTimeout;

    // public static int inGHDT ;
    int inGNoOfValidHosts;
    //Ezlink Parameters
    public static byte[] CEPASver= new byte[1];// = CardResponse[0];
    public static byte[] LastPruseStatus= new byte[1];// = CardResponse[1];
    public static byte[] bOrigBal= new byte[3];//, CardResponse + 2, 3);
    //lnCardBalance = (CardResponse[2]*256*256) + (CardResponse[3]*256) + CardResponse[4];
    public static byte[]  AutoLoadAmt= new byte[3];//, CardResponse + 5, 3);
    public static byte[] CAN= new byte[8];//, CardResponse + 8, 8);
    public static byte[] CSN= new byte[8];//, CardResponse + 16, 8);
    public static byte[] PurseExpiryDate= new byte[2];//, CardResponse + 24, 2);
    public static byte[] PurseCreationDate= new byte[2];//, CardResponse + 26, 2);
    public static byte[] LastCreditTRP= new byte[4];//, CardResponse + 28, 4);
    public static byte[] LastCreditHeader= new byte[8];//, CardResponse + 32, 8);
    public static byte[] TransLogCount= new byte[1];
    public static byte[] IssuerSpecificdataLength= new byte[1];// = CardResponse[41];
    public static byte[] LastTRP= new byte[4];//, CardResponse + 42, 4);
    public static byte[] LastHeader= new byte[16];//, CardResponse + 46, 16);
    public static byte[] IssuerSpecificdata= new byte[32];//, CardResponse + 62, 32);
    public static byte[] LastOptions= new byte[1];
    public static byte[] SignCert= new byte[8];//, &CardResponse[63 + 32], 8);
    public static byte[] CounterData= new byte[8];//, &CardResponse[71 + 32], 8);
    public static byte[] IsoCrc_b= new byte[2];//, &CardResponse[79 + 32], 2);
    public static byte[] bBDC= new byte[1];
    public static byte[] bRefund= new byte[1];
    public static byte[] PurseBalance= new byte[3];
    public static byte[] LastSignCert= new byte[8];//, &CardResponse[63 + 32], 8);
    public static byte[] LastCounterData= new byte[8];//, &CardResponse[71 + 32], 8);

    //transaction
    public static byte[]  TerminalRandomNumber  = new byte[8];
    public static byte[]  CardNumberRandomNumber= new byte[8];
    public static byte[]  SAMSerialNumber= new byte[8];
    public static byte[]  SessionKey= new byte[16];
    public static byte[]  PaymentTRP= new byte[4];
    public static byte[]  EzlinkPaymentAmt= new byte[3];
    public static byte[]  JulianDate=new byte[4];
    public static byte[]  GtxnCRC=new byte[2];


    public static void vdCleanFields(){
        trxAmount="";
        tipAmount="";
        trxDateTime="";//yyyyMMddHHmmssSS
        PAN="";
        ExpDate="";
        trxType=0;
        chApprovalCode="";
        EntryMode="";
        inGHDT=0;
        inGCDT=0;
        inGCTT=0;
        inGCOM=0;
        inGCURR=0;
        inGTrxMode=0;
        processingcode="";
        messagetype="";
        InvoiceNumber="";
        RetrievalRefNumber="";
        ResponseCode="";
        PersonName="";
        POSEntryMode="";
        NII="";
        POS_COND_CODE="";

        AlipayTag72="";
        refundid="";
        //PartnerTransID="";

        
    }

    public int inSortPAN(DaoSession daoSession)
    {
        int i=0;
        String chCardTypeIndex = "";
       // String ui8GlistOfValidHosts = "";//new char[100];
        String[] ui8GlistOfValidHosts = new String[50];
        //memset(chCardTypeIndex,0,sizeof(chCardTypeIndex));
        //S_FS_FILE *pxFid;

        //memset(Pantemp,0,sizeof(Pantemp));
        //memcpy(Pantemp,keystr,strlen(keystr));
        if(TransactionDetails.PAN.isEmpty()|| TransactionDetails.PAN==null )
            return Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED;

        if(daoSession == null){
            return Constants.ReturnValues.RETURN_ERROR;
        }


        Log.i(TAG,"TransDetails::inSortPAN:");
        TransactionDetails.responseMessge = "Case 401";
        CardBinModelDao cardBinModelDao =daoSession.getCardBinModelDao();
        while (true)
        {
		/* get the index */
            i++;

            //if (i>stGlobalex.MAX_CDT)
           // goto lblKO;

            CardBinModel cardbinModeldata = new CardBinModel();
            List<CardBinModel> cardbinModeldataList = cardBinModelDao.loadAll();
            cardbinModeldata=cardbinModeldataList.get(i);
            Log.i(TAG,"TransDetails::LLow Value:"+cardbinModeldata.getCDT_LO_RANGE());
            Log.i(TAG,"TransDetails::High Value:"+cardbinModeldata.getCDT_HI_RANGE());
            Log.i(TAG,"TransDetails::PAN:"+TransactionDetails.PAN);

            TransactionDetails.responseMessge = "Case 402";
            if(cardbinModeldata==null) {
                TransactionDetails.responseMessge = "Case 408";
                break;
            }

            TransactionDetails.responseMessge = "Case 403"+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"LOW "+cardbinModeldata.getCDT_LO_RANGE()+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"HIGH "+cardbinModeldata.getCDT_HI_RANGE()+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"PAN "+this.PAN+"\n";
            Log.i(TAG,"TransDetails::LLow Value_2:"+cardbinModeldata.getCDT_LO_RANGE());
            Log.i(TAG,"TransDetails::High Value_2:"+cardbinModeldata.getCDT_HI_RANGE());
            Log.i(TAG,"TransDetails::getCDT_HDT_REFERENCE:"+cardbinModeldata.getCDT_HDT_REFERENCE());
            Log.i(TAG,"TransDetails::getCDT_CARD_TYPE_ARRAY:"+cardbinModeldata.getCDT_CARD_TYPE_ARRAY());
            Log.i(TAG,"TransDetails::PAN_2:"+this.PAN);



            Log.i(TAG,"TransDetails::Checking Loop");
            if((this.PAN.substring(0,cardbinModeldata.getCDT_LO_RANGE().length()).compareTo(cardbinModeldata.getCDT_LO_RANGE())>=0) &&
                    (this.PAN.substring(0,cardbinModeldata.getCDT_HI_RANGE().length()).compareTo(cardbinModeldata.getCDT_HI_RANGE())<=0))
            //if(Integer.parseInt(this.PAN.substring(0,cardbinModeldata.getCDT_LO_RANGE().length())) >= Integer.parseInt(cardbinModeldata.getCDT_LO_RANGE()) &&
                //Integer.parseInt(this.PAN.substring(0,cardbinModeldata.getCDT_HI_RANGE().length())) <= Integer.parseInt(cardbinModeldata.getCDT_HI_RANGE()) &&
                    //Integer.parseInt(cardbinModeldata.getCDT_LO_RANGE()) != 0 &&
                    //Integer.parseInt(cardbinModeldata.getCDT_HI_RANGE())!=0)
            {
                TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4033"+"\n";
                TransactionDetails.responseMessge = "Case 404";
                Log.i(TAG,"TransDetails::Inside the Loop:");
                inGNoOfValidHosts = vecGetValidCurrVsHDTList( 1
                        , cardbinModeldata.getCDT_HDT_REFERENCE()
                        , cardbinModeldata.getCDT_CARD_TYPE_ARRAY()
                        , cardbinModeldata.getCDT_HDT_REFERENCE().length()
                        ,ui8GlistOfValidHosts,daoSession);

                chCardTypeIndex = cardbinModeldata.getCDT_CARD_TYPE_ARRAY().substring(0,2);

                Log.i(TAG,"TransDetails::chCardTypeIndex:"+chCardTypeIndex);


                //TODO TECK: Use Only first 2 bytes of the array for now... need to expand to dynamic later
                chCardTypeIndex = cardbinModeldata.getCDT_CARD_TYPE_ARRAY().substring(0,2);
                Log.i(TAG,"TransDetails::chCardTypeIndex:"+chCardTypeIndex);

                //cardTypeModeldata = databaseObj.getCardTypeData(Integer.parseInt(chCardTypeIndex));
                inGCDT = Integer.parseInt(cardbinModeldata.getCDT_ID());

                Log.i(TAG,"TransDetails::inGCDT:"+inGCDT);

                TransactionDetails.responseMessge = "Case 409";
               break;
            }

            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4032"+"\n";
            Log.i(TAG,"TransDetails::come out of loop");
            if(cardbinModeldata.getCDT_LO_RANGE().length() == 0 || cardbinModeldata.getCDT_HI_RANGE().length() == 0) {
                TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4033"+"\n";
                TransactionDetails.responseMessge = "Case 405";
                Log.i(TAG,"TransDetails::cardbinModeldata.getCDT_LO_RANGE().length()");
                return Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED;
            }
            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4034"+"\n";
        }

        TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4035"+"\n";
        Log.i(TAG,"TransDetails::Find Valid:inGNoOfValidHosts "+inGNoOfValidHosts);
        HostModelDao hostdataDao = daoSession.getHostModelDao();
        //inGHDT = inSelectHost();
        for (i = 0; i < inGNoOfValidHosts; i++) {
            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4036"+"\n";

            String localHdtIndex=ui8GlistOfValidHosts[i];
            Log.i(TAG,"TransDetails::localHdtIndex:"+localHdtIndex);


            int inlocalHdtIndex = Integer.parseInt(localHdtIndex);
            HostModel hostdata =daoSession.getHostModelDao().loadAll().get(0);
           // hostdata = databaseObj.getHostTableData(inlocalHdtIndex);
            if(!hostdata.getHDT_HOST_ENABLED().equals("1"))
            {
                Log.i(TAG,"TransDetails::Continue...");
                continue;
            }
            //if(hostdata.getHDT_DESCRIPTION().contains("ALI"))
            {
                Log.i(TAG,"TransDetails::ALIPAY FOUND ...");
                inGHDT = Integer.parseInt(hostdata.getHDT_HOST_ID());
                inGCOM = Integer.parseInt(hostdata.getHDT_COM_INDEX());
                inGCURR = Integer.parseInt(hostdata.getHDT_CURR_INDEX());
                Log.i(TAG,"TransDetails::inGHDT1..."+inGHDT);
                Log.i(TAG,"TransDetails::inGCOM..."+inGCOM);
                Log.i(TAG,"TransDetails::inGCURR..."+inGCURR);
                inFindGetCTT(inGHDT, daoSession);
                TransactionDetails.responseMessge = "Case 4101";
                return Constants.ReturnValues.RETURN_OK;

            }
            /*else {
                Log.i(TAG, "inlocalHdtIndex2..." + inlocalHdtIndex);
                inGHDT = Integer.parseInt(hostdata.getHDT_HOST_ID());
                inGHDT = Integer.parseInt(hostdata.getHDT_HOST_ID());
                inGCOM = Integer.parseInt(hostdata.getHDT_COM_INDEX());
                inGCURR = Integer.parseInt(hostdata.getHDT_CURR_INDEX());
                Log.i(TAG,"TransDetails::inGHDT1..."+inGHDT);
                Log.i(TAG,"TransDetails::inGCOM..."+inGCOM);
                Log.i(TAG,"TransDetails::inGCURR..."+inGCURR);
                inFindGetCTT(inGHDT, databaseObj);
                return Constants.ReturnValues.RETURN_OK;
            }*/
            //Log.i(TAG,"TransDetails::inGHDT..."+inGHDT);

        }

        TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4037"+"\n";
    return Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED;
    }

    int inFindGetCTT(int inHDTIndex, DaoSession daoSession)
    {
        //HDT_STRUCT localHDT;
        String chHDTIndex="";
        String chCTTIndex="";
        //int inRedirectHDTIndex =0;
        //open CTT file based on HDT & CDT
        int i=0;
        Log.i(TAG,"TransDetails::inFindGetCTT_1");
        Log.i(TAG,"TransDetails::inFindGetCTT_1");
        Log.i(TAG,"TransDetails::inFindGetCTT_1");
        //CardBinModel cardBindata = databaseObj.getCardBinData(inGCDT);
        CardBinModelDao cardBinModelDao=daoSession.getCardBinModelDao();
        CardBinModel cardBindata =cardBinModelDao.loadAll().get(0);
        HostModelDao hostModelDao=daoSession.getHostModelDao();
        CardTypeModelDao cardTypeModelDao=daoSession.getCardTypeModelDao();


        Log.i(TAG,"TransDetails::cardBindata.getCDT_HDT_REFERENCE()"+cardBindata.getCDT_HDT_REFERENCE());

        for (i=0;i<(cardBindata.getCDT_HDT_REFERENCE().length()/2); i++)
        {
            //memcpy(chHDTIndex,stGCDTStruct.CDT_HDT_REFERENCE+(i*2),2);
            HostModel localHDT = new HostModel();
            Log.i(TAG,"TransDetails::inside-Loop_1");
            chHDTIndex = cardBindata.getCDT_HDT_REFERENCE().substring((i*2),((i*2)+2));
            localHDT = hostModelDao.loadAll().get(0);//databaseObj.getHostTableData(inGHDT);

            Log.i(TAG,"TransDetails::getHDT_HOST_LABEL::"+localHDT.getHDT_HOST_LABEL());
            Log.i(TAG,"TransDetails::getHDT_HOST_LABEL::"+localHDT.getHDT_DESCRIPTION());
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
                CardTypeModel cardTypeData =new CardTypeModel();
                Log.i(TAG,"TransDetails::inside-Loop_1 Matched HDT");
                // extract CTT index in CDT_CARD_TYPE_ARRAY from the same position as CDT_HDT_REFERENCE
                //memcpy(chCTTIndex,stGCDTStruct.CDT_CARD_TYPE_ARRAY+(i*2),2 );
                chCTTIndex = cardBindata.getCDT_CARD_TYPE_ARRAY().substring((i*2),((i*2)+2));
                cardTypeData =  cardTypeModelDao.loadAll().get(0);//databaseObj.getCardTypeData(Integer.parseInt(chCTTIndex));
                Log.i(TAG,"TransDetails::CTT_CARD_LABEL::"+cardTypeData.getCTT_CARD_LABEL());
                Log.i(TAG,"TransDetails::CTT_CARD_FORMAT::"+cardTypeData.getCTT_CARD_FORMAT());
                inGCTT = Integer.parseInt(chCTTIndex);

                Log.i(TAG,"TransDetails::inGCTT::"+inGCTT);
                //open CTT
                //if(inGetSetCTTConfig(GET,atoi(chCTTIndex)) == FALSE)
                    //return FALSE;

                return 0;
            }

        }

        return 1;
    }

    int vecGetValidCurrVsHDTList( int ui16CurrencyIndex, String CDT_HDT_REFERENCEVal, String CDT_CARD_TYPE_ARRAYVal,
                                  int CHReferenceSize, String[] listOfValidHosts, DaoSession daoSession)
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
        HostModelDao hostdataDao = daoSession.getHostModelDao();

        Log.i(TAG,"TransDetails::vecGetValidCurrVsHDTList_1");




        //Calculate how many host(valid & invalid) are there.
        //List down all valid and invalid host
        CHReferenceSize = CDT_HDT_REFERENCEVal.length();
        for (i = 0; i < CHReferenceSize; i += 2) {

            //memset(HDTRef, 0, sizeof(HDTRef));
            HDTRef="";
            CTTRef="";
            HDTRef = CDT_HDT_REFERENCEVal.substring(i,i+2);
            Log.i(TAG,"TransDetails::HDTRef:"+HDTRef);
            //memcpy(HDTRef, CDT_HDT_REFERENCEVal + i, 2);
            //memset(CTTRef, 0, sizeof(CTTRef));
            CTTRef = CDT_CARD_TYPE_ARRAYVal.substring(i,i+2);
            Log.i(TAG,"TransDetails::CTTRef:"+CTTRef);
            //memcpy(CTTRef, CDT_CARD_TYPE_ARRAYVal + i, 2);

            if (Integer.parseInt(HDTRef) > 0 && Integer.parseInt(HDTRef) < 100) {
                ui8ListOfValinInvalidHosts[inTotalValidInvalidHosts]= HDTRef;
                Log.i(TAG,"TransDetails::ui8ListOfValinInvalidHosts:"+(ui8ListOfValinInvalidHosts[inTotalValidInvalidHosts]));
                inTotalValidInvalidHosts++;
            }
        }




        //early exit
        if(inTotalValidInvalidHosts == 0){
            TransactionDetails.responseMessge = "Case 407";
            return 0;
        }

        //since we now have a list of both valids and invalids,
        //we can now go through each of them to find out which is valid
        for( i=0; i< inTotalValidInvalidHosts; i++)
        {
            localHDTIndex= Integer.parseInt(ui8ListOfValinInvalidHosts[i]);
             List<HostModel> hostdatalist =hostdataDao.loadAll();
             HostModel hostdata=hostdatalist.get(0);
            //Open HDT file and read structure
            //if(inGetHDTConfig(GET,localHDTIndex, (HDT_STRUCT*)&localHDTStruct)==FALSE)
            if(hostdata == null)
                continue;


            Log.i(TAG,"TransDetails::HOST_DESCRIPTION:::"+ hostdata.getHDT_DESCRIPTION());
            Log.i(TAG,"TransDetails::getHDT_HOST_LABEL:::"+ hostdata.getHDT_HOST_LABEL());
            Log.i(TAG,"TransDetails::getHDT_HOST_ENABLED:::"+ hostdata.getHDT_HOST_ENABLED());
            //Teck: Commented because currency matching is no should no longer be required
            //if(atoi(localHDTStruct.HDT_CURR_INDEX) == ui16CurrencyIndex)
            if(hostdata.getHDT_HOST_ENABLED().substring(0,1).equals("1"))
            {
                Log.i(TAG,"TransDetails::ENABLED HOST:::");
                //2014030400 Teck: Fix issue where terminal is not validating entrymode if the HDT is redirected.
                //if(boValidateHDTEntryMode(localHDTStruct) == FALSE)//Implement later
                  //  continue;

                // Do not include DCC Travelex host if the NoDCC indicator is true
                // NoDCC is usually raised when the chip's 5F28 matches with the terminal's 9F1A
                //if(stGlobalex.inGNoDCC == TRUE && localHDTStruct.HDT_HOST_TYPE[0] == TRAVELEX_HOST)
                   // continue;


                listOfValidHosts[inTotalValidHosts] = ui8ListOfValinInvalidHosts[i];
                Log.i(TAG,"TransDetails::listOfValidHosts:"+ listOfValidHosts[inTotalValidHosts]);
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





    /*public void setMessageType(String messagetype) {this.messagetype = messagetype;}
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
    public String gettrxAmount() {return trxAmount;}*/



}
