package com.mobileeftpos.android.eftpos.EziWallet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.async.ServiceCall;
import com.mobileeftpos.android.eftpos.scan.SunmiScanner;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.Const;
import com.mobileeftpos.android.eftpos.utils.EFTPOSLog;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mobileeftpos.android.eftpos.activity.AlipayActivity.REQUEST_ID_MULTIPLE_PERMISSIONS;

/**
 * Created by Prathap on 4/23/17.
 */

public class EziWalletActivity extends Activity implements View.OnClickListener{

private Button refundBtn,deductBtn,orderBtn;
     private String TAG=EziWalletActivity.class.getSimpleName();
     private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eziwallet_home_activity);
        context=EziWalletActivity.this;
        checkAndRequestPermissions();
        orderBtn=(Button)findViewById(R.id.eziwallet_order);
        deductBtn=(Button)findViewById(R.id.eziwallet_deduct);
        refundBtn=(Button)findViewById(R.id.eziwallet_refund);
        orderBtn.setOnClickListener(this);
        deductBtn.setOnClickListener(this);
        refundBtn.setOnClickListener(this);
        TextView backBtn=(TextView)findViewById(R.id.eziwallet_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
     }

     private void startIntent(String transactionType){

         Intent intent =new Intent(EziWalletActivity.this, EziWalletOrderActivity.class);
         intent.putExtra("TxnType",transactionType);
         startActivity(intent);

     }

    private  boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.eziwallet_deduct:
                startIntent("Deduct");
                break;
            case R.id.eziwallet_refund:
                startIntent("Refund");
                break;
            case R.id.eziwallet_order:
                startIntent("Order");

                break;
        }
    }
}
