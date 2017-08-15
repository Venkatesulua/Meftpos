package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;

/**
 * Created by Prathap on 4/26/17.
 */

public class ChangePasswordActivity extends Activity {
    TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
         backButton=(TextView) findViewById(R.id.backBtn);
         backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
