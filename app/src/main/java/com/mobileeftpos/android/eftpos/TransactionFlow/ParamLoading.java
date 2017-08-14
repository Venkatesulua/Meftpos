package com.mobileeftpos.android.eftpos.TransactionFlow;

import android.app.Activity;

import com.mobileeftpos.android.eftpos.SupportClasses.TransactionDetails;
import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.AlipayModel;
import com.mobileeftpos.android.eftpos.db.BatchModel;
import com.mobileeftpos.android.eftpos.db.CardBinModel;
import com.mobileeftpos.android.eftpos.db.CardTypeModel;
import com.mobileeftpos.android.eftpos.db.CommsModel;
import com.mobileeftpos.android.eftpos.db.CurrencyModel;
import com.mobileeftpos.android.eftpos.db.EthernetModel;
import com.mobileeftpos.android.eftpos.db.EzlinkModel;
import com.mobileeftpos.android.eftpos.db.HTTModel;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.LimitModel;
import com.mobileeftpos.android.eftpos.db.MaskingModel;
import com.mobileeftpos.android.eftpos.db.MerchantModel;
import com.mobileeftpos.android.eftpos.db.PasswordModel;
import com.mobileeftpos.android.eftpos.db.ReceiptModel;
import com.mobileeftpos.android.eftpos.db.ReportModel;
import com.mobileeftpos.android.eftpos.db.TraceModel;
import com.mobileeftpos.android.eftpos.db.TransactionControlModel;
import com.mobileeftpos.android.eftpos.db.UtilityModel;
import com.mobileeftpos.android.eftpos.utils.StringByteUtils;

/**
 * Created by venkat on 8/2/2017.
 */

public class ParamLoading  {

    private Activity locontext;

    protected CommsModel comModel = new CommsModel();
    protected BatchModel batchModel = new BatchModel();
    protected AlipayModel alipayModel = new AlipayModel();
    protected CardBinModel cardBinModel = new CardBinModel();
    protected CardTypeModel cardTypeModel = new CardTypeModel();
    protected CurrencyModel currencyModel = new CurrencyModel();
    protected EthernetModel ethernetModel = new EthernetModel();
    protected EzlinkModel ezlinkModel = new EzlinkModel();
    protected HostModel hostModel = new HostModel();
    protected HTTModel httModel = new HTTModel();
    protected LimitModel limitModel = new LimitModel();
    protected MaskingModel maskingModel = new MaskingModel();
    protected MerchantModel merchantModel = new MerchantModel();
    protected PasswordModel passwordModel = new PasswordModel();
    protected ReceiptModel receiptModel = new ReceiptModel();
    protected ReportModel reportModel = new ReportModel();
    protected TraceModel traceModel = new TraceModel();
    protected TransactionControlModel transactionControlModel = new TransactionControlModel();
    protected UtilityModel utilityModel = new UtilityModel();
    public byte[] byRequestData = new byte[1512];
    public byte[] byResponseData = new byte[1512];


   /* private Activity loContext;
    public AGetCard(Activity context)
    {
        loContext=context;
    }*/



    public void LoadAll(Activity context){
        merchantModel = GreenDaoSupport.getMerchantModelOBJ(context);
        ethernetModel = GreenDaoSupport.getEthernetModelOBJ(context);
        ezlinkModel = GreenDaoSupport.getEzlinkTableModelOBJ(context);//ezlinkModelDao.loadAll().get(0);
        httModel = GreenDaoSupport.getHTTModelOBJ(context);;//hostTransModelDao.loadAll().get(0);
        limitModel = GreenDaoSupport.getLimitTableModelOBJ(context);;//limitModelDao.loadAll().get(0);
        alipayModel = GreenDaoSupport.getAlipayModelOBJ(context);//alipayModelDao.loadAll().get(0);
        receiptModel = GreenDaoSupport.getReceiptModelOBJ(context);//receiptModelDao.loadAll().get(0);
        reportModel = GreenDaoSupport.getReportModelOBJ(context);//reportModelDao.loadAll().get(0);
        transactionControlModel = GreenDaoSupport.getTransactionControlModelOBJ(context);//transctrlModelDao.loadAll().get(0);
        utilityModel = GreenDaoSupport.getUtilityModelOBJ(context);//utilityModelDao.loadAll().get(0);
        traceModel = GreenDaoSupport.getTraceModelOBJ(context);//traceModelDao.loadAll().get(0);
        passwordModel = GreenDaoSupport.getPasswordModelOBJ(context);//pwdModelDao.loadAll().get(0);
        maskingModel = GreenDaoSupport.getMaskingTableModelOBJ(context);//maskModelDao.loadAll().get(0);

        TransactionDetails.PaymentTRP = StringByteUtils.HexString2Bytes(ezlinkModel.getEZLINK_PAYMENT_TRP());
    }

    public HostModel GetHostModel() {
        return hostModel;}
    public void SetHostModel(HostModel hostModel) {
        this.hostModel=hostModel;
    }

    public CardBinModel GetCardBinModel() {return cardBinModel;}
    public void SetCardBinModel(CardBinModel cardBinModel) {
        this.cardBinModel=cardBinModel;}

    public CardTypeModel GetCardTypeModel() {return cardTypeModel;}
    public void SetCardTypeModel(CardTypeModel cardTypeModel) {
        this.cardTypeModel=cardTypeModel;}

    public CommsModel GetCommsModel() {return comModel;}
    public void SetCommsModel(CommsModel comModel) {
        this.comModel=comModel;}

    public CurrencyModel GetCurrencyModel() {return currencyModel;}
    public void SetCurrencyModel(CurrencyModel currencyModel) {
        this.currencyModel=currencyModel;}

}
