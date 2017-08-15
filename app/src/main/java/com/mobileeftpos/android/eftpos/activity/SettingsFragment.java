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
public class SettingsFragment extends Fragment {

    private Button changePwd,logoutBtn;
    private static final int REQUEST_INTERNET = 200;

    public SettingsFragment() {
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

        changePwd=(Button)view.findViewById(R.id.settings_changepassword);
        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getActivity(),ChangePasswordActivity.class));
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
