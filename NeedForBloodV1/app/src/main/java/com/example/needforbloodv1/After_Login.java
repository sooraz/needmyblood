package com.example.needforbloodv1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class After_Login extends AppCompatActivity {
    private String username=null;
    TextView profile,donor_profile;
    FrameLayout search_layout,search_list,donor_profile_view;
    EditText location_search,bgroup_search;
    ListView lv;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after__login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Button b=(Button)findViewById(R.id.view_profile);
        c=this;
        profile=(TextView)findViewById(R.id.profile);
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
                display_Donor(((TextView)view.findViewById(R.id.list_name)).getText().toString());
            }
        });
    }
    public void display_Donor(String donor_name){

        profile.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.VISIBLE);
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
                                    donor_profile.setText(response);
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
    public void viewProfile(View v){
        search_layout.setVisibility(View.GONE);
        search_list.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
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
                                    list.add(Arrays.asList("Name","Location","Blood_group"));
                                    int size=temp.getInt("tol_users");
                                    for (int i = 0; i < size; ++i) {
                                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
                                        list.add(Arrays.asList(looptemp.getString("name"),looptemp.getString("loc_p"),looptemp.getString("bgroup")));
                                    }
                                    final MyCustomAdapter adapter = new MyCustomAdapter(c,R.layout.list_item, list);
                                    lv.setAdapter(adapter);
                                    break;

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
        search_list.setVisibility(View.GONE);
        donor_profile_view.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
    }
    private class MyCustomAdapter extends ArrayAdapter<List<String>> {
        private final Context context;
        private final List<List<String>> values;

         MyCustomAdapter(Context context, int id,List<List<String>> values) {
            super(context, id, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView1 = (TextView) rowView.findViewById(R.id.list_name);
            TextView textView2 = (TextView) rowView.findViewById(R.id.list_location);
            TextView textView3 = (TextView) rowView.findViewById(R.id.list_group);
            if(position==0)
            {
                textView1.setTextSize(30);
                textView2.setTextSize(30);
                textView3.setTextSize(30);
            }
            textView1.setText(values.get(position).get(0));
            textView2.setText(values.get(position).get(1));
            textView3.setText(values.get(position).get(2));

            return rowView;
        }
    }
}


