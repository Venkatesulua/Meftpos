package com.mobileeftpos.android.eftpos.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;

import java.util.ArrayList;
import java.util.List;


import sunmi.paylib.SunmiPayKernel;


/**
 * Created by Prathap on 4/26/17.
 */

public class ViewPagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.receipt,
            R.drawable.historyicon,
            R.drawable.user_settings
    };
    public static Context context;
    private SunmiPayKernel mSunmiPayKernel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);
        context=ViewPagerActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;


        TransactionDetails.deviceId = "" + tm.getDeviceId();
        conn();
    }

    private void conn() {
        mSunmiPayKernel = SunmiPayKernel.getInstance();
        mSunmiPayKernel.connectPayService(getApplicationContext(), mConnCallback);
    }

    private SunmiPayKernel.ConnCallback mConnCallback = new SunmiPayKernel.ConnCallback() {
        @Override
        public void onServiceConnected() {
            try {
                MyApplication.mPinPadOpt = mSunmiPayKernel.mPinPadOpt;
                MyApplication.mBasicOpt = mSunmiPayKernel.mBasicOpt;
                MyApplication.mReadCardOpt = mSunmiPayKernel.mReadCardOpt;
                MyApplication.mEMVOpt = mSunmiPayKernel.mEMVOpt;
                //存储KEK,TMK,PIK,MAK,TDK,各种默认的密钥
                //MyApplication.initSecretKey();
                //加载默认aid和capk
                //MyApplication.loadaidcapk();
                //MyApplication.mReadCardOpt.cancelCheckCard();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(),"");
        adapter.addFrag(new ReceiptMenuFragment(), "");
        adapter.addFrag(new TechnicianPasswordFragment(), "");
        adapter.addFrag(new SettingsFragment(), "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
         new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewPagerActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
