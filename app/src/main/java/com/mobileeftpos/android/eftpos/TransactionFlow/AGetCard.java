package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.GlobalVar;
import com.mobileeftpos.android.eftpos.SupportClasses.ISOPackager1;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.activity.AlipayActivity;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.AlipayModel;
import com.mobileeftpos.android.eftpos.db.AlipayModelDao;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.BatchModelDao;
import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardBinModelDao;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModelDao;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.CommsModelDao;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.CurrencyModelDao;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.EthernetModel;
import com.mobileeftpos.android.eftpos.db.EthernetModelDao;
import com.mobileeftpos.android.eftpos.db.EzlinkModel;
import com.mobileeftpos.android.eftpos.db.EzlinkModelDao;
import com.mobileeftpos.android.eftpos.db.HTTModel;
import com.mobileeftpos.android.eftpos.db.HTTModelDao;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.HostModelDao;
import com.mobileeftpos.android.eftpos.db.LimitModel;
import com.mobileeftpos.android.eftpos.db.LimitModelDao;
import com.mobileeftpos.android.eftpos.db.MaskingModel;
import com.mobileeftpos.android.eftpos.db.MaskingModelDao;
import com.mobileeftpos.android.eftpos.db.MerchantModel;
import com.mobileeftpos.android.eftpos.db.MerchantModelDao;
import com.mobileeftpos.android.eftpos.db.PasswordModel;
import com.mobileeftpos.android.eftpos.db.PasswordModelDao;
import com.mobileeftpos.android.eftpos.db.ReceiptModel;
import com.mobileeftpos.android.eftpos.db.ReceiptModelDao;
import com.mobileeftpos.android.eftpos.db.ReportModel;
import com.mobileeftpos.android.eftpos.db.ReportModelDao;
import com.mobileeftpos.android.eftpos.db.TraceModel;
import com.mobileeftpos.android.eftpos.db.TraceModelDao;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModelDao;
import com.mobileeftpos.android.eftpos.db.UtilityModel;
import com.mobileeftpos.android.eftpos.db.UtilityModelDao;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by venkat on 8/2/2017.
 */

public class AGetCard extends ParamLoading{

    protected final String TAG = "my_custom_msg";
    public int readCard(){
        return Constants.ReturnValues.RETURN_OK;
    }

}
