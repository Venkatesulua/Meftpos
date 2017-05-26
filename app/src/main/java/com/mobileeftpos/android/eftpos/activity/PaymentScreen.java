package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.mobileeftpos.android.eftpos.R;

/**
 * Created by Prathap on 5/24/17.
 */

public class PaymentScreen extends Activity {

    private String barCodestr, payableAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaymentlayout);
        barCodestr=getIntent().getStringExtra("barCode");
        payableAmt=getIntent().getStringExtra("payableAmt");


    }

}
