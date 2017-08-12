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

public class EziWalletActivity extends Activity {

private Button submitBtn;
    public static String amount;
    private EditText payet,merchantId,storeId,terminalID,desc;
    private String TAG=EziWalletActivity.class.getSimpleName();
    public static int EZiWallet_CONSTANT=111;
    private Context context;
    private String scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_eziwallet);
        context=EziWalletActivity.this;
        checkAndRequestPermissions();
        submitBtn=(Button)findViewById(R.id.eziwallet_submitBtn);
        payet=(EditText)findViewById(R.id.eziwallet_payAmount);
        merchantId=(EditText)findViewById(R.id.eziwallet_merchantid);
        storeId=(EditText)findViewById(R.id.eziwallet_storeid);
        terminalID=(EditText)findViewById(R.id.terminal_id);
        desc=(EditText)findViewById(R.id.eziwallet_desc);

        TextView backBtn=(TextView)findViewById(R.id.eziwallet_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateDetails()) {

                    Intent i = new Intent(EziWalletActivity.this, SunmiScanner.class);
                    i.putExtra("FromEziwalletActivity", true);
                    startActivityForResult(i, EZiWallet_CONSTANT);

                }
                else{
                    Toast.makeText(EziWalletActivity.this,"Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        payet.setRawInputType(Configuration.KEYBOARD_12KEY);
        payet.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$"))
                {
                    String userInput= ""+s.toString().replaceAll("[^\\d]", "");
                    StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                    while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                        cashAmountBuilder.deleteCharAt(0);
                    }
                    while (cashAmountBuilder.length() < 3) {
                        cashAmountBuilder.insert(0, '0');
                    }
                    cashAmountBuilder.insert(cashAmountBuilder.length()-2, '.');
                    cashAmountBuilder.insert(0, '$');

                    payet.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(payet.getText(), cashAmountBuilder.toString().length());

                }

            }
        });
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==EZiWallet_CONSTANT && intent!=null){
            String contents = intent.getStringExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY);
            scannedCode =contents;
            processPayment();
        } else {
            EFTPOSLog.d(TAG,"AlipayActivity::onActivityResult_3");
            Toast.makeText(getApplicationContext(), "QRCODE READ FAILED", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private  String getConstrunctedObject() {
         String thirdOrderNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
        System.out.println("thirdOrderNo is " + thirdOrderNo);
        DeductRequestParam deductRequestParam = new DeductRequestParam();
        deductRequestParam.setThirdOrderNo(thirdOrderNo); //The order no in your system
        deductRequestParam.setStoreId(Long.parseLong(storeId.getText().toString())); //store id which is from Ezi,(Fixed value)
        deductRequestParam.setMerchantId(merchantId.getText().toString()); //merchant id which is from Ezi,(Fixed value)
        deductRequestParam.setTerminalId(terminalID.getText().toString()); //Terminal Id of your device
        deductRequestParam.setAmount(Long.parseLong(payet.getText().toString())); //pay amount, without decimal, 188 means $1.88
        deductRequestParam.setQrcode(scannedCode); //QR Code from Ezi wallet
        deductRequestParam.setDesc(desc.getText().toString()); //description of this transaction, optional

        //1.get content for sign
        String signContent = null;
        try {
            signContent = RSAUtil.getSignContent(deductRequestParam);
        } catch (IllegalAccessException e) {
            return "failed to get sign content";
        }

        //2.sign the request parameters by private key
        String signStr = null;
        try {
            signStr = RSAUtil.sign(signContent, Const.privateKey);
        } catch (Exception e) {
            return "failed to sign param";
        }

        deductRequestParam.setSign(signStr);

        //3.call the api to deduct
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(deductRequestParam);

            return jsonStr;

        } catch (Exception e) {

        }
        return null;
    }

    private void clearFields(){
        submitBtn.setText("");
        payet.setText("");
        merchantId.setText("");
        storeId.setText("");
        terminalID.setText("");
        desc.setText("");

    }


    private void processPayment() {

            ServiceCall serv = new ServiceCall(context) {
                @Override
                public void onResp(JSONObject response) {
                    Log.d("Response:::", response.toString());
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    clearFields();
                    try {
                        String status = response.getJSONObject("response").getString("status");
                        String message = response.getJSONObject("response").getString("message");

                        Log.i("status : ", status);
                        if (status.equalsIgnoreCase("Success")) {
                            Toast.makeText(context, "Payment Done Successfully.", Toast.LENGTH_LONG).show();
                            finish();

                        } else {

                            AppUtil.showDialogAlert(context, " " + message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();

                }
            };

            if (AppUtil.isNetworkAvailable(context)) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(getConstrunctedObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serv.makeJsonObjPOSTReq(Const.URL_BASE_FINAL + Const.METHOD_DEDUCT, obj);
            } else {
                AppUtil.showDialogAlert(context, " " + Const.NO_INTERNET);
                serv.hideProgressDialog();
            }

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
        private boolean validateDetails(){

            if(terminalID.getText().toString().length()>0 &&  storeId.getText().toString().length()>0 && payet.getText().toString().length()>0 && merchantId.getText().toString().length()>0 && desc.getText().toString().length()>0){
                return true;
            }else {
                return false;
            }
        }



}
