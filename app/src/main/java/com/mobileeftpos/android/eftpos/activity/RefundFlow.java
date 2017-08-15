package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;

public class RefundFlow extends AppCompatActivity {

    private Button submitBtn;
    private EditText ETRefAmount;
    private EditText ETOriAmount;
    private EditText ETPartnetTransactionID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_flow);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        ETRefAmount=(EditText)findViewById(R.id.RefAmount);
        ETOriAmount=(EditText)findViewById(R.id.OriAmount);
        ETPartnetTransactionID=(EditText)findViewById(R.id.PartnerTransID);
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
                String stRefAmount;
                String stOriAmount;
                String stPartnerTransID;
                if((ETRefAmount.getText().toString().length()>0) && (ETOriAmount.getText().toString().length()>0) && (ETPartnetTransactionID.getText().toString().length()>0)) {
                    stRefAmount = ETRefAmount.getText().toString();
                    stRefAmount =stRefAmount.replace("$","");
                    stRefAmount =stRefAmount.replace(".","");

                    stOriAmount = ETOriAmount.getText().toString();
                    stOriAmount =stOriAmount.replace("$","");
                    stOriAmount =stOriAmount.replace(".","");

                    stPartnerTransID = ETPartnetTransactionID.getText().toString();
                    stPartnerTransID = stPartnerTransID.replace("$","");
                    stPartnerTransID = stPartnerTransID.replace(".","");

                    TransactionDetails.trxAmount = stRefAmount;
                    TransactionDetails.stOriAmount = stOriAmount;
                    TransactionDetails.RetrievalRefNumber = stPartnerTransID;
                    if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
                        startActivity(new Intent(RefundFlow.this, ConfirmRefund.class));
                }else{

                    Toast.makeText(RefundFlow.this,"Missing Required Fields", Toast.LENGTH_LONG).show();

                }
            }
        });

        ETOriAmount.setRawInputType(Configuration.KEYBOARD_12KEY);
        ETOriAmount.addTextChangedListener(new TextWatcher() {
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

                    ETOriAmount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(ETOriAmount.getText(), cashAmountBuilder.toString().length());

                }

            }
        });

        ETRefAmount.setRawInputType(Configuration.KEYBOARD_12KEY);
        ETRefAmount.addTextChangedListener(new TextWatcher() {
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

                    ETRefAmount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(ETRefAmount.getText(), cashAmountBuilder.toString().length());

                }

            }
        });
    }
}
