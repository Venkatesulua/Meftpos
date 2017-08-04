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
		hostModel=hostModelDao.loadAll().get(0);

		return hostModel;
	}

	public static EzlinkModel getEzlinkTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		EzlinkModelDao ezlinkModelDao =daosession.getEzlinkModelDao();
		EzlinkModel ezlinkModel=new EzlinkModel();
		ezlinkModel=ezlinkModelDao.loadAll().get(0);
		return ezlinkModel;
	}

	public static CurrencyModel getCurrencyTableModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CurrencyModelDao currencyModelDao =daosession.getCurrencyModelDao();
		CurrencyModel curencyModel=new CurrencyModel();
		curencyModel=currencyModelDao.loadAll().get(0);
		return curencyModel;
	}

	public static CardBinModel getCardBinModellOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CardBinModelDao cardBinModelDao =daosession.getCardBinModelDao();
		CardBinModel cardBinModel=new CardBinModel();
		cardBinModel=cardBinModelDao.loadAll().get(0);
		return cardBinModel;
	}


	public static CardTypeModel getCardTypeModellOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CardTypeModelDao cardTypeModelDao =daosession.getCardTypeModelDao();
		CardTypeModel cardTypeModel=new CardTypeModel();
		cardTypeModel=cardTypeModelDao.loadAll().get(0);
		return cardTypeModel;
	}


	public static PasswordModel getPasswordModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		PasswordModelDao passwordModelDao =daosession.getPasswordModelDao();
		PasswordModel passwordModel=new PasswordModel();
		passwordModel=passwordModelDao.loadAll().get(0);
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


	public static LimitModel getLimitModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		LimitModelDao limitModelDao =daosession.getLimitModelDao();
		LimitModel limitModel=new LimitModel();
		limitModel=limitModelDao.loadAll().get(0);
		return limitModel;
	}



	public static MaskingModel getMaskingModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		MaskingModelDao maskingModelDao =daosession.getMaskingModelDao();
		MaskingModel maskingModel=new MaskingModel();
		maskingModel=maskingModelDao.loadAll().get(0);
		return maskingModel;
	}


	public static ReceiptModel getReceiptModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		ReceiptModelDao receiptModelDao =daosession.getReceiptModelDao();
		ReceiptModel receiptModel=new ReceiptModel();
		receiptModel=receiptModelDao.loadAll().get(0);
		return receiptModel;
	}


	public static UtilityModel getUtilityModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		UtilityModelDao utilityModelDao =daosession.getUtilityModelDao();
		UtilityModel utilityModel=new UtilityModel();
		utilityModel=utilityModelDao.loadAll().get(0);
		return utilityModel;
	}


	public static MerchantModel getMerchantModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		MerchantModelDao merchantModelDao =daosession.getMerchantModelDao();
		MerchantModel merchantModel=new MerchantModel();
		merchantModel=merchantModelDao.loadAll().get(0);
		return merchantModel;
	}



	public static ReportModel getReportModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		ReportModelDao reportModelDao =daosession.getReportModelDao();
		ReportModel reportModel=new ReportModel();
		reportModel=reportModelDao.loadAll().get(0);
		return reportModel;
	}


	public static TraceModel getTraceModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		TraceModelDao traceModelDao =daosession.getTraceModelDao();
		TraceModel traceModel=new TraceModel();
		traceModel=traceModelDao.loadAll().get(0);
		return traceModel;
	}

	public static BatchModel getBatchModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		BatchModelDao batchModelDao =daosession.getBatchModelDao();
		BatchModel batchModel=new BatchModel();
		batchModel=batchModelDao.loadAll().get(0);
		return batchModel;
	}



	public static HTTModel getHTTModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		HTTModelDao hTTModelDao =daosession.getHTTModelDao();
		HTTModel reportModel=new HTTModel();
		reportModel=hTTModelDao.loadAll().get(0);
		return reportModel;
	}


	public static AlipayModel getAlipayModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		AlipayModelDao alipayModelDao =daosession.getAlipayModelDao();
		AlipayModel alipayModel=new AlipayModel();
		alipayModel=alipayModelDao.loadAll().get(0);
		return alipayModel;
	}

	public static CommsModel getCommsModelOBJ(Activity context){
		if(daosession == null){
			getInstance(context);
		}
		CommsModelDao commsModelDao =daosession.getCommsModelDao();
		CommsModel batchModel=new CommsModel();
		batchModel=commsModelDao.loadAll().get(0);
		return batchModel;
	}

	public static List<BatchModel> getBatchModelOBJList(Activity context, String hostId){
		if(daosession == null){
			getInstance(context);
		}
		BatchModelDao batchModelDao =daosession.getBatchModelDao();
		BatchModel batchModel=new BatchModel();
		QueryBuilder<BatchModel> qb = batchModelDao.queryBuilder();
		qb.where(BatchModelDao.Properties.Hdt_index.eq(hostId));
		List<BatchModel> batchModelList = qb.list();
		return batchModelList;
	}

	public static CurrencyModel getCurrencyModelOBJList(Activity context, String hostId){
		if(daosession == null){
			getInstance(context);
		}
		CurrencyModelDao currencyModelDao =daosession.getCurrencyModelDao();
		CurrencyModel currencyModel=new CurrencyModel();
		QueryBuilder<CurrencyModel> qb = currencyModelDao.queryBuilder();
		qb.where(BatchModelDao.Properties.Hdt_index.eq(hostId));
 		return qb.list().get(0);
	}


	public static CommsModel getCommsModelObj(Activity context, String hostId){
		if(daosession == null){
			getInstance(context);
		}
		CommsModelDao commsModelDao =daosession.getCommsModelDao();
		CommsModel commsModel=new CommsModel();
		QueryBuilder<CommsModel> qb = commsModelDao.queryBuilder();
		qb.where(BatchModelDao.Properties.Hdt_index.eq(hostId));
		return qb.list().get(0);
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
