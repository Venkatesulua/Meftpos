package com.mobileeftpos.android.eftpos.async;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Prathap on 4/26/17.
 */

public interface OnServiceCallCompleteListener {

	void onJSONObjectResponse(JSONObject jsonObject);

	void onJSONArrayResponse(JSONArray jsonArray);

	void onErrorResponse(VolleyError error);
}
