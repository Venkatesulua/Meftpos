package com.mobileeftpos.android.eftpos.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mobileeftpos.android.eftpos.R.id.btn_create_an_account;
import static com.mobileeftpos.android.eftpos.R.id.btn_submit;
import static com.mobileeftpos.android.eftpos.R.id.edt_username;
import static com.mobileeftpos.android.eftpos.R.id.txt_forgotpassword;

/**
 * Created by Prathap on 4/20/17.
 */

public class LoginActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 111;
    @BindView(edt_username)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(txt_forgotpassword)
    TextView txtForgotPassword;
    @BindView(btn_submit)
    Button submitBtn;
    @BindView(btn_create_an_account)
    TextView signup;
    private boolean isValid = false;

    private final String TAG = "my_custom_msg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login_layout);
        init();
        checkPermission();
        submitBtn.setOnClickListener(new ClickLIstener());
        txtForgotPassword.setOnClickListener(new ClickLIstener());
        signup.setOnClickListener(new ClickLIstener());


    }

    private void init(){

        edtUserName=(EditText)findViewById(R.id.edt_username);
        edtPassword=(EditText)findViewById(R.id.edt_password);
        txtForgotPassword=(TextView) findViewById(R.id.txt_forgotpassword);
        signup=(TextView) findViewById(R.id.btn_create_an_account);
        submitBtn=(Button) findViewById(R.id.btn_submit);

    }

    private class ClickLIstener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == submitBtn) {

                processLogin();

            } else if (view == txtForgotPassword) {
                Intent i = new Intent(LoginActivity.this, ForgotScreenActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
            else if (view == signup) {


                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        }
    }


    private void processLogin() {

        if (validateData()) {

            String userNameStr=edtUserName.getText().toString();
            String passWordStr=edtPassword.getText().toString();

            if(userNameStr.equalsIgnoreCase("admin") && passWordStr.equals("admin")) {

                final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);


                Log.i(TAG,"LogOn SCREEN");
                final String tmDevice, tmSerial, androidId;
                TransactionDetails.deviceId = "" + tm.getDeviceId();
                Log.i(TAG,"TransactionDetails.deviceId::"+TransactionDetails.deviceId);
                /*tmSerial = "" + tm.getSimSerialNumber();
                Log.i(TAG,"tmSerial::"+tmSerial);
                androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                Log.i(TAG,"androidId::"+androidId);

                UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
                TransactionDetails.deviceId = deviceUuid.toString();
                Log.i(TAG,"TransactionDetails.deviceId::"+TransactionDetails.deviceId);*/

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                SharedPreferenceStore
                        .setEncryptedSharedPref(
                                SharedPreferenceStore.KEY_LOGIN_STATUS,
                                true + "");
            }else {
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        LoginActivity.this).create();
                alertDialog.setTitle("Login Failed");
                alertDialog.setMessage("Please enter valid credentials");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        }

    }

    private boolean validateData() {

        if ((edtPassword.getText().toString() != null && edtPassword.getText().toString().length() == 0)) {

            edtPassword.setError("Please Enter Password");
            edtPassword.requestFocus();
            isValid = false;
        } else {
            isValid = true;
        }

        if ((edtUserName.getText().toString() != null && edtUserName.getText().toString().length() == 0)) {

            edtUserName.setError("Please Enter UserID");
            edtUserName.requestFocus();
            isValid = false;
        } else {
            isValid = true;

        }

        return isValid;
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



}
