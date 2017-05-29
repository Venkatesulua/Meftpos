package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobileeftpos.android.eftpos.R;


/**
 * Created by Prathap on 4/26/17.
 */
public class ThirdFragment extends Fragment implements View.OnClickListener{

    private LinearLayout reversalBtn, batchBtn, printConfigBtn, mmsBtn, secureBtn, KeyManagementBtn;

    public ThirdFragment() {
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
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        reversalBtn = (LinearLayout) view.findViewById(R.id.clrreverseItem);
        batchBtn = (LinearLayout) view.findViewById(R.id.clrbatchItem);
        printConfigBtn = (LinearLayout) view.findViewById(R.id.printconfigitem);
        mmsBtn = (LinearLayout) view.findViewById(R.id.mmsdownloaditem);
        secureBtn = (LinearLayout) view.findViewById(R.id.secureeditoritem);
        KeyManagementBtn = (LinearLayout) view.findViewById(R.id.keyManagmentitem);
        reversalBtn.setOnClickListener(this);
        batchBtn.setOnClickListener(this);
        printConfigBtn.setOnClickListener(this);
        mmsBtn.setOnClickListener(this);
        secureBtn.setOnClickListener(this);
        KeyManagementBtn.setOnClickListener(this);
        return view;
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
                break;

            case R.id.secureeditoritem:
                break;

            case R.id.keyManagmentitem:
                break;



        }
    }
}
