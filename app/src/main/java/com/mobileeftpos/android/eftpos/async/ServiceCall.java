package com.mobileeftpos.android.eftpos.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mobileeftpos.android.eftpos.utils.AppUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prathap on 17/04/16.
 */
public abstract class ServiceCall {
    Context mContext;
    private String TAG = "Service Class";
    private Button btnJsonObj, btnJsonArray;
    private TextView msgResponse;
    private ProgressDialog pDialog;


    public ServiceCall(Context mContext) {
        this.mContext = mContext;
        pDialog = ProgressDialog.show(this.mContext, Const.PROGRESS_DIALOG_TITLE,
                Const.PROGRESS_DIALOG_MESSAGE, true);
    }


    public abstract void onResp(JSONObject response);

    public abstract void onError(VolleyError error);


    public void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }


    public void makeJsonObjReq(String url, JSONObject jsonObject) {
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideProgressDialog();

                        onResp(response);

                    }
                }, new Response.ErrorListener(

        ) {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
                onError(error);
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //     headers.put("Content-Type", "application/json");
                return headers;
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjReq);

    }

    public void makeJsonObjGETReq(String url, JSONObject jsonObject, final String VGROWKEY) {

        {
            showProgressDialog();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            hideProgressDialog();
                            onResp(response);


                        }
                    }, new Response.ErrorListener(

            ) {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                    onError(error);
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("VGROWKEY", VGROWKEY);

                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjReq);

        }
    }

    public void makeJsonObjPUTReq(String url, JSONObject jsonObject, final String VGROWKEY) {
        {
            //  Log.e(TAG, "URL IS: "+url);
            showProgressDialog();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            hideProgressDialog();
                            onResp(response);

                        }
                    }, new Response.ErrorListener(

            ) {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                    onError(error);
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("VGROWKEY", VGROWKEY);

                    return headers;
                }


            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjReq);


        }
    }

    public void makeJsonObjPOSTReq(String url, JSONObject jsonObject) {

        {
            Log.e(TAG, "URL IS: " + url);
            showProgressDialog();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            hideProgressDialog();
                            onResp(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                    onError(error);
                }
            }) {

                /**
                 * Passing some request headers
                 */
               /* @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    return headers;
                }*/
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjReq);
        }
    }
}
