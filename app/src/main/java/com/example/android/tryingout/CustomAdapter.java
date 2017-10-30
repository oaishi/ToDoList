package com.example.android.tryingout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faria huq on 07-Sep-17.
 */

public class CustomAdapter extends ArrayAdapter<String> {

    ArrayList<String > store ;


    public CustomAdapter(@NonNull Context context, ArrayList<String> store ) {
        super(context, R.layout.customrow , store);
        this.store = store ;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return store.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myflater = LayoutInflater.from(getContext());
        View CustomView = myflater.inflate(R.layout.customrow , parent , false);
        String str = getItem(position);
        TextView txt = (TextView) CustomView.findViewById(R.id.textview);
        txt.setText(str);
        return CustomView;
    }

    public void additem(String str)
    {
        store.add(str);
    }

    public void notifyDataSetChanged(String str) {
        super.notifyDataSetChanged();
        additem(str);
    }
}
