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
import com.example.needforbloodv1.activityRefernce.ActivityInterface;
import com.example.needforbloodv1.adapter.DonorSearchListAdapter;
import com.example.needforbloodv1.define.ServerFile;
import com.example.needforbloodv1.enums.NFBEnum;
import com.example.needforbloodv1.sharedpref.NFBSharedPreference;
import com.example.needforbloodv1.volley.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class After_Login extends AppCompatActivity {
    private String username=null;
    TextView profile,mDonorProfile,mNoNotificationText;
    String mCurrDonor;
    ImageView mProfileImg,mDonorDP;
    FrameLayout mSearchLayout,mSearchList, mSettingsView;
    ScrollView mDonorProfileView,mNotificationView;
    EditText mLocationText,mBGrouptext,mDonorMsg,mMapKM;
    ListView searchListView,notificationListView;
    ActivityInterface mActivity;
    
    Context c;
    final private String URL="http://sooraz.000webhostapp.com/need_for_blood/";
    final private String META_PATH="http://sooraz.000webhostapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username=getIntent().getStringExtra("name");
        MyVolley.setActivityHandler(responceHandler);
        init();
    }

    private void init(){
        c=this;
        profile=(TextView)findViewById(R.id.profile);
        mProfileImg=(ImageView)findViewById(R.id.profile_img);
        mDonorProfile=(TextView)findViewById(R.id.donor_name);
        mDonorDP=(ImageView)findViewById(R.id.donor_dispic);
        mSearchLayout=(FrameLayout)findViewById(R.id.search_Layout);//search
        mSearchList=(FrameLayout)findViewById(R.id.list_frame);//search results
        mSettingsView=(FrameLayout)findViewById(R.id.settings_view);
        mDonorProfileView=(ScrollView)findViewById(R.id.donor_profile);
        mNotificationView=(ScrollView)findViewById(R.id.notification_view);
        mNoNotificationText=(TextView)findViewById(R.id.no_notification);
        mLocationText=(EditText)findViewById(R.id.location_search);
        mBGrouptext=(EditText)findViewById(R.id.group_search);
        mDonorMsg=(EditText)findViewById(R.id.donor_msg);
        mMapKM=(EditText)findViewById(R.id.map_km);
        searchListView=(ListView)findViewById(R.id.user_list);
        notificationListView=(ListView)findViewById(R.id.notification_list);
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
                        temp=mDonorProfile;
                        Glide.with(c).load(META_PATH + img_path).into(mDonorDP);
                        break;
                    case ServerFile.DISPLAYPROFILE:
                        temp=profile;
                        Glide.with(c).load(META_PATH + img_path).into(mProfileImg);
                        break;
                }
                if(temp != null) {
                    temp.setText("location:" + resp.getString("location") + "\n" +
                            "mail:" + resp.getString("mail") + "\n" +
                            "gender:" + resp.getString("gender") + "\n" +
                            "bgroup:" + resp.getString("bgroup"));

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
        makeAllViewGone();
        mDonorProfile.setVisibility(View.VISIBLE);
        profile.setVisibility(View.VISIBLE);
        mProfileImg.setVisibility(View.VISIBLE);
        profile(username,ServerFile.DISPLAYPROFILE);
    }
    public void profile(String uname,int code){
        final String url = String.format(URL+"view_profile.php?name=%1$s&usertype=%2$s",uname,code);
        MyVolley.connectGET(url,this);
    }


    public void search(View v) {
        makeAllViewGone();
        mDonorProfileView.setVisibility(View.GONE);
        mSearchList.setVisibility(View.VISIBLE);
        String location = mLocationText.getText().toString();
        String bgroup = mBGrouptext.getText().toString();
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
                                    final DonorSearchListAdapter adapter = new DonorSearchListAdapter(c,R.layout.donor_search_item, list, NFBEnum.ADAPTERTYPE.DONOR);
                                    searchListView.setAdapter(adapter);
                                    setListner(searchListView, NFBEnum.LISTTYPE.DONOR);
                                    break;

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
    private void setListner(ListView mListView, NFBEnum.LISTTYPE type) {
        switch (type){
            case DONOR:
                donorListItem(mListView);
                break;
            case NOTIFICATION:
                notificationListItem(mListView);
                break;
        }
    }
    private void donorListItem(ListView mListView){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.list_name);
                String name = temp.getText().toString();
                mCurrDonor = name;
                profile.setVisibility(View.GONE);
                profile(name, ServerFile.DISPLAYDONOR);
                makeAllViewGone();
                mDonorProfileView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void notificationListItem(ListView mListView){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.notification_sender);
                String name = temp.getText().toString();
                temp = view.findViewById(R.id.notification_time);
                String time = temp.getText().toString();
                makeAllViewGone();
                final String url = String.format(URL + "viewNotification.php?from=%1$s&to=%2$s&time=%2$s", name, NFBSharedPreference.getUserName(c),time);
                MyVolley.connectGET(url,c);
            }
        });
    }

    public void showSearchLayout(View view) {
        makeAllViewGone();
        profile.setVisibility(View.GONE);
        mProfileImg.setVisibility(View.GONE);
        mSearchLayout.setVisibility(View.VISIBLE);
    }

    public void logout(View view) {
    logout();
    }
    private void logout(){
        NFBSharedPreference.clearData(c);
        Intent i=new Intent(c,MainActivity.class);
        startActivity(i);
        finish();


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

    private void viewNotification(String response){
        try {
            JSONObject temp = new JSONObject(response);
            switch (temp.getInt("success")) {
                case 1:
                     //name,time,message
                    String tex="from :: "+temp.getString("name")
                            +"\n time :: "+temp.getString("time")
                            +"\n message :: "+temp.getString("message")
                            +"\n to :: "+NFBSharedPreference.getUserName(c);
                            ;
                    Toast.makeText(After_Login.this, tex, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.d("sooraz"," viewNotification send failed");
                    //error

            }
        }catch (Exception e) {
            Log.d("sooraz","error viewNotification");
            e.printStackTrace();
        }
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
            makeAllViewGone();
            mNotificationView.setVisibility(View.VISIBLE);
            switch (temp.getInt("tol_noti")) {
                case 0:
//                                    no notifications
                    notificationListView.setVisibility(View.GONE);
                    mNoNotificationText.setVisibility(View.VISIBLE);
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
                    displayNotificationList(list);
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
}
    private void displayNotificationList(ArrayList<List<String>> mList){
        mNoNotificationText.setVisibility(View.GONE);
        notificationListView.setVisibility(View.VISIBLE);

        DonorSearchListAdapter notificationAdapter=new DonorSearchListAdapter(c,R.layout.notification_list,mList,NFBEnum.ADAPTERTYPE.NOTIFICATION);
        notificationListView.setAdapter(notificationAdapter);
        setListner(notificationListView, NFBEnum.LISTTYPE.NOTIFICATION);
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
                case ServerFile.GETNOTIFICATIONS:
                    notificationDisplay(msg.obj.toString());
                    break;
                case ServerFile.VIEWNOTIFICATION:
                    viewNotification(msg.obj.toString());
                    break;


            }
        }
    };
    private void makeAllViewGone(){
        mSearchLayout.setVisibility(View.GONE);
        mSearchList.setVisibility(View.GONE);
        mDonorProfileView.setVisibility(View.GONE);
        mNotificationView.setVisibility(View.GONE);
        mSettingsView.setVisibility(View.GONE);
    }

    public void displayMap(View view) {
//        int km=Integer.parseInt(mMapKM.getText().toString());



        Intent i=new Intent(c,MapsActivity.class);
        startActivity(i);
    }

    public void viewSettings(View view) {
        makeAllViewGone();
        mSettingsView.setVisibility(View.VISIBLE);

    }

    public void updateLocation(View view) {

    }
}


