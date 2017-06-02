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
public class TwoFragment extends Fragment implements View.OnClickListener{

    private LinearLayout reprintBtn, reportBtn, reviewBtn, logonBtn, blacklistBtn, cepasBtn;

    public TwoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        reprintBtn = (LinearLayout) view.findViewById(R.id.reprintItem);
        reportBtn = (LinearLayout) view.findViewById(R.id.reportItem);
        reviewBtn = (LinearLayout) view.findViewById(R.id.reviewItem);
        logonBtn = (LinearLayout) view.findViewById(R.id.logonItem);
        blacklistBtn = (LinearLayout) view.findViewById(R.id.blacklistItem);
        cepasBtn = (LinearLayout) view.findViewById(R.id.balanceItem);
        reprintBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        logonBtn.setOnClickListener(this);
        blacklistBtn.setOnClickListener(this);
        cepasBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reprintItem:

                break;

            case R.id.reportItem:
                break;

            case R.id.reviewItem:
                break;

            case R.id.logonItem:
                break;

            case R.id.blacklistItem:
                break;

            case R.id.balanceItem:
                break;



        }
    }
}
