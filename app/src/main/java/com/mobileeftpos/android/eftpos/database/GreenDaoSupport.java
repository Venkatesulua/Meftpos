package com.mobileeftpos.android.eftpos.database;

import android.app.Activity;

import com.mobileeftpos.android.eftpos.app.EftposApp;
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

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prathap on 29/07/17.
 */

public class GreenDaoSupport {

	public static HostModelDao hostModelDao;
	public static BatchModelDao batchModelDao;
	public static CardTypeModelDao cttModelDao;
	public static CommsModelDao comModelDao;
	public static CurrencyModelDao currModelDao ;
	public static EthernetModelDao ethernetModelDao ;
	public static EzlinkModelDao ezlinkModelDao ;
	public static HTTModelDao hostTransModelDao ;
	public static LimitModelDao limitModelDao;
	public static MaskingModelDao maskModelDao ;
	public static AlipayModelDao alipayModelDao ;
	public static MerchantModelDao merchantModelDao ;
	public static ReceiptModelDao receiptModelDao ;
	public static ReportModelDao reportModelDao ;
	public static TransactionControlModelDao transctrlModelDao ;
	public static UtilityModelDao utilityModelDao ;
	public static TraceModelDao traceModelDao;
	public static PasswordModelDao pwdModelDao;

public static DaoSession daosession;
	//Single ton objects

	public static DaoSession getInstance(Activity context) {
		if(daosession == null) {
			daosession = ((EftposApp)context.getApplication()).getDaoSession();
		}
		return daosession;
	}

