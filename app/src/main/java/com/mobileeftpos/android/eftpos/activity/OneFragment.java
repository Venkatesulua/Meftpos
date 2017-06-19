package com.mobileeftpos.android.eftpos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.utils.MenuConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OneFragment extends Fragment  {

     private ArrayList<String>homeListData;
    private GridView gridLayout;
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
        homeListData=new ArrayList<>();
        LoadData();
        gridLayout=(GridView)view.findViewById(R.id.grid_view);
        gridLayout.setAdapter(new CustomAdapter(homeListData));

        return view;
    }

    private void LoadData() {

//TransactionControlModel transactionControlModel=//get the date from where it's aavailable

        if("1".equalsIgnoreCase("1")){
             homeListData.add(MenuConstants.SALE);
        }

        if("2".equalsIgnoreCase("2")){
            homeListData.add(MenuConstants.SETTLEMENT);
        }

        if("3".equalsIgnoreCase("3")){
            homeListData.add(MenuConstants.VOID);
        }

        if("4".equalsIgnoreCase("4")){
            homeListData.add(MenuConstants.PREAUTH);
        }

        if("5".equalsIgnoreCase("5")){
            homeListData.add(MenuConstants.ADJUST);
        }

        if("6".equalsIgnoreCase("6")){
            homeListData.add(MenuConstants.OFFLINE);
        }

        if("7".equalsIgnoreCase("7")){
            homeListData.add(MenuConstants.CASH_ADV);
        }

        if("8".equalsIgnoreCase("8")){
            homeListData.add(MenuConstants.EZLINK_TOPUP);
        }

        if("9".equalsIgnoreCase("9")){
            homeListData.add(MenuConstants.CEPAS_SALE);
        }
        if("10".equalsIgnoreCase("10")){
            homeListData.add(MenuConstants.ALIPAY_SALE);
        }


    }


     public void processSelectedItem(String selectedItem) {

        //Read the transaction date and time and send the ame to host
        TransactionDetails transDetails = new TransactionDetails();

        transDetails.vdCleanFields();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String stDate = dateFormat.format(date);
        TransactionDetails.trxDateTime=stDate;


        switch (selectedItem) {

            case MenuConstants.SALE:

                startActivity(new Intent(HomeActivity.context, SaleActivity.class));
                break;

            case MenuConstants.SETTLEMENT:
                break;

            case MenuConstants.VOID:
                TransactionDetails.trxType = Constants.TransType.VOID;
                startActivity(new Intent(HomeActivity.context, VoidFlow.class));
                break;

            case MenuConstants.PREAUTH:
                break;

            case MenuConstants.ADJUST:
                break;

            case MenuConstants.OFFLINE:
                break;

            case MenuConstants.CASH_ADV:
                break;

            case MenuConstants.EZLINK_TOPUP:
                break;

            case MenuConstants.CEPAS_SALE:
                break;
            case MenuConstants.ALIPAY_SALE:
                TransactionDetails.trxType = Constants.TransType.ALIPAY_SALE;
                startActivity(new Intent(HomeActivity.context, SaleActivity.class));
                break;


        }
    }

    class CustomAdapter extends BaseAdapter {

        ArrayList<String> result;
          private  LayoutInflater inflater=null;
        public CustomAdapter( ArrayList<String> data) {
            // TODO Auto-generated constructor stub
            result=data;
            inflater = ( LayoutInflater )getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            LinearLayout gridLayout;
            ImageView gridImg;
            TextView gridText;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.grid_item, null);
            holder.gridLayout=(LinearLayout) rowView.findViewById(R.id.gridItem);
            holder.gridText=(TextView) rowView.findViewById(R.id.gridItemtext);
            holder.gridImg=(ImageView) rowView.findViewById(R.id.gridItemicon);

            String itemData=result.get(position);

            if(itemData.equalsIgnoreCase(MenuConstants.SALE)){
                holder.gridText.setText(MenuConstants.SALE);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.sale_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.SETTLEMENT)){
                holder.gridText.setText(MenuConstants.SETTLEMENT);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.settelment_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.VOID)){
                holder.gridText.setText(MenuConstants.VOID);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.void_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.PREAUTH)){
                holder.gridText.setText(MenuConstants.PREAUTH);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.preauth_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.ADJUST)){
                holder.gridText.setText(MenuConstants.ADJUST);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.adjust_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.OFFLINE)){
                holder.gridText.setText(MenuConstants.OFFLINE);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.offline_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.CASH_ADV)){
                holder.gridText.setText(MenuConstants.CASH_ADV);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.advance_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.EZLINK_TOPUP)){
                holder.gridText.setText(MenuConstants.EZLINK_TOPUP);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.exlinktopup));
            }else if(itemData.equalsIgnoreCase(MenuConstants.CEPAS_SALE)){
                holder.gridText.setText(MenuConstants.CEPAS_SALE);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.cepas_icon));
            }else if(itemData.equalsIgnoreCase(MenuConstants.ALIPAY_SALE)){
                holder.gridText.setText(MenuConstants.ALIPAY_SALE);
                holder.gridImg.setImageDrawable(getResources().getDrawable(R.drawable.alipay_logo));
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    processSelectedItem(result.get(position));
                }
            });

            return rowView;
        }

    }


}
