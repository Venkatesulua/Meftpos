package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;
import android.content.Context;

import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.SupportClasses.PacketCreation;
import com.mobileeftpos.android.eftpos.SupportClasses.PayServices;
import com.mobileeftpos.android.eftpos.SupportClasses.PrintReceipt;
import com.mobileeftpos.android.eftpos.SupportClasses.RemoteHost;
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

/**
 * Created by venkat on 8/2/2017.
 */

public class AGetCard {

    public final String TAG = "my_custom_msg";
   public CommsModel comModel = new CommsModel();
    public BatchModel batchModel = new BatchModel();
    public AlipayModel alipayModel = new AlipayModel();
    public CardBinModel cardBinModel = new CardBinModel();
    public CardTypeModel cardTypeModel = new CardTypeModel();
    public CurrencyModel currencyModel = new CurrencyModel();
    public EthernetModel ethernetModel = new EthernetModel();
    public EzlinkModel ezlinkModel = new EzlinkModel();
    public HostModel hostModel = new HostModel();
    public HTTModel httModel = new HTTModel();
    public LimitModel limitModel = new LimitModel();
    public MaskingModel maskingModel = new MaskingModel();
    public MerchantModel merchantModel = new MerchantModel();
    public PasswordModel passwordModel = new PasswordModel();
    public ReceiptModel receiptModel = new ReceiptModel();
    public ReportModel reportModel = new ReportModel();
    public TraceModel traceModel = new TraceModel();
    public TransactionControlModel transactionControlModel = new TransactionControlModel();
    public UtilityModel utilityModel = new UtilityModel();
    private  DaoSession daoSession;

    public HostModelDao hostModelDao;
    public BatchModelDao batchModelDao;
    public CardBinModelDao cardBinModelDao;
    public CardTypeModelDao cttModelDao;
    public CommsModelDao comModelDao;
    public CurrencyModelDao currModelDao ;
    public EthernetModelDao ethernetModelDao ;
    public EzlinkModelDao ezlinkModelDao ;
    public HTTModelDao hostTransModelDao ;
    public LimitModelDao limitModelDao;
    public MaskingModelDao maskModelDao ;
    public AlipayModelDao alipayModelDao ;
    public MerchantModelDao merchantModelDao ;
    public ReceiptModelDao receiptModelDao ;
    public ReportModelDao reportModelDao ;
    public TransactionControlModelDao transctrlModelDao ;
    public UtilityModelDao utilityModelDao ;
    public TraceModelDao traceModelDao;
    public PasswordModelDao pwdModelDao;

    public PacketCreation isoPacket = new PacketCreation();
    public RemoteHost remoteHost = new RemoteHost();
    public PrintReceipt printReceipt = new PrintReceipt();
    public PayServices payServices = new PayServices();


    public byte[] byRequestData = new byte[1512];
    public byte[] byResponseData = new byte[1512];


    public AGetCard(Activity context)
    {
        daoSession = GreenDaoSupport.getInstance(context);

        hostModelDao = daoSession.getHostModelDao();
        cardBinModelDao= daoSession.getCardBinModelDao();
        cttModelDao= daoSession.getCardTypeModelDao();
        comModelDao= daoSession.getCommsModelDao();
        currModelDao= daoSession.getCurrencyModelDao();
        ethernetModelDao= daoSession.getEthernetModelDao();
        ezlinkModelDao= daoSession.getEzlinkModelDao();
        hostTransModelDao= daoSession.getHTTModelDao();
        limitModelDao= daoSession.getLimitModelDao();
        maskModelDao= daoSession.getMaskingModelDao();
        alipayModelDao= daoSession.getAlipayModelDao();
        merchantModelDao= daoSession.getMerchantModelDao();
        receiptModelDao= daoSession.getReceiptModelDao();
        reportModelDao= daoSession.getReportModelDao();
        transctrlModelDao= daoSession.getTransactionControlModelDao();
        utilityModelDao= daoSession.getUtilityModelDao();
        traceModelDao= daoSession.getTraceModelDao();
        pwdModelDao= daoSession.getPasswordModelDao();

        merchantModel = GreenDaoSupport.getMerchantModelOBJ(context);
        ethernetModel = ethernetModelDao.loadAll().get(0);
        ezlinkModel = ezlinkModelDao.loadAll().get(0);
        httModel = hostTransModelDao.loadAll().get(0);
        limitModel = limitModelDao.loadAll().get(0);
        alipayModel = alipayModelDao.loadAll().get(0);
        receiptModel = receiptModelDao.loadAll().get(0);
        reportModel = reportModelDao.loadAll().get(0);
        transactionControlModel = transctrlModelDao.loadAll().get(0);
        utilityModel = utilityModelDao.loadAll().get(0);
        traceModel = traceModelDao.loadAll().get(0);
        passwordModel = pwdModelDao.loadAll().get(0);
        maskingModel = maskModelDao.loadAll().get(0);

        /*
        hostModel = hostModelDao.loadAll().get(0);
        cardBinModel = cardBinModelDao.loadAll().get(0);
        cardTypeModel = cttModelDao.loadAll().get(0);
        comModel = comModelDao.loadAll().get(0);
        currencyModel = currModelDao.loadAll().get(0);*/
    }

    public int readCard(){
        return Constants.ReturnValues.RETURN_OK;
    }

}
