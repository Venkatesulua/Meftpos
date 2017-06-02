package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobileeftpos.android.eftpos.R;

public class PaymentSuccess extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    private static final int TIME_OUT = 5000;
    private final String TAG = "my_custom_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PaymentSuccess.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };

        mHandler.postDelayed(mRunnable, TIME_OUT);
    }
}
