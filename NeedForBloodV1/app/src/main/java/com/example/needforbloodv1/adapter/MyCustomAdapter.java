package com.example.needforbloodv1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.needforbloodv1.R;

import java.util.List;

public class MyCustomAdapter  extends ArrayAdapter<List<String>> {
    private final Context context;
    private final List<List<String>> values;

    public MyCustomAdapter(Context context, int id, List<List<String>> values) {
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