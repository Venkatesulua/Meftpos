package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.KeyValueDB;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.CurrencyModel;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.MerchantModel;

public class PaymentSuccess extends AppCompatActivity {

      private Button shareBtnAction, gotoHomeAction,printCopy;
      private DBHelper databaseObj;
        private TextView TVHeading, TVContent,TVFooter;
    String Header="";
    String Content="";
    String Footer="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
         databaseObj = new DBHelper(PaymentSuccess.this);

         TVHeading=(TextView) findViewById(R.id.MerDetails);
         TVContent=(TextView) findViewById(R.id.ContentDetails);
         TVFooter=(TextView) findViewById(R.id.FooterDetails);

         Header = MakeHeader();
         Content = MakeContent();
         Footer = MakeFooter();

         TVHeading.setText(Header);
         TVContent.setText(Content);
         TVFooter.setText(Footer);

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
                 printReceipt.inPrintReceipt(databaseObj,PaymentSuccess.this);
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
        String disclaimer = "\nI AGREE TO PAY THE ABOVE TOTAL AMOUNT\n";
        disclaimer = disclaimer+ "    ACCORDING TO ISSUER AGREEMENT\n";

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Header +"\n"+ Content+ "\n"+Footer +disclaimer);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    String MakeHeader()
    {
        MerchantModel merchantData = databaseObj.getMerchantData(0);
        String hh ="";
        if(merchantData.getMERCHANT_NAME().length() !=0)
            hh = hh+ merchantData.getMERCHANT_NAME() +"\n";
        if(merchantData.getMERCHANT_HEADER1().length() !=0)
            hh = hh + merchantData.getMERCHANT_HEADER1() +"\n";
        if(merchantData.getMERCHANT_HEADER2().length() !=0)
            hh = hh + merchantData.getMERCHANT_HEADER2() +"\n";
        if(merchantData.getADDRESS_LINE1().length() !=0)
            hh = hh + merchantData.getADDRESS_LINE1() +"\n";
        if(merchantData.getADDRESS_LINE2().length() !=0)
            hh = hh + merchantData.getADDRESS_LINE2() +"\n";
        if(merchantData.getADDRESS_LINE3().length() !=0)
            hh = hh + merchantData.getADDRESS_LINE3() +"\n";
        if(merchantData.getADDRESS_LINE4().length() !=0)
            hh = hh + merchantData.getADDRESS_LINE4() +"\n";
        if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE)
            hh = hh + "ALIPAY SALE";
        else if(TransactionDetails.trxType == Constants.TransType.VOID)
            hh = hh + "VOID";
        else if(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)
            hh = hh + "SETTLEMENT";
        else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND)
            hh = hh + "REFUND";
        return hh;
    }
    String MakeContent()
    {
        String cc ="";
        HostModel hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        String dateTime = "DATE/TIME : " + TransactionDetails.trxDateTime.substring(2,4)+"/"+TransactionDetails.trxDateTime.substring(4,6)+"/"+TransactionDetails.trxDateTime.substring(6,8)+" "+
                TransactionDetails.trxDateTime.substring(8,10) +":"+TransactionDetails.trxDateTime.substring(10,12)+":"+TransactionDetails.trxDateTime.substring(12,14);
        cc = cc + dateTime + "\n";
        String stTemp1 = "MID:" + hostData.getHDT_MERCHANT_ID();
        String stTemp2 = "TID:"+hostData.getHDT_TERMINAL_ID();
        String Temp3 ="";
        for(int i=0; i< (31- stTemp1.length() - stTemp2.length()); i++){
            Temp3 = Temp3+" ";
        }
        cc = cc + stTemp1 + Temp3+  stTemp2 + "\n";

        stTemp1 = "INVOICE:" + TransactionDetails.InvoiceNumber;
        stTemp2 = "BATCH:"+hostData.getHDT_BATCH_NUMBER();
        Temp3 ="";
        for(int i=0; i< (31-stTemp1.length() - stTemp2.length()); i++){
            Temp3 = Temp3+" ";
        }
        cc = cc + stTemp1 + Temp3+  stTemp2+"\n";

        cc = cc + "HOST: "+hostData.getHDT_HOST_LABEL()+"\n";

        if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND))
            cc = cc + "BUYER IDENTITY CODE\n"+TransactionDetails.PAN+"\n";
        cc = cc + TransactionDetails.responseMessge+"\n";

        return cc;
    }
    String MakeFooter(){
        String ff = "";
        if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)) {
            int inAmount = Integer.parseInt(TransactionDetails.trxAmount);
            String stAmount = String.format("%01d.%02d", (inAmount / 100), (inAmount % 100));

            CurrencyModel curr = new CurrencyModel();
            curr = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
            String cuLabel = curr.getCURR_LABEL();
            ff = ff + "AMT "+ cuLabel +" "+ stAmount ;
        }
        return ff;

    }
}
