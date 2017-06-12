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
        Log.i(TAG,"PayServices::vdUpdateSystemTrace_1");
        TraceNumberModel traceno=new TraceNumberModel();
        Log.i(TAG,"PayServices::vdUpdateSystemTrace_2");
        traceno= databaseObj.getTraceNumberData(0);
        Log.i(TAG,"PayServices::vdUpdateSystemTrace_3");
        long ulSystemTraceL=0L;

        ulSystemTraceL=Integer.parseInt(traceno.getSYSTEM_TRACE());
        Log.i(TAG,"PayServices::ulSystemTraceL"+ulSystemTraceL);

        if (++ulSystemTraceL>=900000L)
            ulSystemTraceL=1L;

        Log.i(TAG,"PayServices::vdUpdateSystemTrace_4");
        String newno = String.format("%06d", ulSystemTraceL);
        Log.i(TAG,"PayServices::newno"+newno);
        traceno.setSYSTEM_TRACE(newno);
        Log.i(TAG,"PayServices::vdUpdateSystemTrace_6");
        databaseObj.UpdateTraceNumberData(traceno);
        Log.i(TAG,"PayServices::vdUpdateSystemTrace_7");
    }

}
