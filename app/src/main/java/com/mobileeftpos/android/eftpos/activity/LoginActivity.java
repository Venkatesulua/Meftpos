package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SplashScreen.SplashActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        submitBtn.setOnClickListener(new ClickLIstener());
        txtForgotPassword.setOnClickListener(new ClickLIstener());
        signup.setOnClickListener(new ClickLIstener());

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

            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
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






}
