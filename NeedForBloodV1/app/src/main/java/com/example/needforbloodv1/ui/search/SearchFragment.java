package com.example.needforbloodv1.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.needforbloodv1.Log.NFBLog;
import com.example.needforbloodv1.MapsActivity;
import com.example.needforbloodv1.R;
import com.example.needforbloodv1.activityRefernce.ActivityInterface;
import com.example.needforbloodv1.adapter.DonorSearchListAdapter;
import com.example.needforbloodv1.define.HandlerType;
import com.example.needforbloodv1.define.ServerFile;
import com.example.needforbloodv1.enums.NFBEnum;
import com.example.needforbloodv1.utils.NFBUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private static SearchFragment mFrag;
    private EditText locationEditTextView,bGroupEditTextView,distanceEditTextView;
    private TextView noUserTextView,mDonorDetail;
    private ImageView mDonorDP;
    private Button searchByText,searchByMap;
    private Context context;
    private FrameLayout searchLayout,listLayout;
    private LinearLayout donorProfileLayout;
    private ListView searchResultListView;
    private static ActivityInterface ref;
    private static Handler activityHandler;

    public static SearchFragment getSearchFragment() {
        return mFrag;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        init(root);
        initClickListners();
        return root;
    }
    private void init(View root){
        mFrag = this;
        context = NFBUtils.getContext();
        searchLayout = (FrameLayout)root.findViewById(R.id.search_Layout);
        listLayout = (FrameLayout)root.findViewById(R.id.list_frame);
        donorProfileLayout = (LinearLayout)root.findViewById(R.id.donor_profile);
        locationEditTextView = (EditText)root.findViewById(R.id.location_search);
        bGroupEditTextView = (EditText)root.findViewById(R.id.group_search);
        distanceEditTextView = (EditText)root.findViewById(R.id.map_km);
        searchByText = (Button)root.findViewById(R.id.search_value);
        searchByMap = (Button)root.findViewById(R.id.search_maps);
        searchResultListView = (ListView)root.findViewById(R.id.user_list);
        noUserTextView = (TextView)root.findViewById(R.id.no_users_text);
        mDonorDetail = (TextView)root.findViewById(R.id.donor_msg);
        mDonorDP = (ImageView)root.findViewById(R.id.donor_img);
        activityHandler = NFBUtils.getActivityHandler();
        Message msg =new Message();
        msg.arg1=HandlerType.SEARCH_FRAGMENT_ATTACHED;
        activityHandler.sendMessage(msg);
    }

    private void initClickListners() {
       searchByMap.setOnClickListener(searchFragmentButtonListner);
       searchByText.setOnClickListener(searchFragmentButtonListner);
    }
    View.OnClickListener searchFragmentButtonListner =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.search_value:
                    if(validInput()){
                        String location = locationEditTextView.getText().toString();
                        String bGroup = bGroupEditTextView.getText().toString();
                        ref.search(location,bGroup);
                    }
                    else
                    {
                        //show waring
                    }
                    break;
                case R.id.search_maps:
                    NFBLog.debugOut("launching maps activity");
                    Intent i=new Intent(context, MapsActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    private boolean validInput() {
        return !locationEditTextView.getText().toString().isEmpty()||!bGroupEditTextView.getText().toString().isEmpty();
    }

    public void setComunication(ActivityInterface activityInterface) {
        ref=activityInterface;
        NFBLog.debugOut("sooraz ref:"+ref);
    }
    public void search(String response){
        try {
            JSONObject temp = new JSONObject(response);
            listLayout.setVisibility(View.VISIBLE);
            switch (temp.getInt("tol_users")) {
                case 0:
//                                    no users
                    noUserTextView.setVisibility(View.VISIBLE);
                    searchResultListView.setVisibility(View.GONE);
                    break;
                default:
                    NFBLog.debugOut("yug arrey");
                    noUserTextView.setVisibility(View.GONE);
                    searchResultListView.setVisibility(View.VISIBLE);
                    final ArrayList<List<String>> list = new ArrayList<List<String>>();
                    //list.add(Arrays.asList("Name","Location","Blood_group"));
                    int size=temp.getInt("tol_users");
                    for (int i = 0; i < size; ++i) {
                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
                        list.add(Arrays.asList(looptemp.getString("image_path"),looptemp.getString("name"),looptemp.getString("loc_p"),looptemp.getString("bgroup")));
                    }
                    final DonorSearchListAdapter adapter = new DonorSearchListAdapter(context,R.layout.donor_search_item, list, NFBEnum.ADAPTERTYPE.DONOR);
                    NFBLog.debugOut("yug adapter:: "+adapter);
                    searchResultListView.setAdapter(adapter);
                    donorListItem(searchResultListView);
                    break;

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
        private void donorListItem(ListView mListView){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.list_name);
                String name = temp.getText().toString();
                ref.profile(name, ServerFile.DISPLAYDONOR);
            }
        });
    }

    public void setData(String textData, String imageData, Context c) {
        donorProfileLayout.setVisibility(View.VISIBLE);
        Glide.with(context).load(imageData).into(mDonorDP);
        mDonorDetail.setText(textData);
    }

}