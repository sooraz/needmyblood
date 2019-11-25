package com.example.needforbloodv1.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.needforbloodv1.AppContoller;
import org.json.JSONObject;

public class MyVolley {
    static Handler activityHandler=null;
    public static void setActivityHandler(Handler handler){
        activityHandler=handler;
    }
    public static void connectGET(String url,Context c) {
//        String resp;
        final ProgressDialog pDialog = new ProgressDialog(c);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject temp = new JSONObject(response);
                            Message msg = new Message();
                            msg.arg1 = temp.getInt("serverResponce");
                            msg.obj = response;
                            activityHandler.sendMessage(msg);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                pDialog.dismiss();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }

}
