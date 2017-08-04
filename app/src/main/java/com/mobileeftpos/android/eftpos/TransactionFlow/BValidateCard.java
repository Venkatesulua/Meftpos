package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardBinModelDao;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModelDao;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.HostModelDao;

import java.util.List;

/**
 * Created by venkat on 8/2/2017.
 */

public class BValidateCard extends AGetCard {

    public BValidateCard(Activity context){

        super (context);
    }
    int inGNoOfValidHosts=0;

    public int inSortPAN( )
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




        Log.i(TAG,"TransDetails::inSortPAN:");
        TransactionDetails.responseMessge = "Case 401";
        //CardBinModelDao cardBinModelDao =daoSession.getCardBinModelDao();
        while (true)
        {
		/* get the index */
            i++;

            //if (i>stGlobalex.MAX_CDT)
            // goto lblKO;

            //CardBinModel cardBinModel = new CardBinModel();
            List<CardBinModel> cardBinModelList = cardBinModelDao.loadAll();
            cardBinModel=cardBinModelList.get(0);
            Log.i(TAG,"TransDetails::LLow Value:"+cardBinModel.getCDT_LO_RANGE());
            Log.i(TAG,"TransDetails::High Value:"+cardBinModel.getCDT_HI_RANGE());
            Log.i(TAG,"TransDetails::PAN:"+TransactionDetails.PAN);

            if(cardBinModel==null) {
                break;
            }

            TransactionDetails.responseMessge = "Case 403"+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"LOW "+cardBinModel.getCDT_LO_RANGE()+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"HIGH "+cardBinModel.getCDT_HI_RANGE()+"\n";
            TransactionDetails.responseMessge = TransactionDetails.responseMessge+"PAN "+TransactionDetails.PAN+"\n";
            Log.i(TAG,"TransDetails::LLow Value_2:"+cardBinModel.getCDT_LO_RANGE());
            Log.i(TAG,"TransDetails::High Value_2:"+cardBinModel.getCDT_HI_RANGE());
            Log.i(TAG,"TransDetails::getCDT_HDT_REFERENCE:"+cardBinModel.getCDT_HDT_REFERENCE());
            Log.i(TAG,"TransDetails::getCDT_CARD_TYPE_ARRAY:"+cardBinModel.getCDT_CARD_TYPE_ARRAY());
            Log.i(TAG,"TransDetails::PAN_2:"+TransactionDetails.PAN);



            Log.i(TAG,"TransDetails::Checking Loop");
            if((TransactionDetails.PAN.substring(0,cardBinModel.getCDT_LO_RANGE().length()).compareTo(cardBinModel.getCDT_LO_RANGE())>=0) &&
                    (TransactionDetails.PAN.substring(0,cardBinModel.getCDT_HI_RANGE().length()).compareTo(cardBinModel.getCDT_HI_RANGE())<=0))
            //if(Integer.parseInt(this.PAN.substring(0,cardBinModel.getCDT_LO_RANGE().length())) >= Integer.parseInt(cardBinModel.getCDT_LO_RANGE()) &&
            //Integer.parseInt(this.PAN.substring(0,cardBinModel.getCDT_HI_RANGE().length())) <= Integer.parseInt(cardBinModel.getCDT_HI_RANGE()) &&
            //Integer.parseInt(cardBinModel.getCDT_LO_RANGE()) != 0 &&
            //Integer.parseInt(cardBinModel.getCDT_HI_RANGE())!=0)
            {
                TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4033"+"\n";
                TransactionDetails.responseMessge = "Case 404";
                Log.i(TAG,"TransDetails::Inside the Loop:");
                inGNoOfValidHosts = vecGetValidCurrVsHDTList( 1
                        , cardBinModel.getCDT_HDT_REFERENCE()
                        , cardBinModel.getCDT_CARD_TYPE_ARRAY()
                        , cardBinModel.getCDT_HDT_REFERENCE().length()
                        ,ui8GlistOfValidHosts);

                chCardTypeIndex = cardBinModel.getCDT_CARD_TYPE_ARRAY().substring(0,2);

                Log.i(TAG,"TransDetails::chCardTypeIndex:"+chCardTypeIndex);


                //TODO TECK: Use Only first 2 bytes of the array for now... need to expand to dynamic later
                chCardTypeIndex = cardBinModel.getCDT_CARD_TYPE_ARRAY().substring(0,2);
                Log.i(TAG,"TransDetails::chCardTypeIndex:"+chCardTypeIndex);

                //cardTypeModeldata = databaseObj.getCardTypeData(Integer.parseInt(chCardTypeIndex));
                TransactionDetails.inGCDT = Integer.parseInt(cardBinModel.getCDT_ID());

                Log.i(TAG,"TransDetails::inGCDT:"+TransactionDetails.inGCDT);

                TransactionDetails.responseMessge = "Case 409";
                break;
            }

            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4032"+"\n";
            Log.i(TAG,"TransDetails::come out of loop");
            if(cardBinModel.getCDT_LO_RANGE().length() == 0 || cardBinModel.getCDT_HI_RANGE().length() == 0) {
                TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4033"+"\n";
                TransactionDetails.responseMessge = "Case 405";
                Log.i(TAG,"TransDetails::cardBinModel.getCDT_LO_RANGE().length()");
                return Constants.ReturnValues.TRANSACTION_NOT_SUPPORTED;
            }
            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4034"+"\n";
        }

        TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4035"+"\n";
        Log.i(TAG,"TransDetails::Find Valid:inGNoOfValidHosts "+inGNoOfValidHosts);
       // HostModelDao hostModelDao = daoSession.getHostModelDao();
        //inGHDT = inSelectHost();
        for (i = 0; i < inGNoOfValidHosts; i++) {
            TransactionDetails.responseMessge = TransactionDetails.responseMessge + "Case 4036"+"\n";

            String localHdtIndex=ui8GlistOfValidHosts[i];
            Log.i(TAG,"TransDetails::localHdtIndex:"+localHdtIndex);


            int inlocalHdtIndex = Integer.parseInt(localHdtIndex);
           // HostModel hostdata =daoSession.getHostModelDao().loadAll().get(0);
            // hostdata = databaseObj.getHostTableData(inlocalHdtIndex);
            if(!hostModel.getHDT_HOST_ENABLED().equals("1"))
            {
                Log.i(TAG,"TransDetails::Continue...");
                continue;
            }
            //if(hostdata.getHDT_DESCRIPTION().contains("ALI"))
            {
                Log.i(TAG,"TransDetails::ALIPAY FOUND ...");
                TransactionDetails.inGHDT = Integer.parseInt(hostModel.getHDT_HOST_ID());
                TransactionDetails.inGCOM = Integer.parseInt(hostModel.getHDT_COM_INDEX());
                TransactionDetails.inGCURR = Integer.parseInt(hostModel.getHDT_CURR_INDEX());
                Log.i(TAG,"TransDetails::inGHDT1..."+TransactionDetails.inGHDT);
                Log.i(TAG,"TransDetails::inGCOM..."+TransactionDetails.inGCOM);
                Log.i(TAG,"TransDetails::inGCURR..."+TransactionDetails.inGCURR);
                inFindGetCTT(TransactionDetails.inGHDT);
                //TransactionDetails.responseMessge = "Case 4101";
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

    int inFindGetCTT(int inHDTIndex)
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
        //CardBinModelDao cardBinModelDao=daoSession.getCardBinModelDao();
        //CardBinModel cardBindata =cardBinModelDao.loadAll().get(0);
        //HostModelDao hostModelDao=daoSession.getHostModelDao();
        //CardTypeModelDao cardTypeModelDao=daoSession.getCardTypeModelDao();


        Log.i(TAG,"TransDetails::cardBindata.getCDT_HDT_REFERENCE()"+cardBinModel.getCDT_HDT_REFERENCE());

        for (i=0;i<(cardBinModel.getCDT_HDT_REFERENCE().length()/2); i++)
        {
            //memcpy(chHDTIndex,stGCDTStruct.CDT_HDT_REFERENCE+(i*2),2);
            HostModel localHDT = new HostModel();
            Log.i(TAG,"TransDetails::inside-Loop_1");
            chHDTIndex = cardBinModel.getCDT_HDT_REFERENCE().substring((i*2),((i*2)+2));
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
                //CardTypeModel cardTypeData =new CardTypeModel();
                Log.i(TAG,"TransDetails::inside-Loop_1 Matched HDT");
                // extract CTT index in CDT_CARD_TYPE_ARRAY from the same position as CDT_HDT_REFERENCE
                //memcpy(chCTTIndex,stGCDTStruct.CDT_CARD_TYPE_ARRAY+(i*2),2 );
                chCTTIndex = cardBinModel.getCDT_CARD_TYPE_ARRAY().substring((i*2),((i*2)+2));
                cardTypeModel =  cttModelDao.loadAll().get(0);//databaseObj.getCardTypeData(Integer.parseInt(chCTTIndex));
                Log.i(TAG,"TransDetails::CTT_CARD_LABEL::"+cardTypeModel.getCTT_CARD_LABEL());
                Log.i(TAG,"TransDetails::CTT_CARD_FORMAT::"+cardTypeModel.getCTT_CARD_FORMAT());
                TransactionDetails.inGCTT = Integer.parseInt(chCTTIndex);

                Log.i(TAG,"TransDetails::inGCTT::"+TransactionDetails.inGCTT);


                //Load HDT,CTT,CURR
                hostModel = hostModelDao.loadAll().get(TransactionDetails.inGHDT);
                cardBinModel = cardBinModelDao.loadAll().get(TransactionDetails.inGCDT);
                cardTypeModel = cttModelDao.loadAll().get(TransactionDetails.inGCTT);
                comModel = comModelDao.loadAll().get(TransactionDetails.inGCOM);
                currencyModel = currModelDao.loadAll().get(TransactionDetails.inGCURR);
                //open CTT
                //if(inGetSetCTTConfig(GET,atoi(chCTTIndex)) == FALSE)
                //return FALSE;

                return 0;
            }

        }

        return 1;
    }

