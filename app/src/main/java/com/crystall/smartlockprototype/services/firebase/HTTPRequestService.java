package com.crystall.smartlockprototype.services.firebase;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Time;

public class HTTPRequestService {

    RequestQueue queue ;
    Context mContext;
    static boolean success= false;
    public HTTPRequestService (Context mContext){
        this.mContext = mContext;
        queue = Volley.newRequestQueue(mContext);

    }

    public void lockRequest(){


            String url = "http://www.google.com";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(mContext, response.substring(0, 20), Toast.LENGTH_LONG).show();
                    success = true;

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    success = false;
                }
            });
            queue.add(stringRequest);
        }









}
