package com.example.needforbloodv1.ui.user_profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.needforbloodv1.Log.NFBLog;
import com.example.needforbloodv1.R;
import com.example.needforbloodv1.define.HandlerType;
import com.example.needforbloodv1.utils.NFBUtils;

public class ProfileFragment extends Fragment {

//    private ProfileViewModel profileViewModel;
    private static ProfileFragment mfrag = null;
    TextView textDataView;
    ImageView imageView;
    private Handler activityHandler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        textDataView = (TextView)root.findViewById(R.id.profile);
        imageView = (ImageView)root.findViewById(R.id.profile_img);
        mfrag = this;
        activityHandler = NFBUtils.getActivityHandler();
        NFBLog.debugOut("yug oncreateView");
        if(activityHandler != null){
            NFBLog.debugOut("yug 1");
            Message msg =new Message();
            msg.arg1=HandlerType.PROFILE_FRAGMENT_ATTACHED;
            activityHandler.sendMessage(msg);
        }
        return root;
    }
    public static ProfileFragment getProfileFragment(){
        return mfrag;
    }
    public void setData(String textData,String imageData,Context context){
        Glide.with(context).load(imageData).into(imageView);
        textDataView.setText(textData);
    }
}