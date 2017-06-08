package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mobileeftpos.android.eftpos.R;

/**
 * Created by Prathap on 6/4/17.
 */

public class TecnicianMenuActivity extends Activity implements View.OnClickListener{
    
    private LinearLayout reversalBtn, batchBtn, printConfigBtn, mmsBtn, secureBtn, KeyManagementBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_three);

        reversalBtn = (LinearLayout) findViewById(R.id.clrreverseItem);
        batchBtn = (LinearLayout) findViewById(R.id.clrbatchItem);
        printConfigBtn = (LinearLayout) findViewById(R.id.printconfigitem);
        mmsBtn = (LinearLayout) findViewById(R.id.mmsdownloaditem);
        secureBtn = (LinearLayout) findViewById(R.id.secureeditoritem);
        KeyManagementBtn = (LinearLayout) findViewById(R.id.keyManagmentitem);
        reversalBtn.setOnClickListener(this);
        batchBtn.setOnClickListener(this);
        printConfigBtn.setOnClickListener(this);
        mmsBtn.setOnClickListener(this);
        secureBtn.setOnClickListener(this);
        KeyManagementBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clrreverseItem:

                break;

            case R.id.clrbatchItem:
                break;

            case R.id.printconfigitem:
                break;

            case R.id.mmsdownloaditem:
                startActivity(new Intent(TecnicianMenuActivity.this, AdminActivity.class));

                break;

            case R.id.secureeditoritem:
                break;

            case R.id.keyManagmentitem:
                break;



        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TecnicianMenuActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();    }
}
