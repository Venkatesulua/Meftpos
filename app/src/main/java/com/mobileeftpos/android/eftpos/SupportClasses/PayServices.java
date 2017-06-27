package com.mobileeftpos.android.eftpos.SupportClasses;

import android.util.Log;

import com.mobileeftpos.android.eftpos.database.DBHelper;
import com.mobileeftpos.android.eftpos.model.TraceNumberModel;

/**
 * Created by venkat on 6/9/2017.
 */
public class PayServices {

    private final String TAG ="my_custom_msg";
    public String pGetSystemTrace(DBHelper databaseObj)
    {
        Log.i(TAG,"pGetSystemTrace");
        TraceNumberModel traceno=new TraceNumberModel();
        traceno= databaseObj.getTraceNumberData(0);

        Log.i(TAG,"pGetSystemTrace_2");
        if(traceno == null)
        {
            TraceNumberModel traceno1=new TraceNumberModel();
            Log.i(TAG,"pGetSystemTrace_3");
            traceno1.setSYSTEM_TRACE("000001");
            databaseObj.InsertTraceNumberData(traceno1);
        }
        Log.i(TAG,"pGetSystemTrace_4::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"pGetSystemTrace_4::"+traceno.getSYSTEM_TRACE());

        traceno= databaseObj.getTraceNumberData(0);

        Log.i(TAG,"pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());
        Log.i(TAG,"pGetSystemTrace_5::"+traceno.getSYSTEM_TRACE());

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

}
