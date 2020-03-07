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
import com.bumptech.glide.request.RequestOptions;
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
    private EditText locationEditTextView,bGroupEditTextView,distanceEditTextView,mDonorRequestEditTextView;
    private TextView noUserTextView;
    private TextView mNameView,mEmaiView,mLocationView,mGenderView,mBGroupView;
    private ImageView mDonorDP;
    private Button searchByText,searchByMap,requestButton;
    private Context context;
    private FrameLayout searchLayout,listLayout;
    private LinearLayout donorProfileLayout;
    private ListView searchResultListView;
    private static ActivityInterface ref;
    private static Handler activityHandler;
    private String donorName;

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
        mNameView = (TextView)root.findViewById(R.id.profile_name);
        mEmaiView = (TextView)root.findViewById(R.id.profile_email);
        mLocationView = (TextView)root.findViewById(R.id.profile_location);
        mBGroupView = (TextView)root.findViewById(R.id.profile_bgroup);
        mGenderView = (TextView)root.findViewById(R.id.profile_gender);
        mDonorDP = (ImageView)root.findViewById(R.id.profile_img);
//        mDonorRequestEditTextView = (EditText)root.findViewById(R.id.donor_request_text);
//        requestButton = (Button)root.findViewById(R.id.donor_request_button);
        activityHandler = NFBUtils.getActivityHandler();
        Message msg =new Message();
        msg.arg1=HandlerType.SEARCH_FRAGMENT_ATTACHED;
        activityHandler.sendMessage(msg);
    }

    private void initClickListners() {
       searchByMap.setOnClickListener(searchFragmentButtonListner);
       searchByText.setOnClickListener(searchFragmentButtonListner);
//       requestButton.setOnClickListener(searchFragmentButtonListner);
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
//                case R.id.donor_request_button:
//                    String requestText = mDonorRequestEditTextView.getText().toString();
//                    ref.requestBlood(donorName,requestText);
//                    break;
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
            makeAllViewsGone();
            listLayout.setVisibility(View.VISIBLE);
            switch (temp.getInt("tol_users")) {
                case 0:
//                                    no users
                    noUserTextView.setVisibility(View.VISIBLE);
                    searchResultListView.setVisibility(View.GONE);
                    break;
                default:
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
                donorName = temp.getText().toString();
                ref.profile(donorName, ServerFile.DISPLAYDONOR);
            }
        });
    }

    public void setData(String email,String location,String bgroup,String gender,String imageData,Context context){
        makeAllViewsGone();
        donorProfileLayout.setVisibility(View.VISIBLE);
        mNameView.setText(donorName);
        mEmaiView.setText(email);
        mLocationView.setText(location);
        mBGroupView.setText(bgroup);
        mGenderView.setText(gender);
        Glide.with(context).load(imageData).apply(RequestOptions.circleCropTransform()).into(mDonorDP);
    }

    private void makeAllViewsGone(){
        donorProfileLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.GONE);
    }
}
