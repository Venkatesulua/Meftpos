package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;

import java.text.DecimalFormat;

/**
 * Created by Prathap on 4/23/17.
 */

public class SaleActivity extends Activity {

private Button submitBtn;
    public static String amount;
    private EditText payet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activty_sale);

        submitBtn=(Button)findViewById(R.id.submitBtn);
        payet=(EditText)findViewById(R.id.payAmount);


        TextView backBtn=(TextView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payet.getText().toString().length()>0) {
                    amount = payet.getText().toString();
                    amount =amount.replace("$","");
                    amount =amount.replace(".","");
                    TransactionDetails.trxAmount = amount;
                    if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE)
                        startActivity(new Intent(SaleActivity.this, AlipayActivity.class));
                    //startActivity(new Intent(SaleActivity.this, ScannerActivity.class));
                    //startActivity(new Intent(SaleActivity.this,FullScannerActivity.class));
                }else{

                    Toast.makeText(SaleActivity.this,"Please Enter Sale Amount", Toast.LENGTH_LONG).show();

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

}
