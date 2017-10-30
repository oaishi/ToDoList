package com.example.android.tryingout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Faria huq on 07-Sep-17.
 */

public class baseadapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    baseadapter(Context c, ArrayList<String> l){
        context = c;
        list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void add(String str)
    {
        list.add(str);
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.customrow, null);
        }
        TextView txt = (TextView) rowView.findViewById(R.id.textview);
        CheckBox chq = (CheckBox)  rowView.findViewById(R.id.checkBox);
        txt.setText(getItem(position));
        String str = txt.getText().toString();
        if(str.endsWith("Done"))
        {
            if(chq.isChecked()==false)
            chq.toggle();
        }
        else
        {
            if(chq.isChecked()==true)
                chq.toggle();
        }
        return rowView;
    }

    public ArrayList<String> getList(){
        return list;
    }

  /*  public void notifyDataSetChanged(String str) {
        super.notifyDataSetChanged();
        list.add(str);
    }*/
}
