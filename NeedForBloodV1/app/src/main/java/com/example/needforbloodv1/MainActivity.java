package com.example.needforbloodv1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.needforbloodv1.sharedpref.NFBSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
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


    static final int PICK_IMAGE_REQUEST = 1;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        String uName=NFBSharedPreference.getUserName(c);
        if(uName.equals("0")) {
            setContentView(R.layout.activity_main);
            mUserName = findViewById(R.id.editText);
            mPassword = findViewById(R.id.editText2);
            mErrorText = findViewById(R.id.errorText);
        }
        else{
            Intent i=new Intent(c,After_Login.class);
            i.putExtra("name",uName);
            startActivity(i);
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void start(View view) {
        switch(view.getId()){
            case R.id.login:
                login(mUserName.getText().toString(),mPassword.getText().toString());
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
    private void login(final String name, final String password) {
        NFBSharedPreference.clearData(c);
        String url = "http://sooraz.000webhostapp.com/login.php";
        if (name == null || name.equals("")) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText("enter name");

        } else if (password == null || password.equals("")) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText("enter password");
        } else {
            Log.d("sooraz","tes1");
            setToken();
            Log.d("sooraz","tes2");
            mErrorText.setVisibility(View.GONE);
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.d("sooraz","tes3");
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
                    //params.put("fcm_token", NFBSharedPreference.getFCMKey(c));
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
        String url = "http://sooraz.000webhostapp.com/register.php";
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

}
