package com.example.needforbloodv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.needforbloodv1.adapter.MyCustomAdapter;
import com.example.needforbloodv1.sharedpref.NFBSharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class After_Login extends AppCompatActivity {
    private String username=null;
    TextView profile,donor_profile;
    String mCurrDonor;
    ImageView profile_img;
    FrameLayout search_layout,search_list,donor_profile_view;
    EditText location_search,bgroup_search;
    ListView lv;
    Context c;
    final private String META_PATH="http://sooraz.000webhostapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Button b=(Button)findViewById(R.id.view_profile);
        c=this;
        profile=(TextView)findViewById(R.id.profile);
        profile_img=(ImageView)findViewById(R.id.profile_img);
        donor_profile=(TextView)findViewById(R.id.donor_name);
        search_layout=(FrameLayout)findViewById(R.id.search_Layout);
        search_list=(FrameLayout)findViewById(R.id.list_frame);
        donor_profile_view=(FrameLayout)findViewById(R.id.donor_profile);
        location_search=(EditText)findViewById(R.id.location_search);
        bgroup_search=(EditText)findViewById(R.id.group_search);
        lv=(ListView)findViewById(R.id.user_list);
        username=getIntent().getStringExtra("name");

        initList();
    }
    private void initList(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  //Log.d("sooraz",((TextView)view.findViewById(R.id.list_location)).getText().toString());
                //mCurrDonor=((TextView)view.findViewById(R.id.list_name)).getText().toString();
                display_Donor();
            }
        });
    }
    public void display_Donor(){

        profile.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.VISIBLE);
        profile_img.setVisibility(View.VISIBLE);

        final String url = String.format("http://sooraz.000webhostapp.com/view_profile.php?name=%1$s",username);
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
                                    JSONObject resp = new JSONObject(response);
                                    String img_path=resp.getString("image_path");
                                    donor_profile.setText("location:"+resp.getString("location")+"\n"+
                                            "mail:"+resp.getString("mail")+"\n"+
                                            "gender:"+resp.getString("gender")+"\n"+
                                            "bgroup:"+resp.getString("bgroup"));
                                    Glide.with(c).load(META_PATH+img_path).into(profile_img);
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            Log.d("sooraz","error profile");
                            e.printStackTrace();
                        }
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog.hide();
                pDialog.dismiss();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void viewProfile(View v) {
        search_layout.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        profile.setVisibility(View.VISIBLE);
        profile_img.setVisibility(View.VISIBLE);
        profile(username);
    }
    public void profile(String uname){
        final String url = String.format("http://sooraz.000webhostapp.com/view_profile.php?name=%1$s",uname);
//String.format(."http://somesite.com/some_endpoint.php?param1=%1$s&param2=%2$s",
//                           username,
//                           num2);
        final ProgressDialog pDialog1 = new ProgressDialog(this);
        pDialog1.setMessage("Loading...");
        pDialog1.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject temp = new JSONObject(response);
                            switch (temp.getInt("success")) {
                                case 1:JSONObject resp = new JSONObject(response);
                                    String img_path=resp.getString("image_path");
                                    profile.setText("location:"+resp.getString("location")+"\n"+
                                            "mail:"+resp.getString("mail")+"\n"+
                                            "gender:"+resp.getString("gender")+"\n"+
                                            "bgroup:"+resp.getString("bgroup"));
                                    Glide.with(c).load(META_PATH+img_path).into(profile_img);
                                    //profile.setText(response);
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            Log.d("sooraz","error profile");
                            e.printStackTrace();
                        }
                        pDialog1.hide();
                        pDialog1.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog1.hide();
                pDialog1.dismiss();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void search(View v){

//        final mItemClickListner mItemListner=new mItemClickListner(
//
//        );

        profile.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        search_list.setVisibility(View.VISIBLE);
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
                            switch (temp.getInt("tol_users")) {
                                case 0:
//                                    no users
                                    profile.setText("sorry no users with your search request");
                                    break;
                                default:

                                    final ArrayList<List<String>> list = new ArrayList<List<String>>();
                                    //list.add(Arrays.asList("Name","Location","Blood_group"));
                                    int size=temp.getInt("tol_users");
                                    for (int i = 0; i < size; ++i) {
                                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
                                        list.add(Arrays.asList(looptemp.getString("image_path"),looptemp.getString("name"),looptemp.getString("loc_p"),looptemp.getString("bgroup")));
                                    }
                                    final MyCustomAdapter adapter = new MyCustomAdapter(c,R.layout.list_item, list);
                                    lv.setAdapter(adapter);
                                    setLister(lv);
                                    break;

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                        pDialog.dismiss();
                    }

                    private void setLister(ListView lv) {
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView temp=view.findViewById(R.id.list_name);
                                    String name=temp.getText().toString();
                                    mCurrDonor=name;
                                    profile(name);
                                profile.setVisibility(View.VISIBLE);
                                search_list.setVisibility(View.GONE);
                                donor_profile_view.setVisibility(View.GONE);
                                search_layout.setVisibility(View.GONE);
                            }
                        });
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog.hide();
                pDialog.dismiss();
            }

        }

        );


// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void search_layout(View view) {
        profile.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        profile_img.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
    }

    public void logout(View view) {
    logout();
    }
    private void logout(){
        NFBSharedPreference.clearData(c);
        Intent i=new Intent(c,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        logout();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("You are Logging Out,Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void request(View view) {
        //notifiction to donar
        sendNotification(mCurrDonor);
    }

    private void sendNotification(String mCurrDonor) {
        final String url = String.format("https://sooraz.000webhostapp.com/sendNotification.php?from_name=%1$s&to_name=%2$s",NFBSharedPreference.getUserName(c),mCurrDonor);
        final ProgressDialog pDialog = new ProgressDialog(this);
        Log.d("sooraz","mCurrDonor: "+mCurrDonor+" NFBSharedPreference.getUserName(c): "+NFBSharedPreference.getUserName(c));
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
                                    Log.d("sooraz","sendNotification response :"+response);
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            Log.d("sooraz","error profile");
                            e.printStackTrace();
                        }
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("sooraz", "Error: " + error.getMessage());
                pDialog.hide();
                pDialog.dismiss();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);

    }
}


