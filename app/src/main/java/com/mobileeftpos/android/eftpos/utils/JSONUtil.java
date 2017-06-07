package com.mobileeftpos.android.eftpos.utils;


import android.util.Log;

import com.google.gson.JsonParseException;
import com.mobileeftpos.android.eftpos.model.AlipayResponceModel;
import com.vgrowedunet.parentapp.pojo.AssignmentPOJO;
import com.vgrowedunet.parentapp.pojo.AttendancePOJO;
import com.vgrowedunet.parentapp.pojo.ExamsDatesPOJO;
import com.vgrowedunet.parentapp.pojo.ExamsPOJO;
import com.vgrowedunet.parentapp.pojo.FeeTransactions;
import com.vgrowedunet.parentapp.pojo.LeaveApplicationPOJO;
import com.vgrowedunet.parentapp.pojo.LibraryBooksFinePOJO;
import com.vgrowedunet.parentapp.pojo.LibraryBooksReturnPOJO;
import com.vgrowedunet.parentapp.pojo.LibraryBooksTakenPOJO;
import com.vgrowedunet.parentapp.pojo.NotificationsPOJO;
import com.vgrowedunet.parentapp.pojo.PerformanceDetails;
import com.vgrowedunet.parentapp.pojo.PerformancePOJO;
import com.vgrowedunet.parentapp.stationary.ViewOrderItemsPOJO;
import com.vgrowedunet.parentapp.stationary.ViewOrdersPOJO;
import com.vgrowedunet.parentapp.transportnew.EndPointPOJO;
import com.vgrowedunet.parentapp.transportnew.RouteMap;
import com.vgrowedunet.parentapp.transportnew.StartPointPojo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONUtil {

    public static AlipayResponceModel parseTransactionResponce(JSONObject jsonObject)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        AlipayResponceModel object = mapper.readValue(jsonObject.toString(), AlipayResponceModel.class);

        return object;
    }




}
