package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;

public class PaymentFailure extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    private static final int TIME_OUT = 5000;
    private final String TAG = "my_custom_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txTextView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failure);
        txTextView = (TextView) findViewById(R.id.FailurStatus);
        try {
            if (TransactionDetails.responseMessge == null || TransactionDetails.responseMessge.isEmpty()) {

            } else {
                txTextView.setText(TransactionDetails.responseMessge);
            }
            TransactionDetails.responseMessge = "";
        }
        catch(Exception e){
            Log.i(TAG,"Exception_PaymentFailure "+e.getMessage());
        }
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PaymentFailure.this, HomePagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };

        mHandler.postDelayed(mRunnable, TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
