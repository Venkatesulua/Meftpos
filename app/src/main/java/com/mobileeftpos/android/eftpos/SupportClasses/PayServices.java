package com.mobileeftpos.android.eftpos.SupportClasses;

import android.util.Log;

import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.HostModel;
import com.mobileeftpos.android.eftpos.model.TraceNumberModel;

/**
 * Created by venkat on 6/9/2017.
 */
public class PayServices {

    private final String TAG ="my_custom_msg";
    public String pGetSystemTrace(DBHelper databaseObj)
    {
        Log.i(TAG,"PayServices::pGetSystemTrace");
        TraceNumberModel traceno=new TraceNumberModel();
        traceno= databaseObj.getTraceNumberData(0);

        Log.i(TAG,"PayServices::pGetSystemTrace_2");
        if(traceno == null)
        {
            TraceNumberModel traceno1=new TraceNumberModel();
            Log.i(TAG,"PayServices::pGetSystemTrace_3");
            traceno1.setSYSTEM_TRACE("000001");
            databaseObj.InsertTraceNumberData(traceno1);
        }
        Log.i(TAG,"PayServices::pGetSystemTrace_4::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"PayServices::getTRACE_UNIQUE_ID::"+traceno.getTRACE_UNIQUE_ID());

        traceno= databaseObj.getTraceNumberData(0);

        Log.i(TAG,"PayServices::pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"PayServices::pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());

        return traceno.getSYSTEM_TRACE();
    }

    public void vdUpdateSystemTrace(DBHelper databaseObj)
    {
        TraceNumberModel traceno=new TraceNumberModel();
        traceno= databaseObj.getTraceNumberData(0);
        long ulSystemTraceL=0L;
        if(traceno.getSYSTEM_TRACE()!=null && !traceno.getSYSTEM_TRACE().equalsIgnoreCase("null")) {
            ulSystemTraceL = Integer.parseInt(traceno.getSYSTEM_TRACE());
        }
        if (++ulSystemTraceL>=900000L)
            ulSystemTraceL=1L;

        String newno = String.format("%06d", ulSystemTraceL);
        traceno.setSYSTEM_TRACE(newno);
        databaseObj.UpdateTraceNumberData(traceno);
    }

    public void vdUpdateSystemBatch(DBHelper databaseObj)
    {
        HostModel hostData=new HostModel();
        hostData = databaseObj.getHostTableData(TransactionDetails.inGHDT);
        long ulSystemTraceL=0L;
        if(hostData.getHDT_BATCH_NUMBER()!=null && !hostData.getHDT_BATCH_NUMBER().equalsIgnoreCase("null")) {
            ulSystemTraceL = Integer.parseInt(hostData.getHDT_BATCH_NUMBER());
        }
        if (++ulSystemTraceL>=900000L)
            ulSystemTraceL=1L;

        String newno = String.format("%06d", ulSystemTraceL);
        hostData.setHDT_BATCH_NUMBER(newno);
        databaseObj.UpdateHostData(hostData);
    }

}
