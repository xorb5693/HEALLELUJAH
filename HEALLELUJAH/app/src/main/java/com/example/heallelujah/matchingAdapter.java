package com.example.heallelujah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class matchingAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Trainerlist> trainerlist;

    public matchingAdapter(Context context, ArrayList<Trainerlist> trainers) {
        mContext = context;
        trainerlist = trainers;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return trainerlist.size();
    }

    @Override
    public Trainerlist getItem(int position) {
        return trainerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = mLayoutInflater.inflate(R.layout.matchingitem,null);
       TextView name = (TextView) view.findViewById(R.id.Human_name2);
       TextView sa = (TextView) view.findViewById(R.id.textView44);
       ImageView trainerinfocheck = (ImageView) view.findViewById(R.id.imageView5);

       name.setText(trainerlist.get(position).gettrainername()+ "/트레이너");
       sa.setText(trainerlist.get(position).gettrainerage()+"/"+trainerlist.get(position).gettrainersex());
       return view;
    }
}
