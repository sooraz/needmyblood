package com.example.needforbloodv1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.needforbloodv1.R;
import com.example.needforbloodv1.enums.NFBEnum;

import java.util.List;

public class DonorSearchListAdapter extends ArrayAdapter<List<String>> {
    private final Context context;
    private final List<List<String>> values;
    private NFBEnum.ADAPTERTYPE type;

    public DonorSearchListAdapter(Context context, int id, List<List<String>> values,NFBEnum.ADAPTERTYPE type) {
        super(context, id, values);
        this.context = context;
        this.values = values;
        this.type=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(this.type ==NFBEnum.ADAPTERTYPE.DONOR) {
            View rowView = inflater.inflate(R.layout.donor_search_item, parent, false);
            TextView textView1 = (TextView) rowView.findViewById(R.id.list_name);
            TextView textView2 = (TextView) rowView.findViewById(R.id.list_location);
            TextView textView3 = (TextView) rowView.findViewById(R.id.list_group);
            ImageView im = (ImageView) rowView.findViewById(R.id.list_image);

            String path = "http://sooraz.000webhostapp.com/" + values.get(position).get(0);
//        Log.d("sooraz","in  getview path:"+path);

            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100)
                    .circleCrop();


            Glide.with(parent)
                    .asBitmap()
                    .apply(myOptions)
                    .load(path)
                    .into(im);


//        Glide.with(parent).load(path).centerCrop().into(im);
            textView1.setText(values.get(position).get(1));
            textView2.setText(values.get(position).get(2));
            textView3.setText(values.get(position).get(3));

          return rowView;
        }
        else if(this.type ==NFBEnum.ADAPTERTYPE.NOTIFICATION){
            View rowView = inflater.inflate(R.layout.notification_list, parent, false);

            TextView textView1 = (TextView) rowView.findViewById(R.id.notification_sender);
            TextView textView2 = (TextView) rowView.findViewById(R.id.notification_time);

            textView1.setText(values.get(position).get(0));
            textView2.setText(values.get(position).get(1));

            return rowView;
        }
        return convertView;
    }
}