    int vecGetValidCurrVsHDTList( int ui16CurrencyIndex, String CDT_HDT_REFERENCEVal, String CDT_CARD_TYPE_ARRAYVal,
                                  int CHReferenceSize, String[] listOfValidHosts)
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
        //HostModelDao hostModelDao = daoSession.getHostModelDao();

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
            //List<HostModel> hostdatalist =hostModelDao.loadAll();
            hostModel=hostModelDao.loadAll().get(0);
            hostModel = hostModelDao.loadAll().get(localHDTIndex);
            //Open HDT file and read structure
            //if(inGetHDTConfig(GET,localHDTIndex, (HDT_STRUCT*)&localHDTStruct)==FALSE)
            if(hostModel == null)
                continue;


            Log.i(TAG,"TransDetails::HOST_DESCRIPTION:::"+ hostModel.getHDT_DESCRIPTION());
            Log.i(TAG,"TransDetails::getHDT_HOST_LABEL:::"+ hostModel.getHDT_HOST_LABEL());
            Log.i(TAG,"TransDetails::getHDT_HOST_ENABLED:::"+ hostModel.getHDT_HOST_ENABLED());
            //Teck: Commented because currency matching is no should no longer be required
            //if(atoi(localHDTStruct.HDT_CURR_INDEX) == ui16CurrencyIndex)
            if(hostModel.getHDT_HOST_ENABLED().substring(0,1).equals("1"))
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

}
