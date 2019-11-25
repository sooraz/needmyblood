package com.example.needforbloodv1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.needforbloodv1.sharedpref.NFBSharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mUserName,mPassword;
    EditText mEmail,mGender,mBGroup,mPLocation;
    TextView mErrorText;
    ImageView mProPic;
    Bitmap mBitmap;
    ProgressDialog pDialog;
    Context c;

    public static final String MyPREFERENCES = "NFB" ;
    public static final String mFcm = "fcm";
    final private String URL="http://sooraz.000webhostapp.com/need_for_blood/";


    static final int PICK_IMAGE_REQUEST = 1;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        String uName=NFBSharedPreference.getUserName(c);
        if(uName.equals("0")) {
            setContentView(R.layout.activity_main);
            NFBSharedPreference.clearData(c);
            setToken();
            mUserName = findViewById(R.id.editText);
            mPassword = findViewById(R.id.editText2);
            mErrorText = findViewById(R.id.errorText);
        }
        else{
            //write condition for allowing
//            Log.d("sooraz","sendLocationToServer()");
            sendLocationToServer();
            Intent i=new Intent(c,After_Login.class);
            i.putExtra("name",uName);
            startActivity(i);
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void sendLocationToServer() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Log.d("sooraz",location.getLatitude()+" "+location.getLongitude());
                                sendDatatoVolley(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

                            }
                        }
                    });
        }
        catch(SecurityException e){
            Log.d("sooraz","permission denied "+e);
        }
    }

    public void start(View view) {
        switch(view.getId()){
            case R.id.login:
                login();
                break;
            case R.id.register:
                setContentView(R.layout.register);
                mProPic=findViewById(R.id.image_browse);
                break;
            case R.id.make_register:
                register();
                break;
        }
    }
    private void login() {
//        NFBSharedPreference.clearData(c);
//        setToken();

        mUserName=findViewById(R.id.editText);
        mPassword=findViewById(R.id.editText2);
        final String name=mUserName.getText().toString();
        final String password=mPassword.getText().toString();
        String url = URL+"login.php";
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
                                        sendLocationToServer();
                                        Intent i=new Intent(c,After_Login.class);
                                        i.putExtra("name",name);
                                        startActivity(i);
                                        NFBSharedPreference.setUserName(c,name);
                                        //NFBSharedPreference
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
                    params.put("fcm_token", NFBSharedPreference.getFCMKey(c));
                    Log.d("sooraz", " in map " + params);

                    return params;
                }

            };

// Adding request to request queue
            AppContoller.getInstance().addToRequestQueue(strReq);
        }
    }
    private void register() {
        mUserName=findViewById(R.id.name);
        mPassword=findViewById(R.id.password);
        mEmail=findViewById(R.id.email);
        mGender=findViewById(R.id.gender);
        mBGroup=findViewById(R.id.bgroup);
        mPLocation=findViewById(R.id.plocation);
        //setToken();
        final String token=NFBSharedPreference.getFCMKey(c);
        final String name=mUserName.getText().toString(),password=mPassword.getText().toString(),gender=mGender.getText().toString(),location=mPLocation.getText().toString()
                ,bgroup=mBGroup.getText().toString(),mail=mEmail.getText().toString();
        final String img=getImageString();
        String url = URL+"register.php";
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
                                        //mErrorText.setVisibility(View.VISIBLE);
                                        //mErrorText.setText(temp.getString("message"));
                                        break;
                                    case 1:
                                        startActivity(new Intent(c,MainActivity.class));

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
                    params.put("mail", mail);
                    params.put("gender", gender);
                    params.put("bgroup", bgroup);
                    params.put("location", location);
                    params.put("image",img);
                    //mail,final String gender,final String bgroup,final String location
                    Log.d("sooraz", " in map " + params);

                    return params;
                }

            };

// Adding request to request queue
            AppContoller.getInstance().addToRequestQueue(strReq);
        setContentView(R.layout.activity_main);

    }
    void setToken()
    {
        final String[] token = new String[2];
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("sooraz", "getInstanceId failed", task.getException());
                            return;
                        }
                        String /*token*/ msg = task.getResult().getId();
                        token[0] =msg;
                        Log.d("sooraz", task.getResult().getToken());
                        token[1] = task.getResult().getToken();
                        NFBSharedPreference.setFCMKey(c,task.getResult().getToken());
                        Log.d("sooraz","NFBSharedPreference settotken :"+NFBSharedPreference.getFCMKey(c));
                        try {
                            FirebaseInstanceId.getInstance().getToken(task.getResult().getToken(),"FCM");
                        } catch (IOException e) {
                            Log.d("sooraz", "error ra");
                        }

//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //String  authorizedEntity="myfirstapp-af0af",scope="FCM";
    }

    public void browse(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    if(mProPic!=null) {
                        mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        mProPic.setImageBitmap(mBitmap);
        //uploadImage(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "img picking Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private String getImageString(){
        if(mBitmap!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }
        else
            return null;
        }

        private void sendDatatoVolley(String lat,String lng){
            final String url = String.format(URL + "sendLocationData.php?lat=%1$s&lng=%2$s&name=%3$s", lat, lng, NFBSharedPreference.getUserName(this));
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
                                Log.d("sooraz","location responce correct");
//                                Message msg = new Message();
//                                msg.arg1 = temp.getInt("serverResponce");
//                                msg.obj = response;
//                                activityHandler.sendMessage(msg);
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
                    Log.d("sooraz","location responce error");
                    pDialog.hide();
                    pDialog.dismiss();
                }

            }
            );

// Adding request to request queue
            AppContoller.getInstance().addToRequestQueue(jsonObjReq);

        }
}
