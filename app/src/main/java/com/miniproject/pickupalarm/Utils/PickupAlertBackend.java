package com.miniproject.pickupalarm.Utils;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miniproject.pickupalarm.Interfaces.BackendResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PickupAlertBackend {

    private BackendResponseListener backendResponseListener;
    private RequestQueue queue;

    public PickupAlertBackend(Activity activity, BackendResponseListener backendResponseListener) {
        this.backendResponseListener = backendResponseListener;
        this.queue = Volley.newRequestQueue(activity.getApplicationContext());
    }

    public PickupAlertBackend postRequest(String url, JSONObject data, JSONObject header) {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        backendResponseListener.backendResponse(false, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    try {
                        JSONObject data = new JSONObject(new String(networkResponse.data));
                        backendResponseListener.backendResponse(true, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                for (Iterator<String> it = header.keys(); it.hasNext(); ) {
                    String key = it.next();
                    try {
                        headers.put(key, header.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return headers;
            }
        };
        queue.add(objectRequest);
        return this;
    }
}
