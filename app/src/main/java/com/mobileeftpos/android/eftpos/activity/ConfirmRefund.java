package com.mobileeftpos.android.eftpos.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.async.AsyncTaskRequestResponse;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class ConfirmRefund extends AppCompatActivity {

    private Button BTConfirmBtn;
    private Button BTCancelBtn;
    private TextView TVRefAmount;
    private TextView TVOriAmount;
    private TextView TVPartnetTransactionID;


    private AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse();
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_refund);

        context=ConfirmRefund.this;

        BTConfirmBtn=(Button)findViewById(R.id.ConfirmBtn);
        BTCancelBtn=(Button)findViewById(R.id.CancelBtn);
        TVRefAmount=(TextView) findViewById(R.id.ConfirmRefAmount);
        TVOriAmount=(TextView)findViewById(R.id.ConfirmOriAmount);
        TVPartnetTransactionID=(TextView)findViewById(R.id.ConfirmPartnerTransID);

        String Temp = TransactionDetails.trxAmount;
        long dubValue = Long.parseLong(Temp);
        Temp = "$"+ String.format("%01d",(dubValue/100)) + "." + String.format("%02d",(dubValue%100));
        TVRefAmount.setText(Temp);

        Temp = TransactionDetails.stOriAmount;
        dubValue = Long.parseLong(Temp);
        Temp = "$"+ String.format("%01d",(dubValue/100)) + "." + String.format("%02d",(dubValue%100));
        TVOriAmount.setText(Temp);

        TVPartnetTransactionID.setText(TransactionDetails.RetrievalRefNumber);

        TextView backBtn=(TextView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BTConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.isNetworkAvailable(ConfirmRefund.this)) {
                    TransactionDetails.trxType = Constants.TransType.ALIPAY_REFUND;
                    TransactionDetails.inOritrxType = Constants.TransType.ALIPAY_REFUND;
                    ASTask.AsyncTaskCreation(context);
                }else {
                    Toast.makeText(ConfirmRefund.this, "REFUND CONNECTION ISSUE",Toast.LENGTH_SHORT).show();
                }
            }
        });

        BTCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmRefund.this, HomeActivity.class));
            }
        });


    }
}
