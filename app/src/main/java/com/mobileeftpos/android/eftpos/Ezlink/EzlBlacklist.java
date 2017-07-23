package com.mobileeftpos.android.eftpos.Ezlink;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.async.AsyncTaskRequestResponse;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

public class EzlBlacklist extends AppCompatActivity {

    private Context context;
    private AsyncTaskRequestResponse ASTask = new AsyncTaskRequestResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ezl_blacklist);
        context = EzlBlacklist.this;
        DownloadBlacklist();
    }

    private void DownloadBlacklist() {
        //Parameter if ..else
        TransactionDetails.vdCleanFields();
        if (AppUtil.isNetworkAvailable(context)) {
            TransactionDetails.trxType = Constants.TransType.BLACKLIST_FIRST_DOWNLOAD;
            TransactionDetails.inOritrxType = Constants.TransType.BLACKLIST_FIRST_DOWNLOAD;
            ASTask.AsyncTaskCreation(context);
        }else {
            Toast.makeText(context, "BLACKLIST :NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

    }
}
