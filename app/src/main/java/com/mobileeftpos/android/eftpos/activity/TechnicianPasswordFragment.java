package com.mobileeftpos.android.eftpos.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;


/**
 * Created by Prathap on 4/26/17.
 */
public class TechnicianPasswordFragment extends Fragment {

    EditText alertinputField;
     ImageView closeBtn;

    public TechnicianPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.password_custom_layout, container, false);
        alertinputField = (EditText) view.findViewById(R.id.pwdalert_et);
        Button alertButton = (Button) view.findViewById(R.id.pwdalert_btn);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alertinputField.getText().toString()!=null && alertinputField.getText().toString().length()>0){

                    if(alertinputField.getText().toString().equals("745231")){

                        startActivity(new Intent(getActivity(),TecnicianMenuActivity.class));
                    }else{
                        Toast.makeText(getActivity(),"Please enter valid password",Toast.LENGTH_SHORT).show();
                     }

                }else{
                    alertinputField.setError("Field cannot be empty!");
                }
            }
        });



        return view;
    }








}
