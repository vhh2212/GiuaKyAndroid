package com.vhh.vanhai.gkk;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyArrayAdapter extends BaseAdapter{

    private Context mcontext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<GiaRau> myarr;

    public MyArrayAdapter( Context context,  ArrayList<GiaRau> arr) {
        this.mcontext = context;
        this.myarr = arr;
        this.mLayoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return myarr != null ? myarr.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return myarr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.my_item_layout, null);
            holder.mloai = (TextView) convertView.findViewById(R.id.txtLoaiRCQ);
            holder.mgia = (TextView) convertView.findViewById(R.id.txtGia);
            holder.mImage = (ImageView) convertView.findViewById(R.id.imgView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GiaRau gr = (GiaRau) getItem(position);
        holder.mloai.setText(gr.LoaiRCQ.toString());
        holder.mgia.setText(gr.Gia.toString());
        Picasso.with(mcontext).load(gr.urlImage.toString()).into(holder.mImage);

        return convertView;

    }
    public class ViewHolder{
        private TextView mloai;
        private TextView mgia;
        private ImageView mImage;
    }
}
