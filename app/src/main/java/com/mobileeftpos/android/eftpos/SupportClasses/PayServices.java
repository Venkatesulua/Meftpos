package com.mobileeftpos.android.eftpos.SupportClasses;

import android.app.Activity;
import android.util.Log;

import com.mobileeftpos.android.eftpos.database.GreenDaoSupport;
import com.mobileeftpos.android.eftpos.db.DaoSession;
import com.mobileeftpos.android.eftpos.db.HostModel;
import com.mobileeftpos.android.eftpos.db.TraceModel;
import com.mobileeftpos.android.eftpos.db.TraceModelDao;

import java.util.List;

/**
 * Created by venkat on 6/9/2017.
 */
public class PayServices {

    private final String TAG ="my_custom_msg";
    public String pGetSystemTrace(Activity context)
    {
        Log.i(TAG,"PayServices::pGetSystemTrace");
         TraceModel traceno= GreenDaoSupport.getTraceModelOBJ(context);

        Log.i(TAG,"PayServices::pGetSystemTrace_2");
        if(traceno == null)
        {
            TraceModel traceno1=new TraceModel();
            Log.i(TAG,"PayServices::pGetSystemTrace_3");
            traceno1.setSYSTEM_TRACE("000001");
            GreenDaoSupport.insertTraceModelOBJ(context,traceno1);
            //databaseObj.InsertTraceNumberData(traceno1);
        }
        Log.i(TAG,"PayServices::pGetSystemTrace_4::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"PayServices::getTRACE_UNIQUE_ID::"+traceno.getTRACE_UNIQUE_ID());

        traceno=GreenDaoSupport.getTraceModelOBJ(context);// databaseObj.getTraceNumberData(0);

        Log.i(TAG,"PayServices::pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"PayServices::pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());

        return traceno.getSYSTEM_TRACE();
    }

    public void vdUpdateSystemTrace(DaoSession daoSession)
    {
        TraceModelDao traceModelDeo= daoSession.getTraceModelDao();
        List<TraceModel> traceModelsList= traceModelDeo.loadAll();
        TraceModel traceno=traceModelsList.get(0);

        long ulSystemTraceL=0L;
        if(traceno.getSYSTEM_TRACE()!=null && !traceno.getSYSTEM_TRACE().equalsIgnoreCase("null")) {
            ulSystemTraceL = Integer.parseInt(traceno.getSYSTEM_TRACE());
        }
        if (++ulSystemTraceL>=900000L)
            ulSystemTraceL=1L;

        String newno = String.format("%06d", ulSystemTraceL);
        traceno.setSYSTEM_TRACE(newno);
        traceModelDeo.insertOrReplace(traceno);
        //databaseObj.UpdateTraceNumberData(traceno);
    }

    public void vdUpdateSystemBatch(Activity activity)
    {
        HostModel hostData=new HostModel();
        //hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        hostData = GreenDaoSupport.getHostTableModelOBJ(activity);
        long ulSystemTraceL=0L;
        if(hostData.getHDT_BATCH_NUMBER()!=null && !hostData.getHDT_BATCH_NUMBER().equalsIgnoreCase("null")) {
            ulSystemTraceL = Integer.parseInt(hostData.getHDT_BATCH_NUMBER());
        }
        if (++ulSystemTraceL>=900000L)
            ulSystemTraceL=1L;

        String newno = String.format("%06d", ulSystemTraceL);
        hostData.setHDT_BATCH_NUMBER(newno);
        //databaseObj.UpdateHostData(hostData);
        GreenDaoSupport.insertHostModelOBJ(activity,hostData);
    }

}
