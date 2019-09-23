package com.example.needforbloodv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.needforbloodv1.adapter.MyCustomAdapter;
import com.example.needforbloodv1.define.ServerFile;
import com.example.needforbloodv1.sharedpref.NFBSharedPreference;
import com.example.needforbloodv1.volley.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class After_Login extends AppCompatActivity {
    private String username=null;
    TextView profile,donor_profile;
    String mCurrDonor;
    ImageView profile_img,mDonorDP;
    FrameLayout search_layout,search_list;
    ScrollView donor_profile_view;
    EditText location_search,bgroup_search,mDonorMsg;
    ListView lv;
    Context c;
    final private String URL="http://sooraz.000webhostapp.com/need_for_blood/";
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
        mDonorDP=(ImageView)findViewById(R.id.donor_dispic);
        search_layout=(FrameLayout)findViewById(R.id.search_Layout);
        search_list=(FrameLayout)findViewById(R.id.list_frame);
        donor_profile_view=(ScrollView)findViewById(R.id.donor_profile);
        location_search=(EditText)findViewById(R.id.location_search);
        bgroup_search=(EditText)findViewById(R.id.group_search);
        mDonorMsg=(EditText)findViewById(R.id.donor_msg);
        lv=(ListView)findViewById(R.id.user_list);
        username=getIntent().getStringExtra("name");
        MyVolley.setActivityHandler(responceHandler);
        initList();
    }
    private void initList(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                display_Donor();
            }
        });
    }
    public void display_Donor(){

        profile.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.VISIBLE);
        donor_profile.setVisibility(View.VISIBLE);
        mDonorDP.setVisibility(View.VISIBLE);
        mDonorMsg.setVisibility(View.VISIBLE);
        profile_img.setVisibility(View.GONE);

        final String url = String.format(URL+"view_profile.php?name=%1$s",username);
        MyVolley.connectGET(url,this);
    }
    //method for both donor and self
    private void display_Profile(String responce,int profileType){
        try {
        JSONObject resp = new JSONObject(responce);
        TextView temp=null;
            if(resp.getInt("success")==1) {
                String img_path = null;
                img_path = resp.getString("image_path");
                switch(profileType){
                    case ServerFile.DISPLAYDONOR:
                        temp=donor_profile;
                        Glide.with(c).load(META_PATH + img_path).into(mDonorDP);
                        break;
                    case ServerFile.DISPLAYPROFILE:
                        temp=profile;
                        Glide.with(c).load(META_PATH + img_path).into(profile_img);
                        break;
                }
                if(temp != null) {
                    temp.setText("location:" + resp.getString("location") + "\n" +
                            "mail:" + resp.getString("mail") + "\n" +
                            "gender:" + resp.getString("gender") + "\n" +
                            "bgroup:" + resp.getString("bgroup"));
//                    Log.d("sooraz", "location:" + resp.getString("location") + "\n" +
//                            "mail:" + resp.getString("mail") + "\n" +
//                            "gender:" + resp.getString("gender") + "\n" +
//                            "bgroup:" + resp.getString("bgroup"));

                }
            }
            else{
                //fail  send to server
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void viewProfile(View v) {
        search_layout.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        donor_profile.setVisibility(View.VISIBLE);
        profile.setVisibility(View.VISIBLE);
        profile_img.setVisibility(View.VISIBLE);
        profile(username,ServerFile.DISPLAYPROFILE);
    }
    public void profile(String uname,int code){
        final String url = String.format(URL+"view_profile.php?name=%1$s&usertype=%2$s",uname,code);
        MyVolley.connectGET(url,this);
    }


    public void search(View v) {

        profile.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        search_list.setVisibility(View.VISIBLE);
        String location = location_search.getText().toString();
        String bgroup = bgroup_search.getText().toString();
        final String url = String.format(URL + "search.php?location=%1$s&bgroup=%2$s", location, bgroup);
        MyVolley.connectGET(url,this);
    }
    private void search(String response){
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
                    }
    private void setLister(ListView lv) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp=view.findViewById(R.id.list_name);
                String name=temp.getText().toString();
                mCurrDonor=name;
                profile.setVisibility(View.GONE);
                profile(name,ServerFile.DISPLAYDONOR);
                search_list.setVisibility(View.GONE);
                donor_profile_view.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
            }
        });
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
        String msg=mDonorMsg.getText().toString();
        final String url = String.format(URL+"sendNotification.php?from_name=%1$s&to_name=%2$s&message=%3$s",NFBSharedPreference.getUserName(c),mCurrDonor,msg);
        MyVolley.connectGET(url,this);
    }
   //callback request
    private void sendNotificationAck(String response) {
        Log.d("sooraz","mCurrDonor: "+mCurrDonor+" NFBSharedPreference.getUserName(c): "+NFBSharedPreference.getUserName(c));
                        try {
                            JSONObject temp = new JSONObject(response);
                            switch (temp.getInt("success")) {
                                case 1:
//                                    Log.d("sooraz","sendNotification response :"+response);
                                    Toast.makeText(After_Login.this, "sent request", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    //error

                            }
                        }catch (Exception e) {
                            Log.d("sooraz","error profile");
                            e.printStackTrace();
                        }
    }

    public void showNotifications(View view) {
        final String url = String.format(URL+"getNotifications.php?to=%1$s",NFBSharedPreference.getUserName(this));
        MyVolley.connectGET(url,this);

    }
    private void notificationDisplay(String response){
        try {
            JSONObject temp = new JSONObject(response);
            if(temp.getInt("success")==0){
                return;
            }
            switch (temp.getInt("tol_noti")) {
                case 0:
//                                    no notifications
                    profile.setText("no requests are available");
                    break;
                default:

                    final ArrayList<List<String>> list = new ArrayList<List<String>>();
                    //list.add(Arrays.asList("Name","Location","Blood_group"));
                    int size=temp.getInt("tol_noti");
                    for (int i = 0; i < size; ++i) {
                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
                        list.add(Arrays.asList(looptemp.getString("name"),looptemp.getString("time")));
                    }
                    break;
// adapterlogic
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
}
    private Handler responceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case ServerFile.SEARCH:
                    search(msg.obj.toString());
                    break;
                case ServerFile.SENDNOTIFICATION:
                    sendNotificationAck(msg.obj.toString());
                    break;
                case ServerFile.DISPLAYDONOR:
                case ServerFile.DISPLAYPROFILE:
                    display_Profile(msg.obj.toString(),msg.arg1);
                    break;
                case ServerFile.GETNOTIFICATION:
                    notificationDisplay(msg.obj.toString());
                    break;


            }
        }
    };

}


