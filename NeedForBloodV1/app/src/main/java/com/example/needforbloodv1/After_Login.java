package com.example.needforbloodv1;

import android.app.ProgressDialog;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class After_Login extends AppCompatActivity {
    private String username=null;
    TextView profile;
    FrameLayout search_layout;
    EditText location_search,bgroup_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Button b=(Button)findViewById(R.id.view_profile);
        profile=(TextView)findViewById(R.id.profile);
        search_layout=(FrameLayout)findViewById(R.id.search_Layout);
        location_search=(EditText)findViewById(R.id.location_search);
        bgroup_search=(EditText)findViewById(R.id.group_search);
        username=getIntent().getStringExtra("name");
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }
    public void viewProfile(View v){
        search_layout.setVisibility(View.GONE);
        profile.setVisibility(View.VISIBLE);
        final String url = String.format("http://sooraz.000webhostapp.com/view_profile.php?name=%1$s",username);
//String.format(."http://somesite.com/some_endpoint.php?param1=%1$s&param2=%2$s",
//                           username,
//                           num2);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject temp = new JSONObject(response);
                            switch (temp.getInt("success")) {
                                case 1:
                                    Log.d("sooraz",response);
                                    profile.setText(response);
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            Log.d("sooraz","error profile");
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog.hide();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void search(View v){

        profile.setVisibility(View.VISIBLE);
        search_layout.setVisibility(View.GONE);
        String location=location_search.getText().toString();
        String bgroup=bgroup_search.getText().toString();
        final String url = String.format("http://sooraz.000webhostapp.com/search.php?location=%1$s&bgroup=%2$s",location,bgroup);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject temp = new JSONObject(response);
                            switch (temp.getInt("success")) {
                                case 1:
                                    profile.setText(response);
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog.hide();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void search_layout(View view) {
        profile.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
    }
}
