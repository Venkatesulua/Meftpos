package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;

import java.math.BigInteger;

/**
 * Created by venkat on 8/2/2017.
 */

public class EHostConnectivity extends CPacketHandling {

    private Activity locontext;
    public int inConnectSendRecv(String ServerIP, String Port){
        if(remoteHost.inConnection(ServerIP, Port) != Constants.ReturnValues.RETURN_OK)
            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
        //FinalData = UploadData.getBytes();

        if ((byResponseData = remoteHost.inSendRecvPacket(byRequestData,TransactionDetails.inFinalLength)) ==null) {
            Log.i(TAG,"AlipayActivity::REversal Send Failed");
            return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
        }

        if (isoPacket.inProcessPacket(byResponseData,TransactionDetails.inFinalLength) != 0) {
            Log.i(TAG,"AlipayActivity::REversal Receive Failed");
            return Constants.ReturnValues.RETURN_SEND_RECV_FAILED;
        }

        if (remoteHost.inDisconnection() != 0) {
            return Constants.ReturnValues.RETURN_ERROR;
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public int inHostConnect()
    {
        TransactionDetails.inFinalLength = inFCreatePacket(byRequestData, Constants.TransType.ALIPAY_SALE,locontext);
        String IP_Port = GetCommsModel().getCOM_PRIMARY_IP_PORT();
        int indexOffset = IP_Port.indexOf("|");
        if(TransactionDetails.inFinalLength != 0)
        {
            inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
        }

        return Constants.ReturnValues.RETURN_OK;
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

            return connectSendRecv.inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public int inCheckUpload()
    {
        String UploadData = KeyValueDB.getUpload(locontext);

        String IP_Port = GetCommsModel().getCOM_PRIMARY_IP_PORT();
        int indexOffset = IP_Port.indexOf("|");
        if(!UploadData.isEmpty()) {
            EHostConnectivity connectSendRecv = new EHostConnectivity();

            byRequestData = new BigInteger(UploadData, 16).toByteArray();
            TransactionDetails.inFinalLength = byRequestData[0] *256;
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (byRequestData[1]);
            TransactionDetails.inFinalLength = TransactionDetails.inFinalLength +2;
            return connectSendRecv.inConnectSendRecv(IP_Port.substring(0, indexOffset),IP_Port.substring(indexOffset + 1));
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
