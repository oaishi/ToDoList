package com.example.android.tryingout;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class DynmicFragments extends AppCompatActivity {

    private PendingIntent pendingIntent;
    AlarmManager manager;
    AlarmReceiver alarmReceiver;

    ArrayList<String> listItems;
    private ListView list;
    ListAdapter adapter;
    MyDBHandler dbHandler;

    private SharedPreferences mPreferences;
    private final String COUNT_KEY = "count";
    private final String DONE_KEY = "done";
    private final String SET_KEY = "alarm";

    private static final String mSharedPrefFile = "com.example.android.tryingout";
    int clickCounter=0;
    int donecounter = 0;
    int alarmset = 0;

    CheckBox chq ;
    TextView percentage;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynmic_fragments);
        listItems=new ArrayList<>();
        //sharedpref
        mPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        clickCounter = mPreferences.getInt(COUNT_KEY, 0);
        donecounter = mPreferences.getInt(DONE_KEY , 0);
        alarmset = mPreferences.getInt(SET_KEY , 0);
        //listview
        list =  (ListView) findViewById(R.id.list);
        percentage = (TextView) findViewById(R.id.textView2);
        double i = (donecounter / (double) clickCounter);
        i = i * 100;
        percentage.setText(i + "%");
        dbHandler =new MyDBHandler(this,null,null,1);
        adapter = new baseadapter(this , listItems);
        list.setAdapter(adapter);
        list.setOnItemClickListener(
               new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        chq = (CheckBox)view.findViewById(R.id.checkBox);
                        txt = (TextView)view.findViewById(R.id.textview);
                        chq.toggle();
                        if(chq.isChecked())
                        {
                            String str = txt.getText().toString();
                            dbHandler.deleteProduct(str);
                            str = str + " - Done";
                            dbHandler.addProduct(str);
                            listItems.set(i,str);
                            baseadapter list2Adapter = (baseadapter) (list.getAdapter());
                            list2Adapter.notifyDataSetChanged();
                            donecounter++;
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putInt(DONE_KEY, donecounter);
                            preferencesEditor.apply();
                            double in = (donecounter / (double) clickCounter);
                            in = in * 100;
                            percentage.setText(in + "%");
                        }
                        else
                        {
                            String str = txt.getText().toString();
                            dbHandler.deleteProduct(str);
                            str = str.substring(0 , str.length()-7);
                            dbHandler.addProduct(str);
                            listItems.set(i,str);
                            baseadapter list2Adapter = (baseadapter) (list.getAdapter());
                            list2Adapter.notifyDataSetChanged();
                            if(donecounter!=0)
                            donecounter--;
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putInt(DONE_KEY, donecounter);
                            preferencesEditor.apply();
                            double in = (donecounter / (double) clickCounter);
                            in = in * 100;
                            percentage.setText(in + "%");
                        }
                    }
               }
        );

        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        chq = (CheckBox)view.findViewById(R.id.checkBox);
                        txt = (TextView)view.findViewById(R.id.textview);
                        clickCounter--;
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        preferencesEditor.putInt(COUNT_KEY, clickCounter);
                        percentage.setText(donecounter + " " + clickCounter);
                        if(chq.isChecked())
                        {
                            donecounter++;
                            preferencesEditor.putInt(DONE_KEY, donecounter);
                            preferencesEditor.apply();
                            double in = (donecounter / (double) clickCounter);
                            in = in * 100;
                            percentage.setText(in + "%");
                        }
                        else
                        {
                            if(donecounter!=0)
                            donecounter--;
                            preferencesEditor.putInt(DONE_KEY, donecounter);
                            preferencesEditor.apply();
                            double in = (donecounter / (double) clickCounter);
                            in = in * 100;
                            percentage.setText(in + "%");
                        }
                        listItems.remove(i);
                        dbHandler.deleteProduct(txt.getText().toString());
                        baseadapter list2Adapter = (baseadapter) (list.getAdapter());
                        list2Adapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        //alarm
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmReceiver = new AlarmReceiver();

    }

    public void addItems(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        alertDialogBuilder.setView(et);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clickCounter++;
                listItems.add(et.getText().toString());
                baseadapter list2Adapter = (baseadapter) (list.getAdapter());
                list2Adapter.notifyDataSetChanged();
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putInt(COUNT_KEY, clickCounter);
                preferencesEditor.apply();
                dbHandler.addProduct(et.getText().toString());
                double in = (donecounter / (double) clickCounter);
                in = in * 100;
                percentage.setText(in + "%");
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            clickCounter = mPreferences.getInt(COUNT_KEY, 0);
            donecounter = mPreferences.getInt(DONE_KEY, 0);
            if (clickCounter==0)
            {
                percentage.setText("You have no tasks !");
            }
            else
            {
                double i = (donecounter / (double) clickCounter);
                i = i * 100;
                percentage.setText(i + "%");
            }
            popup();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        alarmset = mPreferences.getInt(SET_KEY , 0);
        if(listItems.size()==0) {
            ArrayList<String> lis = dbHandler.databasetostring();
            listItems.addAll(lis);
            baseadapter list2Adapter = (baseadapter) (list.getAdapter());
            list2Adapter.notifyDataSetChanged();
        }
        if(alarmset==0)
            startAlarm();
    }

    public void startAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        final int FIFTEEN_SEC_MILLIS = 15000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE , 10 );

       // manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIFTEEN_SEC_MILLIS,
                FIFTEEN_SEC_MILLIS, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        alarmset=1;
        preferencesEditor.putInt(SET_KEY, alarmset);
        preferencesEditor.apply();
        }

    public void popup(View view)
    {
        clickCounter = 0;
        donecounter = 0;
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY , clickCounter);
        preferencesEditor.putInt(DONE_KEY , donecounter);
        preferencesEditor.apply();
        int i = listItems.size();
        i--;
        while(i>-1)
        {
            dbHandler.deleteProduct(listItems.get(i));
            i--;
        }
        listItems.clear();
        baseadapter list2Adapter = (baseadapter) (list.getAdapter());
        list2Adapter.notifyDataSetChanged();
        percentage.setText("0%");
    }

    public void popup()
    {
        clickCounter = 0;
        donecounter = 0;
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(COUNT_KEY , clickCounter);
        preferencesEditor.putInt(DONE_KEY , donecounter);
        preferencesEditor.apply();
        int i = listItems.size();
        i--;
        while(i>-1)
        {
            dbHandler.deleteProduct(listItems.get(i));
            i--;
        }
        listItems.clear();
        baseadapter list2Adapter = (baseadapter) (list.getAdapter());
        list2Adapter.notifyDataSetChanged();
        percentage.setText("0%");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}
