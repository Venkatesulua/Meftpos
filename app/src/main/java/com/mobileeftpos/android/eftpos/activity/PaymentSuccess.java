package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;

public class PaymentSuccess extends AppCompatActivity {

      private Button shareBtnAction, gotoHomeAction,printCopy;
      private DBHelper databaseObj;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
         databaseObj = new DBHelper(PaymentSuccess.this);

        shareBtnAction=(Button)findViewById(R.id.sharebtn);
        shareBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareIt();
            }
        });
        gotoHomeAction=(Button)findViewById(R.id.complete);
        gotoHomeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSuccess.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
         printCopy=(Button)findViewById(R.id.customercopy);
         printCopy.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 PrintReceipt printReceipt = new PrintReceipt();
                 printReceipt.inPrintReceipt(databaseObj);
                 Intent intent = new Intent(PaymentSuccess.this, HomeActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }
         });

    }

    private void shareIt() {
         Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Transaction Details");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Transaction No :"+ TransactionDetails.chApprovalCode +"and Transaction Amount: $ "+TransactionDetails.trxAmount);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
