package com.mobileeftpos.android.eftpos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;


/**
 * Created by Prathap on 4/26/17.
 */
public class FouthFragment extends Fragment {

    private Button merchantInfo,logoutBtn;
    private static final int REQUEST_INTERNET = 200;

    public FouthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_four, container, false);


        merchantInfo = (Button) view.findViewById(R.id.merchant_info);
        merchantInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
                }else {
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                }
            }
        });

        logoutBtn=(Button)view.findViewById(R.id.settings_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceStore
                        .setEncryptedSharedPref(
                                SharedPreferenceStore.KEY_LOGIN_STATUS,
                                false + "");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
        // Inflate the layout for this fragment

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                    //Show an explanation to the user *asynchronously*
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_INTERNET);
                }else{
                    startActivity(new Intent(getActivity(), AdminActivity.class));

                    //Never ask again and handle your app without permission.
                }
            }
        }
    }

}
