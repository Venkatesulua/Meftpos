package com.mobileeftpos.android.eftpos.utils;


import android.util.Log;

import com.google.gson.JsonParseException;
import com.mobileeftpos.android.eftpos.model.AlipayResponceModel;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import java.io.IOException;



@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONUtil {

    public static AlipayResponceModel parseTransactionResponce(JSONObject jsonObject)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        AlipayResponceModel object = mapper.readValue(jsonObject.toString(), AlipayResponceModel.class);

        return object;
    }




}