	public static HostModel getHostTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		HostModelDao hostModelDao =daosession.getHostModelDao();
		HostModel hostModel=new HostModel();
        if(hostModelDao.loadAll().size()>0) {
            hostModel = hostModelDao.loadAll().get(0);
        }
		return hostModel;
	}


    public static HostModel getHostTableIDBasedModelOBJ(Activity context, String hostId){
        if(daosession == null){
            getInstance(context);
        }
        HostModel hostModel =new HostModel();
        QueryBuilder<HostModel> qb = daosession.getHostModelDao().queryBuilder();
        qb.where(HostModelDao.Properties.HDT_HOST_ID.eq(hostId));
        if (qb.list().size() > 0) {
            hostModel= qb.list().get(0);
        }
         return hostModel;
    }

	public static EzlinkModel getEzlinkTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		EzlinkModelDao ezlinkModelDao =daosession.getEzlinkModelDao();
		EzlinkModel ezlinkModel=new EzlinkModel();
        if(ezlinkModelDao.loadAll().size()>0) {
            ezlinkModel = ezlinkModelDao.loadAll().get(0);
        }
		return ezlinkModel;
	}

	public static EzlinkModel getEzlinkTableIDBasedModelOBJ(Activity context, String ezlinkId){
		if(daosession == null){
			getInstance(context);
		}
		EzlinkModel ezlinkModel =new EzlinkModel();
		QueryBuilder<EzlinkModel> qb = daosession.getEzlinkModelDao().queryBuilder();
		qb.where(EzlinkModelDao.Properties.Ezlink_ID.eq(ezlinkId));
		if (qb.list().size() > 0) {
			ezlinkModel= qb.list().get(0);
		}
		return ezlinkModel;
	}


	public static CurrencyModel getCurrencyTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CurrencyModelDao currencyModelDao =daosession.getCurrencyModelDao();
		CurrencyModel curencyModel=new CurrencyModel();
        if(currencyModelDao.loadAll().size()>0) {
            curencyModel = currencyModelDao.loadAll().get(0);
        }
		return curencyModel;
	}

	public static CurrencyModel getCurrencyTableIDBasedModelOBJ(Activity context, String currencyId){
		if(daosession == null){
			getInstance(context);
		}
		CurrencyModel currencyModel =new CurrencyModel();
		QueryBuilder<CurrencyModel> qb = daosession.getCurrencyModelDao().queryBuilder();
		qb.where(CurrencyModelDao.Properties.CURRENCY_ID.eq(currencyId));
		if (qb.list().size() > 0) {
			currencyModel= qb.list().get(0);
		}
		return currencyModel;
	}


	public static CardBinModel getCardBinModellOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CardBinModelDao cardBinModelDao =daosession.getCardBinModelDao();
		CardBinModel cardBinModel=new CardBinModel();
        if(cardBinModelDao.loadAll().size()>0) {
            cardBinModel = cardBinModelDao.loadAll().get(0);
        }
		return cardBinModel;
	}

	public static CardBinModel getCardBinTableIDBasedModelOBJ(Activity context, String cardBinId){
		if(daosession == null){
			getInstance(context);
		}
		CardBinModel cardBinModel =new CardBinModel();
		QueryBuilder<CardBinModel> qb = daosession.getCardBinModelDao().queryBuilder();
		qb.where(CardBinModelDao.Properties.CDT_ID.eq(cardBinId));
		if (qb.list().size() > 0) {
			cardBinModel= qb.list().get(0);
		}
		return cardBinModel;
	}


	public static CardTypeModel getCardTypeModellOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CardTypeModelDao cardTypeModelDao =daosession.getCardTypeModelDao();
		CardTypeModel cardTypeModel=new CardTypeModel();
        if(cardTypeModelDao.loadAll().size()>0) {
            cardTypeModel = cardTypeModelDao.loadAll().get(0);
        }
		return cardTypeModel;
	}

	public static CardTypeModel getCardTypeIDBasedModelOBJ(Activity context, String cttId){
		if(daosession == null){
			getInstance(context);
		}
		CardTypeModel cardTypeModel =new CardTypeModel();
		QueryBuilder<CardTypeModel> qb = daosession.getCardTypeModelDao().queryBuilder();
		qb.where(CardTypeModelDao.Properties.CTT_ID.eq(cttId));
		if (qb.list().size() > 0) {
			cardTypeModel= qb.list().get(0);
		}
		return cardTypeModel;
	}

	public static PasswordModel getPasswordModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		PasswordModelDao passwordModelDao =daosession.getPasswordModelDao();
		PasswordModel passwordModel=new PasswordModel();
        if(passwordModelDao.loadAll().size()>0) {
            passwordModel = passwordModelDao.loadAll().get(0);
        }
		return passwordModel;
	}

	public static PasswordModel getPasswordIDBasedModelOBJ(Activity context, String pwdId){
		if(daosession == null){
			getInstance(context);
		}
		PasswordModel passwordModel =new PasswordModel();
		QueryBuilder<PasswordModel> qb = daosession.getPasswordModelDao().queryBuilder();
		qb.where(PasswordModelDao.Properties.PWD_ID.eq(pwdId));
		if (qb.list().size() > 0) {
			passwordModel= qb.list().get(0);
		}
		return passwordModel;
	}

	public static TransactionControlModel getTransactionControlModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		TransactionControlModelDao transactionControlModelDao =daosession.getTransactionControlModelDao();
		TransactionControlModel transactionControlModel=new TransactionControlModel();
		if(transactionControlModelDao.loadAll().size()>0) {
			transactionControlModel = transactionControlModelDao.loadAll().get(0);
		}
		return transactionControlModel;
	}

	public static TransactionControlModel getTransactionControlModelOBJ(Activity context, String tctId){
		if(daosession == null){
			getInstance(context);
		}
		TransactionControlModel transactionControlModel =new TransactionControlModel();
		QueryBuilder<TransactionControlModel> qb = daosession.getTransactionControlModelDao().queryBuilder();
		qb.where(TransactionControlModelDao.Properties.TCT_ID.eq(tctId));
		if (qb.list().size() > 0) {
			transactionControlModel= qb.list().get(0);
		}
		return transactionControlModel;
	}

	public static EthernetModel getEthernetModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		EthernetModelDao ethernetModelDao =daosession.getEthernetModelDao();
		EthernetModel ethernetModel=new EthernetModel();
		if(ethernetModelDao.loadAll().size()>0) {
			ethernetModel = ethernetModelDao.loadAll().get(0);
		}
		return ethernetModel;
	}

	public static EthernetModel getEthernetModelOBJ(Activity context, String EthernetID){
		if(daosession == null){
			getInstance(context);
		}
		EthernetModel ethernetModel =new EthernetModel();
		QueryBuilder<EthernetModel> qb = daosession.getEthernetModelDao().queryBuilder();
		qb.where(EthernetModelDao.Properties.ETHERNET_ID.eq(EthernetID));
		if (qb.list().size() > 0) {
			ethernetModel= qb.list().get(0);
		}
		return ethernetModel;
	}

	public static LimitModel getLimitTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		LimitModelDao limitModelDao =daosession.getLimitModelDao();
		LimitModel limitModel=new LimitModel();
        if(limitModelDao.loadAll().size()>0) {
            limitModel = limitModelDao.loadAll().get(0);
        }
		return limitModel;
	}

	public static LimitModel getLimitTableIdBasedModelOBJ(Activity context, String limitId){
		if(daosession == null){
			getInstance(context);
		}
		LimitModel limitModel =new LimitModel();
		QueryBuilder<LimitModel> qb = daosession.getLimitModelDao().queryBuilder();
		qb.where(LimitModelDao.Properties.LIMIT_ID.eq(limitId));
		if (qb.list().size() > 0) {
			limitModel= qb.list().get(0);
		}
		return limitModel;
	}


	public static MaskingModel getMaskingTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		MaskingModelDao maskingModelDao =daosession.getMaskingModelDao();
		MaskingModel maskingModel=new MaskingModel();
        if(maskingModelDao.loadAll().size()>0) {
            maskingModel = maskingModelDao.loadAll().get(0);
        }
		return maskingModel;
	}

	public static MaskingModel getMaskingTableIdBasedModelOBJ(Activity context, String maskingId){
		if(daosession == null){
			getInstance(context);
		}
		MaskingModel maskingModel =new MaskingModel();
		QueryBuilder<MaskingModel> qb = daosession.getMaskingModelDao().queryBuilder();
		qb.where(MaskingModelDao.Properties.MASKING_ID.eq(maskingId));
		if (qb.list().size() > 0) {
            maskingModel= qb.list().get(0);
		}
		return maskingModel;
	}

	public static ReceiptModel getReceiptModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		ReceiptModelDao receiptModelDao =daosession.getReceiptModelDao();
		ReceiptModel receiptModel=new ReceiptModel();
        if(receiptModelDao.loadAll().size()>0) {
            receiptModel = receiptModelDao.loadAll().get(0);
        }
		return receiptModel;
	}

    public static ReceiptModel getReceiptTableIdBasedModelOBJ(Activity context, String receiptId){
        if(daosession == null){
            getInstance(context);
        }
        ReceiptModel receiptModel =new ReceiptModel();
        QueryBuilder<ReceiptModel> qb = daosession.getReceiptModelDao().queryBuilder();
        qb.where(MaskingModelDao.Properties.MASKING_ID.eq(receiptId));
        if (qb.list().size() > 0) {
            receiptModel= qb.list().get(0);
        }
        return receiptModel;
    }

	public static UtilityModel getUtilityModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		UtilityModelDao utilityModelDao =daosession.getUtilityModelDao();
		UtilityModel utilityModel=new UtilityModel();
        if(utilityModelDao.loadAll().size()>0){
            utilityModel=utilityModelDao.loadAll().get(0);

        }
		return utilityModel;
	}

    public static UtilityModel getUtilityTableIdBasedModelOBJ(Activity context, String UtilityId){
        if(daosession == null){
            getInstance(context);
        }
        UtilityModel utilityModel =new UtilityModel();
        QueryBuilder<UtilityModel> qb = daosession.getUtilityModelDao().queryBuilder();
        qb.where(UtilityModelDao.Properties.UTILITY_ID.eq(UtilityId));
        if (qb.list().size() > 0) {
            utilityModel= qb.list().get(0);
        }
        return utilityModel;
    }

	public static MerchantModel getMerchantModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		MerchantModelDao merchantModelDao =daosession.getMerchantModelDao();
		MerchantModel merchantModel=new MerchantModel();
        if(merchantModelDao.loadAll().size()>0) {
            merchantModel = merchantModelDao.loadAll().get(0);
        }
		return merchantModel;
	}

    public static MerchantModel getMerchantTableIdBasedModelOBJ(Activity context, String merchantId){
        if(daosession == null){
            getInstance(context);
        }
        MerchantModel merchantModel =new MerchantModel();
        QueryBuilder<MerchantModel> qb = daosession.getMerchantModelDao().queryBuilder();
        qb.where(MerchantModelDao.Properties.MERCHANT_ID.eq(merchantId));
        if (qb.list().size() > 0) {
            merchantModel= qb.list().get(0);
        }
        return merchantModel;
    }

	public static ReportModel getReportModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		ReportModelDao reportModelDao =daosession.getReportModelDao();
		ReportModel reportModel=new ReportModel();
        if(reportModelDao.loadAll().size()>0) {
            reportModel = reportModelDao.loadAll().get(0);
        }
		return reportModel;
	}

    public static ReportModel getReportTableIdBasedModelOBJ(Activity context, String reportId){
        if(daosession == null){
            getInstance(context);
        }
        ReportModel reportModel =new ReportModel();
        QueryBuilder<ReportModel> qb = daosession.getReportModelDao().queryBuilder();
        qb.where(ReportModelDao.Properties.REPORTTABLE_ID.eq(reportId));
        if (qb.list().size() > 0) {
            reportModel= qb.list().get(0);
        }
        return reportModel;
    }


	public static TraceModel getTraceModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		TraceModelDao traceModelDao =daosession.getTraceModelDao();
		TraceModel traceModel=new TraceModel();
        if(traceModelDao.loadAll().size()>0) {
            traceModel = traceModelDao.loadAll().get(0);
        }
		return traceModel;
	}

    public static TraceModel getTraceTableIdBasedModelOBJ(Activity context, String reportId){
        if(daosession == null){
            getInstance(context);
        }
        TraceModel traceModel =new TraceModel();
        QueryBuilder<TraceModel> qb = daosession.getTraceModelDao().queryBuilder();
        qb.where(ReportModelDao.Properties.REPORTTABLE_ID.eq(reportId));
        if (qb.list().size() > 0) {
            traceModel= qb.list().get(0);
        }
        return traceModel;
    }

	public static BatchModel getBatchModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		BatchModelDao batchModelDao =daosession.getBatchModelDao();
		BatchModel batchModel=new BatchModel();
        if(batchModelDao.loadAll().size()>0) {
            batchModel = batchModelDao.loadAll().get(0);
        }
		return batchModel;
	}

    public static BatchModel getBatchTableIdBasedModelOBJ(Activity context, String batchId){
        if(daosession == null){
            getInstance(context);
        }
        BatchModel batchModel =new BatchModel();
        QueryBuilder<BatchModel> qb = daosession.getBatchModelDao().queryBuilder();
        qb.where(BatchModelDao.Properties.BATCH_ID.eq(batchId));
        if (qb.list().size() > 0) {
            batchModel= qb.list().get(0);
        }
        return batchModel;
    }


	public static HTTModel getHTTModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		HTTModelDao hTTModelDao =daosession.getHTTModelDao();
		HTTModel reportModel=new HTTModel();
        if(hTTModelDao.loadAll().size()>0) {
            reportModel = hTTModelDao.loadAll().get(0);
        }
		return reportModel;
	}


    public static HTTModel getHTTTableIdBasedModelOBJ(Activity context, String httId){
        if(daosession == null){
            getInstance(context);
        }
        HTTModel httModel =new HTTModel();
        QueryBuilder<HTTModel> qb = daosession.getHTTModelDao().queryBuilder();
        qb.where(HTTModelDao.Properties.HTT_ID.eq(httId));
        if (qb.list().size() > 0) {
            httModel= qb.list().get(0);
        }
        return httModel;
    }


	public static AlipayModel getAlipayModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		AlipayModelDao alipayModelDao =daosession.getAlipayModelDao();
		AlipayModel alipayModel=new AlipayModel();
        if(alipayModelDao.loadAll().size()>0) {
            alipayModel = alipayModelDao.loadAll().get(0);
        }
		return alipayModel;
	}


    public static AlipayModel getAlipayTableIdBasedModelOBJ(Activity context, String httId){
        if(daosession == null){
            getInstance(context);
        }
        AlipayModel alipayModel =new AlipayModel();
        QueryBuilder<AlipayModel> qb = daosession.getAlipayModelDao().queryBuilder();
        qb.where(AlipayModelDao.Properties.ALIPAY_ID.eq(httId));
        if (qb.list().size() > 0) {
            alipayModel= qb.list().get(0);
        }
        return alipayModel;
    }


    public static CommsModel getCommsModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CommsModelDao commsModelDao =daosession.getCommsModelDao();
		CommsModel commsModel=new CommsModel();
        if(commsModelDao.loadAll().size()>0){
            commsModel=commsModelDao.loadAll().get(0);
        }
		return commsModel;
	}

    public static CommsModel getCommsTableIdBasedModelOBJ(Activity context, String commosId){
        if(daosession == null){
            getInstance(context);
        }
        CommsModel commsModel =new CommsModel();
        QueryBuilder<CommsModel> qb = daosession.getCommsModelDao().queryBuilder();
        qb.where(CommsModelDao.Properties.COMMOS_ID.eq(commosId));
        if (qb.list().size() > 0) {
            commsModel= qb.list().get(0);
        }
        return commsModel;
    }

    public static BatchModel getBatchModelbyInvoiceOBJ(Activity context, String InVoice){
        BatchModel batchModel = new BatchModel();
        if(daosession == null){
            getInstance(context);
        }
        QueryBuilder<BatchModel> qb = daosession.getBatchModelDao().queryBuilder();
        qb.where(BatchModelDao.Properties.Invoice_number.eq(InVoice));
        if(qb.list().size()>0){

            batchModel = qb.list().get(0);
        }
        return batchModel;
    }

	public static List<BatchModel> getBatchModelOBJList(Activity context, String hostId){
		 List<BatchModel> batchModelList = new ArrayList<>();
		if(daosession == null){
			getInstance(context);
		}
 		QueryBuilder<BatchModel> qb = daosession.getBatchModelDao().queryBuilder();
		qb.where(BatchModelDao.Properties.Hdt_index.eq(hostId));
		if(qb.list().size()>0){

			 batchModelList = qb.list();
		}
		return batchModelList;
	}

	public static List<HostModel> getHostModelOBJList(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		HostModelDao hostModelDao =daosession.getHostModelDao();
		return hostModelDao.loadAll();
	}

	public static List<BatchModel> getBatchModelOBJList(Activity context){

		if(daosession == null){
			getInstance(context);
		}
		BatchModelDao batchModelDao =daosession.getBatchModelDao();
		return batchModelDao.loadAll();
	}




	public static void insertTraceModelOBJ(Activity context, TraceModel traceModel){
		if(daosession == null){
			getInstance(context);
		}
		TraceModelDao traceModelDao =daosession.getTraceModelDao();
		traceModelDao.insertOrReplace(traceModel);
 	}


	public static void insertBatchModelOBJ(Activity context, BatchModel batchModel){
		if(daosession == null){
			getInstance(context);
		}
		BatchModelDao batchModelDao =daosession.getBatchModelDao();
		batchModelDao.insertOrReplace(batchModel);
	}


	public static void insertHostModelOBJ(Activity context, HostModel hostModel){
		if(daosession == null){
			getInstance(context);
		}
		HostModelDao hostModelDao =daosession.getHostModelDao();
		hostModelDao.insertOrReplace(hostModel);
	}


	public static void deleteAllTablesData(Activity activity){
		if(daosession == null){
			getInstance(activity);
		}

		hostModelDao = daosession.getHostModelDao();
		batchModelDao = daosession.getBatchModelDao();
		pwdModelDao = daosession.getPasswordModelDao();
		cttModelDao = daosession.getCardTypeModelDao();
		comModelDao = daosession.getCommsModelDao();
		currModelDao = daosession.getCurrencyModelDao();
		ethernetModelDao = daosession.getEthernetModelDao();
		ezlinkModelDao = daosession.getEzlinkModelDao();
		hostTransModelDao = daosession.getHTTModelDao();
		limitModelDao = daosession.getLimitModelDao();
		maskModelDao = daosession.getMaskingModelDao();
		alipayModelDao = daosession.getAlipayModelDao();
		merchantModelDao = daosession.getMerchantModelDao();
		receiptModelDao =daosession.getReceiptModelDao();
		reportModelDao = daosession.getReportModelDao();
		transctrlModelDao = daosession.getTransactionControlModelDao();
		utilityModelDao = daosession.getUtilityModelDao();
		traceModelDao=daosession.getTraceModelDao();

	}
}
