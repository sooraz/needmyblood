package com.example.needforbloodv1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.example.needforbloodv1.activityRefernce.ActivityInterface;
import com.example.needforbloodv1.define.ServerFile;
import com.example.needforbloodv1.ui.search.SearchFragment;
import com.example.needforbloodv1.ui.user_profile.ProfileFragment;
import com.example.needforbloodv1.volley.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

public class AfterLogin_2 extends AppCompatActivity implements ActivityInterface {

    private AppBarConfiguration mAppBarConfiguration;
    ProfileFragment mProfile;
    SearchFragment mSearch;
    String username;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        username=getIntent().getStringExtra("name");
        MyVolley.setActivityHandler(responceHandler);


    }

    @Override
    protected void onResume() {
        super.onResume();
        init();

        profile(username,ServerFile.DISPLAYPROFILE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.after_login_2, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
//    private String username=null;
//    TextView profile,mDonorProfile,mNoNotificationText;
//    String mCurrDonor;
//    ImageView mProfileImg,mDonorDP;
//    FrameLayout mSearchLayout,mSearchList, mSettingsView;
//    ScrollView mDonorProfileView,mNotificationView;
//    EditText mLocationText,mBGrouptext,mDonorMsg,mMapKM;
//    ListView searchListView,notificationListView;
//
//    Context c;
    final private String URL="http://sooraz.000webhostapp.com/need_for_blood/";
    final private String META_PATH="http://sooraz.000webhostapp.com/";
//
    private void init(){
        c=this;
        mProfile = ProfileFragment.getProfileFragment();
        mSearch = SearchFragment.getSearchFragment();
        mSearch.setComunitcation(this);
    }
    //method for both donor and self
    private void display_Profile(String responce,int profileType){
        try {
            JSONObject resp = new JSONObject(responce);
            TextView temp=null;
            if(resp.getInt("success")==1) {
                String img_path = null;
                img_path = resp.getString("image_path");
                String tex="location:" + resp.getString("location") + "\n" +
                        "mail:" + resp.getString("mail") + "\n" +
                        "gender:" + resp.getString("gender") + "\n" +
                        "bgroup:" + resp.getString("bgroup");
                String imgData=META_PATH + img_path;
                switch(profileType){
                    case ServerFile.DISPLAYDONOR:
                        mSearch.setData(tex,imgData,c);
                        break;
                    case ServerFile.DISPLAYPROFILE:
                        mProfile.setData(tex,imgData,c);
                        break;
                }
            }
            else{
                //fail  send to server
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    public void viewProfile(View v) {
//        makeAllViewGone();
//        mDonorProfile.setVisibility(View.VISIBLE);
//        profile.setVisibility(View.VISIBLE);
//        mProfileImg.setVisibility(View.VISIBLE);
//        profile(username,ServerFile.DISPLAYPROFILE);
//    }

    @Override
    public void profile(String uname,int code){
        final String url = String.format(URL+"view_profile.php?name=%1$s&usertype=%2$s",uname,code);
        MyVolley.connectGET(url,this);
    }
//
//
    @Override
    public void search(String location,String bgroup) {
        final String url = String.format(URL + "search.php?location=%1$s&bgroup=%2$s", location, bgroup);
        MyVolley.connectGET(url,this);
    }

//    private void setListner(ListView mListView, NFBEnum.LISTTYPE type) {
//        switch (type){
//            case DONOR:
//                donorListItem(mListView);
//                break;
//            case NOTIFICATION:
//                notificationListItem(mListView);
//                break;
//        }
//    }

//    private void notificationListItem(ListView mListView){
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView temp = view.findViewById(R.id.notification_sender);
//                String name = temp.getText().toString();
//                temp = view.findViewById(R.id.notification_time);
//                String time = temp.getText().toString();
//                makeAllViewGone();
//                final String url = String.format(URL + "viewNotification.php?from=%1$s&to=%2$s&time=%2$s", name, NFBSharedPreference.getUserName(c),time);
//                MyVolley.connectGET(url,c);
//            }
//        });
//    }
//
//    public void showSearchLayout(View view) {
//        makeAllViewGone();
//        //profile.setVisibility(View.GONE);
//        //mProfileImg.setVisibility(View.GONE);
//        mSearchLayout.setVisibility(View.VISIBLE);
//    }
//
//    public void logout(View view) {
//        logout();
//    }
//    private void logout(){
//        NFBSharedPreference.clearData(c);
//        Intent i=new Intent(c,MainActivity.class);
//        startActivity(i);
//        finish();
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        //Yes button clicked
//                        logout();
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        //No button clicked
//                        break;
//                }
//            }
//        };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(c);
//        builder.setMessage("You are Logging Out,Are you sure?").setPositiveButton("Yes", dialogClickListener)
//                .setNegativeButton("No", dialogClickListener).show();
//    }
//
//    public void request(View view) {
//        //notifiction to donar
//        String msg=mDonorMsg.getText().toString();
//        final String url = String.format(URL+"sendNotification.php?from_name=%1$s&to_name=%2$s&message=%3$s",NFBSharedPreference.getUserName(c),mCurrDonor,msg);
//        MyVolley.connectGET(url,this);
//    }
//
//    private void viewNotification(String response){
//        try {
//            JSONObject temp = new JSONObject(response);
//            switch (temp.getInt("success")) {
//                case 1:
//                    //name,time,message
//                    String tex="from :: "+temp.getString("name")
//                            +"\n time :: "+temp.getString("time")
//                            +"\n message :: "+temp.getString("message")
//                            +"\n to :: "+NFBSharedPreference.getUserName(c);
//                    ;
//                    Toast.makeText(AfterLogin_2.this, tex, Toast.LENGTH_LONG).show();
//                    break;
//                default:
//                    Log.d("sooraz"," viewNotification send failed");
//                    //error
//
//            }
//        }catch (Exception e) {
//            Log.d("sooraz","error viewNotification");
//            e.printStackTrace();
//        }
//    }
//
//    //callback request
//    private void sendNotificationAck(String response) {
//        Log.d("sooraz","mCurrDonor: "+mCurrDonor+" NFBSharedPreference.getUserName(c): "+NFBSharedPreference.getUserName(c));
//        try {
//            JSONObject temp = new JSONObject(response);
//            switch (temp.getInt("success")) {
//                case 1:
////                                    Log.d("sooraz","sendNotification response :"+response);
//                    Toast.makeText(AfterLogin_2.this, "sent request", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    //error
//
//            }
//        }catch (Exception e) {
//            Log.d("sooraz","error profile");
//            e.printStackTrace();
//        }
//    }
//
//    public void showNotifications(View view) {
//        final String url = String.format(URL+"getNotifications.php?to=%1$s",NFBSharedPreference.getUserName(this));
//        MyVolley.connectGET(url,this);
//
//    }
//    private void notificationDisplay(String response){
//        try {
//            JSONObject temp = new JSONObject(response);
//            if(temp.getInt("success")==0){
//                return;
//            }
//            makeAllViewGone();
//            mNotificationView.setVisibility(View.VISIBLE);
//            switch (temp.getInt("tol_noti")) {
//                case 0:
////                                    no notifications
//                    notificationListView.setVisibility(View.GONE);
//                    mNoNotificationText.setVisibility(View.VISIBLE);
//                    profile.setText("no requests are available");
//                    break;
//                default:
//                    final ArrayList<List<String>> list = new ArrayList<List<String>>();
//                    //list.add(Arrays.asList("Name","Location","Blood_group"));
//                    int size=temp.getInt("tol_noti");
//                    for (int i = 0; i < size; ++i) {
//                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
//                        list.add(Arrays.asList(looptemp.getString("name"),looptemp.getString("time")));
//                    }
//                    displayNotificationList(list);
//                    break;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void displayNotificationList(ArrayList<List<String>> mList){
//        mNoNotificationText.setVisibility(View.GONE);
//        notificationListView.setVisibility(View.VISIBLE);
//
//        DonorSearchListAdapter notificationAdapter=new DonorSearchListAdapter(c,R.layout.notification_list,mList,NFBEnum.ADAPTERTYPE.NOTIFICATION);
//        notificationListView.setAdapter(notificationAdapter);
//        setListner(notificationListView, NFBEnum.LISTTYPE.NOTIFICATION);
//    }
    private Handler responceHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case ServerFile.SEARCH:
                    mSearch.search(msg.obj.toString());
                    break;
                case ServerFile.SENDNOTIFICATION:
//                    sendNotificationAck(msg.obj.toString());
                    break;
                case ServerFile.DISPLAYDONOR:
                case ServerFile.DISPLAYPROFILE:
                    display_Profile(msg.obj.toString(),msg.arg1);
                    break;
                case ServerFile.GETNOTIFICATIONS:
//                    notificationDisplay(msg.obj.toString());
                    break;
                case ServerFile.VIEWNOTIFICATION:
//                    viewNotification(msg.obj.toString());
                    break;


            }
        }
    };
//    private Handler requestHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.arg1){
//                case ServerFile.SEARCH:
////                    search(msg.obj.toString());
//                    break;
//                case ServerFile.SENDNOTIFICATION:
//                    sendNotificationAck(msg.obj.toString());
//                    break;
//                case ServerFile.DISPLAYDONOR:
//                case ServerFile.DISPLAYPROFILE:
//                    display_Profile(msg.obj.toString(),msg.arg1);
//                    break;
//                case ServerFile.GETNOTIFICATIONS:
//                    notificationDisplay(msg.obj.toString());
//                    break;
//                case ServerFile.VIEWNOTIFICATION:
//                    viewNotification(msg.obj.toString());
//                    break;
//
//
//            }
//        }
//    };
//    private void makeAllViewGone(){
////        mSearchLayout.setVisibility(View.GONE);
////        mSearchList.setVisibility(View.GONE);
////        mDonorProfileView.setVisibility(View.GONE);
////        mNotificationView.setVisibility(View.GONE);
////        mSettingsView.setVisibility(View.GONE);
//    }
//
//    public void displayMap(View view) {
////        int km=Integer.parseInt(mMapKM.getText().toString());
//
//
//
//        Intent i=new Intent(c,MapsActivity.class);
//        startActivity(i);
//    }
//
//    public void viewSettings(View view) {
//        makeAllViewGone();
//        mSettingsView.setVisibility(View.VISIBLE);
//
//    }
//
//    public void updateLocation(View view) {
//
//    }
}
