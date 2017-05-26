package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.model.TransactionDetails;

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
                    startActivity(new Intent(SaleActivity.this, TransactionType.class));
                    //startActivity(new Intent(SaleActivity.this, ScannerActivity.class));
                    //startActivity(new Intent(SaleActivity.this,FullScannerActivity.class));
                }else{

                    Toast.makeText(SaleActivity.this,"Please Enter Sale Amount", Toast.LENGTH_LONG).show();

                }
            }
        });
     }

}
