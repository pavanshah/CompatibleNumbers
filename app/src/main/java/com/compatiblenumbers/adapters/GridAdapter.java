package com.compatiblenumbers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.compatiblenumbers.R;

/**
 * Created by avinash on 5/4/17.
 */

public class GridAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;


    public GridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public String getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.empty, parent, false);
        return view;
    }


}
