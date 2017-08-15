package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.BluetoothUtil;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;

import java.io.IOException;

/**
 * Created by Prathap on 6/3/17.
 */

public class ReprintMenuActivity extends Activity{

    private Button LastReceipt;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reprintmenu_activity);
        context = ReprintMenuActivity.this;

        LastReceipt = (Button) findViewById(R.id.last_receipt);
        LastReceipt.setOnClickListener(new ClickLIstener());


    }

    private class ClickLIstener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if (view == LastReceipt)
                inPrintLastActivity();
        }
    }
    int inPrintLastActivity()
    {
        String inPrintBuffer = KeyValueDB.getLastReceipt(context);
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            return 1;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            return 1;
        }
        byte[] data = inPrintBuffer.getBytes();
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(data, socket);
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return Constants.ReturnValues.RETURN_OK;
    }
}
