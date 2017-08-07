package com.mobileeftpos.android.eftpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.activity.TransactionMenuFragment;
import com.mobileeftpos.android.eftpos.utils.MenuConstants;

import java.util.ArrayList;

/**
 * Created by Prathap on 07/08/17.
 */

public class TechnicianMenuAdapter extends BaseAdapter {
    private ArrayList<String>result;
    private  LayoutInflater inflater=null;
    private Activity context;

    public TechnicianMenuAdapter(ArrayList<String> data, Activity activity) {
        // TODO Auto-generated constructor stub
        result=data;
        context=activity;
        inflater = (LayoutInflater)activity.
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
        TechnicianMenuAdapter.Holder holder=new TechnicianMenuAdapter.Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_item, null);
        holder.gridLayout=(LinearLayout) rowView.findViewById(R.id.gridItem);
        holder.gridText=(TextView) rowView.findViewById(R.id.gridItemtext);
        holder.gridImg=(ImageView) rowView.findViewById(R.id.gridItemicon);

        String itemData=result.get(position);

        if(itemData.equalsIgnoreCase(MenuConstants.SALE)){
            holder.gridText.setText(MenuConstants.SALE);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.sale_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.SETTLEMENT)){
            holder.gridText.setText(MenuConstants.SETTLEMENT);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.settelment_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.VOID)){
            holder.gridText.setText(MenuConstants.VOID);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.void_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.REFUND)){
            holder.gridText.setText(MenuConstants.REFUND);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ref_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.PREAUTH)){
            holder.gridText.setText(MenuConstants.PREAUTH);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.preauth_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.ADJUST)){
            holder.gridText.setText(MenuConstants.ADJUST);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.adjust_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.OFFLINE)){
            holder.gridText.setText(MenuConstants.OFFLINE);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.offline_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.CASH_ADV)){
            holder.gridText.setText(MenuConstants.CASH_ADV);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.advance_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.EZLINK_TOPUP)){
            holder.gridText.setText(MenuConstants.EZLINK_TOPUP);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.exlinktopup));
        }else if(itemData.equalsIgnoreCase(MenuConstants.CEPAS_SALE)){
            holder.gridText.setText(MenuConstants.CEPAS_SALE);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.cepas_icon));
        }else if(itemData.equalsIgnoreCase(MenuConstants.ALIPAY_SALE)){
            holder.gridText.setText(MenuConstants.ALIPAY_SALE);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.alipay_logo));
        }
        else if(itemData.equalsIgnoreCase(MenuConstants.EZ_iWallet)){
            holder.gridText.setText(MenuConstants.EZ_iWallet);
            holder.gridImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ez_iwallet));
        }



        return rowView;
    }

}


