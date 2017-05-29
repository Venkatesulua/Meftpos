package com.mobileeftpos.android.eftpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobileeftpos.android.eftpos.R;

import butterknife.OnClick;


public class OneFragment extends Fragment implements View.OnClickListener {

    private LinearLayout saleBtn, settelmentBtn, voidBtn, preauthBtn, adjustBtn, offlineBtn, cashAdvBtn, ezlinkBtn, cepasBtn;

    public OneFragment() {
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
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        saleBtn = (LinearLayout) view.findViewById(R.id.saleItem);
        settelmentBtn = (LinearLayout) view.findViewById(R.id.settelmentItem);
        voidBtn = (LinearLayout) view.findViewById(R.id.voidItem);
        preauthBtn = (LinearLayout) view.findViewById(R.id.preauthItem);
        adjustBtn = (LinearLayout) view.findViewById(R.id.adjustItem);
        offlineBtn = (LinearLayout) view.findViewById(R.id.offlineItem);
        cashAdvBtn = (LinearLayout) view.findViewById(R.id.cashadvItem);
        ezlinkBtn = (LinearLayout) view.findViewById(R.id.topupItem);
        cepasBtn = (LinearLayout) view.findViewById(R.id.cepasItem);
        saleBtn.setOnClickListener(this);
        settelmentBtn.setOnClickListener(this);
        voidBtn.setOnClickListener(this);
        preauthBtn.setOnClickListener(this);
        adjustBtn.setOnClickListener(this);
        offlineBtn.setOnClickListener(this);
        cashAdvBtn.setOnClickListener(this);
        ezlinkBtn.setOnClickListener(this);
        cepasBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saleItem:
                startActivity(new Intent(HomeActivity.context, SaleActivity.class));
                break;

            case R.id.settelmentItem:
                break;

            case R.id.voidItem:
                break;

            case R.id.preauthItem:
                break;

            case R.id.adjustItem:
                break;

            case R.id.offlineItem:
                break;

            case R.id.cashadvItem:
                break;

            case R.id.topupItem:
                break;

            case R.id.cepasItem:
                break;


        }
    }
}
