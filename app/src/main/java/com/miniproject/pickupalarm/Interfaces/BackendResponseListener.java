package com.miniproject.pickupalarm.Interfaces;

import org.json.JSONObject;

public interface BackendResponseListener {
    void backendResponse(boolean isError, JSONObject response);
}
