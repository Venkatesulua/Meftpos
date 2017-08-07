package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobileeftpos.android.eftpos.Ezlink.CepasPayment;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.async.ServiceCall;
import com.mobileeftpos.android.eftpos.scan.SunmiScanner;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.Const;
import com.mobileeftpos.android.eftpos.utils.EFTPOSLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prathap on 4/23/17.
 */

public class EziWalletActivity extends Activity {

private Button submitBtn;
    public static String amount;
    private EditText payet,merchantId,storeId,orderNo,desc;
    private String TAG=EziWalletActivity.class.getSimpleName();
    public static int EZiWallet_CONSTANT=111;
    private Context context;
    private String scannedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_eziwallet);
        context=EziWalletActivity.this;
        submitBtn=(Button)findViewById(R.id.eziwallet_submitBtn);
        payet=(EditText)findViewById(R.id.eziwallet_payAmount);
        merchantId=(EditText)findViewById(R.id.eziwallet_merchantid);
        storeId=(EditText)findViewById(R.id.eziwallet_storeid);
        orderNo=(EditText)findViewById(R.id.eziwallet_orderno);
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

    private JSONObject buildJSONRequest() {
        try {
            JSONObject object = new JSONObject();
            object.put("thirdOrderNo",orderNo.getText().toString());
            object.put("storeId", storeId.getText().toString());
            object.put("amount", payet.getText().toString());
            object.put("merchantId", merchantId.getText().toString());
            object.put("desc", desc.getText().toString());
            object.put("qrcode", scannedCode);
            object.put("sign", "testSign");
            return object;
        } catch (Exception e) {
            Log.d(TAG, e + "");
            return null;
        }
    }


    private void processPayment() {

            ServiceCall serv = new ServiceCall(context) {
                @Override
                public void onResp(JSONObject response) {
                    Log.d("Response:::", response.toString());
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();

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
                serv.makeJsonObjPOSTReq(Const.URL_BASE_FINAL + Const.METHOD_DEDUCT, buildJSONRequest());
            } else {
                AppUtil.showDialogAlert(context, " " + Const.NO_INTERNET);
                serv.hideProgressDialog();
            }

        }

        private boolean validateDetails(){

            if(orderNo.getText().toString().length()>0 &&  storeId.getText().toString().length()>0 && payet.getText().toString().length()>0 && merchantId.getText().toString().length()>0 && desc.getText().toString().length()>0){
                return true;
            }else {
                return false;
            }
        }



}
