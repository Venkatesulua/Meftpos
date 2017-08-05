package com.mobileeftpos.android.eftpos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.MerchantModel;


public class PaymentSuccess extends Activity {

      private Button shareBtnAction, gotoHomeAction,printCopy;
        private TextView TVHeading, TVContent,TVFooter,TVTitle,TVDisclaimer;
    String Header="";
    String Title="";
    String Content="";
    String Footer="";
    String Disclaimer="";
    CurrencyModel curr = new CurrencyModel();
    String cuLabel;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        daoSession = GreenDaoSupport.getInstance(PaymentSuccess.this);
         TVHeading=(TextView) findViewById(R.id.MerDetails);
        TVTitle=(TextView) findViewById(R.id.title);
         TVContent=(TextView) findViewById(R.id.ContentDetails);
         TVFooter=(TextView) findViewById(R.id.FooterDetails);
        TVDisclaimer=(TextView) findViewById(R.id.disclaimerMsg);

         Header = MakeHeader();
        Title = MakeTitle();
         Content = MakeContent();
         Footer = MakeFooter();
        Disclaimer=MakeDisclaimer();

         TVHeading.setText(Header);
        TVTitle.setText(Title);
         TVContent.setText(Content);
         TVFooter.setText(Footer);
        TVDisclaimer.setText(Disclaimer);

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
                Intent intent = new Intent(PaymentSuccess.this, HomePagerActivity.class);
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
                 printReceipt.inPrintReceipt(daoSession,PaymentSuccess.this);
                 Intent intent = new Intent(PaymentSuccess.this, HomePagerActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }
         });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

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


//        curr = databaseObj.getCurrencyData(TransactionDetails.inGCURR);
        curr = GreenDaoSupport.getCurrencyModelOBJList(PaymentSuccess.this,TransactionDetails.inGCURR+"");

        cuLabel = curr.getCURR_LABEL();

        MerchantModel merchantData =GreenDaoSupport.getMerchantModelOBJ(PaymentSuccess.this);
        //databaseObj.getMerchantData(0);
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

        return hh;
    }
    String MakeTitle(){
        String tt="";
        if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE)
            tt = tt + "EZLINK PAYMENT";
        else if(TransactionDetails.trxType == Constants.TransType.ALIPAY_SALE)
            tt = tt + "ALIPAY SALE";
        else if(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)
            tt = tt + "SETTLEMENT";
        else if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_SALE )
            tt = tt + "VOID";
        else if(TransactionDetails.inOritrxType == Constants.TransType.ALIPAY_REFUND)
            tt = tt + "REFUND";
        return tt ;
    }
    String MakeContent()
    {
        String cc ="";
        HostModel hostData = GreenDaoSupport.getHostTableModelOBJ(PaymentSuccess.this);
        //databaseObj.getHostTableData(TransactionDetails.inGHDT);
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

        cc = cc + "HOST: "+hostData.getHDT_HOST_LABEL()+"\n\n";

        if(TransactionDetails.trxType == Constants.TransType.EZLINK_SALE)
            cc = cc +TransactionDetails.PAN+"\n";
        else if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.ALIPAY_REFUND))
            cc = cc + "BUYER IDENTITY CODE\n"+TransactionDetails.PAN+"\n";

        if(TransactionDetails.trxType != Constants.TransType.EZLINK_SALE)
            cc = cc + TransactionDetails.responseMessge+"\n";
        else
        {
            int previousBal = (TransactionDetails.bOrigBal[0] * 256 * 256) + (TransactionDetails.bOrigBal[1] * 256) + TransactionDetails.bOrigBal[2];
            String stPreAmount = String.format("%01d.%02d", (previousBal / 100), (previousBal % 100));
            cc = cc +"PREVIOUS BALANCE "+cuLabel +" "+ stPreAmount+"\n";

            int newBal = (TransactionDetails.PurseBalance[0] * 256 * 256) + (TransactionDetails.PurseBalance[1] * 256) + TransactionDetails.PurseBalance[2];
            String stPostAmount = String.format("%01d.%02d", (newBal / 100), (newBal % 100));
            cc = cc +"NEW BALANCE      "+cuLabel +" "+ stPostAmount+"\n";
        }

        return cc;
    }
    String MakeFooter(){
        String ff = "";
        if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)) {
            int inAmount = Integer.parseInt(TransactionDetails.trxAmount);
            String stAmount = String.format("%01d.%02d", (inAmount / 100), (inAmount % 100));


            ff = ff + "AMT "+ cuLabel +" "+ stAmount ;
        }
        return ff;

    }
    String MakeDisclaimer(){
        String tt="";
        if(!(TransactionDetails.trxType == Constants.TransType.INIT_SETTLEMENT || TransactionDetails.trxType == Constants.TransType.FINAL_SETTLEMENT)) {
            tt = tt + "I AGREE TO PAY THE ABOVE AMOUNT" + "\n";
            tt = tt + "ACCORDING TO ISSUER AGREEMENT" + "\n";
            tt = tt + "MERCHANT COPY";
        }
        return tt ;
    }
}
