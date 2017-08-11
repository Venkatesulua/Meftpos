package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.BatchModel;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by venkat on 8/2/2017.
 */

public class EHostConnectivity extends CPacketHandling {

    private Activity locontext;
    int inError=0;
    public int inConnectSendRecv(String ServerIP, String Port){
        if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
        //FinalData = UploadData.getBytes();

        if ((byResponseData = remoteHost.inSendRecvPacket(byRequestData,TransactionDetails.inFinalLength)) ==null) {
            Log.i(TAG,"EHostConnectivity:: Send Failed");
            return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
        }

        inError = inProcessPacket(byResponseData,TransactionDetails.inFinalLength);
        if (inError != Constants.ReturnValues.RETURN_OK) {
            remoteHost.inDisconnection();
            Log.i(TAG,"EHostConnectivity::Process Packet Failed");
            return inError;
        }

        if (remoteHost.inDisconnection() != 0) {
            return Constants.ReturnValues.RETURN_ERROR;
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public int inHostConnect()
    {

        int inError=-1;
        byRequestData=null;
        byRequestData= new byte[1512];
        TransactionDetails.inFinalLength = inFCreatePacket(byRequestData, TransactionDetails.trxType,locontext);
        String IP_Port = GetCommsModel().getCOM_PRIMARY_IP_PORT();
        int indexOffset = IP_Port.indexOf("|");
        if(TransactionDetails.inFinalLength != 0)
        {
            inError = inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
        }


        return inError;
    }

    public int inCheckReversal()
    {
        String ReversalData = KeyValueDB.getReversal(locontext);

        String IP_Port = GetCommsModel().getCOM_PRIMARY_IP_PORT();
        int indexOffset = IP_Port.indexOf("|");
        if(!ReversalData.isEmpty()) {
            EHostConnectivity connectSendRecv = new EHostConnectivity();

            byRequestData = new BigInteger(ReversalData, 16).toByteArray();
            TransactionDetails.inFinalLength = byRequestData[0] *256;
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (byRequestData[1]);
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;

            return inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public int inCheckUpload()
    {
        String UploadData = KeyValueDB.getUpload(locontext);

        String IP_Port = GetCommsModel().getCOM_PRIMARY_IP_PORT();
        int indexOffset = IP_Port.indexOf("|");
        if(!UploadData.isEmpty()) {
            //EHostConnectivity connectSendRecv = new EHostConnectivity();

            byRequestData = new BigInteger(UploadData, 16).toByteArray();
            TransactionDetails.inFinalLength = byRequestData[0] *256;
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (byRequestData[1]);
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
            return inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
        }


        return Constants.ReturnValues.RETURN_OK;
    }

    public int inSaveUpload(){
        //TransactionDetails.trxType=Constants.TransType.ALIPAY_UPLOAD;
        TransactionDetails.inFinalLength = inFCreatePacket(byRequestData, Constants.TransType
                .ALIPAY_UPLOAD,locontext);
        String result;
        result = "";
        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
            result = result + String.format("%02x", byRequestData[k]);
        }
        //Log.i(TAG,"\nSendings:");
        //Log.i(TAG,result);
        KeyValueDB.setUpload(locontext,new String(result));
        return Constants.ReturnValues.RETURN_OK;
    }

    public int inSaveReversal(){
        TransactionDetails.trxType=Constants.TransType.ALIPAY_UPLOAD;
        TransactionDetails.inFinalLength = inFCreatePacket(byRequestData, Constants.TransType
                .REVERSAL,locontext);
        String result;
        result = "";
        for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
            result = result + String.format("%02x", byRequestData[k]);
        }
        Log.i(TAG,"\nSendings:");
        Log.i(TAG,result);
        KeyValueDB.setReversal(locontext,new String(result));
        return Constants.ReturnValues.RETURN_OK;
    }

    public int BatchTranfer(Activity context){

        BatchModel batchModeldata;
        byte[] FinalData = new byte[1512];
        RemoteHost remoteHost = new RemoteHost();
        List<BatchModel> batchModelList= GreenDaoSupport.getBatchModelOBJList(context);//databaseObj.getBatchData(hostData.getHDT_HOST_ID());

        for(int i=0;i<batchModelList.size();i++) {
            batchModeldata = batchModelList.get(i);
            if( !(batchModeldata ==null || batchModeldata.equals(""))) {
                TransactionDetails.trxType = Integer.parseInt(batchModeldata.getTrans_type());
                inVoided = Integer.parseInt(batchModeldata.getVoided());
                TransactionDetails.inOritrxType = Integer.parseInt(batchModeldata.getTrans_type());
                TransactionDetails.inGTrxMode = Integer.parseInt(batchModeldata.getTrans_mode());
                TransactionDetails.processingcode = batchModeldata.getProc_code();
                TransactionDetails.trxAmount = batchModeldata.getAmount();
                TransactionDetails.tipAmount = batchModeldata.getTip_amount();
                TransactionDetails.trxDateTime = batchModeldata.getYear();
                TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getDate();
                TransactionDetails.trxDateTime = TransactionDetails.trxDateTime + batchModeldata.getTime();
                TransactionDetails.messagetype = batchModeldata.getOrg_mess_id();
//        batchModeldata.getSYS_TRACE_NUM(payServices.pGetSystemTrace(databaseObj));
                TransactionDetails.ExpDate = batchModeldata.getDate_exp();
                TransactionDetails.RetrievalRefNumber = batchModeldata.getRetr_ref_num();
                TransactionDetails.chApprovalCode = batchModeldata.getAuth_id_resp();
                TransactionDetails.ResponseCode = batchModeldata.getResp_code();
                TransactionDetails.PAN = batchModeldata.getAcct_number();
                TransactionDetails.PersonName = batchModeldata.getPerson_name();
                TransactionDetails.trxAmount = batchModeldata.getOriginal_amount();
                TransactionDetails.responseMessge = batchModeldata.getAdditional_data();
                //batchModeldata.getPAYMENT_TERM_INFO(res.getString(res.getColumnIndex(DBStaticField.PAYMENT_TERM_INFO)));
                TransactionDetails.PAN = batchModeldata.getPri_acct_num();
                TransactionDetails.POSEntryMode = batchModeldata.getPos_ent_mode();
                TransactionDetails.NII = batchModeldata.getNII();
                TransactionDetails.POS_COND_CODE = batchModeldata.getPos_cond_code();

                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
                Date date = new Date();
                String stDate = dateFormat.format(date);
                TransactionDetails.trxDateTime=stDate;

                TransactionDetails.trxType=Constants.TransType.BATCH_TRANSFER;
                inError = inHostConnect();

                if (inError != Constants.ReturnValues.RETURN_OK) {
                    return inError;
                }

            }
        }
        return Constants.ReturnValues.RETURN_OK;
    }
}

/*Subscription rx =
                TcpClient.newClient(IP_Port.substring(0, indexOffset), Integer.parseInt(IP_Port.substring(indexOffset + 1)))
                        .channelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15 * 1000)
                        .channelOption(ChannelOption.AUTO_READ, false)
                        .readTimeOut(Integer.parseInt(httModel.getCONNECTION_TIMEOUT()), TimeUnit.SECONDS)
                        .createConnectionRequest()


                        .flatMap(connection -> connection.writeBytes(Observable.just(byRequestData)).cast(ByteBuf.class)
                                .concatWith(connection.getInput()))

                        .take(1)
                        .map(byteBuf -> byteBuf.toString(Charset.defaultCharset()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {

                            @Override
                            public void onCompleted() {
                                //logger.info("connection coplete");
                                Log.i("tcp", "COMPLETED");
                            }

                            @Override
                            public void onError(Throwable e) {
                                //logger.error(e.getMessage(), e);
                                //close();
                                Log.i("tcp", "ON ERROR" + e.getMessage());
                            }

                            @Override
                            public void onNext(String str) {

                                byResponseData = str.getBytes();
                                //logger.info("connect server : {}", connection);

                                //futureConnection.setConnection(connection);
                                //future.run();
                                // Log.i("tcp", "received form server:" + str);
                                KeyValueDB.removeReversal(locontext);
                            }
                        });*/
