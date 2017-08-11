package com.mobileeftpos.android.eftpos.SupportClasses;

import android.util.Log;

import org.jpos.iso.ISOException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by venkat on 6/1/2017.
 */
public class RemoteHost {

    private final String TAG = "my_custom_msg";
    private Socket smtpSocket = null;

    public int inConnection(String ServerIp,String Port) {
        Log.i(TAG, "Connection");
        Log.i(TAG, ServerIp);
        Log.i(TAG, Port);
        int connection_time_out=0;
        try {

            if(TransactionDetails.ConnectionTimeout != null && TransactionDetails.ConnectionTimeout.isEmpty() && TransactionDetails.ConnectionTimeout=="")
                connection_time_out = ((Integer.parseInt(TransactionDetails.ConnectionTimeout )*1000));
            else
                connection_time_out = (45000);
            smtpSocket=new Socket();
            smtpSocket.connect(new InetSocketAddress(ServerIp,Integer.parseInt(Port)),connection_time_out);

        } catch (UnknownHostException e) {
            Log.i(TAG, "UnknownHostException");
            Log.i(TAG, "Don't know about host: hostname");
            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return Constants.ReturnValues.RETURN_CONNECTION_ERROR;
        }
        return Constants.ReturnValues.RETURN_OK;
    }

    public byte[] inSendRecvPacket(byte[] FinalData,int nFinalLength) {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = smtpSocket.getOutputStream();
            is = smtpSocket.getInputStream();

            String result;
            result = "";
            for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                result = result + String.format("%02x", FinalData[k]);
            }
            Log.i(TAG,"\nSendings:");
            Log.i(TAG,result);

            if (smtpSocket != null && os != null && is != null) {


                os.write(FinalData, 0, TransactionDetails.inFinalLength);
                FinalData = new byte[1512];
                byte[] recvData = new byte[1512];
                TransactionDetails.inFinalLength = 0;
                //long timeNow = System.currentTimeMillis();

                if(TransactionDetails.ConnectionTimeout != null && TransactionDetails.ConnectionTimeout.isEmpty() && TransactionDetails.ConnectionTimeout=="")
                    smtpSocket.setSoTimeout ((Integer.parseInt(TransactionDetails.ConnectionTimeout )*1000));
                else
                    smtpSocket.setSoTimeout(45000);
                //do {
                    TransactionDetails.inFinalLength = is.read(FinalData, 0, 2);
                    TransactionDetails.inFinalLength = is.read(FinalData, 0, 5);
                    TransactionDetails.inFinalLength = is.read(FinalData);
                //} while (TransactionDetails.inFinalLength <= 0);

                if(TransactionDetails.inFinalLength <= 0)
                    return null;
                //TransactionDetails.inFinalLength = recvData[0] *256;
                //TransactionDetails.inFinalLength = TransactionDetails.inFinalLength + (recvData[1]);
                //TransactionDetails.inFinalLength = TransactionDetails.inFinalLength -5;
                //for (int k = 0; k < TransactionDetails.inFinalLength-7; k++) {
                    //FinalData[k]  = recvData[k+7];
                //}

                result = "";
                for (int k = 0; k < TransactionDetails.inFinalLength; k++) {
                    result = result + String.format("%02x", FinalData[k]);
                }
                Log.i(TAG,"\nRECEIVED:");
                Log.i(TAG,result);
            }
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        } catch (UnknownHostException e) {
            Log.i(TAG, "Don't know about host: hostname");
            return null;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return null;
        }
        return FinalData;
    }

    public int inDisconnection() {
        try {
            Log.i(TAG,"\nRemoveHost::inDisconnection:");
            if (smtpSocket != null) {
                Log.i(TAG,"\nRemoveHost::inDisconnection_1:");
                if(smtpSocket.isClosed())
                    Log.i(TAG,"\nRemoveHost::inDisconnection_Closed:");
                smtpSocket.close();
                if(smtpSocket.isClosed())
                    Log.i(TAG,"\nRemoveHost::inDisconnection_Closed_2:");
            }
        } catch (UnknownHostException e) {
            Log.i(TAG, "Don't know about host: hostname");
            return Constants.ReturnValues.RETURN_ERROR;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for the connection to: hostname");
            return Constants.ReturnValues.RETURN_ERROR;
        }
        return Constants.ReturnValues.RETURN_OK;
    }
}
