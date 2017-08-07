package com.mobileeftpos.android.eftpos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.adapters.TechnicianMenuAdapter;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;
import com.mobileeftpos.android.eftpos.utils.MenuConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransactionMenuFragment extends Fragment  {

     private ArrayList<String>homeListData;
    private GridView gridLayout;
    private DaoSession daoSession;
    public TransactionMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         daoSession = GreenDaoSupport.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        homeListData=new ArrayList<>();
        LoadData();
        gridLayout=(GridView)view.findViewById(R.id.grid_view);
        gridLayout.setAdapter(new TechnicianMenuAdapter(homeListData,getActivity()));
        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processSelectedItem(homeListData.get(position));

            }
        });
        return view;
    }

    private void LoadData() {

//TransactionControlModel transactionControlModel=//get the date from where it's aavailable

        TransactionControlModel controlModel = new TransactionControlModel();
        controlModel = GreenDaoSupport.getTransactionControlModelOBJ(getActivity());
        //controlModel = databaseObj.getTransactionCtrlData(0);
        if(controlModel.getSALE_CTRL() != null && controlModel.getSALE_CTRL().equalsIgnoreCase("1")){
             homeListData.add(MenuConstants.SALE);
        }

//        if(controlModel.getSETTLEMENT_CTRL() != null && controlModel.getSETTLEMENT_CTRL().equalsIgnoreCase("1")){
//            homeListData.add(MenuConstants.SETTLEMENT);
//        }

        if(controlModel.getVOID_CTRL() != null && controlModel.getVOID_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.VOID);
        }

        if(controlModel.getREFUND_CTRL() != null && controlModel.getREFUND_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.REFUND);
        }

        if(controlModel.getAUTH_CTRL() != null && controlModel.getAUTH_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.PREAUTH);
        }

        if(controlModel.getADJUSTMENT_CTRL() != null && controlModel.getADJUSTMENT_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.ADJUST);
        }

        if(controlModel.getOFFLINE_CTRL() != null && controlModel.getOFFLINE_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.OFFLINE);
        }

        if(controlModel.getCASH_ADVANCE_CTRL() != null && controlModel.getCASH_ADVANCE_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.CASH_ADV);
        }
        if(controlModel.getSETTLEMENT_CTRL() != null && controlModel.getSETTLEMENT_CTRL().equalsIgnoreCase("1")){
            homeListData.add(MenuConstants.SETTLEMENT);
        }

        //if(controlModel.get.equalsIgnoreCase("8")){
            //homeListData.add(MenuConstants.EZLINK_TOPUP);
        //}
        //homeListData.add(MenuConstants.SETTLEMENT);
        //if("9".equalsIgnoreCase("9")){
            homeListData.add(MenuConstants.CEPAS_SALE);
       // }
        //if("10".equalsIgnoreCase("10")){
            homeListData.add(MenuConstants.ALIPAY_SALE);

            homeListData.add(MenuConstants.EZ_iWallet);
        //}


    }


     public void processSelectedItem(String selectedItem) {

        //Read the transaction date and time and send the ame to host
        TransactionDetails transDetails = new TransactionDetails();

        transDetails.vdCleanFields();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        Date date = new Date();
        String stDate = dateFormat.format(date);
        TransactionDetails.trxDateTime=stDate;


         if(inCheckParamaters() != Constants.ReturnValues.RETURN_OK)
         {
             Toast.makeText(getActivity(), "Please download TMS Parameters", Toast.LENGTH_SHORT).show();
             return;
         }
         switch (selectedItem) {

            case MenuConstants.SALE:
                //byte[] cc = bb.getBytes();
                startActivity(new Intent(HomePagerActivity.context, SaleActivity.class));
                break;

            case MenuConstants.SETTLEMENT:
                TransactionDetails.trxType = Constants.TransType.INIT_SETTLEMENT;
                startActivity(new Intent(HomePagerActivity.context, SettlementFlow.class));
                break;

            case MenuConstants.VOID:
                TransactionDetails.trxType = Constants.TransType.VOID;
                startActivity(new Intent(HomePagerActivity.context, VoidFlow.class));
                break;

            case MenuConstants.REFUND:
                TransactionDetails.trxType = Constants.TransType.ALIPAY_REFUND;
                startActivity(new Intent(HomePagerActivity.context, RefundFlow.class));
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
                TransactionDetails.trxType = Constants.TransType.EZLINK_SALE;
                startActivity(new Intent(HomePagerActivity.context, SaleActivity.class));
                break;

            case MenuConstants.ALIPAY_SALE:
                TransactionDetails.trxType = Constants.TransType.ALIPAY_SALE;
                startActivity(new Intent(HomePagerActivity.context, SaleActivity.class));
                break;

             case MenuConstants.EZ_iWallet:
                 TransactionDetails.trxType = Constants.TransType.EZ_iWallet;
                 startActivity(new Intent(HomePagerActivity.context, EziWalletActivity.class));
                 break;

        }
    }



    int inCheckParamaters()
    {
        HostModel hostModel = new HostModel();
        List<HostModel> hostModelList = GreenDaoSupport.getHostModelOBJList(getActivity());//databaseObj
        // .getAllHostTableData();
        for (int i = 0; i < hostModelList.size(); i++) {
            hostModel = hostModelList.get(i);
            if (!(hostModel == null || hostModel.equals(""))) {
                if (hostModel.getHDT_HOST_ENABLED().equalsIgnoreCase("1")) {
                    return Constants.ReturnValues.RETURN_OK;
                }
            }
        }
        return Constants.ReturnValues.RETURN_ERROR;
    }


}
