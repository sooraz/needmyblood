package com.example.needforbloodv1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mUserName,mPassword;
    EditText mEmail,mGender,mBGroup,mPLocation;
    TextView mErrorText;
    ProgressDialog pDialog;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName=findViewById(R.id.editText);
        mPassword=findViewById(R.id.editText2);
        mErrorText=findViewById(R.id.errorText);
        c=this;
        
    }

    public void start(View view) {
        switch(view.getId()){
            case R.id.login:
                login(mUserName.getText().toString(),mPassword.getText().toString());
                break;
            case R.id.register:
                register();
                break;
        }
    }
    private void login(final String name, final String password) {
        String url = "http://sooraz.000webhostapp.com/login.php";
        if (name == null || name.equals("")) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText("enter name");

        } else if (password == null || password.equals("")) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText("enter password");
        } else {
            mErrorText.setVisibility(View.GONE);
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("sooraz", response);
                                JSONObject temp = new JSONObject(response);
                                switch (temp.getInt("success")) {
                                    case 0:
                                        mErrorText.setVisibility(View.VISIBLE);
                                        //mErrorText.setText(temp.getString("message"));
                                        break;
                                    case 1:
                                        Log.d("sooraz", "Logeed in success");
                                        Intent i=new Intent(c,After_Login.class);
                                        i.putExtra("name",name);
                                        startActivity(i);
                                        //activitys

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pDialog.hide();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("sooraz", "Error: " + error.getMessage());
                    pDialog.hide();
                }

            }
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("password", password);
                    //params.put("password", "password123");
                    Log.d("sooraz", " in map " + params);

                    return params;
                }

            };

// Adding request to request queue
            AppContoller.getInstance().addToRequestQueue(strReq);
        }
    }
    private void register() {
        setContentView(R.layout.register);
        mUserName=findViewById(R.id.name);
        mPassword=findViewById(R.id.password);
        mEmail=findViewById(R.id.email);
        mGender=findViewById(R.id.gender);
        mBGroup=findViewById(R.id.bgroup);
        mPLocation=findViewById(R.id.plocation);
        final String name=mUserName.getText().toString(),password=mPassword.getText().toString(),gender=mGender.getText().toString(),location=mPLocation.getText().toString()
                ,bgroup=mBGroup.getText().toString(),mail=mEmail.getText().toString();
//        String url = "http://sooraz.000webhostapp.com/register.php";
////        if (name == null || name.equals("")) {
////            mErrorText.setVisibility(View.VISIBLE);
////            mErrorText.setText("fields missing");
////
////        } else if (password == null || password.equals("")) {
////            mErrorText.setVisibility(View.VISIBLE);
////            mErrorText.setText("enter password");
////        } else {
////            mErrorText.setVisibility(View.GONE);
////            pDialog = new ProgressDialog(this);
////            pDialog.setMessage("Loading...");
////            pDialog.show();
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                Log.d("sooraz", response);
//                                JSONObject temp = new JSONObject(response);
//                                switch (temp.getInt("success")) {
//                                    case 0:
//                                        mErrorText.setVisibility(View.VISIBLE);
//                                        mErrorText.setText(temp.getString("message"));
//                                        break;
//                                    case 1:
//                                        Log.d("sooraz", "registered success");
//
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            pDialog.hide();
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("sooraz", "Error: " + error.getMessage());
//                    pDialog.hide();
//                }
//
//            }
//            ) {
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("name", name);
//                    params.put("password", password);
//                    params.put("mail", mail);
//                    params.put("gender", gender);
//                    params.put("bgroup", bgroup);
//                    params.put("location", location);
//                    //mail,final String gender,final String bgroup,final String location
//                    Log.d("sooraz", " in map " + params);
//
//                    return params;
//                }
//
//            };
//
//// Adding request to request queue
//            AppContoller.getInstance().addToRequestQueue(strReq);
        setContentView(R.layout.activity_main);

    }
}
